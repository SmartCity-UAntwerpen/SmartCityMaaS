package be.uantwerpen.controller;

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
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * This controller delivers the physical world and vehicle progress to javascript on the frontend.
 */
@RestController
public class DataController {

    private static final Logger logger = LogManager.getLogger(DataController.class);

    @Value("${core.ip}") // Localhost is default value
    private String serverCoreIP; // Insert value

    @Value("#{new Integer(${core.port}) ?: 1994}") // 1994 default value with bitwise operations (#)
    private int serverCorePort;

    private List<World> worlds = new ArrayList<>();
    private int vehicleID;

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
     * Get the map data from the backend after it is transformed to a List of DummyPoints.
     *
     * @return List of DummyPoints
     */
    private List<DummyPoint> getMapDataBackend() {
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
     * return A list of vehicle IDs
     */
    /*@RequestMapping(value = "/world1/allVehicles") // Retrieve all vehicles via REST to the backbone
    public HashMap<Integer, String> getAllVehicles() {
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
        HttpEntity<String> httpResponse = restTemplate.exchange(
                builder.build().encode().toUri(),
                HttpMethod.GET, // Make post
                entity,
                String.class);
        JSONParser parser = new JSONParser();

        try {
            ////// TEST
            //Object obj = parser.parse(new FileReader("testdata/getAllVehicles.txt"));
            //////
            Object obj = parser.parse(httpResponse.getBody());
            //////
            JSONObject jsonObject = (JSONObject) obj;
            virDevices = (JSONArray) jsonObject.get("vehicles");
            Iterator<String> iterator = virDevices.iterator();
            while (iterator.hasNext()) {
                obj = iterator.next();
                JSONObject par_jsonObject = (JSONObject) obj;
                int idVeh = ((Long) par_jsonObject.get("idVehicle")).intValue();
                String typeVeh = par_jsonObject.get("type").toString();
                idVehicles.put(idVeh, typeVeh);
            }
        } catch (ParseException e) { // | IOException
            e.printStackTrace();
        }
        return idVehicles;
    }*/


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
     * param worldId The ID of the world the vehicle is located
     * param jobId   The ID of the job
     * param jobId   The ID of the map
     * param startId The ID of the startpoint
     * param endId   The ID of the endpoint
     * param type    The type of the vehicle
     * return Returns a JSON response [x, y, percentage, status]
     */
    /*@RequestMapping(value = "/{worldId}/progress/{jobId}/{mapId}/{startId}/{endId}/{type}")
    public JSONObject getProgress(@PathVariable String worldId, @PathVariable int jobId, @PathVariable int mapId, @PathVariable int startId, @PathVariable int endId, @PathVariable String type) {
        World world = new World();
        for (World world1 : worlds) {
            if (world1.getWorld_ID().equals(worldId)) {
                world = world1;
                break;
            }
        }

        JSONObject response = new JSONObject();

        JSONObject jsonObject = new JSONObject();
        String URL = "http://" + serverCoreIP + ":" + serverCorePort + "/job/service/getJobProgress/" + jobId;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<?> entity = new HttpEntity<>(headers);
        HttpEntity<String> httpResponse = restTemplate.exchange(
                URL,
                HttpMethod.GET,
                entity,
                String.class);
        String httpBody = httpResponse.getBody();
        JSONParser parser = new JSONParser();
        try {
            //Object obj = parser.parse(new FileReader("testdata/getJobProgress"+jobId+".txt"));
            Object obj = parser.parse(httpBody);
            jsonObject = (JSONObject) obj;
            String status = (String) jsonObject.get("status");
            int progress = ((Long) jsonObject.get("progress")).intValue();


            int x = 0;
            int y = 0;
            if (status.equals("BUSY")) {
                int[] coordinatesVehicle_temp = world.getDistance(mapId, startId, endId, (double) (progress) / 100.0, type);
                x = coordinatesVehicle_temp[0];
                y = coordinatesVehicle_temp[1];
            }
            response.put("x", x);
            response.put("y", y);
            response.put("progress", progress);
            response.put("status", status);
        } catch (ParseException e) { // | IOException
            logger.error("ParseException", e);
        }

        return response;
    }*/

    @RequestMapping(value = "/{worldId}/delivery/{delivery_id}")
    public JSONObject getDelivery(@PathVariable String worldId, @PathVariable String delivery_id) {
        logger.info("getting delivery: " + delivery_id);
        JSONObject jsonObject = new JSONObject();
        String URL = "http://" + serverCoreIP + ":" + serverCorePort + "/jobs/findOneByDelivery/" + delivery_id;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<?> entity = new HttpEntity<>(headers);
        HttpEntity<String> httpResponse = restTemplate.exchange(
                URL,
                HttpMethod.GET,
                entity,
                String.class);
        String delivery = httpResponse.getBody();
        JSONParser parser = new JSONParser();
        try {
            //Object obj = parser.parse(new FileReader("testdata/getDelivery100.txt"));
            Object obj = parser.parse(delivery);
            jsonObject = (JSONObject) obj;
        } catch (ParseException e) { //IOException
            logger.error("ParseException", e);
        }
        return jsonObject;
    }

    @RequestMapping(value = "/getTrafficLightStats")
    @ResponseBody
    public String getTrafficLightStats() throws IOException {
        String path = "http://" + serverCoreIP + ":" + serverCorePort + "/map/getTrafficLightStats";
        return readStringFromURL(path);
    }

    private String readStringFromURL(String requestURL) throws IOException {
        try (Scanner scanner = new Scanner(new URL(requestURL).openStream(),
                StandardCharsets.UTF_8.toString())) {
            scanner.useDelimiter("\\A");
            return scanner.hasNext() ? scanner.next() : "";
        }
    }

}
