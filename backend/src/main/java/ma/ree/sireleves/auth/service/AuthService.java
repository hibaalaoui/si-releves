package ma.ree.sireleves.auth.service;

import ma.ree.sireleves.auth.dto.LoginRequest;
import ma.ree.sireleves.auth.dto.LoginResponse;
import ma.ree.sireleves.security.jwt.JwtTokenProvider;
import ma.ree.sireleves.security.service.UserDetailsServiceImpl;
import ma.ree.sireleves.user.entity.UtilisateurBackoffice;
import ma.ree.sireleves.user.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
@Transactional
public class AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserDetailsServiceImpl userDetailsService;

    public AuthService(
            AuthenticationManager authenticationManager,
            JwtTokenProvider jwtTokenProvider,
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            UserDetailsServiceImpl userDetailsService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userDetailsService = userDetailsService;
    }

    /**
     * Authenticate user and generate JWT token
     */
    public LoginResponse login(LoginRequest loginRequest) {
        try {
            // Authenticate user
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getEmail(),
                            loginRequest.getPassword()
                    )
            );

            // Load user details
            UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.getEmail());
            
            // Get user entity for additional info
            UtilisateurBackoffice user = userRepository.findByEmail(loginRequest.getEmail())
                    .orElseThrow(() -> new BadCredentialsException("Utilisateur non trouvé"));

            // Generate JWT token with custom claims
            Map<String, Object> claims = new HashMap<>();
            claims.put("id", user.getIdUtilisateur());
            claims.put("nom", user.getNom());
            claims.put("prenom", user.getPrenom());
            claims.put("role", user.getRole().name());
            claims.put("premiereConnexion", user.getPremiereConnexion());

            String token = jwtTokenProvider.generateToken(userDetails.getUsername(), claims);

            // Build response
            return LoginResponse.builder()
                    .token(token)
                    .type("Bearer")
                    .id(user.getIdUtilisateur())
                    .email(user.getEmail())
                    .nom(user.getNom())
                    .prenom(user.getPrenom())
                    .role(user.getRole())
                    .premiereConnexion(user.getPremiereConnexion())
                    .build();

        } catch (BadCredentialsException e) {
            logger.error("Authentication failed for email: {}", loginRequest.getEmail());
            throw new BadCredentialsException("Email ou mot de passe incorrect", e);
        } catch (Exception e) {
            logger.error("Error during authentication for email: {}", loginRequest.getEmail(), e);
            throw new RuntimeException("Erreur lors de l'authentification", e);
        }
    }

    /**
     * Verify password with BCrypt
     */
    public boolean verifyPassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    /**
     * Encode password with BCrypt
     */
    public String encodePassword(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }
}

