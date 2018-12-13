package be.uantwerpen.localization.astar;

import be.uantwerpen.model.Link;
import be.uantwerpen.model.Point;
import be.uantwerpen.services.BackboneService;
import be.uantwerpen.services.GraphBuilder;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.graphstream.algorithm.AStar;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Path;
import org.graphstream.graph.implementations.SingleGraph;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * A Star Class (will be used as a Service)
 * This class will provide a path, while building up a graph in which the path will be calculated from input
 * it gets from the Graphbuilder service. Due to last minute additions, there are 2 determinePath method,s of
 * one was a quickfix to add a Delivery paramter so that the code could be integrated in the entire project
 * <p>
 * VN 2018
 */
@Service
public class Astar {

    private static final Logger logger = LogManager.getLogger(Astar.class);

    private Graph graph;
    @Autowired
    private GraphBuilder graphBuilder;
    @Autowired
    private BackboneService backboneService;

    /**
     * Initialise function. this make sure all the information is passed on and all functions are correctly initilised.
     * this function should only be once (when first referring to the Astar service)
     */
    public void init() {
        this.graph = new SingleGraph("SmartCityGraph");
        graphBuilder.getMap(null, null);
    }

    public void setGraph(Graph graph) {
        this.graph = graph;
    }

    public Graph getGraph() {
        return graph;
    }

    /**
     * MakeNode function
     * will make all the necessairy nodes in the Graph, using information provided by the Graphbuilder service.
     */
    public void makeNode() {
        List<Point> listOfPoints = this.graphBuilder.getPointList();
        // provide all the nodes
        for (int i = 0; i < listOfPoints.size(); i++) {
            this.graph.addNode(listOfPoints.get(i).getId().toString());
            this.graph.getNode(i).setAttribute("xy", listOfPoints.get(i).getX(), listOfPoints.get(i).getY());
        }
    }

    /**
     * Destroy nodes function
     * Will remove all the nodes in the Graph. This is needed when you want to destroy an old graph.
     */
    private void destroyNodes() {
        for (int i = this.graph.getNodeCount(); i > 0; i--) {
            this.graph.removeNode(i);
        }
    }

    /**
     * Make Edge function
     * will make all the necessairy edges in the Graph, using the information provided by the Graphbuilder
     */
    public void makeEdge() {
        List<Link> edges = this.graphBuilder.getLinkList();
        for (Link edge : edges) {
            this.graph.addEdge(edge.getId().toString(), edge.getStartPoint().getId().toString(), edge.getStopPoint().getId().toString(), true);
            this.graph.getEdge(edge.getId().toString()).setAttribute("weight", edge.getWeight());
            //this.graph.getEdge(edge.getId().toString()).setAttribute("vehicleType", edge.getVehicle());
        }
    }

    /**
     * Destroy edges function
     * Will remove all the edges in the Graph. This is needed when you want to destroy an old graph.
     */
    private void destroyEdges() {
        for (int i = this.graph.getEdgeCount(); i > 0; i--) {
            this.graph.removeEdge(i);
        }
    }

    /**
     * Update Nodes and Edges function
     * this function will first destroy all the nodes and edges in a graph, request an update from the Graphbuilder service,
     * before rebuilding the Graph, hence updating the Graph
     */
    public void updateNaE(String startPoint, String endPoint) {
        destroyNodes();
        destroyEdges();
        this.graphBuilder.getMap(startPoint, endPoint);
        makeNode();
        makeEdge();
    }

    /**
     * Way to determine a Path in a Graph
     *
     * @param startPos   (String) startingposition
     * @param endPos     (String) End position
     * @param idDelivery (String) parameter to link an order to a Delivery.
     */
    public void determinePath2(String startPos, String endPos, String idDelivery) {
        // todo get graph from BackBone:
        AStar astar = new AStar(this.graph);
        updateNaE(startPos, endPos);
        astar.compute(startPos, endPos);
        Path path = astar.getShortestPath();
        logger.info("Shortest Path for " + idDelivery + ": " + path.toString());
        JobDispatching jd = new JobDispatching(graphBuilder, backboneService);
        //JobDispatching jd = new JobDispatching( path.toString(), idDelivery, graphBuilder );
        jd.dispatchOrders2(path.toString(), idDelivery);
    }
}