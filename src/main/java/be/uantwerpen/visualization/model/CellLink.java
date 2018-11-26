package be.uantwerpen.visualization.model;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Frédéric Melaerts on 31/05/2017.
 *
 * Cell links are the connections between two spot cells.
 * One cell link contains the start spot cell and end spot cell.
 * The intermediate cells are the cells that connect these two points in the world.
 *
 */
public class CellLink {

    private static final Logger logger = LogManager.getLogger(DummyPoint.class);

    private Cell startCell;
    private Cell endCell;
    private List<Cell> intermediateCells;

    /**
     * Print cellLink information.
     */
    public void print()
    {
        logger.info("CellLink:");
        logger.info("Start : " + startCell.getSpotID() + " ==> x "+startCell.getX()+ " - y "+startCell.getY());
        for(Cell it: intermediateCells)
        {
            logger.info(". x " + it.getX() + " - y "+ it.getY());
        }
        logger.info("End " + endCell.getSpotID() + " ==> x "+endCell.getX()+ " - y "+endCell.getY());
    }


    public void addIntermediateCell(Cell intermediateCell)
    {
        intermediateCells.add(intermediateCell);
    }

    public CellLink() {
        intermediateCells = new ArrayList<Cell>();
    }

    public Cell getStartCell() {
        return startCell;
    }

    public void setStartCell(Cell startCell) {
        this.startCell = startCell;
    }

    public Cell getEndCell() {
        return endCell;
    }

    public void setEndCell(Cell endCell) {
        this.endCell = endCell;
    }

    public List<Cell> getLinkCells() {
        return intermediateCells;
    }

    public void setLinkCells(List<Cell> intermediateCells) {
        this.intermediateCells = intermediateCells;
    }

    public int sizeIntermediateCells()
    {
        return intermediateCells.size();
    }
}
