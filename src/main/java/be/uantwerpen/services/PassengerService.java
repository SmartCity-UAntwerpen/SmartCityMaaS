package be.uantwerpen.services;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 *  NV 2018
 */
@Service
public class PassengerService {
    public Iterable<Integer> findAll() {
        Iterable<Integer> listPassengers;
        List<Integer> persons = new ArrayList<Integer>();
        persons.add(1);
        persons.add(2);
        persons.add(3);
        persons.add(4);
        listPassengers = persons;
        return listPassengers;
    }
}
