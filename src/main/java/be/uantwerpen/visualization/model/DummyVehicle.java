package be.uantwerpen.visualization.model;

/**
 * Created by Frédéric Melaerts on 30/05/2017.
 *
 * This class is used to store the vehicle information received from the backbone core.
 * This class contains the vehicle's ID, tupe, start point, end point and progress.
 */
public class DummyVehicle {
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

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
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
