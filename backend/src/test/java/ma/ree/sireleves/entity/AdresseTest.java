package ma.ree.sireleves.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Tests unitaires de l'entité Adresse")
class AdresseTest {

    @Test
    @DisplayName("Devrait créer une adresse avec le constructeur par défaut")
    void shouldCreateAdresseWithDefaultConstructor() {
        // When
        Adresse adresse = new Adresse();

        // Then
        assertThat(adresse).isNotNull();
        assertThat(adresse.getTypeBien()).isEqualTo(Adresse.TypeBien.Standard);
        assertThat(adresse.getDateCreation()).isNotNull();
    }

    @Test
    @DisplayName("Devrait créer une adresse avec tous les paramètres")
    void shouldCreateAdresseWithAllParameters() {
        // Given
        Client client = new Client();
        Quartier quartier = new Quartier();
        LocalDateTime dateCreation = LocalDateTime.now();

        // When
        Adresse adresse = new Adresse(1, client, quartier, "123 Rue Test", 
                Adresse.TypeBien.Immeuble, "75001", dateCreation, null);

        // Then
        assertThat(adresse.getIdAdresse()).isEqualTo(1);
        assertThat(adresse.getClient()).isEqualTo(client);
        assertThat(adresse.getQuartier()).isEqualTo(quartier);
        assertThat(adresse.getAdresseComplete()).isEqualTo("123 Rue Test");
        assertThat(adresse.getTypeBien()).isEqualTo(Adresse.TypeBien.Immeuble);
        assertThat(adresse.getCodePostal()).isEqualTo("75001");
        assertThat(adresse.getDateCreation()).isEqualTo(dateCreation);
    }

    @Test
    @DisplayName("Devrait modifier les propriétés avec les setters")
    void shouldModifyPropertiesWithSetters() {
        // Given
        Adresse adresse = new Adresse();
        Client client = new Client();
        Quartier quartier = new Quartier();

        // When
        adresse.setIdAdresse(1);
        adresse.setClient(client);
        adresse.setQuartier(quartier);
        adresse.setAdresseComplete("456 Avenue Test");
        adresse.setTypeBien(Adresse.TypeBien.Immeuble);
        adresse.setCodePostal("10000");

        // Then
        assertThat(adresse.getIdAdresse()).isEqualTo(1);
        assertThat(adresse.getClient()).isEqualTo(client);
        assertThat(adresse.getQuartier()).isEqualTo(quartier);
        assertThat(adresse.getAdresseComplete()).isEqualTo("456 Avenue Test");
        assertThat(adresse.getTypeBien()).isEqualTo(Adresse.TypeBien.Immeuble);
        assertThat(adresse.getCodePostal()).isEqualTo("10000");
    }

    @Test
    @DisplayName("Devrait tester les valeurs de l'enum TypeBien")
    void shouldTestTypeBienEnumValues() {
        // Then
        assertThat(Adresse.TypeBien.Standard).isNotNull();
        assertThat(Adresse.TypeBien.Immeuble).isNotNull();
        assertThat(Adresse.TypeBien.values()).hasSize(2);
    }
}
