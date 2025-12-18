package ma.ree.sireleves.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ma.ree.sireleves.dto.AdresseRequestDTO;
import ma.ree.sireleves.dto.AdresseResponseDTO;
import ma.ree.sireleves.entity.Adresse;
import ma.ree.sireleves.service.AdresseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/adresses")
@RequiredArgsConstructor
public class AdresseController {

    private final AdresseService adresseService;

    /**
     * Créer une nouvelle adresse
     * POST /api/adresses
     */
    @PostMapping
    public ResponseEntity<AdresseResponseDTO> creerAdresse(
            @Valid @RequestBody AdresseRequestDTO requestDTO) {
        AdresseResponseDTO response = adresseService.creerAdresse(requestDTO);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Récupérer toutes les adresses
     * GET /api/adresses
     */
    @GetMapping
    public ResponseEntity<List<AdresseResponseDTO>> getAllAdresses(
            @RequestParam(required = false) String idClient,
            @RequestParam(required = false) Integer idQuartier,
            @RequestParam(required = false) Adresse.TypeBien typeBien) {

        List<AdresseResponseDTO> adresses;

        if (idClient != null) {
            adresses = adresseService.getAdressesByClient(idClient);
        } else if (idQuartier != null) {
            adresses = adresseService.getAdressesByQuartier(idQuartier);
        } else if (typeBien != null) {
            adresses = adresseService.getAdressesByTypeBien(typeBien);
        } else {
            adresses = adresseService.getAllAdresses();
        }

        return ResponseEntity.ok(adresses);
    }

    /**
     * Récupérer une adresse par son ID
     * GET /api/adresses/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<AdresseResponseDTO> getAdresseById(@PathVariable Integer id) {
        AdresseResponseDTO adresse = adresseService.getAdresseById(id);
        return ResponseEntity.ok(adresse);
    }
}