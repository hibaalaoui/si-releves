package ma.ree.sireleves.service;

import lombok.RequiredArgsConstructor;
import ma.ree.sireleves.dto.UtilisateurRequestDTO;
import ma.ree.sireleves.dto.UtilisateurResponseDTO;
import ma.ree.sireleves.dto.UtilisateurUpdateDTO;
import ma.ree.sireleves.entity.UtilisateurBackoffice;
import ma.ree.sireleves.exception.BusinessException;
import ma.ree.sireleves.exception.ResourceNotFoundException;
import ma.ree.sireleves.mapper.UtilisateurMapper;
import ma.ree.sireleves.repository.UtilisateurBackofficeRepository;
import ma.ree.sireleves.util.PasswordGenerator;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class UtilisateurService {

    private final UtilisateurBackofficeRepository utilisateurRepository;
    private final UtilisateurMapper utilisateurMapper;
    private final PasswordGenerator passwordGenerator;
    private final PasswordEncoder passwordEncoder;

    /**
     * Créer un nouvel utilisateur
     */
    public UtilisateurResponseDTO creerUtilisateur(UtilisateurRequestDTO requestDTO) {
        // Vérifier si l'email existe déjà
        if (utilisateurRepository.existsByEmail(requestDTO.getEmail())) {
            throw new BusinessException("Un utilisateur avec cet email existe déjà");
        }

        // Convertir DTO vers Entity
        UtilisateurBackoffice utilisateur = utilisateurMapper.toEntity(requestDTO);

        // Générer un mot de passe aléatoire
        String motDePasseGenere = passwordGenerator.generatePassword();
        utilisateur.setPasswordHash(passwordEncoder.encode(motDePasseGenere));

        // Sauvegarder l'utilisateur
        UtilisateurBackoffice savedUser = utilisateurRepository.save(utilisateur);

        // TODO: Envoyer le mot de passe par email (à implémenter plus tard)
        System.out.println("Mot de passe généré pour " + savedUser.getEmail() + " : " + motDePasseGenere);

        return utilisateurMapper.toResponseDTO(savedUser);
    }

    /**
     * Récupérer tous les utilisateurs
     */
    @Transactional(readOnly = true)
    public List<UtilisateurResponseDTO> getAllUtilisateurs() {
        return utilisateurRepository.findAll().stream()
                .map(utilisateurMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Récupérer un utilisateur par son ID
     */
    @Transactional(readOnly = true)
    public UtilisateurResponseDTO getUtilisateurById(Integer id) {
        UtilisateurBackoffice utilisateur = utilisateurRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur", "id", id));
        return utilisateurMapper.toResponseDTO(utilisateur);
    }

    /**
     * Mettre à jour un utilisateur
     */
    public UtilisateurResponseDTO updateUtilisateur(Integer id, UtilisateurUpdateDTO updateDTO) {
        UtilisateurBackoffice utilisateur = utilisateurRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur", "id", id));

        // Vérifier si le nouvel email n'est pas déjà utilisé par un autre utilisateur
        if (!utilisateur.getEmail().equals(updateDTO.getEmail())
                && utilisateurRepository.existsByEmail(updateDTO.getEmail())) {
            throw new BusinessException("Un utilisateur avec cet email existe déjà");
        }

        // Mettre à jour l'entité
        utilisateurMapper.updateEntityFromDTO(utilisateur, updateDTO);

        UtilisateurBackoffice updatedUser = utilisateurRepository.save(utilisateur);
        return utilisateurMapper.toResponseDTO(updatedUser);
    }

    /**
     * Réinitialiser le mot de passe d'un utilisateur
     */
    public String resetPassword(Integer id) {
        UtilisateurBackoffice utilisateur = utilisateurRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur", "id", id));

        // Générer un nouveau mot de passe
        String nouveauMotDePasse = passwordGenerator.generatePassword();
        utilisateur.setPasswordHash(passwordEncoder.encode(nouveauMotDePasse));
        utilisateur.setPremiereConnexion(true);
        utilisateur.setDateModification(LocalDateTime.now());

        utilisateurRepository.save(utilisateur);

        // TODO: Envoyer le nouveau mot de passe par email
        System.out.println("Nouveau mot de passe pour " + utilisateur.getEmail() + " : " + nouveauMotDePasse);

        return "Mot de passe réinitialisé avec succès";
    }

    /**
     * Filtrer les utilisateurs par rôle
     */
    @Transactional(readOnly = true)
    public List<UtilisateurResponseDTO> getUtilisateursByRole(UtilisateurBackoffice.Role role) {
        return utilisateurRepository.findByRole(role).stream()
                .map(utilisateurMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Récupérer uniquement les utilisateurs actifs
     */
    @Transactional(readOnly = true)
    public List<UtilisateurResponseDTO> getActiveUtilisateurs() {
        return utilisateurRepository.findByActifTrue().stream()
                .map(utilisateurMapper::toResponseDTO)
                .collect(Collectors.toList());
    }
}