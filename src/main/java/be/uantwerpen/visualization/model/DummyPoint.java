package be.uantwerpen.visualization.model;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Frédéric Melaerts on 11/05/2017.
 *
 * DummyPoint is a point af the map stored on a cell in the world.
 * Its cell coordinates are stored in this class, together with all the neighbour points of this point.
 * The type specifies the specific vehicle type of this point: robot, car, drone, ...
 * Characteristic represent the extra information of this point: endpoint, light, intersection,...
 * SpotName is the ID if the point.
 * Neighbours contains all the ID's  of the point's neighbours.
 *
 */
public class DummyPoint {

    private static final Logger logger = LogManager.getLogger(DummyPoint.class);

    private int pointName;
    private List<Integer> neighbours;
    private int physicalPoisionX;
    private int physicalPoisionY;
    private String type;
    private String pointCharacteristic;

    public DummyPoint() {
        this.neighbours = new ArrayList<Integer>();
    }

    public DummyPoint(int pointName, List<Integer> neighbours, int physicalPoisionX, int physicalPoisionY, String type) {
        this.pointName = pointName;
        this.neighbours = neighbours;
        this.physicalPoisionX = physicalPoisionX;
        this.physicalPoisionY = physicalPoisionY;
        this.type = type;
    }

    public DummyPoint(int pointName, List<Integer> neighbours, int physicalPoisionX, int physicalPoisionY) {
        this.pointName = pointName;
        this.neighbours = neighbours;
        this.physicalPoisionX = physicalPoisionX;
        this.physicalPoisionY = physicalPoisionY;
    }

    public DummyPoint(int pointName, int physicalPoisionX, int physicalPoisionY) {
        this.pointName = pointName;
        this.neighbours = new ArrayList<Integer>();
        this.physicalPoisionX = physicalPoisionX;
        this.physicalPoisionY = physicalPoisionY;

    }
    public void print()
    {
        logger.info("DummyPoint "+pointName + ": x "+physicalPoisionX + ", y "+physicalPoisionY + ", size neighbours "+neighbours.size());
    }

    public void addNeighbour(int neighbour)
    {
        neighbours.add(neighbour);
    }

    public int getPhysicalPoisionX() {
        return physicalPoisionX;
    }

    public void setPhysicalPoisionX(int physicalPoisionX) {
        this.physicalPoisionX = physicalPoisionX;
    }

    public int getPhysicalPoisionY() {
        return physicalPoisionY;
    }

    public void setPhysicalPoisionY(int physicalPoisionY) {
        this.physicalPoisionY = physicalPoisionY;
    }

    public int getPointName() {
        return pointName;
    }

    public void setPointName(int pointName) {
        this.pointName = pointName;
    }

    public List<Integer> getNeighbours() {
        return neighbours;
    }

    public void setNeighbours(List<Integer> neighbours) {
        this.neighbours = neighbours;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPointCharacteristic() {
        return pointCharacteristic;
    }
    public void setPointCharacteristic(String pointCharacteristic) {
        this.pointCharacteristic = pointCharacteristic;
    }
}
