package be.uantwerpen.controller;

import be.uantwerpen.visualization.model.DummyPoint;
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
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

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

    private HashMap<Integer, Integer> pointTransition;


    /**
     * Retrieves the map information from the backend and stores it in DummyPoints that are used in the world-map.
     * The received point ID are the keys in a HashMap with the associated key's value equal to map point ID.
     *
     * @return A list of dummy points
     */
    @SuppressWarnings("unchecked")
    public List<DummyPoint> getDataBackend() {
        logger.info("Retrieve info from core.");
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl("http://" + serverCoreIP + ":" + serverCorePort + "/map/stringmapjson/visual");
        logger.info("Builder from: " + builder.build().encode().toUri());
        HttpEntity<?> entity = new HttpEntity<>(headers);

        // Get response from the core
        // COMMENT FOR LOCAL TEST
        /*HttpEntity<String> httpResponse = restTemplate.exchange(
                builder.build().encode().toUri(),
                HttpMethod.GET,
                entity,
                String.class);
        logger.info("Response core: " + httpResponse.getBody());
        logger.info("Response body core: " + httpResponse.hasBody());
        String listOfCore = httpResponse.getBody();*/

        new JSONObject();

        pointTransition = new HashMap<>();
        JSONParser parser = new JSONParser();

        Object obj;
        JSONObject point_jsonObject;
        List<DummyPoint> points = new ArrayList<>();

        try {
            ////// TEST - 2018
            obj = parser.parse(new FileReader("testdata/stringmapjson.txt"));
            /////
            // obj = parser.parse(listOfCore);
            /////
            JSONObject jsonObject = (JSONObject) obj;
            JSONArray pointsList = (JSONArray) jsonObject.get("pointList");

            Iterator<JSONObject> iterator = pointsList.iterator();
            int index_counter = 0;
            while (iterator.hasNext()) {
                DummyPoint point = new DummyPoint();

                point_jsonObject = iterator.next();
                int point_ID = ((Long) point_jsonObject.get("id")).intValue();
                if (!pointTransition.containsKey(point_ID)) {
                    pointTransition.put(point_ID, index_counter);
                    point_ID = index_counter;
                    index_counter++;
                } else {
                    point_ID = pointTransition.get(point_ID);
                }
                int x = ((Long) point_jsonObject.get("x")).intValue();
                int y = ((Long) point_jsonObject.get("y")).intValue();

                String type = (String) point_jsonObject.get("access");
                String characteristic;
                characteristic = (String) point_jsonObject.get("type");
                if (characteristic == null) {
                    characteristic = "inbetween";
                }
                JSONArray neighbours = (JSONArray) point_jsonObject.get("neighbours");
                point.setPointName(point_ID);
                point.setPhysicalPoisionX(x);
                point.setPhysicalPoisionY(y);
                point.setType(type);
                point.setPointCharacteristic(characteristic);
                for (JSONObject neighbourJSON : (Iterable<JSONObject>) neighbours) {
                    int neighbour = ((Long) neighbourJSON.get("neighbour")).intValue();
                    point.addNeighbour(neighbour);
                }
                points.add(point);
            }
        } catch (ParseException | IOException e) { //IOException
            e.printStackTrace();
        }

        // Transform the id of the neighbours to the right one of the HashMap
        for (int p = 0; p < points.size(); p++) {
            DummyPoint point = points.get(p);
            List<Integer> neighbours = point.getNeighbours();
            for (int i = 0; i < neighbours.size(); i++) {
                if (pointTransition.containsKey(neighbours.get(i))) {
                    int temp_neigh = pointTransition.get(neighbours.get(i));
                    neighbours.set(i, temp_neigh);
                } else {
                    // Retrieve this world id.
                    logger.warn("Neighbour not found in point hashmap");
                }
            }
            point.setNeighbours(neighbours);
            points.set(p, point);
        }

        return points;
    }

    /**
     * Retrieve the original value from a DummyPoint in the world.
     * It is equal to the key of the imposed value.
     *
     * @param value The DummyPoint value
     * @return The original key, or -1 if not found
     */
    public Integer getKeyHashMap(Integer value) {
        Optional<Integer> optionalEntry = pointTransition.entrySet().stream()
                .filter((entry) -> entry.getValue().equals(value)).map(Map.Entry::getKey).findFirst();
        return optionalEntry.orElse(-1);
    }

    /**
     * Get the value or map point ID from a key or received point ID.
     *
     * @param key Point ID
     * @return DummyPoint value ID
     */
    public Integer getValueOfKeyHashMap(Integer key) {
        return pointTransition.get(key);
    }

}
