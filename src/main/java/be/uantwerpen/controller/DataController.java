package be.uantwerpen.controller;

import be.uantwerpen.model.Job;
import be.uantwerpen.model.JobList;
import be.uantwerpen.services.JobListService;
import be.uantwerpen.visualization.model.DummyPoint;
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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Frédéric Melaerts on 11/05/2017.
 *
 * This controller delivers the physical world and vehicle progress to javascript on the frontend.
 *
 */
@RestController
public class DataController {


    @Value("${core.ip:localhost}") // Localhost is default value
    private String serverCoreIP; // Insert value

    @Value("#{new Integer(${core.port}) ?: 1994}") // 1994 default value with bitwise operations (#)
    private int serverCorePort;

    @Autowired
    private JobListService jobListService;

    //public World world  = new World(200,200);

    List<World> worlds = new ArrayList<World>();
    int vehicleID = 1;

    @Autowired
    private RestTemplate restTemplate;

    public boolean vehicle_start = false;

    @Autowired
    public BackendRestemplate backendRestemplate;


    private World myWorld = null;
    JSONArray virDevices;

    /**
     * Return the world with the current map of the smartcity.
     * @return
     */
    @RequestMapping(value="/retrieveWorld") // Request from http
    public World getWorld(){
        System.out.println("### Retrieve world callled ###");
        if( myWorld == null ) {

            List<DummyPoint> listPoints = getMapDataBackend();
            // Core world or mapCoreQuentinFinal.txt = new world (300,300)
            // Test world or mapCore.txt = new World(250,250);

        /*
            !!! The dimensions of the world must in the right ration with the Mapcanvas from the  html file !!!
            !!! The residual value of there division must be zero !!!
            Example: html : width : 1000 height : 1000 ==> world : 250,250
            Example: html : width : 900 height : 900 ==> world : 900,900
         */
            myWorld = new World(300, 300);
            System.out.println("### CELLIS POINTS SIZE ###" + listPoints.size());
            //myWorld.parseMap(listPoints);
            myWorld.setWorld_ID("world1");
            //this.world.parseMap(listPoints);
            worlds.add(myWorld);
            myWorld.setPoints(listPoints);
        }
        return myWorld;
    }


    /**
     * Retrieve the real name of a point as it is defined in the received map.
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

    /**
     * Get the map data from the backend after it is transformed to the right List of Dummypoints.
     * @return
     */
    @RequestMapping(value="/dataCore")
    public List<DummyPoint> getMapDataBackend(){
        System.out.println("### Retrieve map from backend ###");
        List<DummyPoint> listofPoints = backendRestemplate.getdataBackend();
        return listofPoints;
    }

    /**
     * Return the current vehicle ID.
     * @param id
     */
    @RequestMapping(value="/vehicle/{id}")
    public void changeVehicleID(@PathVariable int id){
        vehicleID = id;
        return;
    }

    /**
     * Return a list with the all vehicle IDs that are present in the smartcity that is received from the core.
     * @return
     */
    @RequestMapping(value="/world1/allVehicles") // Retrieve all vehicles via REST to the backbone
    public List<Integer> getAllVehicles(){
        List<Integer> idVehicles = new ArrayList<Integer>();
        String requestAll = "request all";
        // String URL = "http://localhost:9000/posAll";
        String URL = "http://"+serverCoreIP+":"+serverCorePort+"/bot/getAllVehicles";

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(URL).queryParam("requestAll", requestAll);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
        HttpEntity<?> entity = new HttpEntity<>(headers);
        // Get response from the core
        HttpEntity<String> httpResponse = restTemplate.exchange(
                builder.build().encode().toUri(),
                HttpMethod.GET, // Make post
                entity,
                String.class);
        JSONParser parser = new JSONParser();

        Object obj = null;
        try {
            obj = parser.parse(httpResponse.getBody());

            JSONObject jsonObject = (JSONObject) obj;
            virDevices = (JSONArray) jsonObject.get("vehicles");
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


    @RequestMapping(value="/vehicletype/{id}")
    public String getVehicleType( @PathVariable String id ){
        System.out.println("Asking for vehicletype for " + id);
        Iterator<String> iterator = virDevices.iterator();
        while (iterator.hasNext()) {
            Object obj = iterator.next();
            JSONObject par_jsonObject = (JSONObject) obj;
            int idVeh = ((Long)par_jsonObject.get("idVehicle")).intValue();
            if(idVeh == new Long(id))
                return (String)par_jsonObject.get("type");
        }
        return "unknown";
    }


    /**
     * Return the x and y coordinates of a current used vehicle that is assigned for a specified delivery.
     * Its ID is returned as third value in the returned int array.
     * @param worldid
     * @param delivery_id
     * @param vehicle_id
     * @return
     */
    @RequestMapping(value="/{worldid}/progress/{delivery_id}/{vehicle_id}")
    public int[] getProgress(@PathVariable String worldid, @PathVariable String delivery_id, @PathVariable int vehicle_id){
        int progress = 0;//vehicle.getValue();
        System.out.println("Progress is asked for id delivery "+delivery_id + " vehicle_id "+vehicle_id );
        World world = new World();
        for(int i = 0; i < worlds.size();i++)
        {
            if(worlds.get(i).getWorld_ID().equals(worldid) == true)
            {
                world = worlds.get(i);
                break;
            }
        }
        // TODO ask Oliver service for current job of this delivery with the DeliveryID
        // it returns the value of
        //
        String idVehicle = "request progress";
        String URL = "null";
        UriComponentsBuilder builder;
        boolean jobListNull = false;
        // When delivery_id == null then the getprogress function is asked for the the visualization
        // When delivery is null, then the information of the progress is a request from the visualization.
        if(delivery_id.equals("null") == false)
        {
            //URL = "http://localhost:9000/bot/getOneVehicle/"+vehicleID;
            // Get the ID of the current used vehicle of this delivery.
            for (JobList jl2: jobListService.findAll()) {
                if(jl2.getIdDelivery().equals(delivery_id) == true)
                {
                    vehicleID = Math.toIntExact(jl2.getJobs().get(0).getIdVehicle());
                    jobListNull = true;
                }
            }
            URL = "http://"+serverCoreIP+":"+serverCorePort+"/bot/getOneVehicle/"+vehicleID;
            builder =  UriComponentsBuilder.fromHttpUrl(URL).queryParam("idVehicle", idVehicle);

        } else {
            //URL = "http://localhost:9000/bot/getOneVehicle/"+vehicle_id;
            URL = "http://"+serverCoreIP+":"+serverCorePort+"/bot/getOneVehicle/"+vehicle_id;
            builder =  UriComponentsBuilder.fromHttpUrl(URL).queryParam("idVehicle", vehicle_id);
            System.out.println("ID device "+vehicle_id);
        }

        // coordinatesVehicle[0] = x coordinate vehicle
        // coordinatesVehicle[1] = y coordinate vehicle
        // coordinatesVehicle[2] = id of vehicle
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
            System.out.println("Response backbone : " + httpResponse.toString());
            System.out.println("Response backbone : " + httpResponse.getBody());
            System.out.println("Response body backbone : " + httpResponse.hasBody());
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


                System.out.println("idVeh "+ idVeh + " idStart "+idStart+" idEnd "+idEnd+ " percentage "+percentage);
                if(backendRestemplate == null)System.out.println("BackendRestTemplate is null.");
                int id_start = backendRestemplate.getValueofKeyHashMap( idStart );
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
        return coordinatesVehicle;
    }
}
