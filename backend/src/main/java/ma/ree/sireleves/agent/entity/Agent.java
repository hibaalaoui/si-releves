package ma.ree.sireleves.agent.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ma.ree.sireleves.common.entity.Quartier;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "agent", indexes = {
    @Index(name = "idx_agent_quartier", columnList = "id_quartier"),
    @Index(name = "idx_agent_actif", columnList = "actif")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Agent {

    @Id
    @Column(name = "id_agent", length = 50, nullable = false)
    @Size(max = 50, message = "L'ID agent ne peut pas dépasser 50 caractères")
    private String idAgent;

    @NotNull(message = "Le quartier est requis")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_quartier", nullable = false, foreignKey = @ForeignKey(name = "fk_agent_quartier"))
    private Quartier quartier;

    @NotBlank(message = "Le nom est requis")
    @Size(max = 100, message = "Le nom ne peut pas dépasser 100 caractères")
    @Column(name = "nom", nullable = false, length = 100)
    private String nom;

    @NotBlank(message = "Le prénom est requis")
    @Size(max = 100, message = "Le prénom ne peut pas dépasser 100 caractères")
    @Column(name = "prenom", nullable = false, length = 100)
    private String prenom;

    @Size(max = 20, message = "Le téléphone personnel ne peut pas dépasser 20 caractères")
    @Pattern(regexp = "^[0-9+\\-\\s()]*$", message = "Format de téléphone invalide")
    @Column(name = "tel_personnel", length = 20)
    private String telPersonnel;

    @NotBlank(message = "Le téléphone professionnel est requis")
    @Size(max = 20, message = "Le téléphone professionnel ne peut pas dépasser 20 caractères")
    @Pattern(regexp = "^[0-9+\\-\\s()]*$", message = "Format de téléphone invalide")
    @Column(name = "tel_professionnel", nullable = false, length = 20)
    private String telProfessionnel;

    @Column(name = "date_affectation", nullable = false, updatable = false)
    private LocalDateTime dateAffectation = LocalDateTime.now();

    @Column(name = "actif", nullable = false)
    private Boolean actif = true;

    @OneToMany(mappedBy = "agent", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ma.ree.sireleves.releve.entity.Releve> releves = new ArrayList<>();
}

