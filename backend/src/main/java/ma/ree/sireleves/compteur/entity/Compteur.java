package ma.ree.sireleves.compteur.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ma.ree.sireleves.common.entity.Adresse;
import ma.ree.sireleves.common.enums.TypeCompteur;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "compteur", indexes = {
    @Index(name = "idx_compteur_adresse", columnList = "id_adresse"),
    @Index(name = "idx_compteur_type", columnList = "type"),
    @Index(name = "idx_compteur_actif", columnList = "actif")
}, uniqueConstraints = {
    @UniqueConstraint(name = "uk_compteur_adresse_type", columnNames = {"id_adresse", "type", "pour_espaces_communs"})
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Compteur {

    @Id
    @Column(name = "id_compteur", length = 9, nullable = false)
    @Size(min = 9, max = 9, message = "L'ID compteur doit contenir exactement 9 caractères")
    private String idCompteur;

    @NotNull(message = "L'adresse est requise")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_adresse", nullable = false, foreignKey = @ForeignKey(name = "fk_compteur_adresse"))
    private Adresse adresse;

    @NotNull(message = "Le type de compteur est requis")
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, length = 20)
    private TypeCompteur type;

    @NotNull(message = "L'index actuel est requis")
    @DecimalMin(value = "0.00", message = "L'index actuel doit être positif ou nul")
    @Column(name = "index_actuel", nullable = false, precision = 10, scale = 2)
    private java.math.BigDecimal indexActuel = java.math.BigDecimal.ZERO;

    @Column(name = "date_installation", nullable = false)
    private LocalDate dateInstallation = LocalDate.now();

    @Column(name = "date_derniere_releve")
    private LocalDateTime dateDerniereReleve;

    @Column(name = "pour_espaces_communs", nullable = false)
    private Boolean pourEspacesCommuns = false;

    @Column(name = "actif", nullable = false)
    private Boolean actif = true;

    @OneToMany(mappedBy = "compteur", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ma.ree.sireleves.releve.entity.Releve> releves = new ArrayList<>();
}

