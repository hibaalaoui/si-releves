package ma.ree.sireleves.service;

import lombok.RequiredArgsConstructor;
import ma.ree.sireleves.dto.QuartierRequestDTO;
import ma.ree.sireleves.dto.QuartierResponseDTO;
import ma.ree.sireleves.entity.Quartier;
import ma.ree.sireleves.exception.BusinessException;
import ma.ree.sireleves.exception.ResourceNotFoundException;
import ma.ree.sireleves.mapper.QuartierMapper;
import ma.ree.sireleves.repository.QuartierRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class QuartierService {

    private final QuartierRepository quartierRepository;
    private final QuartierMapper quartierMapper;

    /**
     * Créer un nouveau quartier
     */
    public QuartierResponseDTO creerQuartier(QuartierRequestDTO requestDTO) {
        // Vérifier si le quartier existe déjà
        if (quartierRepository.existsByNomQuartier(requestDTO.getNomQuartier())) {
            throw new BusinessException("Un quartier avec ce nom existe déjà");
        }

        // Créer l'entité
        Quartier quartier = quartierMapper.toEntity(requestDTO);

        // Sauvegarder
        Quartier savedQuartier = quartierRepository.save(quartier);

        return quartierMapper.toResponseDTO(savedQuartier);
    }

    /**
     * Récupérer tous les quartiers
     */
    @Transactional(readOnly = true)
    public List<QuartierResponseDTO> getAllQuartiers() {
        return quartierRepository.findAll().stream()
                .map(quartierMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Récupérer un quartier par son ID
     */
    @Transactional(readOnly = true)
    public QuartierResponseDTO getQuartierById(Integer id) {
        Quartier quartier = quartierRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Quartier", "id", id));
        return quartierMapper.toResponseDTO(quartier);
    }

    /**
     * Mettre à jour un quartier
     */
    public QuartierResponseDTO updateQuartier(Integer id, QuartierRequestDTO requestDTO) {
        Quartier quartier = quartierRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Quartier", "id", id));

        // Vérifier si le nouveau nom n'est pas déjà utilisé par un autre quartier
        if (!quartier.getNomQuartier().equals(requestDTO.getNomQuartier())
                && quartierRepository.existsByNomQuartier(requestDTO.getNomQuartier())) {
            throw new BusinessException("Un quartier avec ce nom existe déjà");
        }

        // Mettre à jour l'entité
        quartierMapper.updateEntityFromDTO(quartier, requestDTO);

        Quartier updatedQuartier = quartierRepository.save(quartier);
        return quartierMapper.toResponseDTO(updatedQuartier);
    }

    /**
     * Supprimer un quartier
     */
    public void deleteQuartier(Integer id) {
        Quartier quartier = quartierRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Quartier", "id", id));

        // Vérifier s'il y a des adresses liées à ce quartier
        if (!quartier.getAdresses().isEmpty()) {
            throw new BusinessException("Impossible de supprimer ce quartier car des adresses y sont liées");
        }

        // Vérifier s'il y a des agents affectés à ce quartier
        if (!quartier.getAgents().isEmpty()) {
            throw new BusinessException("Impossible de supprimer ce quartier car des agents y sont affectés");
        }

        quartierRepository.delete(quartier);
    }
}