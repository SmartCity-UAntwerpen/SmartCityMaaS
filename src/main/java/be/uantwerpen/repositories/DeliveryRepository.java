package be.uantwerpen.repositories;

import be.uantwerpen.model.Delivery;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Frédéric Melaerts on 2/05/2017.
 */
@Repository
public interface DeliveryRepository extends CrudRepository<Delivery, Long> {
    //List<Delivery> findAll();
}
