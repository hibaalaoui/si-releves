package ma.ree.sireleves.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AgentResponseDTO {

    private String idAgent;
    private Integer idQuartier;
    private String nomQuartier;
    private String nom;
    private String prenom;
    private String telPersonnel;
    private String telProfessionnel;
    private LocalDateTime dateAffectation;
    private Boolean actif;
}