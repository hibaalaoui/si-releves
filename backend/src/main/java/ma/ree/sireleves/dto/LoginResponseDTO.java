package ma.ree.sireleves.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.ree.sireleves.entity.UtilisateurBackoffice;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponseDTO {

    private String token;
    private String type = "Bearer";
    private Integer idUtilisateur;
    private String nom;
    private String prenom;
    private String email;
    private UtilisateurBackoffice.Role role;
    private Boolean premiereConnexion;

    public LoginResponseDTO(String token, Integer idUtilisateur, String nom,
                            String prenom, String email,
                            UtilisateurBackoffice.Role role, Boolean premiereConnexion) {
        this.token = token;
        this.idUtilisateur = idUtilisateur;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.role = role;
        this.premiereConnexion = premiereConnexion;
    }
}