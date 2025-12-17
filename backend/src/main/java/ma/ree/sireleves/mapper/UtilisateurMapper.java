package ma.ree.sireleves.mapper;

import ma.ree.sireleves.dto.UtilisateurRequestDTO;
import ma.ree.sireleves.dto.UtilisateurResponseDTO;
import ma.ree.sireleves.dto.UtilisateurUpdateDTO;
import ma.ree.sireleves.entity.UtilisateurBackoffice;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class UtilisateurMapper {

    // Convertir RequestDTO vers Entity (pour création)
    public UtilisateurBackoffice toEntity(UtilisateurRequestDTO dto) {
        UtilisateurBackoffice utilisateur = new UtilisateurBackoffice();
        utilisateur.setNom(dto.getNom().toUpperCase()); // NOM en MAJUSCULES
        utilisateur.setPrenom(capitalizeFirstLetter(dto.getPrenom())); // Prénom en Nom Propre
        utilisateur.setEmail(dto.getEmail().toLowerCase());
        utilisateur.setRole(dto.getRole());
        utilisateur.setPremiereConnexion(true);
        utilisateur.setDateAjout(LocalDateTime.now());
        utilisateur.setActif(true);
        return utilisateur;
    }

    // Convertir Entity vers ResponseDTO
    public UtilisateurResponseDTO toResponseDTO(UtilisateurBackoffice entity) {
        return new UtilisateurResponseDTO(
                entity.getIdUtilisateur(),
                entity.getNom(),
                entity.getPrenom(),
                entity.getEmail(),
                entity.getRole(),
                entity.getPremiereConnexion(),
                entity.getDateAjout(),
                entity.getDateModification(),
                entity.getActif()
        );
    }

    // Mettre à jour une entité existante avec UpdateDTO
    public void updateEntityFromDTO(UtilisateurBackoffice entity, UtilisateurUpdateDTO dto) {
        entity.setNom(dto.getNom().toUpperCase());
        entity.setPrenom(capitalizeFirstLetter(dto.getPrenom()));
        entity.setEmail(dto.getEmail().toLowerCase());
        entity.setRole(dto.getRole());
        entity.setDateModification(LocalDateTime.now());
    }

    // Méthode utilitaire pour mettre en majuscule la première lettre
    private String capitalizeFirstLetter(String text) {
        if (text == null || text.isEmpty()) {
            return text;
        }
        return text.substring(0, 1).toUpperCase() + text.substring(1).toLowerCase();
    }
}