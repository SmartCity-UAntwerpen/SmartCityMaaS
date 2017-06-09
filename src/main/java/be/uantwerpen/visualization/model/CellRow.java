package be.uantwerpen.visualization.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Frédéric Melaerts on 10/05/2017.
 *
 * A CellRow is a row with aal its cells in the world.
 * This class is necessary to create a matrix in the world on a dynamic manner.
 */
public class CellRow {

    private List<Cell> cellList;

    public CellRow() {
        this.cellList = new ArrayList<Cell>();
    }
    public void addCell(Cell cell)
    {
        this.cellList.add(cell);
    }
    public CellRow(List<Cell> cellList) {
        this.cellList = cellList;
    }

    public List<Cell> getCellList() {
        return cellList;
    }

    public void setCellList(List<Cell> cellList) {
        this.cellList = cellList;
    }
}
