package be.uantwerpen.localization.astar;

import java.util.List;

/**
 * Created by Revil on 10/05/2017.
 */
public class Knoop {
    private String name;
    private int x;
    private int y;
    private List<Long> listofVehicles;

    public Knoop (String name, int x, int y) {
        this.name = name;
        this.x = x;
        this.y = y;
    }

    public Knoop (String name, int x, int y, Long idVehicle) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.listofVehicles.add(idVehicle);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public List<Long> getListofVehicles() {
        return listofVehicles;
    }

    public void setListofVehicles(List<Long> listofVehicles) {
        this.listofVehicles = listofVehicles;
    }
}
