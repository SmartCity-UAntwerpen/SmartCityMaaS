package be.uantwerpen.controller;

import be.uantwerpen.visualization.model.CellLink;
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

    DummyVehicle vehicle = new DummyVehicle(1);

    List<DummyVehicle> vehicles = new ArrayList<DummyVehicle>();
    public World world  = new World(200,200);

    List<World> worlds = new ArrayList<World>();

    public boolean vehicle_start = false;
    @Autowired
    public BackendRestemplate backendRestemplate;

    @RequestMapping(value="/retrieveWorld")
    public World getWorld(){
        System.out.println("### Retrieve world callled ###");

        List<DummyPoint> listPoints = getMapDataBackend();
        World world  = new World(200,200);
        world.parseMap(listPoints);
        world.setWorld_ID("world1");
        //this.world.parseMap(listPoints);
        worlds.add(world);
        return world;
    }
    // http://146.175.140.44:1994/map/stringmapjson/top


    @RequestMapping(value="/startVehicle/{id}")
    public String startVehicle(@PathVariable int id){
      //
        vehicle.setID(id);
        new Thread(vehicle).start();
        vehicle_start = true;
        return "vehicle started";
    }




    @RequestMapping(value="/dataCore")
    public List<DummyPoint> getMapDataBackend(){
        System.out.println("### Retrieve map from backend ###");
        List<DummyPoint> listofPoints = backendRestemplate.getdataBackend();
        return listofPoints;
    }

    @RequestMapping(value="/{worldid}/progress/{id}")
    public int[] getProgress(@PathVariable String worldid,@PathVariable int id){
        int progress = vehicle.getValue();
       /* for(int i = 0 ; i < vehicles.size();i++)
        {
            if(id == vehicles.get(i).getID())
            {
                progress = vehicles.get(i).getValue();
                i = vehicles.size()+1;
            }
        }*/
        //world.startDelivery(progress);
        System.out.println("World id " + worldid);
        World world = new World();
        for(int i = 0; i < worlds.size();i++)
        {
            if(worlds.get(i).getWorld_ID().equals(worldid) == true)
            {
                world = worlds.get(i);
                i = worlds.size()+1;
            }
        }

        int[] coordinatesVehicle = new int[2];
        if(vehicle_start == true)
        {
            //System.out.println("Progress request for delivery: "+id + " progress "+progress);
           // System.out.println("World: "+world.getDimensionY());

            List<CellLink> cellLInks = world.getCellLinks();
            /*for(CellLink cl :  cellLInks)
            {
                System.out.println("CellLInk " + cl.getStartCell());
            }*/
            coordinatesVehicle = world.getDistancePoints(progress);
        }else
        {
            coordinatesVehicle[0] = -1;
            coordinatesVehicle[1] = -1;
        }
        return coordinatesVehicle;
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
