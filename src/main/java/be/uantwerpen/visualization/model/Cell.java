package be.uantwerpen.visualization.model;

/**
 * Created by Frédéric Melaerts on 10/05/2017.
 */
public class Cell {
    private int x;
    private int y;
    private String type;
    private String specific;
    private float sur_x;
    private float sur_y;
    /*
     * Type defenition
     * type: 1 = background
     * type: 2 = spot_robot
     * type: 3 = surrounding_point
     * type: 4 = road_robot
     * type: 5 = spot_drone
     * type: 6 = road_drone
     */
    public Cell(int x, int y, String type) {
        this.x = x;
        this.y = y;
        this.type = type;
        this.sur_x = 0;
        this.sur_y = 0;
    }

    public Cell(int x, int y) {
        this.x = x;
        this.y = y;
        type = "background";
        specific = "robot";
        this.sur_x = 0;
        this.sur_y = 0;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public float getSur_x() {
        return sur_x;
    }

    public void setSur_x(float sur_x) {
        this.sur_x = sur_x;
    }

    public float getSur_y() {
        return sur_y;
    }

    public void setSur_y(float sur_y) {
        this.sur_y = sur_y;
    }

    public String getSpecific() {
        return specific;
    }

    public void setSpecific(String specific) {
        this.specific = specific;
    }
}
