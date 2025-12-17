package ma.ree.sireleves.user.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ma.ree.sireleves.common.enums.Role;

import java.time.LocalDateTime;

@Entity
@Table(name = "utilisateur_backoffice", indexes = {
    @Index(name = "idx_user_role", columnList = "role"),
    @Index(name = "idx_user_actif", columnList = "actif")
}, uniqueConstraints = {
    @UniqueConstraint(name = "uk_user_email", columnNames = "email")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UtilisateurBackoffice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_utilisateur")
    private Integer idUtilisateur;

    @NotBlank(message = "Le nom est requis")
    @Size(max = 100, message = "Le nom ne peut pas dépasser 100 caractères")
    @Column(name = "nom", nullable = false, length = 100)
    private String nom;

    @NotBlank(message = "Le prénom est requis")
    @Size(max = 100, message = "Le prénom ne peut pas dépasser 100 caractères")
    @Column(name = "prenom", nullable = false, length = 100)
    private String prenom;

    @NotBlank(message = "L'email est requis")
    @Email(message = "L'email doit être valide")
    @Size(max = 255, message = "L'email ne peut pas dépasser 255 caractères")
    @Column(name = "email", nullable = false, unique = true, length = 255)
    private String email;

    @NotBlank(message = "Le hash du mot de passe est requis")
    @Size(max = 255, message = "Le hash du mot de passe ne peut pas dépasser 255 caractères")
    @Column(name = "password_hash", nullable = false, length = 255)
    private String passwordHash;

    @NotNull(message = "Le rôle est requis")
    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false, length = 20)
    private Role role = Role.Utilisateur;

    @Column(name = "premiere_connexion", nullable = false)
    private Boolean premiereConnexion = true;

    @Column(name = "date_ajout", nullable = false, updatable = false)
    private LocalDateTime dateAjout = LocalDateTime.now();

    @Column(name = "date_modification")
    private LocalDateTime dateModification;

    @Column(name = "actif", nullable = false)
    private Boolean actif = true;

    @PreUpdate
    protected void onUpdate() {
        dateModification = LocalDateTime.now();
    }
}

