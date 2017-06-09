package be.uantwerpen.visualization.model;

/**
 * Created by Frédéric Melaerts on 30/05/2017.
 *
 */
public class DummyVehicle {
        private volatile int distance;
        private int ID;
        private String type;
        private int start;
        private int end;
        private int progress;

    public DummyVehicle(int ID) {
        this.ID = ID;
    }
    public DummyVehicle() {
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getValue() {
        return distance;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }
}
