package ma.ree.sireleves.service;

import ma.ree.sireleves.dto.AdresseRequestDTO;
import ma.ree.sireleves.dto.AdresseResponseDTO;
import ma.ree.sireleves.entity.Adresse;
import ma.ree.sireleves.entity.Client;
import ma.ree.sireleves.entity.Quartier;
import ma.ree.sireleves.exception.ResourceNotFoundException;
import ma.ree.sireleves.mapper.AdresseMapper;
import ma.ree.sireleves.repository.AdresseRepository;
import ma.ree.sireleves.repository.ClientRepository;
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
@DisplayName("Tests unitaires du service AdresseService")
class AdresseServiceTest {

    @Mock
    private AdresseRepository adresseRepository;

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private QuartierRepository quartierRepository;

    @Mock
    private AdresseMapper adresseMapper;

    @InjectMocks
    private AdresseService adresseService;

    @Test
    @DisplayName("Devrait créer une adresse valide")
    void shouldCreateValidAdresse() {
        // Given
        AdresseRequestDTO requestDTO = createAdresseRequestDTO();
        Client client = createClient();
        Quartier quartier = createQuartier();
        Adresse savedAdresse = createAdresse();
        AdresseResponseDTO expectedResponse = createAdresseResponseDTO();

        when(clientRepository.findById("CLI001")).thenReturn(Optional.of(client));
        when(quartierRepository.findById(1)).thenReturn(Optional.of(quartier));
        when(adresseMapper.toEntity(requestDTO, client, quartier)).thenReturn(savedAdresse);
        when(adresseRepository.save(any(Adresse.class))).thenReturn(savedAdresse);
        when(adresseMapper.toResponseDTO(savedAdresse)).thenReturn(expectedResponse);

        // When
        AdresseResponseDTO result = adresseService.creerAdresse(requestDTO);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getIdAdresse()).isEqualTo(1);
        verify(clientRepository).findById("CLI001");
        verify(quartierRepository).findById(1);
        verify(adresseRepository).save(any(Adresse.class));
    }

    @Test
    @DisplayName("Devrait lever exception pour client inexistant")
    void shouldThrowExceptionForNonExistentClient() {
        // Given
        AdresseRequestDTO requestDTO = createAdresseRequestDTO();
        when(clientRepository.findById("CLI001")).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> adresseService.creerAdresse(requestDTO))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    @DisplayName("Devrait récupérer toutes les adresses")
    void shouldGetAllAdresses() {
        // Given
        List<Adresse> adresses = List.of(createAdresse());
        AdresseResponseDTO responseDTO = createAdresseResponseDTO();

        when(adresseRepository.findAll()).thenReturn(adresses);
        when(adresseMapper.toResponseDTO(any(Adresse.class))).thenReturn(responseDTO);

        // When
        List<AdresseResponseDTO> result = adresseService.getAllAdresses();

        // Then
        assertThat(result).hasSize(1);
        verify(adresseRepository).findAll();
    }

    @Test
    @DisplayName("Devrait récupérer une adresse par son ID")
    void shouldGetAdresseById() {
        // Given
        Integer id = 1;
        Adresse adresse = createAdresse();
        AdresseResponseDTO responseDTO = createAdresseResponseDTO();

        when(adresseRepository.findById(id)).thenReturn(Optional.of(adresse));
        when(adresseMapper.toResponseDTO(adresse)).thenReturn(responseDTO);

        // When
        AdresseResponseDTO result = adresseService.getAdresseById(id);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getIdAdresse()).isEqualTo(1);
        verify(adresseRepository).findById(id);
    }

    // Méthodes utilitaires
    private AdresseRequestDTO createAdresseRequestDTO() {
        AdresseRequestDTO dto = new AdresseRequestDTO();
        dto.setIdClient("CLI001");
        dto.setIdQuartier(1);
        dto.setAdresseComplete("123 Rue de la Paix");
        dto.setTypeBien(Adresse.TypeBien.Standard);
        dto.setCodePostal("75001");
        return dto;
    }

    private Client createClient() {
        Client client = new Client();
        client.setIdClient("CLI001");
        client.setNom("Dupont");
        client.setPrenom("Jean");
        return client;
    }

    private Quartier createQuartier() {
        Quartier quartier = new Quartier();
        quartier.setIdQuartier(1);
        quartier.setNomQuartier("Centre-ville");
        return quartier;
    }

    private Adresse createAdresse() {
        Adresse adresse = new Adresse();
        adresse.setIdAdresse(1);
        adresse.setAdresseComplete("123 Rue de la Paix");
        adresse.setClient(createClient());
        adresse.setQuartier(createQuartier());
        return adresse;
    }

    private AdresseResponseDTO createAdresseResponseDTO() {
        AdresseResponseDTO dto = new AdresseResponseDTO();
        dto.setIdAdresse(1);
        dto.setIdClient("CLI001");
        dto.setNomClient("Dupont");
        dto.setPrenomClient("Jean");
        dto.setIdQuartier(1);
        dto.setNomQuartier("Centre-ville");
        dto.setAdresseComplete("123 Rue de la Paix");
        dto.setTypeBien(Adresse.TypeBien.Standard);
        dto.setCodePostal("75001");
        return dto;
    }
}
