package ma.ree.sireleves.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "releve")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Releve {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_releve")
    private Integer idReleve;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_compteur", nullable = false)
    private Compteur compteur;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_agent", nullable = false)
    private Agent agent;

    @Column(name = "date_releve", nullable = false)
    private LocalDateTime dateReleve;

    @Column(name = "ancien_index", nullable = false, precision = 10, scale = 2)
    private BigDecimal ancienIndex;

    @Column(name = "nouvel_index", nullable = false, precision = 10, scale = 2)
    private BigDecimal nouvelIndex;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal consommation;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Unite unite;

    @Column(name = "envoye_facturation", nullable = false)
    private Boolean envoyeFacturation = false;

    @Column(name = "date_envoi_facturation")
    private LocalDateTime dateEnvoiFacturation;

    // Enum pour l'unité
    public enum Unite {
        m3,
        kWh
    }
}