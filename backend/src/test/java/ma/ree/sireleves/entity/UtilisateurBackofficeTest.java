package ma.ree.sireleves.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Tests unitaires de l'entité UtilisateurBackoffice")
class UtilisateurBackofficeTest {

    @Test
    @DisplayName("Devrait créer un utilisateur avec le constructeur par défaut")
    void shouldCreateUtilisateurWithDefaultConstructor() {
        // When
        UtilisateurBackoffice utilisateur = new UtilisateurBackoffice();

        // Then
        assertThat(utilisateur).isNotNull();
        assertThat(utilisateur.getRole()).isEqualTo(UtilisateurBackoffice.Role.Utilisateur);
        assertThat(utilisateur.getPremiereConnexion()).isTrue();
        assertThat(utilisateur.getActif()).isTrue();
        assertThat(utilisateur.getDateAjout()).isNotNull();
    }

    @Test
    @DisplayName("Devrait créer un utilisateur avec tous les paramètres")
    void shouldCreateUtilisateurWithAllParameters() {
        // Given
        LocalDateTime dateAjout = LocalDateTime.now();
        LocalDateTime dateModification = LocalDateTime.now();

        // When
        UtilisateurBackoffice utilisateur = new UtilisateurBackoffice(1, "DUPONT", "Marie",
                "marie.dupont@email.com", "hashedPassword", UtilisateurBackoffice.Role.Superadmin,
                false, dateAjout, dateModification, true);

        // Then
        assertThat(utilisateur.getIdUtilisateur()).isEqualTo(1);
        assertThat(utilisateur.getNom()).isEqualTo("DUPONT");
        assertThat(utilisateur.getPrenom()).isEqualTo("Marie");
        assertThat(utilisateur.getEmail()).isEqualTo("marie.dupont@email.com");
        assertThat(utilisateur.getPasswordHash()).isEqualTo("hashedPassword");
        assertThat(utilisateur.getRole()).isEqualTo(UtilisateurBackoffice.Role.Superadmin);
        assertThat(utilisateur.getPremiereConnexion()).isFalse();
        assertThat(utilisateur.getDateAjout()).isEqualTo(dateAjout);
        assertThat(utilisateur.getDateModification()).isEqualTo(dateModification);
        assertThat(utilisateur.getActif()).isTrue();
    }

    @Test
    @DisplayName("Devrait modifier les propriétés avec les setters")
    void shouldModifyPropertiesWithSetters() {
        // Given
        UtilisateurBackoffice utilisateur = new UtilisateurBackoffice();

        // When
        utilisateur.setIdUtilisateur(2);
        utilisateur.setNom("MARTIN");
        utilisateur.setPrenom("Pierre");
        utilisateur.setEmail("pierre.martin@email.com");
        utilisateur.setPasswordHash("newHashedPassword");
        utilisateur.setRole(UtilisateurBackoffice.Role.Utilisateur);
        utilisateur.setPremiereConnexion(false);
        utilisateur.setActif(false);

        // Then
        assertThat(utilisateur.getIdUtilisateur()).isEqualTo(2);
        assertThat(utilisateur.getNom()).isEqualTo("MARTIN");
        assertThat(utilisateur.getPrenom()).isEqualTo("Pierre");
        assertThat(utilisateur.getEmail()).isEqualTo("pierre.martin@email.com");
        assertThat(utilisateur.getPasswordHash()).isEqualTo("newHashedPassword");
        assertThat(utilisateur.getRole()).isEqualTo(UtilisateurBackoffice.Role.Utilisateur);
        assertThat(utilisateur.getPremiereConnexion()).isFalse();
        assertThat(utilisateur.getActif()).isFalse();
    }

    @Test
    @DisplayName("Devrait tester les valeurs de l'enum Role")
    void shouldTestRoleEnumValues() {
        // Then
        assertThat(UtilisateurBackoffice.Role.Superadmin).isNotNull();
        assertThat(UtilisateurBackoffice.Role.Utilisateur).isNotNull();
        assertThat(UtilisateurBackoffice.Role.values()).hasSize(2);
    }
}
