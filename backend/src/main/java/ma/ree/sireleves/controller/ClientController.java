package ma.ree.sireleves.controller;

import lombok.RequiredArgsConstructor;
import ma.ree.sireleves.dto.ClientResponseDTO;
import ma.ree.sireleves.service.ClientService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clients")
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;

    /**
     * Récupérer tous les clients
     * GET /api/clients
     */
    @GetMapping
    public ResponseEntity<List<ClientResponseDTO>> getAllClients(
            @RequestParam(required = false) String nom,
            @RequestParam(required = false) String prenom) {

        List<ClientResponseDTO> clients;

        if (nom != null) {
            clients = clientService.searchClientsByNom(nom);
        } else if (prenom != null) {
            clients = clientService.searchClientsByPrenom(prenom);
        } else {
            clients = clientService.getAllClients();
        }

        return ResponseEntity.ok(clients);
    }

    /**
     * Récupérer un client par son ID
     * GET /api/clients/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<ClientResponseDTO> getClientById(@PathVariable String id) {
        ClientResponseDTO client = clientService.getClientById(id);
        return ResponseEntity.ok(client);
    }
}