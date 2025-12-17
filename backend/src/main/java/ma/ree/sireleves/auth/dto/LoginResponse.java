package ma.ree.sireleves.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.ree.sireleves.common.enums.Role;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {

    private String token;
    @Builder.Default
    private String type = "Bearer";
    private Integer id;
    private String email;
    private String nom;
    private String prenom;
    private Role role;
    private Boolean premiereConnexion;
}

