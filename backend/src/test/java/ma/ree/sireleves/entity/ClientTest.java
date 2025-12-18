package ma.ree.sireleves.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Tests unitaires de l'entité Client")
class ClientTest {

    @Test
    @DisplayName("Devrait créer un client avec le constructeur par défaut")
    void shouldCreateClientWithDefaultConstructor() {
        // When
        Client client = new Client();

        // Then
        assertThat(client).isNotNull();
        assertThat(client.getDateCreation()).isNotNull();
    }

    @Test
    @DisplayName("Devrait créer un client avec tous les paramètres")
    void shouldCreateClientWithAllParameters() {
        // Given
        LocalDateTime dateCreation = LocalDateTime.now();

        // When
        Client client = new Client("CLI001", "Dupont", "Jean", dateCreation, null);

        // Then
        assertThat(client.getIdClient()).isEqualTo("CLI001");
        assertThat(client.getNom()).isEqualTo("Dupont");
        assertThat(client.getPrenom()).isEqualTo("Jean");
        assertThat(client.getDateCreation()).isEqualTo(dateCreation);
    }

    @Test
    @DisplayName("Devrait modifier les propriétés avec les setters")
    void shouldModifyPropertiesWithSetters() {
        // Given
        Client client = new Client();

        // When
        client.setIdClient("CLI002");
        client.setNom("Martin");
        client.setPrenom("Sophie");

        // Then
        assertThat(client.getIdClient()).isEqualTo("CLI002");
        assertThat(client.getNom()).isEqualTo("Martin");
        assertThat(client.getPrenom()).isEqualTo("Sophie");
    }
}
