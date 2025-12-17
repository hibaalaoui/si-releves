package ma.ree.sireleves.mapper;

import ma.ree.sireleves.dto.ReleveRequestDTO;
import ma.ree.sireleves.dto.ReleveResponseDTO;
import ma.ree.sireleves.entity.Agent;
import ma.ree.sireleves.entity.Compteur;
import ma.ree.sireleves.entity.Releve;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ReleveMapper {

    /**
     * Convertir RequestDTO vers Entity (pour création)
     */
    public Releve toEntity(ReleveRequestDTO dto, Compteur compteur, Agent agent) {
        Releve releve = new Releve();
        releve.setCompteur(compteur);
        releve.setAgent(agent);
        releve.setDateReleve(dto.getDateReleve() != null ? dto.getDateReleve() : LocalDateTime.now());
        releve.setAncienIndex(compteur.getIndexActuel());
        releve.setNouvelIndex(dto.getNouvelIndex());

        // Calcul automatique de la consommation
        releve.setConsommation(dto.getNouvelIndex().subtract(compteur.getIndexActuel()));

        // Déterminer l'unité selon le type de compteur
        if (compteur.getType() == Compteur.TypeCompteur.Eau) {
            releve.setUnite(Releve.Unite.m3);
        } else {
            releve.setUnite(Releve.Unite.kWh);
        }

        releve.setEnvoyeFacturation(false);

        return releve;
    }

    /**
     * Convertir Entity vers ResponseDTO
     */
    public ReleveResponseDTO toResponseDTO(Releve entity) {
        return new ReleveResponseDTO(
                entity.getIdReleve(),
                entity.getCompteur().getIdCompteur(),
                entity.getCompteur().getType(),
                entity.getAgent().getIdAgent(),
                entity.getAgent().getNom(),
                entity.getAgent().getPrenom(),
                entity.getCompteur().getAdresse().getAdresseComplete(),
                entity.getCompteur().getAdresse().getQuartier().getNomQuartier(),
                entity.getDateReleve(),
                entity.getAncienIndex(),
                entity.getNouvelIndex(),
                entity.getConsommation(),
                entity.getUnite(),
                entity.getEnvoyeFacturation()
        );
    }
}