package be.uantwerpen.controller;

import be.uantwerpen.visualization.model.DummyVehicle;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Frédéric Melaerts on 7/06/2017.
 */
@RestController
public class VehicleController {

    DummyVehicle vehicle1 = new DummyVehicle(1);
    DummyVehicle vehicle2 = new DummyVehicle(2);
    DummyVehicle vehicle3 = new DummyVehicle(3);
    public boolean vehicle_start = false;


    @RequestMapping(value="/startVehicle/{id}")
    public String startVehicle(@PathVariable int id){
        //

        if(vehicle1.getID() == id)
        {
            new Thread(vehicle1).start();
        }else if(vehicle2.getID() == id)
        {
            new Thread(vehicle2).start();
        }else
        {
            new Thread(vehicle3).start();
        }
        String output = "Start dummy vehicle with " + id;
        System.out.println(output);

        return output;
    }


    @RequestMapping(value="/bot/getOneVehicle/{id}", method= RequestMethod.GET)
    public String getProgressVehicle(@PathVariable int id) {

        int progress = 0;
        int start = 0;
        int end = 0;
        if(vehicle1.getID() == id)
        {
            progress = vehicle1.getValue();
            start = 3;
            end = 7;
        }else if(vehicle2.getID() == id)
        {
            progress = vehicle1.getValue();
            start = 7;
            end = 8;
        }else
        {
            progress = vehicle1.getValue();
            start = 8;
            end = 2;
        }

        // {"idVehicle":1,"idStart":1,"idEnd":2,"percentage":75}
        String JSON_result = "{\"idVehicle\":"+id+",\"idStart\":"+start+",\"idEnd\":"+end+",\"percentage\":"+progress+"}";
        System.out.println("Progress of vehicle " + JSON_result);
        return JSON_result;
    }
}
