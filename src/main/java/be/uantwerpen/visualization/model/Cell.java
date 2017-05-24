package be.uantwerpen.visualization.model;

/**
 * Created by Frédéric Melaerts on 10/05/2017.
 */
public class Cell {
    private int x;
    private int y;
    private int type;
    private int sur_x;
    private int sur_y;
    /*
     * Type defenition
     * type: 1 = background
     * type: 2 = spot
     * type: 3 = surrounding point
     * type: 4 = road
     */
    public Cell(int x, int y, int type) {
        this.x = x;
        this.y = y;
        this.type = type;
        this.sur_x = 0;
        this.sur_y = 0;
    }

    public Cell(int x, int y) {
        this.x = x;
        this.y = y;
        type = 1;
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

    public int getType() {
        return type;
    }

    public void setType(int type) {
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
}
