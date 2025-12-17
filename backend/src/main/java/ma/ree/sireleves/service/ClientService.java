package ma.ree.sireleves.service;

import lombok.RequiredArgsConstructor;
import ma.ree.sireleves.dto.ClientResponseDTO;
import ma.ree.sireleves.entity.Client;
import ma.ree.sireleves.exception.ResourceNotFoundException;
import ma.ree.sireleves.mapper.ClientMapper;
import ma.ree.sireleves.repository.ClientRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ClientService {

    private final ClientRepository clientRepository;
    private final ClientMapper clientMapper;

    /**
     * Récupérer tous les clients
     */
    public List<ClientResponseDTO> getAllClients() {
        return clientRepository.findAll().stream()
                .map(clientMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Récupérer un client par son ID
     */
    public ClientResponseDTO getClientById(String id) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Client", "id", id));
        return clientMapper.toResponseDTO(client);
    }

    /**
     * Rechercher des clients par nom
     */
    public List<ClientResponseDTO> searchClientsByNom(String nom) {
        return clientRepository.findByNomContainingIgnoreCase(nom).stream()
                .map(clientMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Rechercher des clients par prénom
     */
    public List<ClientResponseDTO> searchClientsByPrenom(String prenom) {
        return clientRepository.findByPrenomContainingIgnoreCase(prenom).stream()
                .map(clientMapper::toResponseDTO)
                .collect(Collectors.toList());
    }
}