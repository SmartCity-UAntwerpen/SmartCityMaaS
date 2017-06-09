package be.uantwerpen.visualization.model;

/**
 * Created by Frédéric Melaerts on 10/05/2017.
 *
 * A cell is one unit element in the world which has a specific x- and y-coordinate in that world.
 * Its type represents its function: road, surrounding, spot,...
 * Specific specifies the specific vehicle type it belongs to: robot, car, drone, ...
 * Characteristic represent the extra information of this cell: endpoint, light, intersection,...
 * SpotID is only specified when a spot is situated in this cell or this cell is a surrounding cell of specific spot.
 * If the cell is a surrounding cell, it has the coordinates of the spot'scell in the sur_x and sur_y parameter.
 */
public class Cell {
    private int x;
    private int y;
    private String type;
    private String specific;
    private String characteristic;
    private int sur_x;
    private int sur_y;
    private int spotID;
    /*
     * Type defenition
     * type: 1 = background
     * type: 2 = spot_robot
     * type: 3 = surrounding_point
     * type: 4 = road_robot
     * type: 5 = spot_drone
     * type: 6 = road_drone
     */
    public Cell() {
        this.x = 0;
        this.y = 0;
        this.type = "unknown";
        this.sur_x = 0;
        this.sur_y = 0;
        this.spotID = -1;
    }
    public Cell(int x, int y) {
        this.x = x;
        this.y = y;
        type = "background";
        specific = "robot";
        this.sur_x = 0;
        this.sur_y = 0;
        this.spotID = -1;
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

    public int getSur_x() {
        return sur_x;
    }

    public void setSur_x(int sur_x) {
        this.sur_x = sur_x;
    }

    public int getSur_y() {
        return sur_y;
    }

    public void setSur_y(int sur_y) {
        this.sur_y = sur_y;
    }

    public String getSpecific() {
        return specific;
    }

    public void setSpecific(String specific) {
        this.specific = specific;
    }

    public int getSpotID() {
        return spotID;
    }

    public void setSpotID(int spotID) {
        this.spotID = spotID;
    }

    public String getCharacteristic() {
        return characteristic;
    }

    public void setCharacteristic(String characteristic) {
        this.characteristic = characteristic;
    }
}
