package ma.ree.sireleves.repository;

import ma.ree.sireleves.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClientRepository extends JpaRepository<Client, String> {

    // Rechercher des clients par nom (pour recherche)
    List<Client> findByNomContainingIgnoreCase(String nom);

    // Rechercher des clients par prénom
    List<Client> findByPrenomContainingIgnoreCase(String prenom);
}