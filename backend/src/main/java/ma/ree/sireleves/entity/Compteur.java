package ma.ree.sireleves.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "compteur")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Compteur {

    @Id
    @Column(name = "id_compteur", length = 9)
    private String idCompteur;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_adresse", nullable = false)
    private Adresse adresse;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TypeCompteur type;

    @Column(name = "index_actuel", nullable = false, precision = 10, scale = 2)
    private BigDecimal indexActuel = BigDecimal.ZERO;

    @Column(name = "date_installation", nullable = false)
    private LocalDate dateInstallation = LocalDate.now();

    @Column(name = "date_derniere_releve")
    private LocalDateTime dateDerniereReleve;

    @Column(name = "pour_espaces_communs", nullable = false)
    private Boolean pourEspacesCommuns = false;

    @Column(nullable = false)
    private Boolean actif = true;

    @OneToMany(mappedBy = "compteur", cascade = CascadeType.ALL)
    private List<Releve> releves;

    // Enum pour le type de compteur
    public enum TypeCompteur {
        Eau,
        Electricite
    }
}