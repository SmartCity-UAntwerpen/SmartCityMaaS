package be.uantwerpen.controller;

import be.uantwerpen.visualization.model.World;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Frédéric Melaerts on 11/05/2017.
 */
@RestController
public class DataController {

    @RequestMapping(value="/retrieveWorld")
    public World getWorld(){
        System.out.println("### Retrieve world callled ###");
        World world = new World(200,200);
        world.parseMap();
        return world;
    }
}
