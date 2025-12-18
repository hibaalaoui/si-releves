package ma.ree.sireleves.service;

import ma.ree.sireleves.dto.QuartierRequestDTO;
import ma.ree.sireleves.dto.QuartierResponseDTO;
import ma.ree.sireleves.entity.Quartier;
import ma.ree.sireleves.exception.BusinessException;
import ma.ree.sireleves.exception.ResourceNotFoundException;
import ma.ree.sireleves.mapper.QuartierMapper;
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
@DisplayName("Tests unitaires du service QuartierService")
class QuartierServiceTest {

    @Mock
    private QuartierRepository quartierRepository;

    @Mock
    private QuartierMapper quartierMapper;

    @InjectMocks
    private QuartierService quartierService;

    @Test
    @DisplayName("Devrait récupérer tous les quartiers")
    void shouldGetAllQuartiers() {
        // Given
        List<Quartier> quartiers = List.of(createQuartier());
        QuartierResponseDTO responseDTO = createQuartierResponseDTO();

        when(quartierRepository.findAll()).thenReturn(quartiers);
        when(quartierMapper.toResponseDTO(any(Quartier.class))).thenReturn(responseDTO);

        // When
        List<QuartierResponseDTO> result = quartierService.getAllQuartiers();

        // Then
        assertThat(result).hasSize(1);
        verify(quartierRepository).findAll();
    }

    @Test
    @DisplayName("Devrait récupérer un quartier par son ID")
    void shouldGetQuartierById() {
        // Given
        Integer id = 1;
        Quartier quartier = createQuartier();
        QuartierResponseDTO responseDTO = createQuartierResponseDTO();

        when(quartierRepository.findById(id)).thenReturn(Optional.of(quartier));
        when(quartierMapper.toResponseDTO(quartier)).thenReturn(responseDTO);

        // When
        QuartierResponseDTO result = quartierService.getQuartierById(id);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getIdQuartier()).isEqualTo(id);
        verify(quartierRepository).findById(id);
    }

    @Test
    @DisplayName("Devrait lever exception pour quartier inexistant")
    void shouldThrowExceptionForNonExistentQuartier() {
        // Given
        Integer id = 999;
        when(quartierRepository.findById(id)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> quartierService.getQuartierById(id))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    // Méthodes utilitaires
    private Quartier createQuartier() {
        Quartier quartier = new Quartier();
        quartier.setIdQuartier(1);
        quartier.setNomQuartier("Centre-ville");
        quartier.setVille("Rabat");
        return quartier;
    }

    private QuartierResponseDTO createQuartierResponseDTO() {
        QuartierResponseDTO dto = new QuartierResponseDTO();
        dto.setIdQuartier(1);
        dto.setNomQuartier("Centre-ville");
        dto.setVille("Rabat");
        return dto;
    }
}
