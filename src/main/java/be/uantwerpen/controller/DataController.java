package be.uantwerpen.controller;

import be.uantwerpen.model.Job;
import be.uantwerpen.model.JobList;
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
        // Quentin wereld = World world (300,300)
        // Dries wereld = World world  = new World(250,250);

        /*
            !!! The dimensions of the world must in the right ration with the Mapcanvas from the  html file !!!
            Example: html : width : 1000 height : 1000 ==> world : 250,250
         */
        World world  = new World(300,300);
        System.out.println("### CELLIS POINTS SIZE ###"+listPoints.size());

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
        System.out.println("START VEHICLE");

        return "vehicle started";
    }

    /**
     * Retrieve the real name a point as it is defined in the received map.
     * @param valuePoint
     * @return
     */
    @RequestMapping(value="/retrieveRealPointName/{valuePoint}")
    public int getPointName(@PathVariable int valuePoint){
        int keyPoint = backendRestemplate.getKeyHashMap(valuePoint);
        if(valuePoint != -1)
        {
            System.out.println("### Retrieve key point name from "+valuePoint+ " which is "+ keyPoint+" ###");
        }else
        {
            System.out.println("### Could not retrieve key point name from "+valuePoint+ ", error "+ keyPoint+" ###");
        }
        return keyPoint;
    }

    @RequestMapping(value="/dataCore")
    public List<DummyPoint> getMapDataBackend(){
        System.out.println("### Retrieve map from backend ###");
        List<DummyPoint> listofPoints = backendRestemplate.getdataBackend();
        return listofPoints;
    }

    @RequestMapping(value="/{worldid}/progress/{delivery_id}")
    public int[] getProgress(@PathVariable String worldid, @PathVariable String delivery_id){
        int progress = vehicle.getValue();

        System.out.println("Progress is asked for id delivery "+delivery_id);


       /* for(int i = 0 ; i < vehicles.size();i++)
        {
            if(id == vehicles.get(i).getID())
            {
                progress = vehicles.get(i).getValue();
                i = vehicles.size()+1;
            }
        }*/
        //world.startDelivery(progress);
        // System.out.println("World id " + worldid);
        World world = new World();
        for(int i = 0; i < worlds.size();i++)
        {
            if(worlds.get(i).getWorld_ID().equals(worldid) == true)
            {
                world = worlds.get(i);
                i = worlds.size()+1;
            }
        }
        // TODO op basis van de deliveryID wordt de juist joblistopgehaald


        Job job1 = new Job();
        job1.setIdStart(1);
        job1.setIdEnd(2);
        job1.setTypeVehicle("car");
        job1.setIdVehicle(1);

        Job job2 = new Job();
        job2.setIdStart(2);
        job2.setIdEnd(3);
        job2.setTypeVehicle("car");
        job2.setIdVehicle(1);

        Job job3 = new Job();
        job3.setIdStart(3);
        job3.setIdEnd(7);
        job3.setTypeVehicle("robot");
        job3.setIdVehicle(1);

        Job job4 = new Job();
        job4.setIdStart(7);
        job4.setIdEnd(8);
        job4.setTypeVehicle("robot");
        job4.setIdVehicle(1);

        Job job5 = new Job();
        job5.setIdStart(8);
        job5.setIdEnd(2);
        job5.setTypeVehicle("drone");
        job5.setIdVehicle(1);

        JobList jobList = new JobList();
        jobList.addJob(job1);
        jobList.addJob(job2);

        jobList.addJob(job3);
        jobList.addJob(job4);
        jobList.addJob(job5);

        List<Job> jobs = jobList.getJobs();

        // TODO ask Oliver service for current job of this delivery with the DeliveryID
        // it returns the value of
        //

        List<Integer> currentListofJobs = new ArrayList<Integer>();

        for(int i = 0; i < jobs.size();i++)
        {
            int id_start = Math.toIntExact(jobs.get(i).getIdStart());
            int id_end = Math.toIntExact(jobs.get(i).getIdEnd());
            id_start = backendRestemplate.getValueofKeyHashMap(id_start);
            id_end = backendRestemplate.getValueofKeyHashMap(id_end);
            currentListofJobs.add(id_start);
            currentListofJobs.add(id_end);
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
            coordinatesVehicle = world.getDistancePoints(currentListofJobs,progress);
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
