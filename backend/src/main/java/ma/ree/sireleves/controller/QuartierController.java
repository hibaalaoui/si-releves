package ma.ree.sireleves.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ma.ree.sireleves.dto.QuartierRequestDTO;
import ma.ree.sireleves.dto.QuartierResponseDTO;
import ma.ree.sireleves.service.QuartierService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/quartiers")
@RequiredArgsConstructor
public class QuartierController {

    private final QuartierService quartierService;

    /**
     * Créer un nouveau quartier
     * POST /api/quartiers
     */
    @PostMapping
    public ResponseEntity<QuartierResponseDTO> creerQuartier(
            @Valid @RequestBody QuartierRequestDTO requestDTO) {
        QuartierResponseDTO response = quartierService.creerQuartier(requestDTO);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Récupérer tous les quartiers
     * GET /api/quartiers
     */
    @GetMapping
    public ResponseEntity<List<QuartierResponseDTO>> getAllQuartiers() {
        List<QuartierResponseDTO> quartiers = quartierService.getAllQuartiers();
        return ResponseEntity.ok(quartiers);
    }

    /**
     * Récupérer un quartier par son ID
     * GET /api/quartiers/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<QuartierResponseDTO> getQuartierById(@PathVariable Integer id) {
        QuartierResponseDTO quartier = quartierService.getQuartierById(id);
        return ResponseEntity.ok(quartier);
    }

    /**
     * Mettre à jour un quartier
     * PUT /api/quartiers/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<QuartierResponseDTO> updateQuartier(
            @PathVariable Integer id,
            @Valid @RequestBody QuartierRequestDTO requestDTO) {
        QuartierResponseDTO response = quartierService.updateQuartier(id, requestDTO);
        return ResponseEntity.ok(response);
    }

    /**
     * Supprimer un quartier
     * DELETE /api/quartiers/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteQuartier(@PathVariable Integer id) {
        quartierService.deleteQuartier(id);
        return ResponseEntity.noContent().build();
    }
}