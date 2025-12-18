package ma.ree.sireleves.controller;

import ma.ree.sireleves.dto.ChangePasswordRequestDTO;
import ma.ree.sireleves.dto.LoginRequestDTO;
import ma.ree.sireleves.dto.LoginResponseDTO;
import ma.ree.sireleves.entity.UtilisateurBackoffice;
import ma.ree.sireleves.service.AuthService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests unitaires du contrôleur AuthController")
class AuthControllerTest {

    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthController authController;

    @Test
    @DisplayName("Devrait authentifier un utilisateur")
    void shouldLogin() {
        // Given
        LoginRequestDTO loginRequest = createLoginRequestDTO();
        LoginResponseDTO loginResponse = createLoginResponseDTO();

        when(authService.login(loginRequest)).thenReturn(loginResponse);

        // When
        ResponseEntity<LoginResponseDTO> response = authController.login(loginRequest);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(loginResponse);
        verify(authService).login(loginRequest);
    }

    @Test
    @DisplayName("Devrait changer le mot de passe")
    void shouldChangePassword() {
        // Given
        Integer userId = 1;
        ChangePasswordRequestDTO changePasswordRequest = createChangePasswordRequestDTO();
        String message = "Mot de passe modifié avec succès";

        when(authService.changePassword(userId, changePasswordRequest)).thenReturn(message);

        // When
        ResponseEntity<String> response = authController.changePassword(userId, changePasswordRequest);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(message);
        verify(authService).changePassword(userId, changePasswordRequest);
    }

    // Méthodes utilitaires
    private LoginRequestDTO createLoginRequestDTO() {
        LoginRequestDTO dto = new LoginRequestDTO();
        dto.setEmail("admin@system.com");
        dto.setPassword("password");
        return dto;
    }

    private LoginResponseDTO createLoginResponseDTO() {
        return new LoginResponseDTO(
                "token",
                1,
                "Admin",
                "System",
                "admin@system.com",
                UtilisateurBackoffice.Role.Superadmin,
                false
        );
    }

    private ChangePasswordRequestDTO createChangePasswordRequestDTO() {
        ChangePasswordRequestDTO dto = new ChangePasswordRequestDTO();
        dto.setOldPassword("oldPassword");
        dto.setNewPassword("newPassword");
        return dto;
    }
}
