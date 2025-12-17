package ma.ree.sireleves.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "utilisateur_backoffice")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UtilisateurBackoffice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_utilisateur")
    private Integer idUtilisateur;

    @Column(nullable = false, length = 100)
    private String nom;

    @Column(nullable = false, length = 100)
    private String prenom;

    @Column(nullable = false, unique = true, length = 255)
    private String email;

    @Column(name = "password_hash", nullable = false, length = 255)
    private String passwordHash;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role = Role.Utilisateur;

    @Column(name = "premiere_connexion", nullable = false)
    private Boolean premiereConnexion = true;

    @Column(name = "date_ajout", nullable = false, updatable = false)
    private LocalDateTime dateAjout = LocalDateTime.now();

    @Column(name = "date_modification")
    private LocalDateTime dateModification;

    @Column(nullable = false)
    private Boolean actif = true;

    // Enum pour le rôle
    public enum Role {
        Superadmin,
        Utilisateur
    }
}