package ma.ree.sireleves.releve.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ma.ree.sireleves.agent.entity.Agent;
import ma.ree.sireleves.compteur.entity.Compteur;
import ma.ree.sireleves.common.enums.Unite;

import java.time.LocalDateTime;

@Entity
@Table(name = "releve", indexes = {
    @Index(name = "idx_releve_compteur", columnList = "id_compteur"),
    @Index(name = "idx_releve_agent", columnList = "id_agent"),
    @Index(name = "idx_releve_date", columnList = "date_releve"),
    @Index(name = "idx_releve_envoye_facturation", columnList = "envoye_facturation"),
    @Index(name = "idx_releve_compteur_date", columnList = "id_compteur, date_releve")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Releve {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_releve")
    private Integer idReleve;

    @NotNull(message = "Le compteur est requis")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_compteur", nullable = false, foreignKey = @ForeignKey(name = "fk_releve_compteur"))
    private Compteur compteur;

    @NotNull(message = "L'agent est requis")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_agent", nullable = false, foreignKey = @ForeignKey(name = "fk_releve_agent"))
    private Agent agent;

    @NotNull(message = "La date de relevé est requise")
    @Column(name = "date_releve", nullable = false)
    private LocalDateTime dateReleve;

    @NotNull(message = "L'ancien index est requis")
    @DecimalMin(value = "0.00", message = "L'ancien index doit être positif ou nul")
    @Column(name = "ancien_index", nullable = false, precision = 10, scale = 2)
    private java.math.BigDecimal ancienIndex;

    @NotNull(message = "Le nouvel index est requis")
    @DecimalMin(value = "0.00", message = "Le nouvel index doit être positif ou nul")
    @Column(name = "nouvel_index", nullable = false, precision = 10, scale = 2)
    private java.math.BigDecimal nouvelIndex;

    @NotNull(message = "La consommation est requise")
    @DecimalMin(value = "0.00", message = "La consommation doit être positive ou nulle")
    @Column(name = "consommation", nullable = false, precision = 10, scale = 2)
    private java.math.BigDecimal consommation;

    @NotNull(message = "L'unité est requise")
    @Enumerated(EnumType.STRING)
    @Column(name = "unite", nullable = false, length = 10)
    private Unite unite;

    @Column(name = "envoye_facturation", nullable = false)
    private Boolean envoyeFacturation = false;

    @Column(name = "date_envoi_facturation")
    private LocalDateTime dateEnvoiFacturation;
}

