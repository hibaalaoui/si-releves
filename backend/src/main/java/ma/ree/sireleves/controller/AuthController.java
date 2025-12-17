package ma.ree.sireleves.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ma.ree.sireleves.dto.ChangePasswordRequestDTO;
import ma.ree.sireleves.dto.LoginRequestDTO;
import ma.ree.sireleves.dto.LoginResponseDTO;
import ma.ree.sireleves.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /**
     * Connexion utilisateur
     * POST /api/auth/login
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO loginRequest) {
        LoginResponseDTO response = authService.login(loginRequest);
        return ResponseEntity.ok(response);
    }

    /**
     * Changer son mot de passe
     * POST /api/auth/change-password/{userId}
     */
    @PostMapping("/change-password/{userId}")
    public ResponseEntity<String> changePassword(
            @PathVariable Integer userId,
            @Valid @RequestBody ChangePasswordRequestDTO changePasswordRequest) {
        String message = authService.changePassword(userId, changePasswordRequest);
        return ResponseEntity.ok(message);
    }
}