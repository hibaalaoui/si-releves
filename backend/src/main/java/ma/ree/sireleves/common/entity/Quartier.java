package ma.ree.sireleves.common.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ma.ree.sireleves.agent.entity.Agent;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "quartier", uniqueConstraints = {
    @UniqueConstraint(name = "uk_quartier_nom", columnNames = "nom_quartier")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Quartier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_quartier")
    private Integer idQuartier;

    @NotBlank(message = "Le nom du quartier est requis")
    @Size(max = 100, message = "Le nom du quartier ne peut pas dépasser 100 caractères")
    @Column(name = "nom_quartier", nullable = false, length = 100)
    private String nomQuartier;

    @NotBlank(message = "La ville est requise")
    @Size(max = 100, message = "La ville ne peut pas dépasser 100 caractères")
    @Column(name = "ville", nullable = false, length = 100)
    private String ville = "Rabat";

    @OneToMany(mappedBy = "quartier", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Adresse> adresses = new ArrayList<>();

    @OneToMany(mappedBy = "quartier", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Agent> agents = new ArrayList<>();
}

