package be.uantwerpen.repositories;

import be.uantwerpen.Models.Cost;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Revil on 17/05/2017.
 */
@Repository
public interface CostRepository extends CrudRepository<Cost, Long> {
}
