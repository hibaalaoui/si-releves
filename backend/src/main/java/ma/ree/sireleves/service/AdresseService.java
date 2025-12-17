package ma.ree.sireleves.service;

import lombok.RequiredArgsConstructor;
import ma.ree.sireleves.dto.AdresseRequestDTO;
import ma.ree.sireleves.dto.AdresseResponseDTO;
import ma.ree.sireleves.entity.Adresse;
import ma.ree.sireleves.entity.Client;
import ma.ree.sireleves.entity.Quartier;
import ma.ree.sireleves.exception.ResourceNotFoundException;
import ma.ree.sireleves.mapper.AdresseMapper;
import ma.ree.sireleves.repository.AdresseRepository;
import ma.ree.sireleves.repository.ClientRepository;
import ma.ree.sireleves.repository.QuartierRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class AdresseService {

    private final AdresseRepository adresseRepository;
    private final ClientRepository clientRepository;
    private final QuartierRepository quartierRepository;
    private final AdresseMapper adresseMapper;

    /**
     * Créer une nouvelle adresse
     */
    public AdresseResponseDTO creerAdresse(AdresseRequestDTO requestDTO) {
        // Vérifier que le client existe
        Client client = clientRepository.findById(requestDTO.getIdClient())
                .orElseThrow(() -> new ResourceNotFoundException("Client", "id", requestDTO.getIdClient()));

        // Vérifier que le quartier existe
        Quartier quartier = quartierRepository.findById(requestDTO.getIdQuartier())
                .orElseThrow(() -> new ResourceNotFoundException("Quartier", "id", requestDTO.getIdQuartier()));

        // Créer l'entité
        Adresse adresse = adresseMapper.toEntity(requestDTO, client, quartier);

        // Sauvegarder
        Adresse savedAdresse = adresseRepository.save(adresse);

        return adresseMapper.toResponseDTO(savedAdresse);
    }

    /**
     * Récupérer toutes les adresses
     */
    @Transactional(readOnly = true)
    public List<AdresseResponseDTO> getAllAdresses() {
        return adresseRepository.findAll().stream()
                .map(adresseMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Récupérer une adresse par son ID
     */
    @Transactional(readOnly = true)
    public AdresseResponseDTO getAdresseById(Integer id) {
        Adresse adresse = adresseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Adresse", "id", id));
        return adresseMapper.toResponseDTO(adresse);
    }

    /**
     * Récupérer les adresses d'un client
     */
    @Transactional(readOnly = true)
    public List<AdresseResponseDTO> getAdressesByClient(String idClient) {
        return adresseRepository.findByClientIdClient(idClient).stream()
                .map(adresseMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Récupérer les adresses d'un quartier
     */
    @Transactional(readOnly = true)
    public List<AdresseResponseDTO> getAdressesByQuartier(Integer idQuartier) {
        return adresseRepository.findByQuartierIdQuartier(idQuartier).stream()
                .map(adresseMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Récupérer les adresses par type de bien
     */
    @Transactional(readOnly = true)
    public List<AdresseResponseDTO> getAdressesByTypeBien(Adresse.TypeBien typeBien) {
        return adresseRepository.findByTypeBien(typeBien).stream()
                .map(adresseMapper::toResponseDTO)
                .collect(Collectors.toList());
    }
}