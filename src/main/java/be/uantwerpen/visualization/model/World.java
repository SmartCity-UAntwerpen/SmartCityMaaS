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

    public void surroundPoint(int y, int x, String specific)
    {
        int y_underLimit = y- surround_layer;
        int y_upperLimit = y+ surround_layer +1;
        int x_underLimit = x- surround_layer;
        int x_upperLimit = x+ surround_layer +1;
        for(int i = y_underLimit; i < y_upperLimit; i++)
        {
           if(i >= 0 && i < dimensionY) {
               for (int j = x_underLimit; j < x_upperLimit; j++) {
                   if(j >= 0 && j < dimensionX) {
                       if (i != y || j != x) {
                           cells.get(i).getCellList().get(j).setType("surrounding_point");
                           cells.get(i).getCellList().get(j).setSpecific(specific);
                           cells.get(i).getCellList().get(j).setSur_x(x);
                           cells.get(i).getCellList().get(j).setSur_y(y);
                       }else
                       {
                           cells.get(i).getCellList().get(j).setType("spot");
                           cells.get(i).getCellList().get(j).setSpecific(specific);
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

    public void parseMap(List<DummyPoint> dummyList)
    {
        DummyMap dummyMap = new DummyMap();
        int x_coor = 0;
        int y_coor = 0;
        String specific = "drone";
        System.out.println("Start pasing world");
        //List<DummyPoint> pointsList =  dummyMap.getDummyPoints();
        List<DummyPoint> pointsList = dummyList;
        for(int m = 0; m < pointsList.size(); m++)
        {
            DummyPoint point = pointsList.get(m);
            x_coor = point.getPhysicalPoisionX()*dummyMap.getUnit();
            y_coor = point.getPhysicalPoisionY()*dummyMap.getUnit();
            //cells.get(y_coor).getCellList().get(x_coor).setType(2);
            surroundPoint(y_coor,x_coor, specific);
            System.out.println("set road point on column "+x_coor + " and row "+ y_coor);
            for(int n = 0; n < point.getNeighbours().size(); n++) {
                int neighbourPoint = point.getNeighbours().get(n);
                int x_coor_neighbour = pointsList.get(neighbourPoint).getPhysicalPoisionX()*dummyMap.getUnit();
                int y_coor_neighbour = pointsList.get(neighbourPoint).getPhysicalPoisionY()*dummyMap.getUnit();
                System.out.println("Check "+neighbourPoint + " real name "+pointsList.get(neighbourPoint).getPointName());
                if(cells.get(y_coor).getCellList().get(x_coor).getType().equals("spot") == false)
                {
                    cells.get(y_coor_neighbour).getCellList().get(x_coor_neighbour).setType("spot");
                    cells.get(y_coor_neighbour).getCellList().get(x_coor_neighbour).setSpecific(specific);
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
                            if(cells.get(y_temp).getCellList().get(c).getType().equals("surrounding_point") == false && cells.get(y_temp).getCellList().get(c).getType().equals("spot") == false) {
                                cells.get(y_temp).getCellList().get(c).setType("road");
                            }
                            if(y_temp !=y_coor)
                            {
                                y_temp++;
                            }
                        }
                    }else if (y_coor_neighbour == y_coor)
                    {
                        for(int c = x_coor_neighbour; c < x_coor; c = c+unitWorld) {
                            if(cells.get(y_coor_neighbour).getCellList().get(c).getType().equals("surrounding_point") == false && cells.get(y_coor_neighbour).getCellList().get(c).getType().equals("spot") == false) {
                                cells.get(y_coor_neighbour).getCellList().get(c).setType("road");
                            }
                        }
                    }else
                    {
                        int y_temp = y_coor_neighbour;
                        for(int c = x_coor_neighbour; c < x_coor; c = c+unitWorld) {
                            if( cells.get(y_temp).getCellList().get(c).getType().equals("surrounding_point") == false && cells.get(y_temp).getCellList().get(c).getType().equals("spot") == false) {
                                cells.get(y_temp).getCellList().get(c).setType("road");
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
                            if( cells.get(c).getCellList().get(x_coor).getType().equals("surrounding_point") && cells.get(c).getCellList().get(x_coor).getType().equals("spot")) {
                                cells.get(c).getCellList().get(x_coor).setType("road");
                            }
                        }
                    }else if (y_coor_neighbour == y_coor)
                    {

                    }else
                    {
                        for(int c = y_coor_neighbour; c > y_coor; c = c-unitWorld) {

                            if( cells.get(c).getCellList().get(x_coor).getType().equals("surrounding_point") == false && cells.get(c).getCellList().get(x_coor).getType().equals("spot") == false) {
                                cells.get(c).getCellList().get(x_coor).setType("road");
                            }
                        }
                    }
                }
                else{
                    if(y_coor_neighbour < y_coor)
                    {
                        int y_temp = y_coor_neighbour;
                        for(int c = x_coor_neighbour; c > x_coor; c = c-unitWorld) {
                            if( cells.get(y_temp).getCellList().get(c).getType().equals("surrounding_point") == false && cells.get(y_temp).getCellList().get(c).getType().equals("spot") == false) {
                                cells.get(y_temp).getCellList().get(c).setType("road");
                            }
                            if(y_temp !=y_coor)
                            {
                                y_temp++;
                            }
                        }
                    }else if (y_coor_neighbour == y_coor)
                    {
                        for(int c = x_coor_neighbour; c > x_coor; c = c-unitWorld) {
                            if( cells.get(y_coor_neighbour).getCellList().get(c).getType().equals("surrounding_point") == false && cells.get(y_coor_neighbour).getCellList().get(c).getType().equals("spot") == false) {
                                cells.get(y_coor_neighbour).getCellList().get(c).setType("road");
                            }
                        }
                    }else
                    {
                        int y_temp = y_coor_neighbour;
                        for(int c = x_coor_neighbour; c > x_coor; c = c-unitWorld) {
                            if( cells.get(y_temp).getCellList().get(c).getType().equals("surrounding_point") == false && cells.get(y_temp).getCellList().get(c).getType().equals("spot") == false) {
                                cells.get(y_temp).getCellList().get(c).setType("road");
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

    public int[] startDelivery(int progress)
    {
        int x_1 = 0;
        int y_1 = 0;
        int x_2 = 50;
        int y_2 = 0;
        int x_3 = 50;
        int y_3 = 50;
        int x = 0;
        int y = 0;
        cells.get(y_1).getCellList().get(x_1);
        cells.get(y_2).getCellList().get(x_2);
        cells.get(y_3).getCellList().get(x_3);
        int distance_1 = 50;
        int distance_2 = 50;
        int total_distance = distance_1+distance_2;
        int progress_distance = total_distance*progress/100;
        if(progress_distance < distance_1)
        {
            x = progress_distance;
        }else
        {
            x = 50;
            y = progress_distance-50;
        }
        int[] distance = new int[2];
        distance[0] = x;
        distance[1] = y;
        return distance;
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
