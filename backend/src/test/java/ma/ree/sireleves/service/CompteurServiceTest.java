package ma.ree.sireleves.service;

import ma.ree.sireleves.dto.CompteurDetailDTO;
import ma.ree.sireleves.dto.CompteurRequestDTO;
import ma.ree.sireleves.dto.CompteurResponseDTO;
import ma.ree.sireleves.entity.Adresse;
import ma.ree.sireleves.entity.Compteur;
import ma.ree.sireleves.exception.ResourceNotFoundException;
import ma.ree.sireleves.mapper.CompteurMapper;
import ma.ree.sireleves.repository.AdresseRepository;
import ma.ree.sireleves.repository.CompteurRepository;
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
@DisplayName("Tests unitaires du service CompteurService")
class CompteurServiceTest {

    @Mock
    private CompteurRepository compteurRepository;

    @Mock
    private AdresseRepository adresseRepository;

    @Mock
    private CompteurMapper compteurMapper;

    @InjectMocks
    private CompteurService compteurService;

    @Test
    @DisplayName("Devrait récupérer tous les compteurs")
    void shouldGetAllCompteurs() {
        // Given
        List<Compteur> compteurs = List.of(createCompteur());
        CompteurResponseDTO responseDTO = createCompteurResponseDTO();

        when(compteurRepository.findAll()).thenReturn(compteurs);
        when(compteurMapper.toResponseDTO(any(Compteur.class))).thenReturn(responseDTO);

        // When
        List<CompteurResponseDTO> result = compteurService.getAllCompteurs();

        // Then
        assertThat(result).hasSize(1);
        verify(compteurRepository).findAll();
    }

    @Test
    @DisplayName("Devrait récupérer un compteur par son ID")
    void shouldGetCompteurById() {
        // Given
        String id = "CMP001";
        Compteur compteur = createCompteur();
        CompteurDetailDTO detailDTO = createCompteurDetailDTO();

        when(compteurRepository.findById(id)).thenReturn(Optional.of(compteur));
        when(compteurMapper.toDetailDTO(compteur)).thenReturn(detailDTO);

        // When
        CompteurDetailDTO result = compteurService.getCompteurById(id);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getIdCompteur()).isEqualTo(id);
        verify(compteurRepository).findById(id);
    }

    @Test
    @DisplayName("Devrait lever exception pour compteur inexistant")
    void shouldThrowExceptionForNonExistentCompteur() {
        // Given
        String id = "CMP999";
        when(compteurRepository.findById(id)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> compteurService.getCompteurById(id))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    // Méthodes utilitaires
    private Compteur createCompteur() {
        Compteur compteur = new Compteur();
        compteur.setIdCompteur("CMP001");
        compteur.setType(Compteur.TypeCompteur.Eau);
        compteur.setAdresse(createAdresse());
        return compteur;
    }

    private Adresse createAdresse() {
        Adresse adresse = new Adresse();
        adresse.setIdAdresse(1);
        adresse.setAdresseComplete("123 Rue Test");
        return adresse;
    }

    private CompteurResponseDTO createCompteurResponseDTO() {
        CompteurResponseDTO dto = new CompteurResponseDTO();
        dto.setIdCompteur("CMP001");
        dto.setIdAdresse(1);
        dto.setAdresseComplete("123 Rue Test");
        dto.setType(Compteur.TypeCompteur.Eau);
        return dto;
    }

    private CompteurDetailDTO createCompteurDetailDTO() {
        CompteurDetailDTO dto = new CompteurDetailDTO();
        dto.setIdCompteur("CMP001");
        dto.setIdAdresse(1);
        dto.setAdresseComplete("123 Rue Test");
        dto.setType(Compteur.TypeCompteur.Eau);
        return dto;
    }
}
