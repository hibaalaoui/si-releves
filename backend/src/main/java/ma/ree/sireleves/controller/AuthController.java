package ma.ree.sireleves.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ma.ree.sireleves.dto.ChangePasswordRequestDTO;
import ma.ree.sireleves.dto.LoginRequestDTO;
import ma.ree.sireleves.dto.LoginResponseDTO;
import ma.ree.sireleves.entity.UtilisateurBackoffice;
import ma.ree.sireleves.repository.UtilisateurBackofficeRepository;
import ma.ree.sireleves.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final UtilisateurBackofficeRepository utilisateurRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Connexion utilisateur
     * POST /api/auth/login
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO loginRequest) {
        LoginResponseDTO response = authService.login(loginRequest);
        return ResponseEntity.ok(response);
    }

    /**
     * Changer son mot de passe
     * POST /api/auth/change-password/{userId}
     */
    @PostMapping("/change-password/{userId}")
    public ResponseEntity<String> changePassword(
            @PathVariable Integer userId,
            @Valid @RequestBody ChangePasswordRequestDTO changePasswordRequest) {
        String message = authService.changePassword(userId, changePasswordRequest);
        return ResponseEntity.ok(message);
    }

    /**
     * TEMPORAIRE: Générer les hashs BCrypt pour les mots de passe de test
     * GET /api/auth/generate-hashes
     * À SUPPRIMER EN PRODUCTION
     */
    @GetMapping("/generate-hashes")
    public ResponseEntity<String> generateHashes() {
        try {
            String adminHash = passwordEncoder.encode("admin123");
            String userHash = passwordEncoder.encode("user123");
            
            return ResponseEntity.ok(
                "Hash pour 'admin123': " + adminHash + "\n" +
                "Hash pour 'user123': " + userHash + "\n"
            );
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erreur: " + e.getMessage() + "\nStack: " + java.util.Arrays.toString(e.getStackTrace()));
        }
    }

    /**
     * TEMPORAIRE: Mettre à jour les mots de passe des utilisateurs de test
     * POST /api/auth/reset-test-passwords
     * À SUPPRIMER EN PRODUCTION
     */
    @PostMapping("/reset-test-passwords")
    public ResponseEntity<String> resetTestPasswords() {
        try {
            StringBuilder result = new StringBuilder();
            
            // Mettre à jour l'admin
            var adminOpt = utilisateurRepository.findByEmail("admin@sireleves.ma");
            if (adminOpt.isPresent()) {
                UtilisateurBackoffice admin = adminOpt.get();
                String newHash = passwordEncoder.encode("admin123");
                admin.setPasswordHash(newHash);
                admin.setPremiereConnexion(false);
                admin.setActif(true);
                admin.setRole(UtilisateurBackoffice.Role.Superadmin);
                utilisateurRepository.saveAndFlush(admin);
                result.append("✓ Admin mis à jour: admin@sireleves.ma / admin123\n");
            } else {
                result.append("✗ Admin non trouvé\n");
            }
            
            // Mettre à jour l'utilisateur normal
            var userOpt = utilisateurRepository.findByEmail("user@sireleves.ma");
            if (userOpt.isPresent()) {
                UtilisateurBackoffice user = userOpt.get();
                String newHash = passwordEncoder.encode("user123");
                user.setPasswordHash(newHash);
                user.setPremiereConnexion(false);
                user.setActif(true);
                user.setRole(UtilisateurBackoffice.Role.Utilisateur);
                utilisateurRepository.saveAndFlush(user);
                result.append("✓ Utilisateur mis à jour: user@sireleves.ma / user123\n");
            } else {
                result.append("✗ Utilisateur non trouvé\n");
            }
            
            return ResponseEntity.ok(result.toString());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Erreur: " + e.getMessage() + "\nCause: " + (e.getCause() != null ? e.getCause().getMessage() : "N/A"));
        }
    }
}