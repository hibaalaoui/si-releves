package ma.ree.sireleves.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Tests unitaires de l'entité Agent")
class AgentTest {

    @Test
    @DisplayName("Devrait créer un agent avec le constructeur par défaut")
    void shouldCreateAgentWithDefaultConstructor() {
        // When
        Agent agent = new Agent();

        // Then
        assertThat(agent).isNotNull();
        assertThat(agent.getActif()).isTrue();
        assertThat(agent.getDateAffectation()).isNotNull();
    }

    @Test
    @DisplayName("Devrait créer un agent avec tous les paramètres")
    void shouldCreateAgentWithAllParameters() {
        // Given
        Quartier quartier = new Quartier();
        LocalDateTime dateAffectation = LocalDateTime.now();

        // When
        Agent agent = new Agent("AGT001", quartier, "DUPONT", "Marie", 
                "0612345678", "0145678901", dateAffectation, true, null);

        // Then
        assertThat(agent.getIdAgent()).isEqualTo("AGT001");
        assertThat(agent.getQuartier()).isEqualTo(quartier);
        assertThat(agent.getNom()).isEqualTo("DUPONT");
        assertThat(agent.getPrenom()).isEqualTo("Marie");
        assertThat(agent.getTelPersonnel()).isEqualTo("0612345678");
        assertThat(agent.getTelProfessionnel()).isEqualTo("0145678901");
        assertThat(agent.getDateAffectation()).isEqualTo(dateAffectation);
        assertThat(agent.getActif()).isTrue();
    }

    @Test
    @DisplayName("Devrait modifier les propriétés avec les setters")
    void shouldModifyPropertiesWithSetters() {
        // Given
        Agent agent = new Agent();
        Quartier quartier = new Quartier();

        // When
        agent.setIdAgent("AGT002");
        agent.setQuartier(quartier);
        agent.setNom("MARTIN");
        agent.setPrenom("Pierre");
        agent.setTelPersonnel("0623456789");
        agent.setTelProfessionnel("0123456789");
        agent.setActif(false);

        // Then
        assertThat(agent.getIdAgent()).isEqualTo("AGT002");
        assertThat(agent.getQuartier()).isEqualTo(quartier);
        assertThat(agent.getNom()).isEqualTo("MARTIN");
        assertThat(agent.getPrenom()).isEqualTo("Pierre");
        assertThat(agent.getTelPersonnel()).isEqualTo("0623456789");
        assertThat(agent.getTelProfessionnel()).isEqualTo("0123456789");
        assertThat(agent.getActif()).isFalse();
    }
}
