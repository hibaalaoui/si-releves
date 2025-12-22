package ma.ree.sireleves.config;

import lombok.RequiredArgsConstructor;
import ma.ree.sireleves.entity.UtilisateurBackoffice;
import ma.ree.sireleves.repository.UtilisateurBackofficeRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * Initialise les données de test au démarrage de l'application
 * Crée les utilisateurs admin et user si ils n'existent pas
 */
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UtilisateurBackofficeRepository utilisateurRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        System.out.println("==========================================");
        System.out.println("DataInitializer: Démarrage de l'initialisation des utilisateurs");
        System.out.println("==========================================");
        
        try {
            // Créer ou mettre à jour l'admin
            utilisateurRepository.findByEmail("admin@sireleves.ma").ifPresentOrElse(
                admin -> {
                    // Mettre à jour le mot de passe si nécessaire
                    String newHash = passwordEncoder.encode("admin123");
                    admin.setPasswordHash(newHash);
                    admin.setPremiereConnexion(false);
                    admin.setActif(true);
                    admin.setRole(UtilisateurBackoffice.Role.Superadmin);
                    utilisateurRepository.save(admin);
                    System.out.println("==========================================");
                    System.out.println("✓ Admin mis à jour: admin@sireleves.ma / admin123");
                    System.out.println("Hash: " + newHash.substring(0, 30) + "...");
                    System.out.println("==========================================");
                },
                () -> {
                    // Créer un nouvel admin
                    UtilisateurBackoffice admin = new UtilisateurBackoffice();
                    admin.setNom("Admin");
                    admin.setPrenom("System");
                    admin.setEmail("admin@sireleves.ma");
                    String newHash = passwordEncoder.encode("admin123");
                    admin.setPasswordHash(newHash);
                    admin.setRole(UtilisateurBackoffice.Role.Superadmin);
                    admin.setPremiereConnexion(false);
                    admin.setActif(true);
                    admin.setDateAjout(LocalDateTime.now());
                    utilisateurRepository.save(admin);
                    System.out.println("==========================================");
                    System.out.println("✓ Admin créé: admin@sireleves.ma / admin123");
                    System.out.println("Hash: " + newHash.substring(0, 30) + "...");
                    System.out.println("==========================================");
                }
            );

            // Créer ou mettre à jour l'utilisateur normal
            utilisateurRepository.findByEmail("user@sireleves.ma").ifPresentOrElse(
                user -> {
                    // Mettre à jour le mot de passe si nécessaire
                    String newHash = passwordEncoder.encode("user123");
                    user.setPasswordHash(newHash);
                    user.setPremiereConnexion(false);
                    user.setActif(true);
                    user.setRole(UtilisateurBackoffice.Role.Utilisateur);
                    utilisateurRepository.save(user);
                    System.out.println("==========================================");
                    System.out.println("✓ Utilisateur mis à jour: user@sireleves.ma / user123");
                    System.out.println("Hash: " + newHash.substring(0, 30) + "...");
                    System.out.println("==========================================");
                },
                () -> {
                    // Créer un nouvel utilisateur
                    UtilisateurBackoffice user = new UtilisateurBackoffice();
                    user.setNom("User");
                    user.setPrenom("Test");
                    user.setEmail("user@sireleves.ma");
                    String newHash = passwordEncoder.encode("user123");
                    user.setPasswordHash(newHash);
                    user.setRole(UtilisateurBackoffice.Role.Utilisateur);
                    user.setPremiereConnexion(false);
                    user.setActif(true);
                    user.setDateAjout(LocalDateTime.now());
                    utilisateurRepository.save(user);
                    System.out.println("==========================================");
                    System.out.println("✓ Utilisateur créé: user@sireleves.ma / user123");
                    System.out.println("Hash: " + newHash.substring(0, 30) + "...");
                    System.out.println("==========================================");
                }
            );
            
            System.out.println("DataInitializer: Initialisation terminée avec succès");
        } catch (Exception e) {
            System.err.println("ERREUR dans DataInitializer: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

