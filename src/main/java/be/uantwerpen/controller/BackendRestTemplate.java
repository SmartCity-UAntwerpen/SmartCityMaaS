package be.uantwerpen.controller;

import be.uantwerpen.visualization.model.DummyPoint;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * Created by Frédéric Melaerts on 30/05/2017.
 */
@Component
public class BackendRestTemplate {

    private static final Logger logger = LogManager.getLogger(BackendRestTemplate.class);

    @Value("${core.ip:localhost}")
    private String serverCoreIP;

    @Value("#{new Integer(${core.port}) ?: 1994}")
    private int serverCorePort;

    @Autowired
    private RestTemplate restTemplate;

    private HashMap<Integer,Integer> pointTransition;


    /**
     * Retrieves the map information from the backend and stores it in DummyPoints that are used in the world-map.
     * The received point ID are the keys in a HashMap with the associated key's value equal to map point ID.
     * @return
     */
    public List<DummyPoint> getdataBackend() {
        logger.info("Retrieve info from core ");
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);

        List<String> pointList = new ArrayList<String>();

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl("http://"+serverCoreIP+":"+serverCorePort+"/map/stringmapjson/visual");
        System.out.println("Make builder to Quentin " +builder.build().encode().toUri());

        HttpEntity<?> entity = new HttpEntity<>(headers);
        System.out.println("Entity to Quentin" );

        // Get response from the core
        /*HttpEntity<String> httpResponse = restTemplate.exchange(
                builder.build().encode().toUri(),
                HttpMethod.GET,
                entity,
                String.class);

        System.out.println("Performed exchange to Quentin" );
        System.out.println("Response core : "+httpResponse.getBody());
        System.out.println("Response body core : "+ httpResponse.hasBody());
        String listOfCore = httpResponse.getBody();
*/
        new JSONObject();

        pointTransition = new HashMap<Integer,Integer>();
        JSONParser parser = new JSONParser();

        Object obj = null;
        Object pointObject = null;
        Object neighbourObject = null;
        List<DummyPoint> points = new ArrayList<DummyPoint>();

        /*
        Code for test purposes:
        This code in comments is used to read JSON data from a txt-file instead of the HTTP-response.
        BufferedReader br = new BufferedReader(new FileReader("mapCoreQuentinFinal.txt"));
        String line;
        while ((line = br.readLine()) != null)
            obj = parser.parse(line);
        */
        /*
            Parse the received string of JSON objects and transform it to a list of points that can be used for
            the map of the world.
         */

        try {
            ////// TEST - 2018
            JSONObject jsonObject = (JSONObject) parser.parse(new FileReader("mapCoreQuentinFinal.txt"));
            /////
            //obj = parser.parse(listOfCore);
            //JSONObject jsonObject = (JSONObject) obj;
            /////
            JSONArray pointsList = (JSONArray) jsonObject.get("pointList");
            System.out.println("Whole list of points:  "+pointsList.toString());

            int counter =0;
            Iterator<String> iterator = pointsList.iterator();
            int index_counter  = 0;
            while (iterator.hasNext()) {
                DummyPoint point = new DummyPoint();

                pointObject = iterator.next();
                JSONObject point_jsonObject = (JSONObject) pointObject;
                int point_ID = ((Long)point_jsonObject.get("id")).intValue();
                if(pointTransition.containsKey(point_ID) == false) {
                    pointTransition.put(point_ID,index_counter);
                    point_ID = pointTransition.get(point_ID);
                    index_counter++;
                } else {
                    point_ID = pointTransition.get(point_ID);
                }
                int x = ((Long)point_jsonObject.get("x")).intValue();
                int y = ((Long)point_jsonObject.get("y")).intValue();

                String type = (String) point_jsonObject.get("access");
                String characteristic = "unknown";
                characteristic = (String) point_jsonObject.get("type");
                if(characteristic == null) {
                    characteristic = "inbetween";
                }
                JSONArray neighbours = (JSONArray) point_jsonObject.get("neighbours");
                point.setPointName(point_ID);
                point.setPhysicalPoisionX(x);
                point.setPhysicalPoisionY(y);
                point.setType(type);
                point.setPointCharacteristic(characteristic);
                Iterator<String> iter = neighbours.iterator();
                while (iter.hasNext()) {
                    neighbourObject = iter.next();
                    JSONObject neigbourJSON = (JSONObject) neighbourObject;
                    int neighbour = ((Long)neigbourJSON.get("neighbour")).intValue();
                    point.addNeighbour(neighbour);
                }
                points.add(point);
                counter++;
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        // Transform the id of the neigbours to the right one of the Hashmap
        for(int p =0 ; p < points.size(); p++) {
            DummyPoint point = points.get(p);
            List<Integer> neighbours = point.getNeighbours();
            for(int i =0 ; i < neighbours.size(); i++) {
                if(pointTransition.containsKey(neighbours.get(i)) == true) {
                    int temp_neig = pointTransition.get(neighbours.get(i));
                    neighbours.set(i,temp_neig);
                } else {
                    // Retrieve this world id.
                    System.out.println("Neighbour not found in point hashmap");
                }
            }
            point.setNeighbours(neighbours);
            points.set(p,point);
        }

        Iterator it = pointTransition.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
        }
        System.out.println("Print points");
        for(int i = 0 ; i <points.size(); i++) {
            points.get(i).print();
        }
        return points; //new MessageWrapper<>(tracksamples, "server called using eureka with rest template");
    }

    /**
     * Retrieve the orginal value from a DummyPoint in the world.
     * It is equal to the key of the imposed value.
     * @param value
     * @return
     */
    public Integer getKeyHashMap(Integer value) {
        for (Integer pointKey : pointTransition.keySet()) {
            if (pointTransition.get(pointKey).equals(value) == true) {
                return pointKey;
            }
        }
        return -1;
    }

    /**
     * Get the value or map point ID from a key or received point ID.
     * @param key
     * @return
     */
    public Integer getValueofKeyHashMap(Integer key) {
        return pointTransition.get(key);
    }
}
