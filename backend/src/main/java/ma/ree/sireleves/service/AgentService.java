package ma.ree.sireleves.service;

import lombok.RequiredArgsConstructor;
import ma.ree.sireleves.dto.AgentRequestDTO;
import ma.ree.sireleves.dto.AgentResponseDTO;
import ma.ree.sireleves.dto.AgentUpdateDTO;
import ma.ree.sireleves.entity.Agent;
import ma.ree.sireleves.entity.Quartier;
import ma.ree.sireleves.exception.BusinessException;
import ma.ree.sireleves.exception.ResourceNotFoundException;
import ma.ree.sireleves.mapper.AgentMapper;
import ma.ree.sireleves.repository.AgentRepository;
import ma.ree.sireleves.repository.QuartierRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class AgentService {

    private final AgentRepository agentRepository;
    private final QuartierRepository quartierRepository;
    private final AgentMapper agentMapper;

    /**
     * Créer un nouvel agent et l'affecter à un quartier
     */
    public AgentResponseDTO creerAgent(AgentRequestDTO requestDTO) {
        // Vérifier si l'agent existe déjà
        if (agentRepository.existsById(requestDTO.getIdAgent())) {
            throw new BusinessException("Un agent avec cet ID existe déjà");
        }

        // Vérifier que le quartier existe
        Quartier quartier = quartierRepository.findById(requestDTO.getIdQuartier())
                .orElseThrow(() -> new ResourceNotFoundException("Quartier", "id", requestDTO.getIdQuartier()));

        // Créer l'entité
        Agent agent = agentMapper.toEntity(requestDTO, quartier);

        // Sauvegarder
        Agent savedAgent = agentRepository.save(agent);

        return agentMapper.toResponseDTO(savedAgent);
    }

    /**
     * Récupérer tous les agents
     */
    @Transactional(readOnly = true)
    public List<AgentResponseDTO> getAllAgents() {
        return agentRepository.findAll().stream()
                .map(agentMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Récupérer un agent par son ID
     */
    @Transactional(readOnly = true)
    public AgentResponseDTO getAgentById(String id) {
        Agent agent = agentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Agent", "id", id));
        return agentMapper.toResponseDTO(agent);
    }

    /**
     * Mettre à jour un agent (y compris changement de quartier)
     */
    public AgentResponseDTO updateAgent(String id, AgentUpdateDTO updateDTO) {
        Agent agent = agentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Agent", "id", id));

        // Vérifier que le quartier existe
        Quartier quartier = quartierRepository.findById(updateDTO.getIdQuartier())
                .orElseThrow(() -> new ResourceNotFoundException("Quartier", "id", updateDTO.getIdQuartier()));

        // Mettre à jour l'entité
        agentMapper.updateEntityFromDTO(agent, updateDTO, quartier);

        Agent updatedAgent = agentRepository.save(agent);
        return agentMapper.toResponseDTO(updatedAgent);
    }

    /**
     * Récupérer les agents d'un quartier
     */
    @Transactional(readOnly = true)
    public List<AgentResponseDTO> getAgentsByQuartier(Integer idQuartier) {
        return agentRepository.findByQuartierIdQuartier(idQuartier).stream()
                .map(agentMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Récupérer les agents actifs
     */
    @Transactional(readOnly = true)
    public List<AgentResponseDTO> getActiveAgents() {
        return agentRepository.findByActifTrue().stream()
                .map(agentMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Activer/Désactiver un agent
     */
    public AgentResponseDTO toggleAgentStatus(String id) {
        Agent agent = agentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Agent", "id", id));

        agent.setActif(!agent.getActif());

        Agent updatedAgent = agentRepository.save(agent);
        return agentMapper.toResponseDTO(updatedAgent);
    }
}