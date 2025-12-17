package ma.ree.sireleves.mapper;

import ma.ree.sireleves.dto.QuartierRequestDTO;
import ma.ree.sireleves.dto.QuartierResponseDTO;
import ma.ree.sireleves.entity.Quartier;
import org.springframework.stereotype.Component;

@Component
public class QuartierMapper {

    /**
     * Convertir RequestDTO vers Entity (pour création)
     */
    public Quartier toEntity(QuartierRequestDTO dto) {
        Quartier quartier = new Quartier();
        quartier.setNomQuartier(dto.getNomQuartier());
        quartier.setVille(dto.getVille() != null ? dto.getVille() : "Rabat");
        return quartier;
    }

    /**
     * Convertir Entity vers ResponseDTO
     */
    public QuartierResponseDTO toResponseDTO(Quartier entity) {
        return new QuartierResponseDTO(
                entity.getIdQuartier(),
                entity.getNomQuartier(),
                entity.getVille()
        );
    }

    /**
     * Mettre à jour une entité existante avec RequestDTO
     */
    public void updateEntityFromDTO(Quartier entity, QuartierRequestDTO dto) {
        entity.setNomQuartier(dto.getNomQuartier());
        entity.setVille(dto.getVille() != null ? dto.getVille() : "Rabat");
    }
}