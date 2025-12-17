package ma.ree.sireleves.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "adresse")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Adresse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_adresse")
    private Integer idAdresse;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_client", nullable = false)
    private Client client;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_quartier", nullable = false)
    private Quartier quartier;

    @Column(name = "adresse_complete", nullable = false, columnDefinition = "TEXT")
    private String adresseComplete;

    @Enumerated(EnumType.STRING)
    @Column(name = "type_bien", nullable = false)
    private TypeBien typeBien = TypeBien.Standard;

    @Column(name = "code_postal", length = 10)
    private String codePostal;

    @Column(name = "date_creation", nullable = false, updatable = false)
    private LocalDateTime dateCreation = LocalDateTime.now();

    @OneToMany(mappedBy = "adresse", cascade = CascadeType.ALL)
    private List<Compteur> compteurs;

    // Enum pour le type de bien
    public enum TypeBien {
        Standard,
        Immeuble
    }
}