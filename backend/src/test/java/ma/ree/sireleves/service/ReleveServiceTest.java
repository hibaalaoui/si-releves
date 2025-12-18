package ma.ree.sireleves.service;

import ma.ree.sireleves.dto.ReleveRequestDTO;
import ma.ree.sireleves.dto.ReleveResponseDTO;
import ma.ree.sireleves.entity.Agent;
import ma.ree.sireleves.entity.Compteur;
import ma.ree.sireleves.entity.Releve;
import ma.ree.sireleves.exception.BusinessException;
import ma.ree.sireleves.exception.ResourceNotFoundException;
import ma.ree.sireleves.mapper.ReleveMapper;
import ma.ree.sireleves.repository.AgentRepository;
import ma.ree.sireleves.repository.CompteurRepository;
import ma.ree.sireleves.repository.ReleveRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests unitaires du service ReleveService")
class ReleveServiceTest {

    @Mock
    private ReleveRepository releveRepository;

    @Mock
    private CompteurRepository compteurRepository;

    @Mock
    private AgentRepository agentRepository;

    @Mock
    private ReleveMapper releveMapper;

    @InjectMocks
    private ReleveService releveService;

    @Test
    @DisplayName("Devrait récupérer tous les relevés")
    void shouldGetAllReleves() {
        // Given
        List<Releve> releves = List.of(createReleve());
        ReleveResponseDTO responseDTO = createReleveResponseDTO();

        when(releveRepository.findAll()).thenReturn(releves);
        when(releveMapper.toResponseDTO(any(Releve.class))).thenReturn(responseDTO);

        // When
        List<ReleveResponseDTO> result = releveService.getAllReleves();

        // Then
        assertThat(result).hasSize(1);
        verify(releveRepository).findAll();
    }

    @Test
    @DisplayName("Devrait récupérer un relevé par son ID")
    void shouldGetReleveById() {
        // Given
        Integer id = 1;
        Releve releve = createReleve();
        ReleveResponseDTO responseDTO = createReleveResponseDTO();

        when(releveRepository.findById(id)).thenReturn(Optional.of(releve));
        when(releveMapper.toResponseDTO(releve)).thenReturn(responseDTO);

        // When
        ReleveResponseDTO result = releveService.getReleveById(id);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getIdReleve()).isEqualTo(id);
        verify(releveRepository).findById(id);
    }

    @Test
    @DisplayName("Devrait lever exception pour relevé inexistant")
    void shouldThrowExceptionForNonExistentReleve() {
        // Given
        Integer id = 999;
        when(releveRepository.findById(id)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> releveService.getReleveById(id))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    // Méthodes utilitaires
    private Releve createReleve() {
        Releve releve = new Releve();
        releve.setIdReleve(1);
        releve.setCompteur(createCompteur());
        releve.setAgent(createAgent());
        releve.setAncienIndex(new BigDecimal("1000.00"));
        releve.setNouvelIndex(new BigDecimal("1250.75"));
        releve.setConsommation(new BigDecimal("250.75"));
        releve.setUnite(Releve.Unite.m3);
        return releve;
    }

    private Compteur createCompteur() {
        Compteur compteur = new Compteur();
        compteur.setIdCompteur("CMP001");
        compteur.setType(Compteur.TypeCompteur.Eau);
        return compteur;
    }

    private Agent createAgent() {
        Agent agent = new Agent();
        agent.setIdAgent("AGT001");
        agent.setNom("Dupont");
        agent.setPrenom("Marie");
        return agent;
    }

    private ReleveResponseDTO createReleveResponseDTO() {
        ReleveResponseDTO dto = new ReleveResponseDTO();
        dto.setIdReleve(1);
        dto.setIdCompteur("CMP001");
        dto.setTypeCompteur(Compteur.TypeCompteur.Eau);
        dto.setIdAgent("AGT001");
        dto.setAncienIndex(new BigDecimal("1000.00"));
        dto.setNouvelIndex(new BigDecimal("1250.75"));
        dto.setConsommation(new BigDecimal("250.75"));
        dto.setUnite(Releve.Unite.m3);
        return dto;
    }
}
