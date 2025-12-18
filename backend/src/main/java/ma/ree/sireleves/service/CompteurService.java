package ma.ree.sireleves.service;

import lombok.RequiredArgsConstructor;
import ma.ree.sireleves.dto.CompteurDetailDTO;
import ma.ree.sireleves.dto.CompteurRequestDTO;
import ma.ree.sireleves.dto.CompteurResponseDTO;
import ma.ree.sireleves.entity.Adresse;
import ma.ree.sireleves.entity.Compteur;
import ma.ree.sireleves.exception.BusinessException;
import ma.ree.sireleves.exception.ResourceNotFoundException;
import ma.ree.sireleves.mapper.CompteurMapper;
import ma.ree.sireleves.repository.AdresseRepository;
import ma.ree.sireleves.repository.CompteurRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CompteurService {

    private final CompteurRepository compteurRepository;
    private final AdresseRepository adresseRepository;
    private final CompteurMapper compteurMapper;

    /**
     * Créer un nouveau compteur avec validation des contraintes
     */
    public CompteurResponseDTO creerCompteur(CompteurRequestDTO requestDTO) {
        // Vérifier que l'adresse existe
        Adresse adresse = adresseRepository.findById(requestDTO.getIdAdresse())
                .orElseThrow(() -> new ResourceNotFoundException("Adresse", "id", requestDTO.getIdAdresse()));

        // Validation des contraintes métier
        validerContraintesCompteur(adresse, requestDTO);

        // Générer l'ID du compteur (9 chiffres)
        String idCompteur = genererIdCompteur();

        // Créer l'entité
        Compteur compteur = compteurMapper.toEntity(requestDTO, adresse, idCompteur);

        // Sauvegarder
        Compteur savedCompteur = compteurRepository.save(compteur);

        return compteurMapper.toResponseDTO(savedCompteur);
    }

    /**
     * Valider les contraintes métier pour la création d'un compteur
     */
    private void validerContraintesCompteur(Adresse adresse, CompteurRequestDTO requestDTO) {
        // Vérifier si ce type de compteur existe déjà pour cette adresse
        boolean existe = compteurRepository.existsByAdresseIdAdresseAndTypeAndPourEspacesCommuns(
                adresse.getIdAdresse(),
                requestDTO.getType(),
                requestDTO.getPourEspacesCommuns()
        );

        if (existe) {
            throw new BusinessException(
                    String.format("Un compteur %s %s existe déjà pour cette adresse",
                            requestDTO.getType(),
                            requestDTO.getPourEspacesCommuns() ? "pour espaces communs" : "standard")
            );
        }

        // Vérifier le nombre maximum de compteurs selon le type
        if (requestDTO.getPourEspacesCommuns()) {
            // Compteurs pour espaces communs (réservés aux immeubles)
            if (adresse.getTypeBien() != Adresse.TypeBien.Immeuble) {
                throw new BusinessException("Les compteurs pour espaces communs sont réservés aux immeubles");
            }

            long nbCompteurEspaceCommun = compteurRepository.countCompteurEspaceCommunByAdresse(adresse.getIdAdresse());
            if (nbCompteurEspaceCommun >= 2) {
                throw new BusinessException("Maximum 2 compteurs pour espaces communs par immeuble atteint");
            }
        } else {
            // Compteurs standards
            long nbCompteurStandard = compteurRepository.countCompteurStandardByAdresse(adresse.getIdAdresse());
            if (nbCompteurStandard >= 2) {
                throw new BusinessException("Maximum 2 compteurs standards par adresse atteint");
            }
        }
    }

    /**
     * Générer un ID de compteur (9 chiffres avec zéros précédents)
     */
    private String genererIdCompteur() {
        // Récupérer le dernier ID
        List<Compteur> compteurs = compteurRepository.findAll();

        long maxId = 0;
        for (Compteur c : compteurs) {
            try {
                long id = Long.parseLong(c.getIdCompteur());
                if (id > maxId) {
                    maxId = id;
                }
            } catch (NumberFormatException e) {
                // Ignorer les IDs non numériques
            }
        }

        // Incrémenter et formater avec des zéros précédents
        long newId = maxId + 1;
        return String.format("%09d", newId);
    }

    /**
     * Récupérer tous les compteurs
     */
    @Transactional(readOnly = true)
    public List<CompteurResponseDTO> getAllCompteurs() {
        return compteurRepository.findAll().stream()
                .map(compteurMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Récupérer un compteur par son ID
     */
    @Transactional(readOnly = true)
    public CompteurDetailDTO getCompteurById(String id) {
        Compteur compteur = compteurRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Compteur", "id", id));
        return compteurMapper.toDetailDTO(compteur);
    }

    /**
     * Récupérer les compteurs d'une adresse
     */
    @Transactional(readOnly = true)
    public List<CompteurResponseDTO> getCompteursByAdresse(Integer idAdresse) {
        return compteurRepository.findByAdresseIdAdresse(idAdresse).stream()
                .map(compteurMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Récupérer les compteurs d'un quartier
     */
    @Transactional(readOnly = true)
    public List<CompteurResponseDTO> getCompteursByQuartier(Integer idQuartier) {
        return compteurRepository.findByQuartier(idQuartier).stream()
                .map(compteurMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Récupérer les compteurs actifs
     */
    @Transactional(readOnly = true)
    public List<CompteurResponseDTO> getActiveCompteurs() {
        return compteurRepository.findByActifTrue().stream()
                .map(compteurMapper::toResponseDTO)
                .collect(Collectors.toList());
    }
}