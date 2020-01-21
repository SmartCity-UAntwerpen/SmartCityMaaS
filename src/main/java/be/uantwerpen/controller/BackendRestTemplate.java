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

    @Value("${core.ip}")
    private String serverCoreIP;

    @Value("#{new Integer(${core.port}) ?: 1994}")
    private int serverCorePort;

    @Autowired
    private RestTemplate restTemplate;


    /**
     * Retrieves the map information from the backend and stores it in DummyPoints that are used in the world-map.
     * The received point ID are the keys in a HashMap with the associated key's value equal to map point ID.
     *
     * @return A list of dummy points
     */
    @SuppressWarnings("unchecked")
    public JSONObject getDataBackend() {
        logger.info("Retrieve info from core.");
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl("http://" + serverCoreIP + ":" + serverCorePort + "/map/getmap");
        logger.info("Builder from: " + builder.build().encode().toUri());
        HttpEntity<?> entity = new HttpEntity<>(headers);

        // Get response from the core
        // COMMENT FOR LOCAL TEST
        HttpEntity<String> httpResponse = restTemplate.exchange(
                builder.build().encode().toUri(),
                HttpMethod.GET,
                entity,
                String.class);
        logger.info("Response core: " + httpResponse.getBody());
        logger.info("Response body core: " + httpResponse.hasBody());
        String listofCore;
        String listOfCore = httpResponse.getBody();

        JSONParser parser = new JSONParser();

        Object obj;
        JSONObject point_jsonObject;
        JSONObject mapListObject;
        List<DummyPoint> points = new ArrayList<>();

        try {
            ////// TEST - 2018
            //obj = parser.parse(new FileReader("testdata/map_volledig.json"));
            /////
            obj = parser.parse(listOfCore);
            /////
            JSONObject jsonObject = (JSONObject) obj;
            return jsonObject;

            /*

            Iterator<JSONObject> mapIterator = maplist.iterator();
            while (mapIterator.hasNext()) {
                mapListObject = mapIterator.next();
                JSONArray pointsList = (JSONArray) mapListObject.get("pointList");

                Iterator<JSONObject> iterator = pointsList.iterator();

                while (iterator.hasNext()) {
                    DummyPoint point = new DummyPoint();

                    point_jsonObject = iterator.next();
                    int point_ID = ((Long) point_jsonObject.get("id")).intValue();
                    int x = ((Long) point_jsonObject.get("x")).intValue() + ((Long) mapListObject.get("offsetX")).intValue(); // EVERY MAP X & Y OFFSET ADDED TO COORDINATES
                    int y = ((Long) point_jsonObject.get("y")).intValue() + ((Long) mapListObject.get("offsetY")).intValue();

                    String type = (String) mapListObject.get("access");
                    String characteristic;
                    characteristic = (String) point_jsonObject.get("type");
                    if (characteristic == null) {
                        characteristic = "inbetween";
                    }
                    int mapId = ((Long) mapListObject.get("mapId")).intValue();
                    JSONArray neighbours = (JSONArray) point_jsonObject.get("neighbours");
                    point.setPointName(point_ID);
                    point.setPhysicalPoisionX(x);
                    point.setPhysicalPoisionY(y);
                    point.setType(type);
                    point.setMapId(mapId);
                    point.setPointCharacteristic(characteristic);
                    for (JSONObject neighbourJSON : (Iterable<JSONObject>) neighbours) {
                        int neighbour = ((Long) neighbourJSON.get("neighbour")).intValue();
                        point.addNeighbour(neighbour);
                    }
                    points.add(point);
                }
            }*/
        } catch (ParseException e) { //IOException
            e.printStackTrace();
        }

        return null;
    }


    public boolean placeOrder() {

        return true;
    }
}
