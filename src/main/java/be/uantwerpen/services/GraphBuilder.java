package be.uantwerpen.services;

import be.uantwerpen.model.Link;
import be.uantwerpen.model.Point;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


/**
 * NV 2018
 * Made for getting all the information for the A* algorithm.
 */
@Service
public class GraphBuilder {

    private static final Logger logger = LogManager.getLogger(GraphBuilder.class);

    @Autowired
    private BackboneService backboneService;

    private List<Link> linkList;
    private List<Point> pointList;

    //request the map from the core
    public void getMap(String startPoint, String endPoint) {
        linkList = backboneService.getLinks();
        pointList = backboneService.getPointsFromLinks(linkList);
    }

    /**
     * Returns a list with all links of the top map
     */
    public List<Link> getLinkList() {
        return linkList;
    }

    /**
     * Returns a list of all points in the top map
     */
    public List<Point> getPointList() {
        return pointList;
    }

    /**
     * Returns the link with given start and end point, or an empty optional if none is found.
     */
    public Optional<Link> getCertainLink(Long startPoint, Long endPoint) {
        return linkList.stream().filter(link -> link.getStartPoint().getId().equals(startPoint) && link.getStopPoint().getId().equals(endPoint)).findFirst();
    }

}
