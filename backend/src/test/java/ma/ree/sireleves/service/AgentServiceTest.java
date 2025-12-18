package ma.ree.sireleves.service;

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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests unitaires du service AgentService")
class AgentServiceTest {

    @Mock
    private AgentRepository agentRepository;

    @Mock
    private QuartierRepository quartierRepository;

    @Mock
    private AgentMapper agentMapper;

    @InjectMocks
    private AgentService agentService;

    @Test
    @DisplayName("Devrait créer un agent valide")
    void shouldCreateValidAgent() {
        // Given
        AgentRequestDTO requestDTO = createAgentRequestDTO();
        Quartier quartier = createQuartier();
        Agent savedAgent = createAgent();
        AgentResponseDTO expectedResponse = createAgentResponseDTO();

        when(agentRepository.existsById("AGT001")).thenReturn(false);
        when(quartierRepository.findById(1)).thenReturn(Optional.of(quartier));
        when(agentMapper.toEntity(requestDTO, quartier)).thenReturn(savedAgent);
        when(agentRepository.save(any(Agent.class))).thenReturn(savedAgent);
        when(agentMapper.toResponseDTO(savedAgent)).thenReturn(expectedResponse);

        // When
        AgentResponseDTO result = agentService.creerAgent(requestDTO);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getIdAgent()).isEqualTo("AGT001");
        verify(agentRepository).existsById("AGT001");
        verify(agentRepository).save(any(Agent.class));
    }

    @Test
    @DisplayName("Devrait lever exception si agent existe déjà")
    void shouldThrowExceptionIfAgentExists() {
        // Given
        AgentRequestDTO requestDTO = createAgentRequestDTO();
        when(agentRepository.existsById("AGT001")).thenReturn(true);

        // When & Then
        assertThatThrownBy(() -> agentService.creerAgent(requestDTO))
                .isInstanceOf(BusinessException.class)
                .hasMessage("Un agent avec cet ID existe déjà");
    }

    @Test
    @DisplayName("Devrait récupérer tous les agents")
    void shouldGetAllAgents() {
        // Given
        List<Agent> agents = List.of(createAgent());
        AgentResponseDTO responseDTO = createAgentResponseDTO();

        when(agentRepository.findAll()).thenReturn(agents);
        when(agentMapper.toResponseDTO(any(Agent.class))).thenReturn(responseDTO);

        // When
        List<AgentResponseDTO> result = agentService.getAllAgents();

        // Then
        assertThat(result).hasSize(1);
        verify(agentRepository).findAll();
    }

    @Test
    @DisplayName("Devrait récupérer un agent par son ID")
    void shouldGetAgentById() {
        // Given
        String id = "AGT001";
        Agent agent = createAgent();
        AgentResponseDTO responseDTO = createAgentResponseDTO();

        when(agentRepository.findById(id)).thenReturn(Optional.of(agent));
        when(agentMapper.toResponseDTO(agent)).thenReturn(responseDTO);

        // When
        AgentResponseDTO result = agentService.getAgentById(id);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getIdAgent()).isEqualTo(id);
        verify(agentRepository).findById(id);
    }

    @Test
    @DisplayName("Devrait basculer le statut d'un agent")
    void shouldToggleAgentStatus() {
        // Given
        String id = "AGT001";
        Agent agent = createAgent();
        agent.setActif(true);
        AgentResponseDTO responseDTO = createAgentResponseDTO();

        when(agentRepository.findById(id)).thenReturn(Optional.of(agent));
        when(agentRepository.save(agent)).thenReturn(agent);
        when(agentMapper.toResponseDTO(agent)).thenReturn(responseDTO);

        // When
        AgentResponseDTO result = agentService.toggleAgentStatus(id);

        // Then
        assertThat(result).isNotNull();
        assertThat(agent.getActif()).isFalse();
        verify(agentRepository).save(agent);
    }

    // Méthodes utilitaires
    private AgentRequestDTO createAgentRequestDTO() {
        AgentRequestDTO dto = new AgentRequestDTO();
        dto.setIdAgent("AGT001");
        dto.setIdQuartier(1);
        dto.setNom("Dupont");
        dto.setPrenom("Marie");
        dto.setTelProfessionnel("0145678901");
        return dto;
    }

    private Quartier createQuartier() {
        Quartier quartier = new Quartier();
        quartier.setIdQuartier(1);
        quartier.setNomQuartier("Centre-ville");
        return quartier;
    }

    private Agent createAgent() {
        Agent agent = new Agent();
        agent.setIdAgent("AGT001");
        agent.setNom("DUPONT");
        agent.setPrenom("Marie");
        agent.setTelPersonnel("0612345678");
        agent.setTelProfessionnel("0145678901");
        agent.setActif(true);
        agent.setQuartier(createQuartier());
        return agent;
    }

    private AgentResponseDTO createAgentResponseDTO() {
        AgentResponseDTO dto = new AgentResponseDTO();
        dto.setIdAgent("AGT001");
        dto.setIdQuartier(1);
        dto.setNomQuartier("Centre-ville");
        dto.setNom("DUPONT");
        dto.setPrenom("Marie");
        dto.setTelPersonnel("0612345678");
        dto.setTelProfessionnel("0145678901");
        dto.setActif(true);
        return dto;
    }
}
