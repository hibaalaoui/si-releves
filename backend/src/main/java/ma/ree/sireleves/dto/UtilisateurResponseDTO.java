package ma.ree.sireleves.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.ree.sireleves.entity.UtilisateurBackoffice;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UtilisateurResponseDTO {

    private Integer idUtilisateur;
    private String nom;
    private String prenom;
    private String email;
    private UtilisateurBackoffice.Role role;
    private Boolean premiereConnexion;
    private LocalDateTime dateAjout;
    private LocalDateTime dateModification;
    private Boolean actif;
}