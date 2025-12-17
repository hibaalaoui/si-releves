package ma.ree.sireleves.common.repository;

import ma.ree.sireleves.common.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends JpaRepository<Client, String> {
    
    boolean existsByIdClient(String idClient);
}

