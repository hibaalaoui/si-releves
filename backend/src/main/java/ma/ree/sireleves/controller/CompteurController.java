package ma.ree.sireleves.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ma.ree.sireleves.dto.CompteurDetailDTO;
import ma.ree.sireleves.dto.CompteurRequestDTO;
import ma.ree.sireleves.dto.CompteurResponseDTO;
import ma.ree.sireleves.service.CompteurService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/compteurs")
@RequiredArgsConstructor
public class CompteurController {

    private final CompteurService compteurService;

    /**
     * Créer un nouveau compteur
     * POST /api/compteurs
     */
    @PostMapping
    public ResponseEntity<CompteurResponseDTO> creerCompteur(
            @Valid @RequestBody CompteurRequestDTO requestDTO) {
        CompteurResponseDTO response = compteurService.creerCompteur(requestDTO);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Récupérer tous les compteurs
     * GET /api/compteurs
     * Accessible aux Superadmin et Utilisateur
     */
    @GetMapping
    @PreAuthorize("hasRole('Superadmin') or hasRole('Utilisateur')")
    public ResponseEntity<List<CompteurResponseDTO>> getAllCompteurs(
            @RequestParam(required = false) Integer idAdresse,
            @RequestParam(required = false) Integer idQuartier,
            @RequestParam(required = false) Boolean actifOnly) {

        List<CompteurResponseDTO> compteurs;

        if (idAdresse != null) {
            compteurs = compteurService.getCompteursByAdresse(idAdresse);
        } else if (idQuartier != null) {
            compteurs = compteurService.getCompteursByQuartier(idQuartier);
        } else if (actifOnly != null && actifOnly) {
            compteurs = compteurService.getActiveCompteurs();
        } else {
            compteurs = compteurService.getAllCompteurs();
        }

        return ResponseEntity.ok(compteurs);
    }

    /**
     * Récupérer un compteur par son ID
     * GET /api/compteurs/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<CompteurDetailDTO> getCompteurById(@PathVariable String id) {
        CompteurDetailDTO compteur = compteurService.getCompteurById(id);
        return ResponseEntity.ok(compteur);
    }
}