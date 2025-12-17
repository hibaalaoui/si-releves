package ma.ree.sireleves.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AgentRequestDTO {

    @NotBlank(message = "L'ID de l'agent est obligatoire")
    private String idAgent;

    @NotNull(message = "Le quartier est obligatoire")
    private Integer idQuartier;

    @NotBlank(message = "Le nom est obligatoire")
    private String nom;

    @NotBlank(message = "Le prénom est obligatoire")
    private String prenom;

    private String telPersonnel;

    @NotBlank(message = "Le téléphone professionnel est obligatoire")
    private String telProfessionnel;
}