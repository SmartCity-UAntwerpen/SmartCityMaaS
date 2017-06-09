package be.uantwerpen.controller;

import be.uantwerpen.visualization.model.DummyPoint;
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

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.*;

/**
 * Created by Frédéric Melaerts on 30/05/2017.
 */
@Component
public class BackendRestemplate {

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
        System.out.println("Retrieve info from core " );
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);

        List<String> pointList = new ArrayList<String>();

       UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl("http://"+serverCoreIP+":"+serverCorePort+"/map/stringmapjson/visual");
        System.out.println("Make builder to Quentin " +builder.build().encode().toUri());

        HttpEntity<?> entity = new HttpEntity<>(headers);
        System.out.println("Entity to Quentin" );

        // Get response from the core
        HttpEntity<String> httpResponse = restTemplate.exchange(
                builder.build().encode().toUri(),
                HttpMethod.GET,
                entity,
                String.class);

        System.out.println("Performed exchange to Quentin" );
       // System.out.println("Response core : "+httpResponse.toString());
        System.out.println("Response core : "+httpResponse.getBody());
        System.out.println("Response body core : "+ httpResponse.hasBody());
        String listOfCore = httpResponse.getBody();

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

        pointTransition = new HashMap<Integer,Integer>();
        JSONParser parser = new JSONParser();

        Object obj = null;
        Object pointObject = null;
        Object neighbourObject = null;
        List<DummyPoint> points = new ArrayList<DummyPoint>();

        /*
            Parse the received string of JSON objects and transform it to a list of points that can be used for
            the map of the world.
         */
        try {


            BufferedReader br = new BufferedReader(new FileReader("mapCoreQuentinFinal.txt"));
            //BufferedReader br = new BufferedReader(new FileReader("mapCore.txt"));
            String line;
            while ((line = br.readLine()) != null) {
                obj = parser.parse(listOfCore);
               // obj = parser.parse(line);
                JSONObject jsonObject = (JSONObject) obj;
                JSONArray pointsList = (JSONArray) jsonObject.get("pointList");
                System.out.println("Whole list:  "+pointsList.toString());

                int counter =0;
                Iterator<String> iterator = pointsList.iterator();
                int index_counter  = 0;
                while (iterator.hasNext()) {
                    DummyPoint point = new DummyPoint();

                    pointObject = iterator.next();
                    JSONObject point_jsonObject = (JSONObject) pointObject;
                    int point_ID = ((Long)point_jsonObject.get("id")).intValue();
                    if(pointTransition.containsKey(point_ID) == false)
                    {
                        // System.out.println("Point ID is null for "+point_ID+ " index counter "+index_counter);
                        pointTransition.put(point_ID,index_counter);
                        point_ID = pointTransition.get(point_ID);
                        index_counter++;
                    }else
                    {
                        // System.out.println("Point ID is NOT null for "+point_ID+ " index counter "+index_counter);
                        point_ID = pointTransition.get(point_ID);
                    }
                    // System.out.println(" index "+counter + " value of point_ID "+point_ID);
                    int x = ((Long)point_jsonObject.get("x")).intValue();
                    int y = ((Long)point_jsonObject.get("y")).intValue();

                    String type = (String) point_jsonObject.get("access");
                    String characteristic = "unknown";
                    characteristic = (String) point_jsonObject.get("type");
                    if(characteristic == null)
                    {
                        characteristic = "inbetween";
                    }
                    JSONArray neighbours = (JSONArray) point_jsonObject.get("neighbours");
                    //System.out.println("neighbourS " + neighbours.toString());
                    point.setPointName(point_ID);
                    point.setPhysicalPoisionX(x);
                    point.setPhysicalPoisionY(y);
                    point.setType(type);
                    point.setPointCharacteristic(characteristic);
                    Iterator<String> iter = neighbours.iterator();
                    while (iter.hasNext())
                    {
                        neighbourObject = iter.next();
                        JSONObject neigbourJSON = (JSONObject) neighbourObject;
                        int neighbour = ((Long)neigbourJSON.get("neighbour")).intValue();
                       // System.out.println("neighbour " + neighbour);
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


        // Transform the id of the neigbours to the right one of the Hashmap
        for(int p =0 ; p < points.size(); p++)
        {

            DummyPoint point = points.get(p);
            List<Integer> neighbours = point.getNeighbours();

            for(int i =0 ; i < neighbours.size(); i++)
            {
                if(pointTransition.containsKey(neighbours.get(i)) == true)
                {
                    //System.out.println("Neighbour point ID is null for "+neighbour + " index counter "+index_counter);

                    int temp_neig = pointTransition.get(neighbours.get(i));
                    neighbours.set(i,temp_neig);
                }else
                {
                    // Retrieve tis world id.
                    System.out.println("Neighbour not found in point hashmap");

                }
            }
            point.setNeighbours(neighbours);
            points.set(p,point);
        }


        Iterator it = pointTransition.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            System.out.println(pair.getKey() + " = " + pair.getValue());
            //it.remove(); // avoids a ConcurrentModificationException
        }
        System.out.println("Print points");
        for(int i = 0 ; i <points.size(); i++)
        {
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
    public Integer getKeyHashMap(Integer value)
    {
       // System.out.println("Key value request for "+value+"  fuck "+pointTransition.get(1000));
        for (Integer pointKey : pointTransition.keySet()) {
          //  System.out.println("pointTransition.get(pointKey) key "+pointTransition.get(pointKey) + " value "+value);
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
    public Integer getValueofKeyHashMap(Integer key)
    {
        return pointTransition.get(key);
    }
}
