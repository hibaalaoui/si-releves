package ma.ree.sireleves.mapper;

import ma.ree.sireleves.dto.AdresseRequestDTO;
import ma.ree.sireleves.dto.AdresseResponseDTO;
import ma.ree.sireleves.entity.Adresse;
import ma.ree.sireleves.entity.Client;
import ma.ree.sireleves.entity.Quartier;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class AdresseMapper {

    /**
     * Convertir RequestDTO vers Entity (pour création)
     */
    public Adresse toEntity(AdresseRequestDTO dto, Client client, Quartier quartier) {
        Adresse adresse = new Adresse();
        adresse.setClient(client);
        adresse.setQuartier(quartier);
        adresse.setAdresseComplete(dto.getAdresseComplete());
        adresse.setTypeBien(dto.getTypeBien());
        adresse.setCodePostal(dto.getCodePostal());
        adresse.setDateCreation(LocalDateTime.now());
        return adresse;
    }

    /**
     * Convertir Entity vers ResponseDTO
     */
    public AdresseResponseDTO toResponseDTO(Adresse entity) {
        return new AdresseResponseDTO(
                entity.getIdAdresse(),
                entity.getClient().getIdClient(),
                entity.getClient().getNom(),
                entity.getClient().getPrenom(),
                entity.getQuartier().getIdQuartier(),
                entity.getQuartier().getNomQuartier(),
                entity.getAdresseComplete(),
                entity.getTypeBien(),
                entity.getCodePostal(),
                entity.getDateCreation(),
                entity.getCompteurs() != null ? entity.getCompteurs().size() : 0
        );
    }
}