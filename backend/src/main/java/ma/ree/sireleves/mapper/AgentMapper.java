package ma.ree.sireleves.mapper;

import ma.ree.sireleves.dto.AgentRequestDTO;
import ma.ree.sireleves.dto.AgentResponseDTO;
import ma.ree.sireleves.dto.AgentUpdateDTO;
import ma.ree.sireleves.entity.Agent;
import ma.ree.sireleves.entity.Quartier;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class AgentMapper {

    /**
     * Convertir RequestDTO vers Entity (pour création)
     */
    public Agent toEntity(AgentRequestDTO dto, Quartier quartier) {
        Agent agent = new Agent();
        agent.setIdAgent(dto.getIdAgent());
        agent.setQuartier(quartier);
        agent.setNom(dto.getNom().toUpperCase());
        agent.setPrenom(capitalizeFirstLetter(dto.getPrenom()));
        agent.setTelPersonnel(dto.getTelPersonnel());
        agent.setTelProfessionnel(dto.getTelProfessionnel());
        agent.setDateAffectation(LocalDateTime.now());
        agent.setActif(true);
        return agent;
    }

    /**
     * Convertir Entity vers ResponseDTO
     */
    public AgentResponseDTO toResponseDTO(Agent entity) {
        return new AgentResponseDTO(
                entity.getIdAgent(),
                entity.getQuartier().getIdQuartier(),
                entity.getQuartier().getNomQuartier(),
                entity.getNom(),
                entity.getPrenom(),
                entity.getTelPersonnel(),
                entity.getTelProfessionnel(),
                entity.getDateAffectation(),
                entity.getActif()
        );
    }

    /**
     * Mettre à jour une entité existante avec UpdateDTO
     */
    public void updateEntityFromDTO(Agent entity, AgentUpdateDTO dto, Quartier quartier) {
        entity.setQuartier(quartier);
        entity.setNom(dto.getNom().toUpperCase());
        entity.setPrenom(capitalizeFirstLetter(dto.getPrenom()));
        entity.setTelPersonnel(dto.getTelPersonnel());
        entity.setTelProfessionnel(dto.getTelProfessionnel());
    }

    /**
     * Méthode utilitaire pour mettre en majuscule la première lettre
     */
    private String capitalizeFirstLetter(String text) {
        if (text == null || text.isEmpty()) {
            return text;
        }
        return text.substring(0, 1).toUpperCase() + text.substring(1).toLowerCase();
    }
}