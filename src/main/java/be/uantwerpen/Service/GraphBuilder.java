package be.uantwerpen.Service;

import be.uantwerpen.Models.Cost;
import be.uantwerpen.Models.Link;
import be.uantwerpen.Models.Point;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;


/**
 * Created by dries on 10/05/2017.
 */
public class GraphBuilder {
    @Value("${sc.core.ip:localhost}")
    private String serverCoreIP;

    @Value("#{new Integer(${sc.core.port}) ?: 1994}")
    private int serverCorePort;

    @Value("${sc.drone.ip:localhost}")
    private String droneCoreIP;
    @Value("${sc.car.ip:localhost}")
    private String carCoreIP;
    @Value("${sc.robot.ip:localhost}")
    private String robotCoreIP;

    @Value("#{new Integer(${sc.drone.port}) ?: 1994}")
    private String droneCorePort;
    @Value("#{new Integer(${sc.car.port}) ?: 1994}")
    private String carCorePort;
    @Value("#{new Integer(${sc.robot.port}) ?: 1994}")
    private String robotCorePort;

    Link[] linkList;
    Point[] pointList;
    ArrayList<Cost> bestCostList = new ArrayList<>();
    ArrayList<Link> walkLinks = new ArrayList<>();

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
        for(Link link: linkList)
        {
            if(link.getVehicle().equals("walk"))
            {
                link.setWeight(new Long(0));
                walkLinks.add(link);
            }
        }

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
                //link.setWeight(new Long(0));
            } else {
                System.out.println("no supported vehicle was given. See graphbuilder class");
            }

            if(!vehicle.equals("walk")) {
                url += "/calcWeight/" + startPoint + "/to/" + endPoint;
                responseList = restTemplate.getForEntity(url, Cost[].class);
                costs = responseList.getBody();
                Long lowestCost = (costs[0].getWeight() + costs[0].getWeightToStart());
                //Long vehicleID = costs[0].getIdVehicle();
                Cost bestCost = costs[0];
                for (Cost cost : costs) {
                    if(cost.getWeightToStart()+cost.getWeight() < lowestCost)
                    {
                        lowestCost = cost.getWeightToStart()+cost.getWeight();
                        bestCost = cost;
                    }
                }
                for(Link walkLink : walkLinks)
                {
                    if(walkLink.getStopPoint().getId()== endPoint)
                    {
                        walkLink.setWeight(bestCost.getWeightToStart());
                    }
                }
                link.setWeight(bestCost.getWeight());
                link.setVehicleID(bestCost.getIdVehicle());
                bestCostList.add(bestCost);
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

    public ArrayList<Cost> getBestCostList() {return bestCostList;}
}
