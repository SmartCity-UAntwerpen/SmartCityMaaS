package be.uantwerpen.controller;

import be.uantwerpen.sc.models.Job;
import be.uantwerpen.sc.models.JobList;
import be.uantwerpen.services.JobListService;
import be.uantwerpen.visualization.model.DummyPoint;
import be.uantwerpen.visualization.model.World;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * This controller delivers the physical world and vehicle progress to javascript on the frontend.
 */
@RestController
public class DataController {

    private static final Logger logger = LogManager.getLogger(DataController.class);

    @Value("${core.ip:localhost}") // Localhost is default value
    private String serverCoreIP; // Insert value

    @Value("#{new Integer(${core.port}) ?: 1994}") // 1994 default value with bitwise operations (#)
    private int serverCorePort;

    @Autowired
    private JobListService jobListService;

    List<World> worlds = new ArrayList<>();
    int vehicleID = 1;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    public BackendRestTemplate backendRestTemplate;

    private World myWorld = null;
    JSONArray virDevices;

    /**
     * Return the world with the current map of the SmartCity.
     *
     * @return The world
     */
    @RequestMapping(value = "/retrieveWorld") // Request from http
    public World getWorld() {
        logger.info("/retrieveWorld requested");
        if (myWorld == null) {
            List<DummyPoint> listPoints = getMapDataBackend();

        /*
            !!! The dimensions of the world must in the right ration with the Mapcanvas from the  html file !!!
            !!! The residual value of there division must be zero !!!
            Example: html : width : 1000 height : 1000 ==> world : 250,250
            Example: html : width : 900 height : 900 ==> world : 900,900
         */
            myWorld = new World(300, 300);
            logger.info("World's listPoints size is " + listPoints.size());
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
        int keyPoint = backendRestTemplate.getKeyHashMap(valuePoint);
        if(valuePoint != -1) {
            logger.info("Retrieve key point name from "+valuePoint+ " which is "+ keyPoint);
        }
        else{
            logger.error("Could not retrieve key point name from "+valuePoint+ ", error at "+ keyPoint);
        }
        return keyPoint;
    }

    /**
     * Get the map data from the backend after it is transformed to a List of DummyPoints.
     *
     * @return List of DummyPoints
     */
    public List<DummyPoint> getMapDataBackend() {
        logger.info("/dataCore requested, retrieve map from backend");
        return backendRestTemplate.getDataBackend();
    }

    /**
     * Return the current vehicle ID.
     *
     * @param id The ID of the vehicle
     */
    @RequestMapping(value = "/vehicle/{id}")
    public void changeVehicleID(@PathVariable int id) {
        logger.info("/vehicle/" + id + " requested");
        vehicleID = id;
    }

    /**
     * Return a list with the all vehicle IDs that are present in the smartcity that is received from the core.
     *
     * @return A list of vehicle IDs
     */
    @RequestMapping(value="/world1/allVehicles") // Retrieve all vehicles via REST to the backbone
    public HashMap<Integer, String> getAllVehicles(){
        HashMap<Integer, String> idVehicles = new HashMap<Integer, String>();
        String requestAll = "request all";
        // String URL = "http://localhost:9000/posAll";
        String URL = "http://" + serverCoreIP + ":" + serverCorePort + "/bot/getAllVehicles";

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(URL).queryParam("requestAll", requestAll);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
        HttpEntity<?> entity = new HttpEntity<>(headers);
        // Get response from the core
        // COMMENT FOR LOCAL TEST
//        HttpEntity<String> httpResponse = restTemplate.exchange(
//                builder.build().encode().toUri(),
//                HttpMethod.GET, // Make post
//                entity,
//                String.class);
        JSONParser parser = new JSONParser();

        try {
            ////// TEST
            Object obj = parser.parse(new FileReader("test/getAllVehicles.txt"));
            //////
//            Object obj = parser.parse(httpResponse.getBody());
            //////
            JSONObject jsonObject = (JSONObject) obj;
            virDevices = (JSONArray) jsonObject.get("vehicles");
            Iterator<String> iterator = virDevices.iterator();
            while (iterator.hasNext()) {
                obj = iterator.next();
                JSONObject par_jsonObject = (JSONObject) obj;
                int idVeh = ((Long)par_jsonObject.get("idVehicle")).intValue();
                String typeVeh = par_jsonObject.get("type").toString();
                idVehicles.put(idVeh, typeVeh);
            }
        } catch (ParseException | IOException e) {
            e.printStackTrace();
        }
        return idVehicles;
    }


    @RequestMapping(value = "/vehicletype/{id}")
    @SuppressWarnings("unchecked")
    public List<String> getVehicleType(@PathVariable String id) {
        logger.info("/vehicletype/" + id + " requested, to get vehicle type");
        List<String> result = new ArrayList<>();
        for (JSONObject par_jsonObject : (Iterable<JSONObject>) virDevices) {
            int idVeh = ((Long) par_jsonObject.get("idVehicle")).intValue();
            if (idVeh == new Long(id)) {
                result.add((String) par_jsonObject.get("type"));
                logger.info("Vehicle type is found, " + result);
                return result;
            }
        }
        logger.warn("Vehicle type is NOT found for " + id);
        result.add("unknown");
        return result;
    }


    /**
     * Return the x and y coordinates of a current used vehicle that is assigned for a specified delivery.
     * Its ID is returned as third value in the returned int array.
     *
     * @param worldId     The ID of the world the vehicle is located
     * @param delivery_id The ID of the delivery
     * @param vehicle_id  The ID of the vehicle
     * @return Returns an integer array [x, y, vehicle_id, percentage]
     */
    @RequestMapping(value = "/{worldId}/progress/{delivery_id}/{vehicle_id}")
    public int[] getProgress(@PathVariable String worldId, @PathVariable String delivery_id, @PathVariable int vehicle_id) {
        int progress = 0;//vehicle.getValue();
        //logger.info("/" + worldId + "/progress/" + delivery_id + "/" + vehicle_id + " requested, to get the progress.");
        World world = new World();
        for (World world1 : worlds) {
            if (world1.getWorld_ID().equals(worldId)) {
                world = world1;
                break;
            }
        }
        // it returns the value of
        //
        String idVehicle = "request progress";
        String URL;
        UriComponentsBuilder builder;
        boolean jobListNull = false;
        // When delivery_id == null then the getprogress function is asked for the the visualization
        // When delivery is null, then the information of the progress is a request from the visualization.
        if (!delivery_id.equals("null")) {
            // Get the ID of the current used vehicle of this delivery.
            for (JobList jl2 : jobListService.findAll()) {
                if (jl2.getIdDelivery().equals(delivery_id)) {
                    vehicleID = Math.toIntExact(jl2.getJobs().get(0).getIdVehicle());
                    jobListNull = true;
                }
            }
            URL = "http://" + serverCoreIP + ":" + serverCorePort + "/bot/getOneVehicle/" + vehicleID;
            builder = UriComponentsBuilder.fromHttpUrl(URL).queryParam("idVehicle", idVehicle);

        } else {
            URL = "http://" + serverCoreIP + ":" + serverCorePort + "/bot/getOneVehicle/" + vehicle_id;
            builder = UriComponentsBuilder.fromHttpUrl(URL).queryParam("idVehicle", vehicle_id);
        }

        int[] coordinatesVehicle = new int[4]; // {x, y, id, percentage}


        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
        HttpEntity<?> entity = new HttpEntity<>(headers);

        if (delivery_id.equals("null") || jobListNull) {
            // Get response from the core
            // COMMENT FOR LOCAL TEST
//            HttpEntity<String> httpResponse = restTemplate.exchange(
//                    builder.build().encode().toUri(),
//                    HttpMethod.GET,
//                    entity,
//                    String.class);
//            logger.info("[getProgress] Response backbone : " + httpResponse.toString());
//            logger.info("[getProgress] Response backbone : " + httpResponse.getBody());
//            logger.info("[getProgress] Response body backbone : " + httpResponse.hasBody());
//            String vehicleInfo = httpResponse.getBody();
            JSONParser parser = new JSONParser();
            Job job1 = new Job();
            job1.setIdStart(1);
            job1.setIdEnd(2);
            job1.setTypeVehicle("car");
            job1.setIdVehicle(1);
            List<Integer> currentListofJobs = new ArrayList<>();
        String type = null;

            try {
                //////// TEST
                JSONObject jsonObject = (JSONObject) parser.parse(new FileReader("test/getOneVehicle" + vehicle_id + ".txt"));
                ////
//                Object obj = parser.parse(vehicleInfo);
//                JSONObject jsonObject = (JSONObject) obj;
                //////////

                int idVeh = ((Long) jsonObject.get("idVehicle")).intValue();
                int idStart = ((Long) jsonObject.get("idStart")).intValue();
                int idEnd = ((Long) jsonObject.get("idEnd")).intValue();
                int percentage = ((Long) jsonObject.get("percentage")).intValue();
                type = jsonObject.get("type").toString();

                //logger.info("idVeh " + idVeh + ", idStart " + idStart + ", idEnd " + idEnd + ", percentage " + percentage);
                if (backendRestTemplate == null) logger.error("BackendRestTemplate is null.");
                int id_start = backendRestTemplate.getValueOfKeyHashMap(idStart + 1);
                int id_end = backendRestTemplate.getValueOfKeyHashMap(idEnd + 1);
                currentListofJobs.add(id_start);
                currentListofJobs.add(id_end);
                progress = percentage;

            } catch (ParseException | IOException e) {
                logger.error("ParseException", e);
            }
            int[] coordinatesVehicle_temp = world.getDistancePoints(currentListofJobs, progress, type);
            coordinatesVehicle[0] = coordinatesVehicle_temp[0];
            coordinatesVehicle[1] = coordinatesVehicle_temp[1];
            coordinatesVehicle[2] = vehicle_id;
            coordinatesVehicle[3] = progress;
        } else {
            coordinatesVehicle[0] = -1;
            coordinatesVehicle[1] = -1;
            coordinatesVehicle[2] = vehicleID;
            coordinatesVehicle[3] = -1;
        }
        return coordinatesVehicle;
    }

}
