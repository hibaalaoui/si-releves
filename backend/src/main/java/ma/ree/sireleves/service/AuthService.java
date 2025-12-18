package ma.ree.sireleves.service;

import lombok.RequiredArgsConstructor;
import ma.ree.sireleves.dto.ChangePasswordRequestDTO;
import ma.ree.sireleves.dto.LoginRequestDTO;
import ma.ree.sireleves.dto.LoginResponseDTO;
import ma.ree.sireleves.entity.UtilisateurBackoffice;
import ma.ree.sireleves.exception.BusinessException;
import ma.ree.sireleves.repository.UtilisateurBackofficeRepository;
import ma.ree.sireleves.util.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {

    private final UtilisateurBackofficeRepository utilisateurRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    /**
     * Authentifier un utilisateur et générer un token JWT
     */
    public LoginResponseDTO login(LoginRequestDTO loginRequest) {
        // Rechercher l'utilisateur par email
        UtilisateurBackoffice utilisateur = utilisateurRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new BusinessException("Email ou mot de passe incorrect"));

        // Vérifier si l'utilisateur est actif
        if (!utilisateur.getActif()) {
            throw new BusinessException("Ce compte est désactivé");
        }

        // Vérifier le mot de passe
        if (!passwordEncoder.matches(loginRequest.getPassword(), utilisateur.getPasswordHash())) {
            throw new BusinessException("Email ou mot de passe incorrect");
        }

        // Générer le token JWT
        String token = jwtUtil.generateToken(
                utilisateur.getEmail(),
                utilisateur.getRole().name(),
                utilisateur.getIdUtilisateur()
        );

        // Retourner la réponse avec le token
        return new LoginResponseDTO(
                token,
                utilisateur.getIdUtilisateur(),
                utilisateur.getNom(),
                utilisateur.getPrenom(),
                utilisateur.getEmail(),
                utilisateur.getRole(),
                utilisateur.getPremiereConnexion()
        );
    }

    /**
     * Changer le mot de passe d'un utilisateur
     */
    public String changePassword(Integer userId, ChangePasswordRequestDTO changePasswordRequest) {
        // Rechercher l'utilisateur
        UtilisateurBackoffice utilisateur = utilisateurRepository.findById(userId)
                .orElseThrow(() -> new BusinessException("Utilisateur non trouvé"));

        // Vérifier l'ancien mot de passe
        if (!passwordEncoder.matches(changePasswordRequest.getOldPassword(), utilisateur.getPasswordHash())) {
            throw new BusinessException("L'ancien mot de passe est incorrect");
        }

        // Vérifier que le nouveau mot de passe est différent de l'ancien
        if (changePasswordRequest.getOldPassword().equals(changePasswordRequest.getNewPassword())) {
            throw new BusinessException("Le nouveau mot de passe doit être différent de l'ancien");
        }

        // Mettre à jour le mot de passe
        utilisateur.setPasswordHash(passwordEncoder.encode(changePasswordRequest.getNewPassword()));
        utilisateur.setPremiereConnexion(false);
        utilisateur.setDateModification(LocalDateTime.now());

        utilisateurRepository.save(utilisateur);

        return "Mot de passe modifié avec succès";
    }
}