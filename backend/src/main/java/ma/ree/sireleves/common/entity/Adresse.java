package ma.ree.sireleves.common.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ma.ree.sireleves.common.enums.TypeBien;
import ma.ree.sireleves.compteur.entity.Compteur;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "adresse", indexes = {
    @Index(name = "idx_adresse_client", columnList = "id_client"),
    @Index(name = "idx_adresse_quartier", columnList = "id_quartier")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Adresse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_adresse")
    private Integer idAdresse;

    @NotNull(message = "Le client est requis")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_client", nullable = false, foreignKey = @ForeignKey(name = "fk_adresse_client"))
    private Client client;

    @NotNull(message = "Le quartier est requis")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_quartier", nullable = false, foreignKey = @ForeignKey(name = "fk_adresse_quartier"))
    private Quartier quartier;

    @NotBlank(message = "L'adresse complète est requise")
    @Column(name = "adresse_complete", nullable = false, columnDefinition = "TEXT")
    private String adresseComplete;

    @NotNull(message = "Le type de bien est requis")
    @Enumerated(EnumType.STRING)
    @Column(name = "type_bien", nullable = false, length = 20)
    private TypeBien typeBien = TypeBien.Standard;

    @Size(max = 10, message = "Le code postal ne peut pas dépasser 10 caractères")
    @Column(name = "code_postal", length = 10)
    private String codePostal;

    @Column(name = "date_creation", nullable = false, updatable = false)
    private LocalDateTime dateCreation = LocalDateTime.now();

    @OneToMany(mappedBy = "adresse", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Compteur> compteurs = new ArrayList<>();
}

