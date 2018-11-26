package be.uantwerpen.localization.astar;

import java.util.ArrayList;
import java.util.List;


import be.uantwerpen.model.Point;
import be.uantwerpen.model.Link;
import be.uantwerpen.services.GraphBuilder;
import be.uantwerpen.services.JobService;
import be.uantwerpen.services.JobListService;
import org.graphstream.algorithm.AStar;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.Path;
import org.graphstream.graph.implementations.SingleGraph;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * A Star Class (will be used as a Service)
 * This class will provide a path, while building up a graph in which the path will be calculated from input
 * it gets from the Graphbuilder service. Due to last minute additions, there are 2 determinePath method,s of
 * one was a quickfix to add a Delivery paramter so that the code could be integrated in the entire project
 *
 * VN 2018
 */
@Service
public class Astar {

    @Autowired
    private JobService jobService;
    @Autowired
    private JobListService jobListService;
    private Graph graph;
    @Autowired
    private GraphBuilder graphBuilder;

    public Astar() {

    }

    /**
     * Initialise function. this make sure all the information is passed on and all functions are correctly initilised.
     * this function should only be once (when first referring to the Astar service)
     */
    public void init() {
        this.graph = new SingleGraph("SmartCityGraph");
        graphBuilder.getMap();
    }

    public void setGraph(Graph graph) {
        this.graph = graph;
    }

    public Graph getGraph() {
        return graph;
    }

    /**
     * Start the Astar function, in which a Graph will be builded that consists of Edges and Nodes
     * This function remains for testing purposes
     */
    public void startAStar() {
        //Testfiles met correcte graaf
        makeNode();
        makeEdge();
        graphBuilder.getLinkCost();
        //Debuggen met test map
        //testDeterminePath(graph, "1002", "1015");
        //testDeterminePath(graph, "1014", "1002");
        //testDeterminePath(graph, "1004", "1015");
        //testDeterminePath(graph, "1015", "1010");
        testDeterminePath(graph, "46", "47");
    }

    /**
     * MakeNode function
     * will make all the necessairy nodes in the Graph, using information provided by the Graphbuilder service.
     */
    public void makeNode() {
        Point[] listOfPoints = this.graphBuilder.getPointList();
        List<Node> nodes = new ArrayList<Node>();
        // provide all the nodes
        for (int i = 0; i < listOfPoints.length; i++) {
            nodes.add(this.graph.addNode(listOfPoints[i].getId().toString()));
            this.graph.getNode(i).setAttribute("xy", listOfPoints[i].getX(), listOfPoints[i].getY());
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
        Link[] listOfEdges = this.graphBuilder.getLinkList();
        for (int i = 0; i < listOfEdges.length; i++) {
            this.graph.addEdge(listOfEdges[i].getId().toString(), listOfEdges[i].getStartPoint().getId().toString(), listOfEdges[i].getStopPoint().getId().toString(), true);
            this.graph.getEdge(listOfEdges[i].getId().toString()).setAttribute("weight", listOfEdges[i].getWeight());
            //this.graph.getEdge(listOfEdges[i].getId().toString()).setAttribute("vehicleType", listOfEdges[i].getVehicle());
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
        this.graphBuilder.getMap();
        graphBuilder.getLinkCost(startPoint, endPoint);
        makeNode();
        makeEdge();
    }

    /**
     * Simular functionality as Determine path. However, this was done with Hardcoded example based on dummy code, for testing purposes.
     * The method won't be deleted, should a futur group want to continue expanding on it.
     *
     * @param graph    Graph that needs to be handed down.
     * @param startPos (String) Starting position in the graph
     * @param endPos   (String) end position in the graph
     */
    public void testDeterminePath(Graph graph, String startPos, String endPos) {
        AStar astar = new AStar(graph);
        astar.compute(startPos, endPos);
        System.out.println(astar.getShortestPath());
        Path path = astar.getShortestPath();
        System.out.println("Shortest path: " + path.toString());
        JobDispatching jd = new JobDispatching(jobService, jobListService, path.toString(), graphBuilder);
    }

    /**
     * Way to determine a Path in a Graph
     *
     * @param startPos   (String) startingposition
     * @param endPos     (String) End position
     * @param idDelivery (String) parameter to link an order to a Delivery.
     */
    public void determinePath2(String startPos, String endPos, String idDelivery) {
        AStar astar = new AStar(this.graph);
        updateNaE(startPos, endPos);
        astar.compute(startPos, endPos);
        System.out.println(astar.getShortestPath());
        Path path = astar.getShortestPath();
        System.out.println("Shortest Path for " + idDelivery + ": " + path.toString());
        JobDispatching jd = new JobDispatching(jobService, jobListService, path.toString(), graphBuilder, idDelivery);
        //JobDispatching jd = new JobDispatching( path.toString(), idDelivery, graphBuilder );
        //JobDispatching jd = new JobDispatching( path.toString(), idDelivery, graphBuilder );
        jd.dispatchOrders2(path.toString(), idDelivery);
    }
}