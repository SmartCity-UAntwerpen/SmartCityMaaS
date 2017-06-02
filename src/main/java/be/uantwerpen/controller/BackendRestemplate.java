package be.uantwerpen.controller;

import be.uantwerpen.visualization.model.DummyPoint;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Frédéric Melaerts on 30/05/2017.
 */
@Component
public class BackendRestemplate {

    @Autowired
    private RestTemplate restTemplate;

    public List<DummyPoint> getdataBackend() {
        System.out.println("Retrieve info from core " );
       /* HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);

        List<String> pointList = new ArrayList<String>();
        // Set destination of the core for retrieving information.

        // Quentin

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl("http://146.175.140.44:1994/map/stringmapjson/top")
                .queryParam("pointList", pointList);

        HttpEntity<?> entity = new HttpEntity<>(headers);

        // Get response from the core
        HttpEntity<String> httpResponse = restTemplate.exchange(
                builder.build().encode().toUri(),
                HttpMethod.GET,
                entity,
                String.class);

        System.out.println("Response core : "+httpResponse.toString());
        System.out.println("Response body core : "+ httpResponse.hasBody());


        new JSONObject();
        //JSONObject obj = new JSONObject();
       // JSONArray geodata = obj.getJSONArray("geodata");
        /*Object obj = null;
        org.json.simple.parser.JSONParser parser = new org.json.simple.parser.JSONParser();
        List<DummyPoint> points = new ArrayList<DummyPoint>();

        try {
            obj = parser.parse(new FileReader("mapCore.txt"));
            JSONObject jsonObject = (JSONObject) obj;
            JSONArray pointsList = (JSONArray) jsonObject.get("pointList");
            Object entryList = null;
            int counter = 0;
            Iterator<String> iterator = pointsList.iterator();


            while (iterator.hasNext()) {
                entryList = iterator.next();
                JSONObject par_jsonObject = (JSONObject) entryList;


                int point_ID = ((Long)jsonObject.get("id")).intValue();

                System.out.println(" index "+counter + " value of point_ID "+point_ID);
                int x = ((Long)jsonObject.get("x")).intValue();
                int y = ((Long)jsonObject.get("y")).intValue();
                DummyPoint point = new DummyPoint(point_ID, x,y);
                points.add(point);
                counter++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        for(int i = 0; i<points.size();i++)
            points.get(i).print();











        */
        /*
        for(int i = 0; i<propertiesList.size();i++)
            System.out.println(" "+propertiesList.get(i));
        /*

        VAN RESPONS QUENTIN:
        !!!!!
        try {
            obj = parser.parse(httpResponse.toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        */

/*
        int n = geodata.length();
        for (int i = 0; i < n; ++i) {
            final JSONObject person = geodata.getJSONObject(i);
            System.out.println(person.getInt("id"));
            System.out.println(person.getString("name"));
            System.out.println(person.getString("gender"));
            System.out.println(person.getDouble("latitude"));
            System.out.println(person.getDouble("longitude"));
        }

*/







        JSONParser parser = new JSONParser();

        Object obj = null;
        Object pointObject = null;
        Object neighbourObject = null;
        List<DummyPoint> points = new ArrayList<DummyPoint>();

        try {
            BufferedReader br = new BufferedReader(new FileReader("mapCore.txt"));
            String line;
            while ((line = br.readLine()) != null) {
                obj = parser.parse(line);
                JSONObject jsonObject = (JSONObject) obj;
                JSONArray pointsList = (JSONArray) jsonObject.get("pointList");
                System.out.println("Whole list:  "+pointsList.toString());

                int counter =0;
                Iterator<String> iterator = pointsList.iterator();
                while (iterator.hasNext()) {
                    DummyPoint point = new DummyPoint();

                    pointObject = iterator.next();
                    JSONObject point_jsonObject = (JSONObject) pointObject;
                    int point_ID = ((Long)point_jsonObject.get("id")).intValue();
                    System.out.println(" index "+counter + " value of point_ID "+point_ID);
                    int x = ((Long)point_jsonObject.get("x")).intValue();
                    int y = ((Long)point_jsonObject.get("y")).intValue();
                    JSONArray neighbours = (JSONArray) point_jsonObject.get("neighbours");
                    System.out.println("neighbourS " + neighbours.toString());
                    point.setPointName(point_ID);
                    point.setPhysicalPoisionX(x);
                    point.setPhysicalPoisionY(y);

                    Iterator<String> iter = neighbours.iterator();
                    while (iter.hasNext())
                    {
                        neighbourObject = iter.next();
                        JSONObject neigbourJSON = (JSONObject) neighbourObject;
                        int neighbour = ((Long)neigbourJSON.get("neighbour")).intValue();
                        System.out.println("neighbour " + neighbour);
                        point.addNeighbour(neighbour);
                    }
                    points.add(point);
                    counter++;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }


        System.out.println("Print points");
        for(int i = 0 ; i <points.size(); i++)
        {
            points.get(i).print();

        }

        return points; //new MessageWrapper<>(tracksamples, "server called using eureka with rest template");
    }

}
