package ma.ree.sireleves.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.ree.sireleves.entity.Compteur;
import ma.ree.sireleves.entity.Releve;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReleveResponseDTO {

    private Integer idReleve;
    private String idCompteur;
    private Compteur.TypeCompteur typeCompteur;
    private String idAgent;
    private String nomAgent;
    private String prenomAgent;
    private String adresseComplete;
    private String nomQuartier;
    private LocalDateTime dateReleve;
    private BigDecimal ancienIndex;
    private BigDecimal nouvelIndex;
    private BigDecimal consommation;
    private Releve.Unite unite;
    private Boolean envoyeFacturation;
}