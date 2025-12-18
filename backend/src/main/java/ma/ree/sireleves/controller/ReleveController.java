package ma.ree.sireleves.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ma.ree.sireleves.dto.ReleveRequestDTO;
import ma.ree.sireleves.dto.ReleveResponseDTO;
import ma.ree.sireleves.service.ReleveService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/releves")
@RequiredArgsConstructor
public class ReleveController {

    private final ReleveService releveService;

    /**
     * Enregistrer un nouveau relevé
     * POST /api/releves
     */
    @PostMapping
    public ResponseEntity<ReleveResponseDTO> enregistrerReleve(
            @Valid @RequestBody ReleveRequestDTO requestDTO) {
        ReleveResponseDTO response = releveService.enregistrerReleve(requestDTO);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Récupérer tous les relevés
     * GET /api/releves
     */
    @GetMapping
    public ResponseEntity<List<ReleveResponseDTO>> getAllReleves(
            @RequestParam(required = false) String idCompteur,
            @RequestParam(required = false) String idAgent,
            @RequestParam(required = false) Integer idQuartier,
            @RequestParam(required = false) Boolean nonEnvoyes) {

        List<ReleveResponseDTO> releves;

        if (idCompteur != null) {
            releves = releveService.getRelevesByCompteur(idCompteur);
        } else if (idAgent != null) {
            releves = releveService.getRelevesByAgent(idAgent);
        } else if (idQuartier != null) {
            releves = releveService.getRelevesByQuartier(idQuartier);
        } else if (nonEnvoyes != null && nonEnvoyes) {
            releves = releveService.getRelevesNonEnvoyes();
        } else {
            releves = releveService.getAllReleves();
        }

        return ResponseEntity.ok(releves);
    }

    /**
     * Récupérer un relevé par son ID
     * GET /api/releves/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<ReleveResponseDTO> getReleveById(@PathVariable Integer id) {
        ReleveResponseDTO releve = releveService.getReleveById(id);
        return ResponseEntity.ok(releve);
    }
}