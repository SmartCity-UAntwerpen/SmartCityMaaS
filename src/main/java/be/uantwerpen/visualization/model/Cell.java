package be.uantwerpen.visualization.model;

/**
 * Created by Frédéric Melaerts on 10/05/2017.
 */
public class Cell {
    private int x;
    private int y;
    private int type;

    public Cell(int x, int y, int type) {
        this.x = x;
        this.y = y;
        this.type = type;
    }

    public Cell(int x, int y) {
        this.x = x;
        this.y = y;
        type = 1;
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
}
