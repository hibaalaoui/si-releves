package ma.ree.sireleves.service;

import lombok.RequiredArgsConstructor;
import ma.ree.sireleves.dto.ReleveRequestDTO;
import ma.ree.sireleves.dto.ReleveResponseDTO;
import ma.ree.sireleves.entity.Agent;
import ma.ree.sireleves.entity.Compteur;
import ma.ree.sireleves.entity.Releve;
import ma.ree.sireleves.exception.BusinessException;
import ma.ree.sireleves.exception.ResourceNotFoundException;
import ma.ree.sireleves.mapper.ReleveMapper;
import ma.ree.sireleves.repository.AgentRepository;
import ma.ree.sireleves.repository.CompteurRepository;
import ma.ree.sireleves.repository.ReleveRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ReleveService {

    private final ReleveRepository releveRepository;
    private final CompteurRepository compteurRepository;
    private final AgentRepository agentRepository;
    private final ReleveMapper releveMapper;

    /**
     * Enregistrer un nouveau relevé
     * - Valide que le nouvel index >= ancien index
     * - Calcule automatiquement la consommation
     * - Met à jour l'index actuel du compteur
     */
    public ReleveResponseDTO enregistrerReleve(ReleveRequestDTO requestDTO) {
        // Vérifier que le compteur existe
        Compteur compteur = compteurRepository.findById(requestDTO.getIdCompteur())
                .orElseThrow(() -> new ResourceNotFoundException("Compteur", "id", requestDTO.getIdCompteur()));

        // Vérifier que le compteur est actif
        if (!compteur.getActif()) {
            throw new BusinessException("Ce compteur est désactivé");
        }

        // Vérifier que l'agent existe
        Agent agent = agentRepository.findById(requestDTO.getIdAgent())
                .orElseThrow(() -> new ResourceNotFoundException("Agent", "id", requestDTO.getIdAgent()));

        // Vérifier que l'agent est actif
        if (!agent.getActif()) {
            throw new BusinessException("Cet agent est désactivé");
        }

        // Valider que le nouvel index est >= à l'index actuel
        if (requestDTO.getNouvelIndex().compareTo(compteur.getIndexActuel()) < 0) {
            throw new BusinessException(
                    String.format("Le nouvel index (%s) doit être supérieur ou égal à l'index actuel (%s)",
                            requestDTO.getNouvelIndex(), compteur.getIndexActuel())
            );
        }

        // Créer le relevé (le mapper calcule automatiquement la consommation)
        Releve releve = releveMapper.toEntity(requestDTO, compteur, agent);

        // Sauvegarder le relevé
        Releve savedReleve = releveRepository.save(releve);

        // Mettre à jour l'index actuel et la date de dernière relève du compteur
        compteur.setIndexActuel(requestDTO.getNouvelIndex());
        compteur.setDateDerniereReleve(releve.getDateReleve());
        compteurRepository.save(compteur);

        return releveMapper.toResponseDTO(savedReleve);
    }

    /**
     * Récupérer tous les relevés
     */
    @Transactional(readOnly = true)
    public List<ReleveResponseDTO> getAllReleves() {
        return releveRepository.findAll().stream()
                .map(releveMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Récupérer un relevé par son ID
     */
    @Transactional(readOnly = true)
    public ReleveResponseDTO getReleveById(Integer id) {
        Releve releve = releveRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Relevé", "id", id));
        return releveMapper.toResponseDTO(releve);
    }

    /**
     * Récupérer les relevés d'un compteur
     */
    @Transactional(readOnly = true)
    public List<ReleveResponseDTO> getRelevesByCompteur(String idCompteur) {
        return releveRepository.findByCompteurIdCompteur(idCompteur).stream()
                .map(releveMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Récupérer les relevés d'un agent
     */
    @Transactional(readOnly = true)
    public List<ReleveResponseDTO> getRelevesByAgent(String idAgent) {
        return releveRepository.findByAgentIdAgent(idAgent).stream()
                .map(releveMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Récupérer les relevés d'un quartier
     */
    @Transactional(readOnly = true)
    public List<ReleveResponseDTO> getRelevesByQuartier(Integer idQuartier) {
        return releveRepository.findByQuartier(idQuartier).stream()
                .map(releveMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Récupérer les relevés non envoyés à la facturation
     */
    @Transactional(readOnly = true)
    public List<ReleveResponseDTO> getRelevesNonEnvoyes() {
        return releveRepository.findByEnvoyeFacturationFalse().stream()
                .map(releveMapper::toResponseDTO)
                .collect(Collectors.toList());
    }
}