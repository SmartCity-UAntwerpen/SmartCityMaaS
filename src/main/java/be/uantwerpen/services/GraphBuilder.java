package be.uantwerpen.services;

import be.uantwerpen.model.Link;
import be.uantwerpen.model.Point;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;


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
    private List<Point> pointList;

    //request the map from the core
    public void getMap(String startPoint, String endPoint) {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Link[]> responseList;
        String linkUrl = "http://" + serverCoreIP + ":" + serverCorePort + "/map/topmapjson/links";
        responseList = restTemplate.getForEntity(linkUrl, Link[].class);
        linkList = responseList.getBody();

        pointList = Arrays.stream(linkList).flatMap(link -> Stream.of(link.getStartPoint(), link.getStopPoint())).distinct().collect(Collectors.toList());
    }

    //for returning the list with all the links
    public Link[] getLinkList() {
        return linkList;
    }

    //for returning the list with all the points
    public List<Point> getPointList() {
        return pointList;
    }

    //look for a certain link in the list
    public Optional<Link> getCertainLink(Long startPoint, Long endPoint) {
        return Arrays.stream(linkList).filter(link -> link.getStartPoint().getId().equals(startPoint) && link.getStopPoint().getId().equals(endPoint)).findFirst();
    }

}
