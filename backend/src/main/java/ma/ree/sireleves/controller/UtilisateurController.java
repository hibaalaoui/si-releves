package ma.ree.sireleves.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ma.ree.sireleves.dto.UtilisateurRequestDTO;
import ma.ree.sireleves.dto.UtilisateurResponseDTO;
import ma.ree.sireleves.dto.UtilisateurUpdateDTO;
import ma.ree.sireleves.dto.UtilisateurCreateResponseDTO;
import ma.ree.sireleves.entity.UtilisateurBackoffice;
import ma.ree.sireleves.service.UtilisateurService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
     * Accès réservé aux Superadmin uniquement
     * Retourne le mot de passe généré pour que l'admin puisse le voir
     */
    @PostMapping
    @PreAuthorize("hasRole('Superadmin')")
    public ResponseEntity<UtilisateurCreateResponseDTO> creerUtilisateur(
            @Valid @RequestBody UtilisateurRequestDTO requestDTO) {
        UtilisateurCreateResponseDTO response = utilisateurService.creerUtilisateur(requestDTO);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Récupérer tous les utilisateurs
     * GET /api/utilisateurs
     * Accès réservé aux Superadmin uniquement
     */
    @GetMapping
    @PreAuthorize("hasRole('Superadmin')")
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
     * Accès réservé aux Superadmin uniquement
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('Superadmin')")
    public ResponseEntity<UtilisateurResponseDTO> getUtilisateurById(@PathVariable Integer id) {
        UtilisateurResponseDTO utilisateur = utilisateurService.getUtilisateurById(id);
        return ResponseEntity.ok(utilisateur);
    }

    /**
     * Mettre à jour un utilisateur
     * PUT /api/utilisateurs/{id}
     * Accès réservé aux Superadmin uniquement
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('Superadmin')")
    public ResponseEntity<UtilisateurResponseDTO> updateUtilisateur(
            @PathVariable Integer id,
            @Valid @RequestBody UtilisateurUpdateDTO updateDTO) {
        UtilisateurResponseDTO response = utilisateurService.updateUtilisateur(id, updateDTO);
        return ResponseEntity.ok(response);
    }

    /**
     * Réinitialiser le mot de passe d'un utilisateur
     * POST /api/utilisateurs/{id}/reset-password
     * Accès réservé aux Superadmin uniquement
     * Retourne le nouveau mot de passe généré
     */
    @PostMapping("/{id}/reset-password")
    @PreAuthorize("hasRole('Superadmin')")
    public ResponseEntity<UtilisateurCreateResponseDTO> resetPassword(@PathVariable Integer id) {
        UtilisateurCreateResponseDTO response = utilisateurService.resetPassword(id);
        return ResponseEntity.ok(response);
    }
}