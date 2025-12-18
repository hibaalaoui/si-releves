package ma.ree.sireleves.service;

import ma.ree.sireleves.dto.UtilisateurRequestDTO;
import ma.ree.sireleves.dto.UtilisateurResponseDTO;
import ma.ree.sireleves.entity.UtilisateurBackoffice;
import ma.ree.sireleves.exception.BusinessException;
import ma.ree.sireleves.exception.ResourceNotFoundException;
import ma.ree.sireleves.mapper.UtilisateurMapper;
import ma.ree.sireleves.repository.UtilisateurBackofficeRepository;
import ma.ree.sireleves.util.PasswordGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests unitaires du service UtilisateurService")
class UtilisateurServiceTest {

    @Mock
    private UtilisateurBackofficeRepository utilisateurRepository;

    @Mock
    private UtilisateurMapper utilisateurMapper;

    @Mock
    private PasswordGenerator passwordGenerator;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UtilisateurService utilisateurService;

    @Test
    @DisplayName("Devrait récupérer tous les utilisateurs")
    void shouldGetAllUtilisateurs() {
        // Given
        List<UtilisateurBackoffice> utilisateurs = List.of(createUtilisateur());
        UtilisateurResponseDTO responseDTO = createUtilisateurResponseDTO();

        when(utilisateurRepository.findAll()).thenReturn(utilisateurs);
        when(utilisateurMapper.toResponseDTO(any(UtilisateurBackoffice.class))).thenReturn(responseDTO);

        // When
        List<UtilisateurResponseDTO> result = utilisateurService.getAllUtilisateurs();

        // Then
        assertThat(result).hasSize(1);
        verify(utilisateurRepository).findAll();
    }

    @Test
    @DisplayName("Devrait récupérer un utilisateur par son ID")
    void shouldGetUtilisateurById() {
        // Given
        Integer id = 1;
        UtilisateurBackoffice utilisateur = createUtilisateur();
        UtilisateurResponseDTO responseDTO = createUtilisateurResponseDTO();

        when(utilisateurRepository.findById(id)).thenReturn(Optional.of(utilisateur));
        when(utilisateurMapper.toResponseDTO(utilisateur)).thenReturn(responseDTO);

        // When
        UtilisateurResponseDTO result = utilisateurService.getUtilisateurById(id);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getIdUtilisateur()).isEqualTo(id);
        verify(utilisateurRepository).findById(id);
    }

    @Test
    @DisplayName("Devrait lever exception pour utilisateur inexistant")
    void shouldThrowExceptionForNonExistentUtilisateur() {
        // Given
        Integer id = 999;
        when(utilisateurRepository.findById(id)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> utilisateurService.getUtilisateurById(id))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    // Méthodes utilitaires
    private UtilisateurBackoffice createUtilisateur() {
        UtilisateurBackoffice utilisateur = new UtilisateurBackoffice();
        utilisateur.setIdUtilisateur(1);
        utilisateur.setNom("DUPONT");
        utilisateur.setPrenom("Marie");
        utilisateur.setEmail("marie.dupont@email.com");
        utilisateur.setRole(UtilisateurBackoffice.Role.Superadmin);
        utilisateur.setPasswordHash("hashedPassword");
        utilisateur.setPremiereConnexion(false);
        utilisateur.setActif(true);
        return utilisateur;
    }

    private UtilisateurResponseDTO createUtilisateurResponseDTO() {
        UtilisateurResponseDTO dto = new UtilisateurResponseDTO();
        dto.setIdUtilisateur(1);
        dto.setNom("DUPONT");
        dto.setPrenom("Marie");
        dto.setEmail("marie.dupont@email.com");
        dto.setRole(UtilisateurBackoffice.Role.Superadmin);
        dto.setPremiereConnexion(false);
        dto.setActif(true);
        return dto;
    }
}
