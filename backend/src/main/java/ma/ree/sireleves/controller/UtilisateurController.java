package ma.ree.sireleves.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ma.ree.sireleves.dto.UtilisateurRequestDTO;
import ma.ree.sireleves.dto.UtilisateurResponseDTO;
import ma.ree.sireleves.dto.UtilisateurUpdateDTO;
import ma.ree.sireleves.entity.UtilisateurBackoffice;
import ma.ree.sireleves.service.UtilisateurService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/utilisateurs")
@RequiredArgsConstructor
public class UtilisateurController {

    private final UtilisateurService utilisateurService;

    /**
     * Créer un nouveau utilisateur
     * POST /api/utilisateurs
     */
    @PostMapping
    public ResponseEntity<UtilisateurResponseDTO> creerUtilisateur(
            @Valid @RequestBody UtilisateurRequestDTO requestDTO) {
        UtilisateurResponseDTO response = utilisateurService.creerUtilisateur(requestDTO);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Récupérer tous les utilisateurs
     * GET /api/utilisateurs
     */
    @GetMapping
    public ResponseEntity<List<UtilisateurResponseDTO>> getAllUtilisateurs(
            @RequestParam(required = false) UtilisateurBackoffice.Role role,
            @RequestParam(required = false) Boolean actifOnly) {

        List<UtilisateurResponseDTO> utilisateurs;

        if (role != null) {
            utilisateurs = utilisateurService.getUtilisateursByRole(role);
        } else if (actifOnly != null && actifOnly) {
            utilisateurs = utilisateurService.getActiveUtilisateurs();
        } else {
            utilisateurs = utilisateurService.getAllUtilisateurs();
        }

        return ResponseEntity.ok(utilisateurs);
    }

    /**
     * Récupérer un utilisateur par son ID
     * GET /api/utilisateurs/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<UtilisateurResponseDTO> getUtilisateurById(@PathVariable Integer id) {
        UtilisateurResponseDTO utilisateur = utilisateurService.getUtilisateurById(id);
        return ResponseEntity.ok(utilisateur);
    }

    /**
     * Mettre à jour un utilisateur
     * PUT /api/utilisateurs/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<UtilisateurResponseDTO> updateUtilisateur(
            @PathVariable Integer id,
            @Valid @RequestBody UtilisateurUpdateDTO updateDTO) {
        UtilisateurResponseDTO response = utilisateurService.updateUtilisateur(id, updateDTO);
        return ResponseEntity.ok(response);
    }

    /**
     * Réinitialiser le mot de passe d'un utilisateur
     * POST /api/utilisateurs/{id}/reset-password
     */
    @PostMapping("/{id}/reset-password")
    public ResponseEntity<String> resetPassword(@PathVariable Integer id) {
        String message = utilisateurService.resetPassword(id);
        return ResponseEntity.ok(message);
    }
}