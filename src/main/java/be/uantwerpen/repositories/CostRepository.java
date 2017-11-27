package be.uantwerpen.repositories;

import be.uantwerpen.model.Cost;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Revil on 17/05/2017.
 */


// Tabel maken voor Cost, hierin zitten de attributen van het Cost object

@Repository
public interface CostRepository extends CrudRepository<Cost, Long> {
}
