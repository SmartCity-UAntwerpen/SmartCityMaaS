package be.uantwerpen.services;

import be.uantwerpen.model.Delivery;
import be.uantwerpen.repositories.DeliveryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Frédéric Melaerts on 2/05/2017.
 */
@Service
public class DeliveryService {
    @Autowired
    private DeliveryRepository deliveryRepository;

    public Iterable<Delivery> findAll() {
        return this.deliveryRepository.findAll();
    }
    public Delivery findOne(Long id) {
        return this.deliveryRepository.findOne(id);
    }
    public void save(final Delivery user){

        this.deliveryRepository.save(user);
    }

    public void saveSomeAttributes(Delivery delivery) {
        Delivery tempDelivery = delivery.getId()==null?null:findOne(delivery.getId());
        if (tempDelivery != null){
            tempDelivery.setLastName(delivery.getLastName());
            tempDelivery.setFirstName(delivery.getFirstName());
            tempDelivery.setPointA(delivery.getPointA());
            tempDelivery.setPointB(delivery.getPointB());
            tempDelivery.setPassengers(delivery.getPassengers());
            deliveryRepository.save(delivery);
        }
        else{
            deliveryRepository.save(delivery);
        }
    }
}