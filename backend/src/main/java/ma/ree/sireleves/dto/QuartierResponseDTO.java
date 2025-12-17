package ma.ree.sireleves.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuartierResponseDTO {

    private Integer idQuartier;
    private String nomQuartier;
    private String ville;
}