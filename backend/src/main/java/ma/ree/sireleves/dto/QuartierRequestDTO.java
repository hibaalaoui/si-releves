package ma.ree.sireleves.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuartierRequestDTO {

    @NotBlank(message = "Le nom du quartier est obligatoire")
    private String nomQuartier;

    private String ville = "Rabat";
}