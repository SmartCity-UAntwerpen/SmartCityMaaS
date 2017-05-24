package be.uantwerpen.visualization.model;

import java.util.List;

/**
 * Created by Frédéric Melaerts on 11/05/2017.
 */
public class DummyPoint {

    private int pointName;
    private List<Integer> neighbours;
    private int physicalPoisionX;
    private int physicalPoisionY;

    public DummyPoint(int pointName, List<Integer> neighbours, int physicalPoisionX, int physicalPoisionY) {
        this.pointName = pointName;
        this.neighbours = neighbours;
        this.physicalPoisionX = physicalPoisionX;
        this.physicalPoisionY = physicalPoisionY;

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
