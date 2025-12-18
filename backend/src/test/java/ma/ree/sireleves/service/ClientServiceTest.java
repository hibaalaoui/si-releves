package ma.ree.sireleves.service;

import ma.ree.sireleves.dto.ClientResponseDTO;
import ma.ree.sireleves.entity.Client;
import ma.ree.sireleves.exception.ResourceNotFoundException;
import ma.ree.sireleves.mapper.ClientMapper;
import ma.ree.sireleves.repository.ClientRepository;
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
@DisplayName("Tests unitaires du service ClientService")
class ClientServiceTest {

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private ClientMapper clientMapper;

    @InjectMocks
    private ClientService clientService;

    @Test
    @DisplayName("Devrait récupérer tous les clients")
    void shouldGetAllClients() {
        // Given
        List<Client> clients = List.of(createClient());
        ClientResponseDTO responseDTO = createClientResponseDTO();

        when(clientRepository.findAll()).thenReturn(clients);
        when(clientMapper.toResponseDTO(any(Client.class))).thenReturn(responseDTO);

        // When
        List<ClientResponseDTO> result = clientService.getAllClients();

        // Then
        assertThat(result).hasSize(1);
        verify(clientRepository).findAll();
    }

    @Test
    @DisplayName("Devrait récupérer un client par son ID")
    void shouldGetClientById() {
        // Given
        String id = "CLI001";
        Client client = createClient();
        ClientResponseDTO responseDTO = createClientResponseDTO();

        when(clientRepository.findById(id)).thenReturn(Optional.of(client));
        when(clientMapper.toResponseDTO(client)).thenReturn(responseDTO);

        // When
        ClientResponseDTO result = clientService.getClientById(id);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getIdClient()).isEqualTo(id);
        verify(clientRepository).findById(id);
    }

    @Test
    @DisplayName("Devrait lever exception pour client inexistant")
    void shouldThrowExceptionForNonExistentClient() {
        // Given
        String id = "CLI999";
        when(clientRepository.findById(id)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> clientService.getClientById(id))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    @DisplayName("Devrait rechercher des clients par nom")
    void shouldSearchClientsByNom() {
        // Given
        String nom = "Dupont";
        List<Client> clients = List.of(createClient());
        ClientResponseDTO responseDTO = createClientResponseDTO();

        when(clientRepository.findByNomContainingIgnoreCase(nom)).thenReturn(clients);
        when(clientMapper.toResponseDTO(any(Client.class))).thenReturn(responseDTO);

        // When
        List<ClientResponseDTO> result = clientService.searchClientsByNom(nom);

        // Then
        assertThat(result).hasSize(1);
        verify(clientRepository).findByNomContainingIgnoreCase(nom);
    }

    // Méthodes utilitaires
    private Client createClient() {
        Client client = new Client();
        client.setIdClient("CLI001");
        client.setNom("Dupont");
        client.setPrenom("Jean");
        return client;
    }

    private ClientResponseDTO createClientResponseDTO() {
        ClientResponseDTO dto = new ClientResponseDTO();
        dto.setIdClient("CLI001");
        dto.setNom("Dupont");
        dto.setPrenom("Jean");
        dto.setNombreAdresses(2);
        return dto;
    }
}
