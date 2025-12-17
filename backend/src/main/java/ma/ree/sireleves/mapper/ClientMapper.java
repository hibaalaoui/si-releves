package ma.ree.sireleves.mapper;

import ma.ree.sireleves.dto.ClientResponseDTO;
import ma.ree.sireleves.entity.Client;
import org.springframework.stereotype.Component;

@Component
public class ClientMapper {

    /**
     * Convertir Entity vers ResponseDTO
     */
    public ClientResponseDTO toResponseDTO(Client entity) {
        return new ClientResponseDTO(
                entity.getIdClient(),
                entity.getNom(),
                entity.getPrenom(),
                entity.getDateCreation(),
                entity.getAdresses() != null ? entity.getAdresses().size() : 0
        );
    }
}