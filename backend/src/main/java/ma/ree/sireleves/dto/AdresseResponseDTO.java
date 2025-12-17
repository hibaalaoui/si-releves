package ma.ree.sireleves.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.ree.sireleves.entity.Adresse;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdresseResponseDTO {

    private Integer idAdresse;
    private String idClient;
    private String nomClient;
    private String prenomClient;
    private Integer idQuartier;
    private String nomQuartier;
    private String adresseComplete;
    private Adresse.TypeBien typeBien;
    private String codePostal;
    private LocalDateTime dateCreation;
    private Integer nombreCompteurs;
}