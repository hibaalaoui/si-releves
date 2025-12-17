package ma.ree.sireleves.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.ree.sireleves.entity.Compteur;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompteurRequestDTO {

    @NotNull(message = "L'adresse est obligatoire")
    private Integer idAdresse;

    @NotNull(message = "Le type de compteur est obligatoire")
    private Compteur.TypeCompteur type;

    private LocalDate dateInstallation;

    private Boolean pourEspacesCommuns = false;
}