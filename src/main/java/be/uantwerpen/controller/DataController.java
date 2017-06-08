package be.uantwerpen.controller;

import be.uantwerpen.model.Job;
import be.uantwerpen.model.JobList;
import be.uantwerpen.services.JobListService;
import be.uantwerpen.visualization.model.CellLink;
import be.uantwerpen.visualization.model.DummyPoint;
import be.uantwerpen.visualization.model.DummyVehicle;
import be.uantwerpen.visualization.model.World;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Frédéric Melaerts on 11/05/2017.
 */
@RestController
public class DataController {


    @Value("${core.ip:localhost}")
    private String serverCoreIP;

    @Value("#{new Integer(${core.port}) ?: 1994}")
    private int serverCorePort;

    @Autowired
    private JobListService jobListService;

    DummyVehicle vehicle = new DummyVehicle(1);

    List<DummyVehicle> vehicles = new ArrayList<DummyVehicle>();
    public World world  = new World(200,200);

    List<World> worlds = new ArrayList<World>();
    int vehicleID = 1;

    @Autowired
    private RestTemplate restTemplate;

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


    @RequestMapping(value="/vehicle/{id}")
    public void changeVehicleID(@PathVariable int id){
        vehicleID = id;
        return;
    }

    @RequestMapping(value="/world1/allVehicles")
    public List<Integer> getAllVehicles(){
        List<Integer> idVehicles = new ArrayList<Integer>();
        String requestAll = "request all";
        // String URL = "http://localhost:9000/posAll";
        String URL = "http://"+serverCoreIP+":"+serverCorePort+"/posAll";

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(URL).queryParam("requestAll", requestAll);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
        HttpEntity<?> entity = new HttpEntity<>(headers);
        // Get response from the core
        HttpEntity<String> httpResponse = restTemplate.exchange(
                builder.build().encode().toUri(),
                HttpMethod.GET,
                entity,
                String.class);
        JSONParser parser = new JSONParser();

        Object obj = null;
        try {
            obj = parser.parse(httpResponse.getBody());

            JSONObject jsonObject = (JSONObject) obj;
            JSONArray virDevices = (JSONArray) jsonObject.get("vehicles");
            Iterator<String> iterator = virDevices.iterator();
            while (iterator.hasNext()) {
                obj = iterator.next();
                JSONObject par_jsonObject = (JSONObject) obj;
                int idVeh = ((Long)par_jsonObject.get("idVehicle")).intValue();
                idVehicles.add(idVeh);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return idVehicles;
    }



    @RequestMapping(value="/{worldid}/progress/{delivery_id}/{vehicle_id}")
    public int[] getProgress(@PathVariable String worldid, @PathVariable String delivery_id, @PathVariable int vehicle_id){
        int progress = 0;//vehicle.getValue();

        System.out.println("Progress is asked for id delivery "+delivery_id + " vehicle_id "+vehicle_id );

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

/*
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
*/
        // TODO ask Oliver service for current job of this delivery with the DeliveryID
        // it returns the value of
        //
        String idVehicle = "request progress";
        String URL = "null";
        UriComponentsBuilder builder;
        boolean jobListNull = false;
        // When delivery_id == null then the getprogress function is asked for the the visualization
        if(delivery_id.equals("null") == false)
        {
            //URL = "http://localhost:9000/bot/getOneVehicle/"+vehicleID;

            for (JobList jl2: jobListService.findAll()) {
                if(jl2.getIdDelivery().equals(delivery_id) == true)
                {
                    vehicleID = Math.toIntExact(jl2.getJobs().get(0).getIdVehicle());
                    jobListNull = true;
                }
            }

            URL = "http://"+serverCoreIP+":"+serverCorePort+"/bot/getOneVehicle/"+74;//vehicleID;
            builder =  UriComponentsBuilder.fromHttpUrl(URL).queryParam("idVehicle", idVehicle);

        }else
        {
            //URL = "http://localhost:9000/bot/getOneVehicle/"+vehicle_id;
            URL = "http://"+serverCoreIP+":"+serverCorePort+"/bot/getOneVehicle/"+vehicle_id;
            builder =  UriComponentsBuilder.fromHttpUrl(URL).queryParam("idVehicle", vehicle_id);
            System.out.println("ID device "+vehicle_id);
        }
        int[] coordinatesVehicle = new int[3];


        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
        HttpEntity<?> entity = new HttpEntity<>(headers);

        if((delivery_id.equals("null") == false && jobListNull == true) || delivery_id.equals("null") == true) {
            // Get response from the core
            HttpEntity<String> httpResponse = restTemplate.exchange(
                    builder.build().encode().toUri(),
                    HttpMethod.GET,
                    entity,
                    String.class);

            //System.out.println("Performed exchange for bot vehicle" );
            System.out.println("Response core : " + httpResponse.toString());
            System.out.println("Response core : " + httpResponse.getBody());
            System.out.println("Response body core : " + httpResponse.hasBody());
            String vehicleInfo = httpResponse.getBody();
            JSONParser parser = new JSONParser();
            Job job1 = new Job();
            job1.setIdStart(1);
            job1.setIdEnd(2);
            job1.setTypeVehicle("car");
            job1.setIdVehicle(1);
            Object obj = null;
            List<Integer> currentListofJobs = new ArrayList<Integer>();

            try {
                obj = parser.parse(vehicleInfo);

                JSONObject jsonObject = (JSONObject) obj;
                int idVeh = ((Long) jsonObject.get("idVehicle")).intValue();
                int idStart = ((Long) jsonObject.get("idStart")).intValue();
                int idEnd = ((Long) jsonObject.get("idEnd")).intValue();
                int percentage = ((Long) jsonObject.get("percentage")).intValue();

                // System.out.println("idVeh "+ idVeh + " idStart "+idStart+" idEnd "+idEnd+ " percentage "+percentage);
                int id_start = backendRestemplate.getValueofKeyHashMap(idStart);
                int id_end = backendRestemplate.getValueofKeyHashMap(idEnd);
                currentListofJobs.add(id_start);
                currentListofJobs.add(id_end);
                progress = percentage;

            } catch (ParseException e) {
                e.printStackTrace();
            }
            int[] coordinatesVehicle_temp = world.getDistancePoints(currentListofJobs,progress);
            coordinatesVehicle[0] = coordinatesVehicle_temp[0];
            coordinatesVehicle[1] = coordinatesVehicle_temp[1];
            if(delivery_id.equals("null") == false)
            {
                coordinatesVehicle[2] = vehicleID;
            }else
            {
                coordinatesVehicle[2] = vehicle_id;
            }
        }else
        {
            coordinatesVehicle[0] = -1;
            coordinatesVehicle[1] = -1;
            coordinatesVehicle[2] = -1;
        }

/*
        URL url = null;
        try {
            url = new URL("http://localhost:9000/bot/getOneVehicle/0");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-Type", "application/json");

            //String input = "{\"qty\":100,\"name\":\"iPad 4\"}";µ
            String response =  conn.getResponseMessage();
            System.out.println("Response of core " + response);

            System.out.println("Response of core " + response);
            conn.disconnect();
        } catch (MalformedURLException | ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }*/



       /* for(int i = 0; i < jobs.size();i++)
        {
            int id_start = Math.toIntExact(jobs.get(i).getIdStart());
            int id_end = Math.toIntExact(jobs.get(i).getIdEnd());

        }*/

        /*if(progress == 0)
        {
            coordinatesVehicle[0] = -1;
            coordinatesVehicle[1] = -1;
        }else
        {*/

       // }
       /* if(vehicle_start == true)
        {
           //System.out.println("Progress request for delivery: "+id + " progress "+progress);
           // System.out.println("World: "+world.getDimensionY());

            List<CellLink> cellLInks = world.getCellLinks();
            /*for(CellLink cl :  cellLInks)
            {
                System.out.println("CellLInk " + cl.getStartCell());
            }*/
       /*
            coordinatesVehicle = world.getDistancePoints(currentListofJobs,progress);
        }else
        {
            coordinatesVehicle[0] = -1;
            coordinatesVehicle[1] = -1;
        }*/
        return coordinatesVehicle;
    }
}
