package ma.ree.sireleves.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "quartier")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Quartier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_quartier")
    private Integer idQuartier;

    @Column(name = "nom_quartier", nullable = false, unique = true, length = 100)
    private String nomQuartier;

    @Column(nullable = false, length = 100)
    private String ville = "Rabat";

    @OneToMany(mappedBy = "quartier", cascade = CascadeType.ALL)
    private List<Adresse> adresses;

    @OneToMany(mappedBy = "quartier", cascade = CascadeType.ALL)
    private List<Agent> agents;
}