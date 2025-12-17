package ma.ree.sireleves.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.ree.sireleves.entity.Adresse;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdresseRequestDTO {

    @NotBlank(message = "L'ID du client est obligatoire")
    private String idClient;

    @NotNull(message = "Le quartier est obligatoire")
    private Integer idQuartier;

    @NotBlank(message = "L'adresse complète est obligatoire")
    private String adresseComplete;

    @NotNull(message = "Le type de bien est obligatoire")
    private Adresse.TypeBien typeBien;

    private String codePostal;
}