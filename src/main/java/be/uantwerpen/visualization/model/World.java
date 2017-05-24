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
    // Defines the diameter around a location point.
    private int surround_layer = 2;

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

    public void surroundPoint(int y, int x)
    {

        int  y_underLimit = y- surround_layer;
        int y_upperLimit = y+ surround_layer +1;
        int  x_underLimit = x- surround_layer;
        int x_upperLimit = x+ surround_layer +1;
        for(int i = y_underLimit; i < y_upperLimit;i++)
        {
           if(i >= 0 && i < dimensionY) {
               for (int j = x_underLimit; j < x_upperLimit; j++) {
                   if(j >= 0 && j < dimensionX) {
                       if (i != y || j != x) {
                           cells.get(i).getCellList().get(j).setType(3);
                           cells.get(i).getCellList().get(j).setSur_x(x);
                           cells.get(i).getCellList().get(j).setSur_y(y);
                       }else
                       {
                           cells.get(i).getCellList().get(j).setType(2);
                           cells.get(i).getCellList().get(j).setSur_x(x);
                           cells.get(i).getCellList().get(j).setSur_y(y);
                       }
                   }
               }
           }
        }
        /*
        for(int i = 0; i < surround_layer*8;i++)
        {
            cells.get(y_coor).getCellList().get(x_coor).setType(2);
        }*/
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
            //cells.get(y_coor).getCellList().get(x_coor).setType(2);
            surroundPoint(y_coor,x_coor);
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

                            if(cells.get(y_temp).getCellList().get(c).getType() != 3 && cells.get(y_temp).getCellList().get(c).getType() != 2) {
                                cells.get(y_temp).getCellList().get(c).setType(4);
                            }
                            if(y_temp !=y_coor)
                            {
                                y_temp++;
                            }
                        }
                    }else if (y_coor_neighbour == y_coor)
                    {
                        for(int c = x_coor_neighbour; c < x_coor; c = c+unitWorld) {
                            if(cells.get(y_coor_neighbour).getCellList().get(c).getType() != 3 && cells.get(y_coor_neighbour).getCellList().get(c).getType() != 2) {
                                cells.get(y_coor_neighbour).getCellList().get(c).setType(4);
                            }
                        }
                    }else
                    {
                        int y_temp = y_coor_neighbour;
                        for(int c = x_coor_neighbour; c < x_coor; c = c+unitWorld) {
                            if( cells.get(y_temp).getCellList().get(c).getType() != 3 && cells.get(y_temp).getCellList().get(c).getType() != 2){
                                cells.get(y_temp).getCellList().get(c).setType(4);
                            }
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
                            if( cells.get(c).getCellList().get(x_coor).getType() != 3 && cells.get(c).getCellList().get(x_coor).getType() != 2) {
                                cells.get(c).getCellList().get(x_coor).setType(4);
                            }

                        }
                    }else if (y_coor_neighbour == y_coor)
                    {

                    }else
                    {
                        for(int c = y_coor_neighbour; c > y_coor; c = c-unitWorld) {
                            if( cells.get(c).getCellList().get(x_coor).getType() != 3 && cells.get(c).getCellList().get(x_coor).getType() != 2) {
                                cells.get(c).getCellList().get(x_coor).setType(4);
                            }
                        }
                    }
                }
                else{
                    if(y_coor_neighbour < y_coor)
                    {
                        int y_temp = y_coor_neighbour;
                        for(int c = x_coor_neighbour; c > x_coor; c = c-unitWorld) {
                            if( cells.get(y_temp).getCellList().get(c).getType() != 3 && cells.get(y_temp).getCellList().get(c).getType() != 2) {
                                cells.get(y_temp).getCellList().get(c).setType(4);
                            }
                            if(y_temp !=y_coor)
                            {
                                y_temp++;
                            }
                        }
                    }else if (y_coor_neighbour == y_coor)
                    {
                        for(int c = x_coor_neighbour; c > x_coor; c = c-unitWorld) {
                            if( cells.get(y_coor_neighbour).getCellList().get(c).getType() != 3 && cells.get(y_coor_neighbour).getCellList().get(c).getType() != 2) {
                                cells.get(y_coor_neighbour).getCellList().get(c).setType(4);
                            }
                        }
                    }else
                    {
                        int y_temp = y_coor_neighbour;
                        for(int c = x_coor_neighbour; c > x_coor; c = c-unitWorld) {
                            if( cells.get(y_temp).getCellList().get(c).getType() != 3 && cells.get(y_temp).getCellList().get(c).getType() != 2) {
                                cells.get(y_temp).getCellList().get(c).setType(4);
                            }
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

    public int getSurround_layer() {
        return surround_layer;
    }

    public void setSurround_layer(int surround_layer) {
        this.surround_layer = surround_layer;
    }
}
