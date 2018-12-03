package be.uantwerpen.services;

import be.uantwerpen.model.Cost;
import be.uantwerpen.model.Link;
import be.uantwerpen.model.Point;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;


/**
 * NV 2018
 * Made for getting all the information for the A* algorithm.
 */
@Service
public class GraphBuilder {

    private static final Logger logger = LogManager.getLogger(GraphBuilder.class);
    @Value("${core.ip:localhost}")
    private String serverCoreIP;

    @Value("#{new Integer(${core.port}) ?: 1994}")
    private int serverCorePort;

    @Value("${drone.ip:localhost}")
    private String droneCoreIP;
    @Value("${car.ip:localhost}")
    private String carCoreIP;
    @Value("${robot.ip:localhost}")
    private String robotCoreIP;

    @Value("#{new Integer(${drone.port}) ?: 1994}")
    private String droneCorePort;
    @Value("#{new Integer(${car.port}) ?: 1994}")
    private String carCorePort;
    @Value("#{new Integer(${robot.port}) ?: 1994}")
    private String robotCorePort;

    private Link[] linkList;
    private Point[] pointList;

    //request the map from the core
    public void getMap() {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Point[]> responseList;
        String pointUrl = "http://" + serverCoreIP + ":" + serverCorePort + "/map/topmapjson/points";
        responseList = restTemplate.getForEntity(pointUrl, Point[].class);
        pointList = responseList.getBody();

        restTemplate = new RestTemplate();
        ResponseEntity<Link[]> responseList2;
        String linkUrl = "http://" + serverCoreIP + ":" + serverCorePort + "/map/topmapjson/links";
        responseList2 = restTemplate.getForEntity(linkUrl, Link[].class);
        linkList = responseList2.getBody();
    }

    //request the cost from all vehicle cores and fill in the least costly vehicle
    public void getLinkCost(String routeStartPoint, String routeEndPoint) {
        Optional<Point> oStartPoint = Arrays.stream(pointList).filter(point -> point.getId().equals(Long.parseLong(routeStartPoint))).findFirst();
        Optional<Point> oEndPoint = Arrays.stream(pointList).filter(point -> point.getId().equals(Long.parseLong(routeEndPoint))).findFirst();

        Point startP = oStartPoint.orElseThrow(() -> new IllegalArgumentException("Failed to find start point"));
        Point endP = oEndPoint.orElseThrow(() -> new IllegalArgumentException("Failed to find end point"));

        // check if direct link between start and end point exists
        Collection<Link> linkCollection = Arrays.stream(linkList).filter(link -> link.getStartPoint().equals(startP) && link.getStopPoint().equals(endP)).collect(Collectors.toList());
        if (linkCollection.size() == 0) {
            // otherwise loop through all links
            linkCollection = Arrays.stream(linkList).distinct().collect(Collectors.toList());
        }

        for (Link link : linkCollection) {
            Long startPoint = link.getStartPoint().getId();
            Long endPoint = link.getStopPoint().getId();
            String vehicle = link.getVehicle().toUpperCase();
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<Cost[]> responseList;
            Cost[] costs;
            String url = "http://";

            switch (vehicle) {
                case "ROBOTTOP":
                    if ((startP.getType().equals("robot") && link.getStartPoint().equals(startP))
                            || (endP.getType().equals("robot") && link.getStopPoint().equals(endP))
                            || (!startP.getType().equals("robot") && !endP.getType().equals("robot"))) {
                        url += robotCoreIP + ":" + robotCorePort + "/cost";
                        break;
                    } else {
                        // don't calculate weight for unused link
                        link.setWeight(900L);
                        link.setVehicleID(5000L);
                        continue;
                    }
                case "DRONETOP":
                    url += droneCoreIP + ":" + droneCorePort;
                    break;
                case "CARTOP":
                    url += carCoreIP + ":" + carCorePort + "/carmanager";
                    break;
                case "WAIT":
                    link.setWeight(0L);
                    continue;
                default:
                    logger.warn("no supported vehicle was given. See graphbuilder class " + link.getVehicle());
                    link.setWeight(900L);
                    link.setVehicleID(5000L);
                    continue;
            }

            url += "/calcWeight/" + startPoint + "/" + endPoint;
            logger.debug("url: " + url + ", id of link: " + link.getId());
            try {
                responseList = restTemplate.getForEntity(url, Cost[].class);
                costs = responseList.getBody();
                logger.info("Received costs: " + Arrays.toString(costs));
            } catch (HttpClientErrorException e) {
                logger.error("Http error: " + e);
                costs = null;
            }
            //run over all the answers to find the most cost effective vehicle
            if (costs == null || costs.length == 0) {
                logger.error("FAULTY answer from " + vehicle + " cost is NULL, weight set to 999999!");
                link.setWeight(99999L);
            } else {
                long lowestCost = (costs[0].getWeight() + costs[0].getWeightToStart());
                Cost bestCost = costs[0];
                for (Cost cost : costs) {
                    if (cost.getWeightToStart() + cost.getWeight() < lowestCost) {
                        lowestCost = cost.getWeightToStart() + cost.getWeight();
                        bestCost = cost;
                    }
                }

                //run over all the links to look for the wait-links that are connected to the current link
                for (Link link1 : linkList) {
                    if (link1.getVehicle().equals("wait") && link1.getStopPoint().getId().equals(startPoint)) {
                        link1.setWeight(bestCost.getWeightToStart());
                    }
                }
                link.setWeight(bestCost.getWeight());
                link.setVehicleID(bestCost.getIdVehicle());
                logger.debug("WEIGHT: Link id " + link.getId() + " - Weight for this link: " + link.getWeight());
            }
        }
    }

    //for returning the list with all the links
    public Link[] getLinkList() {
        return linkList;
    }

    //for returning the list with all the points
    public Point[] getPointList() {
        return pointList;
    }

    //look for a certain link in the list
    public Link getCertainLink(Long startPoint, Long endPoint) {
        Link foundLink = new Link((long) (-1), pointList[0], pointList[1], "none");
        for (Link link : linkList) {
            if (link.getStartPoint().getId().equals(startPoint) && link.getStopPoint().getId().equals(endPoint)) {
                foundLink = link;
            }
        }
        return foundLink;
    }

}
