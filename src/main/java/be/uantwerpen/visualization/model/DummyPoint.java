package be.uantwerpen.visualization.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Frédéric Melaerts on 11/05/2017.
 */
public class DummyPoint {

    private int pointName;
    private List<Integer> neighbours;
    private int physicalPoisionX;
    private int physicalPoisionY;


    public DummyPoint() {
        this.neighbours = new ArrayList<Integer>();

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
        System.out.print("DummyPoint "+pointName);
        System.out.print(", x "+physicalPoisionX);
        System.out.print(", y "+physicalPoisionY);
        System.out.println(", size neighbours "+neighbours.size());
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

}
