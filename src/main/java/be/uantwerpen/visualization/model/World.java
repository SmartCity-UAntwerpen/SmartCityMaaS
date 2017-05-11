package be.uantwerpen.visualization.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Frédéric Melaerts on 10/05/2017.
 */
public class World {
    private List<CellRow> cells;
    private int dimensionX;
    private int dimensionY;
    private int unitWorld;

    public World(int dimensionX, int dimensionY) {
        this.dimensionX = dimensionX;
        this.dimensionY = dimensionY;
        this.unitWorld = 1; //1 meter
        cells = new ArrayList<CellRow>();
        for(int i =0; i <dimensionX;i++)
        {
            CellRow cellRow = new CellRow();
            for(int j =0; j <dimensionY; j++)
            {
                Cell cell = new Cell(i,j);
                cellRow.addCell(cell);
            }
            cells.add(cellRow);
        }

    }

    public void parseMap()
    {
        DummyMap dummyMap = new DummyMap();
        int x_coor = 0;
        int y_coor = 0;
        System.out.println("Start pasing world");
        List<DummyPoint> pointsList =  dummyMap.getDummyPoints();
        for(int m = 0; m < pointsList.size(); m++)
        {
            DummyPoint point = pointsList.get(m);
            x_coor = point.getPhysicalPoisionX()*dummyMap.getUnit();
            y_coor = point.getPhysicalPoisionY()*dummyMap.getUnit();
            cells.get(y_coor).getCellList().get(x_coor).setType(2);
            System.out.println("set road point on column "+x_coor + " and row "+ y_coor);
            for(int n = 0; n < point.getNeighbours().size(); n++) {
                int neighbourPoint = point.getNeighbours().get(n);
                int x_coor_neighbour = pointsList.get(neighbourPoint).getPhysicalPoisionX()*dummyMap.getUnit();
                int y_coor_neighbour = pointsList.get(neighbourPoint).getPhysicalPoisionY()*dummyMap.getUnit();
                System.out.println("Check "+neighbourPoint + " real name "+pointsList.get(neighbourPoint).getPointName());
                if(cells.get(y_coor).getCellList().get(x_coor).getType() != 2)
                {
                    cells.get(y_coor_neighbour).getCellList().get(x_coor_neighbour).setType(2);
                }
                double tempPow_x = Math.pow(Math.abs(x_coor-x_coor_neighbour),2);
                double tempPow_y = Math.pow(Math.abs(y_coor-y_coor_neighbour),2);
                double temp_interpolate = Math.sqrt(tempPow_x+tempPow_y);
                int interpolate = (int)temp_interpolate;
                if(x_coor_neighbour < x_coor)
                {
                    if(y_coor_neighbour < y_coor)
                    {
                        int y_temp = y_coor_neighbour;
                        for(int c = x_coor_neighbour; c < x_coor; c = c+unitWorld) {
                            cells.get(y_temp).getCellList().get(c).setType(2);
                            if(y_temp !=y_coor)
                            {
                                y_temp++;
                            }
                        }
                    }else if (y_coor_neighbour == y_coor)
                    {
                        for(int c = x_coor_neighbour; c < x_coor; c = c+unitWorld) {
                            cells.get(y_coor_neighbour).getCellList().get(c).setType(2);
                        }
                    }else
                    {
                        int y_temp = y_coor_neighbour;
                        for(int c = x_coor_neighbour; c < x_coor; c = c+unitWorld) {
                            cells.get(y_temp).getCellList().get(c).setType(2);
                            if(y_temp !=y_coor)
                            {
                                y_temp--;
                            }
                        }
                    }
                }else if(x_coor_neighbour == x_coor)
                {
                    if(y_coor_neighbour < y_coor)
                    {
                        for(int c = y_coor_neighbour; c < y_coor; c = c+unitWorld) {
                            cells.get(c).getCellList().get(x_coor).setType(2);
                        }
                    }else if (y_coor_neighbour == y_coor)
                    {

                    }else
                    {
                        for(int c = y_coor_neighbour; c > y_coor; c = c-unitWorld) {
                            cells.get(c).getCellList().get(x_coor).setType(2);
                        }
                    }
                }
                else{
                    if(y_coor_neighbour < y_coor)
                    {
                        int y_temp = y_coor_neighbour;
                        for(int c = x_coor_neighbour; c > x_coor; c = c-unitWorld) {
                            cells.get(y_temp).getCellList().get(c).setType(2);
                            if(y_temp !=y_coor)
                            {
                                y_temp++;
                            }
                        }
                    }else if (y_coor_neighbour == y_coor)
                    {
                        for(int c = x_coor_neighbour; c > x_coor; c = c-unitWorld) {
                            cells.get(y_coor_neighbour).getCellList().get(c).setType(2);
                        }
                    }else
                    {
                        int y_temp = y_coor_neighbour;
                        for(int c = x_coor_neighbour; c > x_coor; c = c-unitWorld) {
                            cells.get(y_temp).getCellList().get(c).setType(2);
                            if(y_temp !=y_coor)
                            {
                                y_temp--;
                            }
                        }
                    }
                }
            }
        }



    }

    public List<CellRow> getCells() {
        return cells;
    }
    public void setCells(List<CellRow> cells) {
        this.cells = cells;
    }

    public int getDimensionX() {
        return dimensionX;
    }

    public void setDimensionX(int dimensionX) {
        this.dimensionX = dimensionX;
    }

    public int getDimensionY() {
        return dimensionY;
    }

    public void setDimensionY(int dimensionY) {
        this.dimensionY = dimensionY;
    }

    public int getUnitWorld() {
        return unitWorld;
    }

    public void setUnitWorld(int unitWorld) {
        this.unitWorld = unitWorld;
    }
}
