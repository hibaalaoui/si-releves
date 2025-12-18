package ma.ree.sireleves.controller;

import ma.ree.sireleves.dto.AgentRequestDTO;
import ma.ree.sireleves.dto.AgentResponseDTO;
import ma.ree.sireleves.dto.AgentUpdateDTO;
import ma.ree.sireleves.service.AgentService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests unitaires du contrôleur AgentController")
class AgentControllerTest {

    @Mock
    private AgentService agentService;

    @InjectMocks
    private AgentController agentController;

    @Test
    @DisplayName("Devrait créer un agent")
    void shouldCreateAgent() {
        // Given
        AgentRequestDTO requestDTO = createAgentRequestDTO();
        AgentResponseDTO responseDTO = createAgentResponseDTO();

        when(agentService.creerAgent(requestDTO)).thenReturn(responseDTO);

        // When
        ResponseEntity<AgentResponseDTO> response = agentController.creerAgent(requestDTO);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isEqualTo(responseDTO);
        verify(agentService).creerAgent(requestDTO);
    }

    @Test
    @DisplayName("Devrait récupérer tous les agents")
    void shouldGetAllAgents() {
        // Given
        List<AgentResponseDTO> agents = List.of(createAgentResponseDTO());
        when(agentService.getAllAgents()).thenReturn(agents);

        // When
        ResponseEntity<List<AgentResponseDTO>> response = agentController.getAllAgents(null, null);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(1);
        verify(agentService).getAllAgents();
    }

    @Test
    @DisplayName("Devrait récupérer un agent par son ID")
    void shouldGetAgentById() {
        // Given
        String id = "AGT001";
        AgentResponseDTO responseDTO = createAgentResponseDTO();
        when(agentService.getAgentById(id)).thenReturn(responseDTO);

        // When
        ResponseEntity<AgentResponseDTO> response = agentController.getAgentById(id);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(responseDTO);
        verify(agentService).getAgentById(id);
    }

    @Test
    @DisplayName("Devrait mettre à jour un agent")
    void shouldUpdateAgent() {
        // Given
        String id = "AGT001";
        AgentUpdateDTO updateDTO = new AgentUpdateDTO();
        AgentResponseDTO responseDTO = createAgentResponseDTO();

        when(agentService.updateAgent(id, updateDTO)).thenReturn(responseDTO);

        // When
        ResponseEntity<AgentResponseDTO> response = agentController.updateAgent(id, updateDTO);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(responseDTO);
        verify(agentService).updateAgent(id, updateDTO);
    }

    @Test
    @DisplayName("Devrait basculer le statut d'un agent")
    void shouldToggleAgentStatus() {
        // Given
        String id = "AGT001";
        AgentResponseDTO responseDTO = createAgentResponseDTO();

        when(agentService.toggleAgentStatus(id)).thenReturn(responseDTO);

        // When
        ResponseEntity<AgentResponseDTO> response = agentController.toggleAgentStatus(id);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(responseDTO);
        verify(agentService).toggleAgentStatus(id);
    }

    // Méthodes utilitaires
    private AgentRequestDTO createAgentRequestDTO() {
        AgentRequestDTO dto = new AgentRequestDTO();
        dto.setIdAgent("AGT001");
        dto.setIdQuartier(1);
        dto.setNom("Dupont");
        return dto;
    }

    private AgentResponseDTO createAgentResponseDTO() {
        AgentResponseDTO dto = new AgentResponseDTO();
        dto.setIdAgent("AGT001");
        dto.setNom("Dupont");
        return dto;
    }
}
