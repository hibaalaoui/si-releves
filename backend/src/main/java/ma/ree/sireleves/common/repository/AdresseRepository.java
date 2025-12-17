package ma.ree.sireleves.common.repository;

import ma.ree.sireleves.common.entity.Adresse;
import ma.ree.sireleves.common.entity.Client;
import ma.ree.sireleves.common.entity.Quartier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdresseRepository extends JpaRepository<Adresse, Integer> {
    
    List<Adresse> findByClient(Client client);
    
    List<Adresse> findByQuartier(Quartier quartier);
    
    List<Adresse> findByQuartierIdQuartier(Integer idQuartier);
    
    List<Adresse> findByClientIdClient(String idClient);
}

