package be.uantwerpen.repositories;

import be.uantwerpen.model.Delivery;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * NV 2018
 */
@Repository
public interface DeliveryRepository extends CrudRepository<Delivery, Long> {
}
