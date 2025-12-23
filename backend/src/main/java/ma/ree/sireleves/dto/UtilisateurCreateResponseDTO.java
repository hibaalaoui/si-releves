package ma.ree.sireleves.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.ree.sireleves.entity.UtilisateurBackoffice;

import java.time.LocalDateTime;

/**
 * DTO pour la réponse lors de la création d'un utilisateur
 * Inclut le mot de passe généré pour que l'admin puisse le voir
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UtilisateurCreateResponseDTO {

    private Integer idUtilisateur;
    private String nom;
    private String prenom;
    private String email;
    private UtilisateurBackoffice.Role role;
    private Boolean premiereConnexion;
    private LocalDateTime dateAjout;
    private LocalDateTime dateModification;
    private Boolean actif;
    
    /**
     * Mot de passe généré (uniquement lors de la création ou réinitialisation)
     * À afficher à l'admin et à communiquer à l'utilisateur
     */
    private String generatedPassword;
}

