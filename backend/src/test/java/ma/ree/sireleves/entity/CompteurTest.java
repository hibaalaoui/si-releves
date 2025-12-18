package ma.ree.sireleves.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Tests unitaires de l'entité Compteur")
class CompteurTest {

    @Test
    @DisplayName("Devrait créer un compteur avec le constructeur par défaut")
    void shouldCreateCompteurWithDefaultConstructor() {
        // When
        Compteur compteur = new Compteur();

        // Then
        assertThat(compteur).isNotNull();
        assertThat(compteur.getIndexActuel()).isEqualTo(BigDecimal.ZERO);
        assertThat(compteur.getPourEspacesCommuns()).isFalse();
        assertThat(compteur.getActif()).isTrue();
        assertThat(compteur.getDateInstallation()).isNotNull();
    }

    @Test
    @DisplayName("Devrait créer un compteur avec tous les paramètres")
    void shouldCreateCompteurWithAllParameters() {
        // Given
        Adresse adresse = new Adresse();
        LocalDate dateInstallation = LocalDate.now();
        LocalDateTime dateDerniereReleve = LocalDateTime.now();

        // When
        Compteur compteur = new Compteur("CMP001", adresse, Compteur.TypeCompteur.Eau,
                new BigDecimal("1000.50"), dateInstallation, dateDerniereReleve, false, true, null);

        // Then
        assertThat(compteur.getIdCompteur()).isEqualTo("CMP001");
        assertThat(compteur.getAdresse()).isEqualTo(adresse);
        assertThat(compteur.getType()).isEqualTo(Compteur.TypeCompteur.Eau);
        assertThat(compteur.getIndexActuel()).isEqualTo(new BigDecimal("1000.50"));
        assertThat(compteur.getDateInstallation()).isEqualTo(dateInstallation);
        assertThat(compteur.getDateDerniereReleve()).isEqualTo(dateDerniereReleve);
        assertThat(compteur.getPourEspacesCommuns()).isFalse();
        assertThat(compteur.getActif()).isTrue();
    }

    @Test
    @DisplayName("Devrait modifier les propriétés avec les setters")
    void shouldModifyPropertiesWithSetters() {
        // Given
        Compteur compteur = new Compteur();
        Adresse adresse = new Adresse();

        // When
        compteur.setIdCompteur("CMP002");
        compteur.setAdresse(adresse);
        compteur.setType(Compteur.TypeCompteur.Electricite);
        compteur.setIndexActuel(new BigDecimal("2000.75"));
        compteur.setPourEspacesCommuns(true);
        compteur.setActif(false);

        // Then
        assertThat(compteur.getIdCompteur()).isEqualTo("CMP002");
        assertThat(compteur.getAdresse()).isEqualTo(adresse);
        assertThat(compteur.getType()).isEqualTo(Compteur.TypeCompteur.Electricite);
        assertThat(compteur.getIndexActuel()).isEqualTo(new BigDecimal("2000.75"));
        assertThat(compteur.getPourEspacesCommuns()).isTrue();
        assertThat(compteur.getActif()).isFalse();
    }

    @Test
    @DisplayName("Devrait tester les valeurs de l'enum TypeCompteur")
    void shouldTestTypeCompteurEnumValues() {
        // Then
        assertThat(Compteur.TypeCompteur.Eau).isNotNull();
        assertThat(Compteur.TypeCompteur.Electricite).isNotNull();
        assertThat(Compteur.TypeCompteur.values()).hasSize(2);
    }
}
