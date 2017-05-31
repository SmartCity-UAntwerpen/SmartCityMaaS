package be.uantwerpen.controller;

import be.uantwerpen.visualization.model.DummyPoint;
import be.uantwerpen.visualization.model.DummyVehicle;
import be.uantwerpen.visualization.model.World;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Frédéric Melaerts on 11/05/2017.
 */
@RestController
public class DataController {

    DummyVehicle vehicle = new DummyVehicle();
    public World world  = new World(200,200);;
    @Autowired
    public BackendRestemplate backendRestemplate;
    @RequestMapping(value="/retrieveWorld")
    public World getWorld(){
        System.out.println("### Retrieve world callled ###");

        List<DummyPoint> listPoints = getMapDataBackend();
        world.parseMap(listPoints);
        return world;
    }
    // http://146.175.140.44:1994/map/stringmapjson/top


    @RequestMapping(value="/startVehicle")
    public String startVehicle(){
        new Thread(vehicle).start();
        return "vehicle started";
    }


    @RequestMapping(value="/dataCore")
    public List<DummyPoint> getMapDataBackend(){
        System.out.println("### Retrieve map from backend ###");
        List<DummyPoint> listofPoints = backendRestemplate.getdataBackend();
        return listofPoints;
    }

    @RequestMapping(value="/{id}/progress")
    public int[] getProgress(@PathVariable int id){
        int progress = vehicle.getValue();
        world.startDelivery(progress);
        System.out.println("Progress request for delivery: "+id + " progress "+progress);

        return world.startDelivery(progress);
    }



   /* @RequestMapping(value="/retrieveData")
    public String getMapBackend(){
        System.out.println("### Retrieve map from backend ###");



        World world = new World(200,200);




        world.parseMap();
        return world;
    }*/


/*

    @RequestMapping(value={"/RESTfulExample/json/product/post"}, method= RequestMethod.POST)
    public String addTimeSample(@RequestBody Product result){
        System.out.println(result);
        if(result.getRfid_reader() != null && result.getRfid_tag() != null && result.getFlag() != null)
        {
            TrackSample trackSample = new TrackSample(null,result.getRfid_tag(),result.getRfid_reader(),result.getFlag(),null);
            List<TrackSample> trackSamples = new ArrayList<TrackSample>();
            trackSamples.add(trackSample);
            BrokerRestTemplateClient_TrackSample.putTimeSample(trackSamples);
        }
        return "received";
    }*/








}
