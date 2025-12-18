package ma.ree.sireleves.controller;

import ma.ree.sireleves.dto.AdresseRequestDTO;
import ma.ree.sireleves.dto.AdresseResponseDTO;
import ma.ree.sireleves.entity.Adresse;
import ma.ree.sireleves.service.AdresseService;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests unitaires du contrôleur AdresseController")
class AdresseControllerTest {

    @Mock
    private AdresseService adresseService;

    @InjectMocks
    private AdresseController adresseController;

    @Test
    @DisplayName("Devrait créer une adresse")
    void shouldCreateAdresse() {
        // Given
        AdresseRequestDTO requestDTO = createAdresseRequestDTO();
        AdresseResponseDTO responseDTO = createAdresseResponseDTO();

        when(adresseService.creerAdresse(requestDTO)).thenReturn(responseDTO);

        // When
        ResponseEntity<AdresseResponseDTO> response = adresseController.creerAdresse(requestDTO);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isEqualTo(responseDTO);
        verify(adresseService).creerAdresse(requestDTO);
    }

    @Test
    @DisplayName("Devrait récupérer toutes les adresses")
    void shouldGetAllAdresses() {
        // Given
        List<AdresseResponseDTO> adresses = List.of(createAdresseResponseDTO());
        when(adresseService.getAllAdresses()).thenReturn(adresses);

        // When
        ResponseEntity<List<AdresseResponseDTO>> response = adresseController.getAllAdresses(null, null, null);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(1);
        verify(adresseService).getAllAdresses();
    }

    @Test
    @DisplayName("Devrait récupérer une adresse par son ID")
    void shouldGetAdresseById() {
        // Given
        Integer id = 1;
        AdresseResponseDTO responseDTO = createAdresseResponseDTO();
        when(adresseService.getAdresseById(id)).thenReturn(responseDTO);

        // When
        ResponseEntity<AdresseResponseDTO> response = adresseController.getAdresseById(id);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(responseDTO);
        verify(adresseService).getAdresseById(id);
    }

    // Méthodes utilitaires
    private AdresseRequestDTO createAdresseRequestDTO() {
        AdresseRequestDTO dto = new AdresseRequestDTO();
        dto.setIdClient("CLI001");
        dto.setIdQuartier(1);
        dto.setAdresseComplete("123 Rue Test");
        return dto;
    }

    private AdresseResponseDTO createAdresseResponseDTO() {
        AdresseResponseDTO dto = new AdresseResponseDTO();
        dto.setIdAdresse(1);
        dto.setIdClient("CLI001");
        dto.setAdresseComplete("123 Rue Test");
        return dto;
    }
}
