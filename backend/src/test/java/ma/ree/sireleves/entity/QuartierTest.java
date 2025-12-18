package ma.ree.sireleves.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Tests unitaires de l'entité Quartier")
class QuartierTest {

    @Test
    @DisplayName("Devrait créer un quartier avec le constructeur par défaut")
    void shouldCreateQuartierWithDefaultConstructor() {
        // When
        Quartier quartier = new Quartier();

        // Then
        assertThat(quartier).isNotNull();
        assertThat(quartier.getVille()).isEqualTo("Rabat");
    }

    @Test
    @DisplayName("Devrait créer un quartier avec tous les paramètres")
    void shouldCreateQuartierWithAllParameters() {
        // When
        Quartier quartier = new Quartier(1, "Centre-ville", "Casablanca", null, null);

        // Then
        assertThat(quartier.getIdQuartier()).isEqualTo(1);
        assertThat(quartier.getNomQuartier()).isEqualTo("Centre-ville");
        assertThat(quartier.getVille()).isEqualTo("Casablanca");
    }

    @Test
    @DisplayName("Devrait modifier les propriétés avec les setters")
    void shouldModifyPropertiesWithSetters() {
        // Given
        Quartier quartier = new Quartier();

        // When
        quartier.setIdQuartier(2);
        quartier.setNomQuartier("Agora");
        quartier.setVille("Tanger");

        // Then
        assertThat(quartier.getIdQuartier()).isEqualTo(2);
        assertThat(quartier.getNomQuartier()).isEqualTo("Agora");
        assertThat(quartier.getVille()).isEqualTo("Tanger");
    }
}
