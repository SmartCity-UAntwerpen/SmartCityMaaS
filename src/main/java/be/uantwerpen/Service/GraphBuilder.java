package be.uantwerpen.Service;

import be.uantwerpen.Models.Cost;
import be.uantwerpen.Models.Link;
import be.uantwerpen.Models.Point;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;


/**
 * Created by dries on 10/05/2017.
 */
public class GraphBuilder {
    @Value("${sc.core.ip:localhost}")
    private String serverCoreIP;

    @Value("#{new Integer(${sc.core.port}) ?: 1994}")
    private int serverCorePort;

    private String droneCoreIP = "";
    private String carCoreIP = "";
    private String robotCoreIP = "";

    private String droneCorePort = "";
    private String carCorePort = "";
    private String robotCorePort = "";

    Link[] linkList;
    Point[] pointList;

    public void getMap()
    {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Point[]> responseList;
        responseList = restTemplate.getForEntity("http://" + serverCoreIP + ":" + serverCorePort + "/point/", Point[].class);
        pointList = responseList.getBody();

        restTemplate = new RestTemplate();
        ResponseEntity<Link[]> responseList2;
        responseList2 = restTemplate.getForEntity("http://" + serverCoreIP + ":" + serverCorePort + "/link/", Link[].class);
        linkList = responseList2.getBody();

    }

    public void getLinkCost()
    {
        for(Link link: linkList)
        {
            Long startPoint = link.getStartPoint().getId();
            Long endPoint = link.getStopPoint().getId();
            String vehicle = link.getVehicle();
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<Cost[]> responseList;
            Cost[] costs;
            String url = "http://";
            if(vehicle.equals("robot")) {
                url += robotCoreIP + ":" + robotCorePort;
            } else if(vehicle.equals("drone")) {
                url += droneCoreIP + ":" + droneCorePort;
            } else if(vehicle.equals("car")) {
                url += carCoreIP + ":" + carCorePort;
            } else if(vehicle.equals("walk")) {
                link.setWeight(new Long(0));
            } else {
                System.out.println("no supported vehicle was given. See graphbuilder class");
            }

            if(!vehicle.equals("walk")) {
                url += "/calcWeight/" + startPoint + "/to/" + endPoint;
                responseList = restTemplate.getForEntity(url, Cost[].class);
                costs = responseList.getBody();
                Long lowestCost = costs[0].getWeight() + costs[0].getWeightToStart();
                Long vehicleID = costs[0].getIdVehicle();
                for (Cost cost : costs) {
                    if(cost.getWeightToStart()+cost.getWeight() < lowestCost)
                    {
                        lowestCost = cost.getWeightToStart()+cost.getWeight();
                        vehicleID = cost.getIdVehicle();
                    }
                }
                link.setWeight(lowestCost);
                link.setVehicleID(vehicleID);
            }
        }
    }

    public Link[] getLinkList()
    {
        return linkList;
    }

    public Point[] getPointList()
    {
            return pointList;
    }
}
