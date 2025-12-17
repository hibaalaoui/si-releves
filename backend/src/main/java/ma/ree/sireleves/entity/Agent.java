package ma.ree.sireleves.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "agent")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Agent {

    @Id
    @Column(name = "id_agent", length = 50)
    private String idAgent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_quartier", nullable = false)
    private Quartier quartier;

    @Column(nullable = false, length = 100)
    private String nom;

    @Column(nullable = false, length = 100)
    private String prenom;

    @Column(name = "tel_personnel", length = 20)
    private String telPersonnel;

    @Column(name = "tel_professionnel", nullable = false, length = 20)
    private String telProfessionnel;

    @Column(name = "date_affectation", nullable = false, updatable = false)
    private LocalDateTime dateAffectation = LocalDateTime.now();

    @Column(nullable = false)
    private Boolean actif = true;

    @OneToMany(mappedBy = "agent", cascade = CascadeType.ALL)
    private List<Releve> releves;
}