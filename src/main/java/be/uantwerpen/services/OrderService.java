package be.uantwerpen.services;

import be.uantwerpen.model.Job;
import be.uantwerpen.model.Order;
import be.uantwerpen.repositories.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Revil on 29/05/2017.
 */
@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    public Iterable<Order> findAll() {
        return this.orderRepository.findAll();
    }

    public void saveOrder(Order order)
    {
        this.orderRepository.save(order);
    }
}
