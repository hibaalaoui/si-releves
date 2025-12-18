package ma.ree.sireleves.service;

import ma.ree.sireleves.dto.ChangePasswordRequestDTO;
import ma.ree.sireleves.dto.LoginRequestDTO;
import ma.ree.sireleves.dto.LoginResponseDTO;
import ma.ree.sireleves.entity.UtilisateurBackoffice;
import ma.ree.sireleves.exception.BusinessException;
import ma.ree.sireleves.repository.UtilisateurBackofficeRepository;
import ma.ree.sireleves.util.JwtUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests unitaires du service AuthService")
class AuthServiceTest {

    @Mock
    private UtilisateurBackofficeRepository utilisateurRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private AuthService authService;

    @Test
    @DisplayName("Devrait authentifier un utilisateur valide")
    void shouldAuthenticateValidUser() {
        // Given
        LoginRequestDTO loginRequest = createLoginRequestDTO();
        UtilisateurBackoffice utilisateur = createUtilisateur();
        String token = "jwt-token";

        when(utilisateurRepository.findByEmail("admin@system.com")).thenReturn(Optional.of(utilisateur));
        when(passwordEncoder.matches("password", "hashedPassword")).thenReturn(true);
        when(jwtUtil.generateToken("admin@system.com", "Superadmin", 1)).thenReturn(token);

        // When
        LoginResponseDTO result = authService.login(loginRequest);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getToken()).isEqualTo(token);
        assertThat(result.getEmail()).isEqualTo("admin@system.com");
        assertThat(result.getRole()).isEqualTo(UtilisateurBackoffice.Role.Superadmin);
        verify(utilisateurRepository).findByEmail("admin@system.com");
        verify(passwordEncoder).matches("password", "hashedPassword");
        verify(jwtUtil).generateToken("admin@system.com", "Superadmin", 1);
    }

    @Test
    @DisplayName("Devrait lever exception pour email inexistant")
    void shouldThrowExceptionForNonExistentEmail() {
        // Given
        LoginRequestDTO loginRequest = createLoginRequestDTO();
        when(utilisateurRepository.findByEmail("admin@system.com")).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> authService.login(loginRequest))
                .isInstanceOf(BusinessException.class)
                .hasMessage("Email ou mot de passe incorrect");
    }

    @Test
    @DisplayName("Devrait lever exception pour mot de passe incorrect")
    void shouldThrowExceptionForIncorrectPassword() {
        // Given
        LoginRequestDTO loginRequest = createLoginRequestDTO();
        UtilisateurBackoffice utilisateur = createUtilisateur();

        when(utilisateurRepository.findByEmail("admin@system.com")).thenReturn(Optional.of(utilisateur));
        when(passwordEncoder.matches("password", "hashedPassword")).thenReturn(false);

        // When & Then
        assertThatThrownBy(() -> authService.login(loginRequest))
                .isInstanceOf(BusinessException.class)
                .hasMessage("Email ou mot de passe incorrect");
    }

    @Test
    @DisplayName("Devrait changer le mot de passe")
    void shouldChangePassword() {
        // Given
        Integer userId = 1;
        ChangePasswordRequestDTO requestDTO = createChangePasswordRequestDTO();
        UtilisateurBackoffice utilisateur = createUtilisateur();

        when(utilisateurRepository.findById(userId)).thenReturn(Optional.of(utilisateur));
        when(passwordEncoder.matches("oldPassword", "hashedPassword")).thenReturn(true);
        when(passwordEncoder.encode("newPassword")).thenReturn("newHashedPassword");
        when(utilisateurRepository.save(utilisateur)).thenReturn(utilisateur);

        // When
        String result = authService.changePassword(userId, requestDTO);

        // Then
        assertThat(result).isEqualTo("Mot de passe modifié avec succès");
        assertThat(utilisateur.getPasswordHash()).isEqualTo("newHashedPassword");
        assertThat(utilisateur.getPremiereConnexion()).isFalse();
        verify(utilisateurRepository).save(utilisateur);
    }

    // Méthodes utilitaires
    private LoginRequestDTO createLoginRequestDTO() {
        LoginRequestDTO dto = new LoginRequestDTO();
        dto.setEmail("admin@system.com");
        dto.setPassword("password");
        return dto;
    }

    private ChangePasswordRequestDTO createChangePasswordRequestDTO() {
        ChangePasswordRequestDTO dto = new ChangePasswordRequestDTO();
        dto.setOldPassword("oldPassword");
        dto.setNewPassword("newPassword");
        return dto;
    }

    private UtilisateurBackoffice createUtilisateur() {
        UtilisateurBackoffice utilisateur = new UtilisateurBackoffice();
        utilisateur.setIdUtilisateur(1);
        utilisateur.setNom("Admin");
        utilisateur.setPrenom("System");
        utilisateur.setEmail("admin@system.com");
        utilisateur.setPasswordHash("hashedPassword");
        utilisateur.setRole(UtilisateurBackoffice.Role.Superadmin);
        utilisateur.setActif(true);
        utilisateur.setPremiereConnexion(false);
        return utilisateur;
    }
}
