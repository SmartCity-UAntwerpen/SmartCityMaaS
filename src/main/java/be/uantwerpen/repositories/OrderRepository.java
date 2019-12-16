package be.uantwerpen.repositories;

import be.uantwerpen.model.Order;
import be.uantwerpen.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * NV 2018
 */
@Repository
public interface OrderRepository extends CrudRepository<Order, Long> {

    List<Order> findAll();
}

