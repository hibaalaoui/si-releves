package ma.ree.sireleves.controller;

import ma.ree.sireleves.dto.CompteurDetailDTO;
import ma.ree.sireleves.dto.CompteurRequestDTO;
import ma.ree.sireleves.dto.CompteurResponseDTO;
import ma.ree.sireleves.entity.Compteur;
import ma.ree.sireleves.service.CompteurService;
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
@DisplayName("Tests unitaires du contrôleur CompteurController")
class CompteurControllerTest {

    @Mock
    private CompteurService compteurService;

    @InjectMocks
    private CompteurController compteurController;

    @Test
    @DisplayName("Devrait créer un compteur")
    void shouldCreateCompteur() {
        // Given
        CompteurRequestDTO requestDTO = createCompteurRequestDTO();
        CompteurResponseDTO responseDTO = createCompteurResponseDTO();

        when(compteurService.creerCompteur(requestDTO)).thenReturn(responseDTO);

        // When
        ResponseEntity<CompteurResponseDTO> response = compteurController.creerCompteur(requestDTO);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isEqualTo(responseDTO);
        verify(compteurService).creerCompteur(requestDTO);
    }

    @Test
    @DisplayName("Devrait récupérer tous les compteurs")
    void shouldGetAllCompteurs() {
        // Given
        List<CompteurResponseDTO> compteurs = List.of(createCompteurResponseDTO());
        when(compteurService.getAllCompteurs()).thenReturn(compteurs);

        // When
        ResponseEntity<List<CompteurResponseDTO>> response = compteurController.getAllCompteurs(null, null, null);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(1);
        verify(compteurService).getAllCompteurs();
    }

    @Test
    @DisplayName("Devrait récupérer un compteur par son ID")
    void shouldGetCompteurById() {
        // Given
        String id = "CMP001";
        CompteurDetailDTO detailDTO = createCompteurDetailDTO();
        when(compteurService.getCompteurById(id)).thenReturn(detailDTO);

        // When
        ResponseEntity<CompteurDetailDTO> response = compteurController.getCompteurById(id);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(detailDTO);
        verify(compteurService).getCompteurById(id);
    }

    // Méthodes utilitaires
    private CompteurRequestDTO createCompteurRequestDTO() {
        CompteurRequestDTO dto = new CompteurRequestDTO();
        dto.setIdAdresse(1);
        dto.setType(Compteur.TypeCompteur.Eau);
        return dto;
    }

    private CompteurResponseDTO createCompteurResponseDTO() {
        CompteurResponseDTO dto = new CompteurResponseDTO();
        dto.setIdCompteur("CMP001");
        dto.setType(Compteur.TypeCompteur.Eau);
        return dto;
    }

    private CompteurDetailDTO createCompteurDetailDTO() {
        CompteurDetailDTO dto = new CompteurDetailDTO();
        dto.setIdCompteur("CMP001");
        dto.setType(Compteur.TypeCompteur.Eau);
        return dto;
    }
}
