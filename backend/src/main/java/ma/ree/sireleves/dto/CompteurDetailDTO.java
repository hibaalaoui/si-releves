package ma.ree.sireleves.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.ree.sireleves.entity.Compteur;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompteurDetailDTO {

    private String idCompteur;
    private Integer idAdresse;
    private String adresseComplete;
    private String nomQuartier;
    private String nomClient;
    private String prenomClient;
    private Compteur.TypeCompteur type;
    private BigDecimal indexActuel;
    private LocalDate dateInstallation;
    private LocalDateTime dateDerniereReleve;
    private Boolean pourEspacesCommuns;
    private Boolean actif;
}