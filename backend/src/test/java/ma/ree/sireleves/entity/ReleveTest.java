package ma.ree.sireleves.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Tests unitaires de l'entité Releve")
class ReleveTest {

    @Test
    @DisplayName("Devrait créer un relevé avec le constructeur par défaut")
    void shouldCreateReleveWithDefaultConstructor() {
        // When
        Releve releve = new Releve();

        // Then
        assertThat(releve).isNotNull();
        assertThat(releve.getEnvoyeFacturation()).isFalse();
    }

    @Test
    @DisplayName("Devrait créer un relevé avec tous les paramètres")
    void shouldCreateReleveWithAllParameters() {
        // Given
        Compteur compteur = new Compteur();
        Agent agent = new Agent();
        LocalDateTime dateReleve = LocalDateTime.now();
        LocalDateTime dateEnvoi = LocalDateTime.now();

        // When
        Releve releve = new Releve(1, compteur, agent, dateReleve,
                new BigDecimal("1000.00"), new BigDecimal("1250.75"),
                new BigDecimal("250.75"), Releve.Unite.m3, true, dateEnvoi);

        // Then
        assertThat(releve.getIdReleve()).isEqualTo(1);
        assertThat(releve.getCompteur()).isEqualTo(compteur);
        assertThat(releve.getAgent()).isEqualTo(agent);
        assertThat(releve.getDateReleve()).isEqualTo(dateReleve);
        assertThat(releve.getAncienIndex()).isEqualTo(new BigDecimal("1000.00"));
        assertThat(releve.getNouvelIndex()).isEqualTo(new BigDecimal("1250.75"));
        assertThat(releve.getConsommation()).isEqualTo(new BigDecimal("250.75"));
        assertThat(releve.getUnite()).isEqualTo(Releve.Unite.m3);
        assertThat(releve.getEnvoyeFacturation()).isTrue();
        assertThat(releve.getDateEnvoiFacturation()).isEqualTo(dateEnvoi);
    }

    @Test
    @DisplayName("Devrait modifier les propriétés avec les setters")
    void shouldModifyPropertiesWithSetters() {
        // Given
        Releve releve = new Releve();
        Compteur compteur = new Compteur();
        Agent agent = new Agent();

        // When
        releve.setIdReleve(2);
        releve.setCompteur(compteur);
        releve.setAgent(agent);
        releve.setAncienIndex(new BigDecimal("2000.00"));
        releve.setNouvelIndex(new BigDecimal("2300.50"));
        releve.setConsommation(new BigDecimal("300.50"));
        releve.setUnite(Releve.Unite.kWh);
        releve.setEnvoyeFacturation(true);

        // Then
        assertThat(releve.getIdReleve()).isEqualTo(2);
        assertThat(releve.getCompteur()).isEqualTo(compteur);
        assertThat(releve.getAgent()).isEqualTo(agent);
        assertThat(releve.getAncienIndex()).isEqualTo(new BigDecimal("2000.00"));
        assertThat(releve.getNouvelIndex()).isEqualTo(new BigDecimal("2300.50"));
        assertThat(releve.getConsommation()).isEqualTo(new BigDecimal("300.50"));
        assertThat(releve.getUnite()).isEqualTo(Releve.Unite.kWh);
        assertThat(releve.getEnvoyeFacturation()).isTrue();
    }

    @Test
    @DisplayName("Devrait tester les valeurs de l'enum Unite")
    void shouldTestUniteEnumValues() {
        // Then
        assertThat(Releve.Unite.m3).isNotNull();
        assertThat(Releve.Unite.kWh).isNotNull();
        assertThat(Releve.Unite.values()).hasSize(2);
    }
}
