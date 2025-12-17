package ma.ree.sireleves.mapper;

import ma.ree.sireleves.dto.CompteurDetailDTO;
import ma.ree.sireleves.dto.CompteurRequestDTO;
import ma.ree.sireleves.dto.CompteurResponseDTO;
import ma.ree.sireleves.entity.Adresse;
import ma.ree.sireleves.entity.Compteur;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;

@Component
public class CompteurMapper {

    /**
     * Convertir RequestDTO vers Entity (pour création)
     */
    public Compteur toEntity(CompteurRequestDTO dto, Adresse adresse, String idCompteur) {
        Compteur compteur = new Compteur();
        compteur.setIdCompteur(idCompteur);
        compteur.setAdresse(adresse);
        compteur.setType(dto.getType());
        compteur.setIndexActuel(BigDecimal.ZERO);
        compteur.setDateInstallation(dto.getDateInstallation() != null ? dto.getDateInstallation() : LocalDate.now());
        compteur.setPourEspacesCommuns(dto.getPourEspacesCommuns());
        compteur.setActif(true);
        return compteur;
    }

    /**
     * Convertir Entity vers ResponseDTO
     */
    public CompteurResponseDTO toResponseDTO(Compteur entity) {
        return new CompteurResponseDTO(
                entity.getIdCompteur(),
                entity.getAdresse().getIdAdresse(),
                entity.getAdresse().getAdresseComplete(),
                entity.getAdresse().getQuartier().getNomQuartier(),
                entity.getType(),
                entity.getIndexActuel(),
                entity.getDateInstallation(),
                entity.getDateDerniereReleve(),
                entity.getPourEspacesCommuns(),
                entity.getActif()
        );
    }

    /**
     * Convertir Entity vers DetailDTO (avec infos client)
     */
    public CompteurDetailDTO toDetailDTO(Compteur entity) {
        return new CompteurDetailDTO(
                entity.getIdCompteur(),
                entity.getAdresse().getIdAdresse(),
                entity.getAdresse().getAdresseComplete(),
                entity.getAdresse().getQuartier().getNomQuartier(),
                entity.getAdresse().getClient().getNom(),
                entity.getAdresse().getClient().getPrenom(),
                entity.getType(),
                entity.getIndexActuel(),
                entity.getDateInstallation(),
                entity.getDateDerniereReleve(),
                entity.getPourEspacesCommuns(),
                entity.getActif()
        );
    }
}