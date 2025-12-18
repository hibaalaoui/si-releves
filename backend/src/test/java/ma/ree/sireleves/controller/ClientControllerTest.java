package ma.ree.sireleves.controller;

import ma.ree.sireleves.dto.ClientResponseDTO;
import ma.ree.sireleves.service.ClientService;
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
@DisplayName("Tests unitaires du contrôleur ClientController")
class ClientControllerTest {

    @Mock
    private ClientService clientService;

    @InjectMocks
    private ClientController clientController;

    @Test
    @DisplayName("Devrait récupérer tous les clients")
    void shouldGetAllClients() {
        // Given
        List<ClientResponseDTO> clients = List.of(createClientResponseDTO());
        when(clientService.getAllClients()).thenReturn(clients);

        // When
        ResponseEntity<List<ClientResponseDTO>> response = clientController.getAllClients(null, null);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(1);
        verify(clientService).getAllClients();
    }

    @Test
    @DisplayName("Devrait récupérer un client par son ID")
    void shouldGetClientById() {
        // Given
        String id = "CLI001";
        ClientResponseDTO responseDTO = createClientResponseDTO();
        when(clientService.getClientById(id)).thenReturn(responseDTO);

        // When
        ResponseEntity<ClientResponseDTO> response = clientController.getClientById(id);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(responseDTO);
        verify(clientService).getClientById(id);
    }

    // Méthodes utilitaires
    private ClientResponseDTO createClientResponseDTO() {
        ClientResponseDTO dto = new ClientResponseDTO();
        dto.setIdClient("CLI001");
        dto.setNom("Dupont");
        dto.setPrenom("Jean");
        return dto;
    }
}
