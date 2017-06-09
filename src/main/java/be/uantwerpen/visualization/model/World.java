package be.uantwerpen.visualization.model;

import be.uantwerpen.model.Job;
import be.uantwerpen.model.JobList;

import java.util.ArrayList;
import java.util.DoubleSummaryStatistics;
import java.util.List;

/**
 * Created by Frédéric Melaerts on 10/05/2017.
 *
 *
 */
public class World {
    private String world_ID;
    private List<CellRow> cells;
    private int dimensionX;
    private int dimensionY;
    private int unitWorld;
    private int unitMap;
    private List<CellLink> cellLinks;
    // Defines the diameter around a location point.
    private int surround_layer = 1;

    public World(int dimensionX, int dimensionY) {
        this.dimensionX = dimensionX;
        this.dimensionY = dimensionY;
        this.unitWorld = 1; //1 meter
        this.unitMap = 2;
        cells = new ArrayList<CellRow>();
        for (int i = 0; i < dimensionX; i++) {
            CellRow cellRow = new CellRow();
            for (int j = 0; j < dimensionY; j++) {
                Cell cell = new Cell(i, j);
                cellRow.addCell(cell);
            }
            cells.add(cellRow);
        }
    }

    public World() {
    }


    public void surroundPoint(int y, int x, String specific, int spotID, String characteristic) {
        int y_underLimit = y - surround_layer;
        int y_upperLimit = y + surround_layer + 1;
        int x_underLimit = x - surround_layer;
        int x_upperLimit = x + surround_layer + 1;
        for (int i = y_underLimit; i < y_upperLimit; i++) {
            if (i >= 0 && i < dimensionY) {
                for (int j = x_underLimit; j < x_upperLimit; j++) {
                    if (j >= 0 && j < dimensionX) {
                        if (i != y || j != x) {
                            cells.get(i).getCellList().get(j).setType("surrounding_point");
                            cells.get(i).getCellList().get(j).setSpecific(specific);
                            cells.get(i).getCellList().get(j).setSur_x(x);
                            cells.get(i).getCellList().get(j).setSur_y(y);
                            cells.get(i).getCellList().get(j).setSpotID(spotID);
                            cells.get(i).getCellList().get(j).setCharacteristic(characteristic);
                        } else {
                            cells.get(i).getCellList().get(j).setType("spot");
                            cells.get(i).getCellList().get(j).setSpecific(specific);
                            cells.get(i).getCellList().get(j).setSur_x(x);
                            cells.get(i).getCellList().get(j).setSur_y(y);
                            cells.get(i).getCellList().get(j).setSpotID(spotID);
                            cells.get(i).getCellList().get(j).setCharacteristic(characteristic);
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

    public void parseMap(List<DummyPoint> dummyList) {
        //DummyMap dummyMap = new DummyMap();


        int x_coor = 0;
        int y_coor = 0;
        String specific = "drone";
        System.out.println("Start pasing world");
        //List<DummyPoint> pointsList =  dummyMap.getDummyPoints();
        List<DummyPoint> pointsList = dummyList;
        cellLinks = new ArrayList<CellLink>();
        for (int m = 0; m < pointsList.size(); m++) {
            DummyPoint point = pointsList.get(m);
            x_coor = point.getPhysicalPoisionX() * unitMap;
            y_coor = point.getPhysicalPoisionY() * unitMap;

            // Start point for the link
            Cell startPoint = new Cell(x_coor,y_coor);
            startPoint.setSpotID(point.getPointName());

            //cells.get(y_coor).getCellList().get(x_coor).setType(2);
            surroundPoint(y_coor, x_coor, point.getType(), point.getPointName(),point.getPointCharacteristic());
            // System.out.println("set road point on column " + x_coor + " and row " + y_coor);
            for (int n = 0; n < point.getNeighbours().size(); n++) {
                CellLink cellLink = new CellLink();
                cellLink.setStartCell(startPoint);


                int neighbourPoint = point.getNeighbours().get(n);
                int x_coor_neighbour = pointsList.get(neighbourPoint).getPhysicalPoisionX() * unitMap;
                int y_coor_neighbour = pointsList.get(neighbourPoint).getPhysicalPoisionY() * unitMap;
                // End point for the link
                Cell endPoint = new Cell(x_coor_neighbour,y_coor_neighbour);
                endPoint.setSpotID(pointsList.get(neighbourPoint).getPointName());
                cellLink.setEndCell(endPoint);

                // System.out.println("Check " + neighbourPoint + " real name " + pointsList.get(neighbourPoint).getPointName());
                if (cells.get(y_coor).getCellList().get(x_coor).getType().equals("spot") == false) {
                    cells.get(y_coor_neighbour).getCellList().get(x_coor_neighbour).setType("spot");
                    cells.get(y_coor_neighbour).getCellList().get(x_coor_neighbour).setSpecific(specific);
                    cells.get(y_coor_neighbour).getCellList().get(x_coor_neighbour).setSpotID(point.getPointName());
                }
                double tempPow_x = Math.pow(Math.abs(x_coor - x_coor_neighbour), 2);
                double tempPow_y = Math.pow(Math.abs(y_coor - y_coor_neighbour), 2);
                double temp_interpolate = Math.sqrt(tempPow_x + tempPow_y);
                int interpolate = (int) temp_interpolate;
                Cell roadCell = new Cell();
                roadCell.setSpecific(point.getType());
                if (x_coor_neighbour < x_coor) {
                    if (y_coor_neighbour < y_coor) {
                        int y_temp = y_coor_neighbour;

                        for (int c = x_coor_neighbour; c <= x_coor; c = c + unitWorld) {
                            if (cells.get(y_temp).getCellList().get(c).getType().equals("surrounding_point") == false && cells.get(y_temp).getCellList().get(c).getType().equals("spot") == false) {
                                cells.get(y_temp).getCellList().get(c).setType("road");
                                cells.get(y_temp).getCellList().get(c).setSpecific(point.getType());
                                if(c != x_coor_neighbour && c != x_coor && y_temp != y_coor_neighbour && y_temp != y_coor)
                                {
                                    roadCell =  cells.get(y_temp).getCellList().get(c);
                                    roadCell.setX(c);
                                    roadCell.setY(y_temp);
                                    cellLink.addIntermediateCell(roadCell);
                                }
                            }else{
                                // When a cell is a surrounding point, it needs to be added to the intermediate point list
                                // of a link.
                                if(cells.get(y_temp).getCellList().get(c).getType().equals("surrounding_point") == true  && c != x_coor_neighbour && c != x_coor && y_temp != y_coor_neighbour && y_temp != y_coor)
                                {
                                    roadCell =  cells.get(y_temp).getCellList().get(c);
                                    roadCell.setX(c);
                                    roadCell.setY(y_temp);
                                    cellLink.addIntermediateCell(roadCell);
                                }
                            }
                            if (y_temp != y_coor) {
                                if(c == x_coor)
                                {
                                    c = x_coor-unitWorld;
                                }
                                y_temp++;
                            }
                        }
                    } else if (y_coor_neighbour == y_coor) {
                        for (int c = x_coor_neighbour; c < x_coor; c = c + unitWorld) {
                            if (cells.get(y_coor_neighbour).getCellList().get(c).getType().equals("surrounding_point") == false && cells.get(y_coor_neighbour).getCellList().get(c).getType().equals("spot") == false) {
                                cells.get(y_coor_neighbour).getCellList().get(c).setType("road");
                                cells.get(y_coor_neighbour).getCellList().get(c).setSpecific(point.getType());
                                if(c != x_coor_neighbour && c != x_coor)
                                {
                                    roadCell =  cells.get(y_coor_neighbour).getCellList().get(c);
                                    roadCell.setX(c);
                                    roadCell.setY(y_coor_neighbour);
                                    cellLink.addIntermediateCell(roadCell);
                                }
                            }
                            if (cells.get(y_coor_neighbour).getCellList().get(c).getType().equals("surrounding_point") == true && c != x_coor_neighbour && c != x_coor)
                            {
                                roadCell =  cells.get(y_coor_neighbour).getCellList().get(c);
                                roadCell.setX(c);
                                roadCell.setY(y_coor_neighbour);
                                cellLink.addIntermediateCell(roadCell);
                            }
                        }
                    } else { //y_coor_neighbour > y_coor
                        int y_temp = y_coor_neighbour;
                        for (int c = x_coor_neighbour; c <= x_coor; c = c + unitWorld) {
                            if (cells.get(y_temp).getCellList().get(c).getType().equals("surrounding_point") == false && cells.get(y_temp).getCellList().get(c).getType().equals("spot") == false) {
                                cells.get(y_temp).getCellList().get(c).setType("road");
                                cells.get(y_temp).getCellList().get(c).setSpecific(point.getType());

                                if(c != x_coor_neighbour && c != x_coor && y_temp != y_coor_neighbour && y_temp != y_coor)
                                {
                                    roadCell =  cells.get(y_temp).getCellList().get(c);
                                    roadCell.setX(c);
                                    roadCell.setY(y_temp);
                                    cellLink.addIntermediateCell(roadCell);
                                }

                            }
                            if(cells.get(y_temp).getCellList().get(c).getType().equals("surrounding_point") == true && c != x_coor_neighbour && c != x_coor && y_temp != y_coor_neighbour && y_temp != y_coor)
                            {
                                roadCell =  cells.get(y_temp).getCellList().get(c);
                                roadCell.setX(c);
                                roadCell.setY(y_temp);
                                cellLink.addIntermediateCell(roadCell);
                            }
                            if (y_temp != y_coor) {
                                if(c == x_coor)
                                {
                                    c = x_coor-unitWorld;
                                }
                                y_temp--;
                            }
                        }
                    }
                } else if (x_coor_neighbour == x_coor) {
                    if (y_coor_neighbour < y_coor) {
                        for (int c = y_coor_neighbour; c < y_coor; c = c + unitWorld) {
                            if (cells.get(c).getCellList().get(x_coor).getType().equals("surrounding_point") == false && cells.get(c).getCellList().get(x_coor).getType().equals("spot")== false) {
                                cells.get(c).getCellList().get(x_coor).setType("road");
                                cells.get(c).getCellList().get(x_coor).setSpecific(point.getType());


                                if(c != y_coor_neighbour && c != y_coor)
                                {
                                    roadCell =  cells.get(c).getCellList().get(x_coor);
                                    roadCell.setX(x_coor);
                                    roadCell.setY(c);
                                    cellLink.addIntermediateCell(roadCell);
                                }
                            }
                            if(cells.get(c).getCellList().get(x_coor).getType().equals("surrounding_point") == true && c != y_coor_neighbour && c != y_coor)
                            {
                                roadCell = cells.get(c).getCellList().get(x_coor);
                                roadCell.setX(x_coor);
                                roadCell.setY(c);
                                cellLink.addIntermediateCell(roadCell);
                            }
                        }
                    } else if (y_coor_neighbour == y_coor) {
                        // This point has itself as neighbour, this is not allowed.
                        //System.out.println("!! Cell has itselfs as neighbour, adjust map so avoid this instance !!");

                    } else {
                        for (int c = y_coor_neighbour; c > y_coor; c = c - unitWorld) {

                            if (cells.get(c).getCellList().get(x_coor).getType().equals("surrounding_point") == false && cells.get(c).getCellList().get(x_coor).getType().equals("spot") == false) {
                                cells.get(c).getCellList().get(x_coor).setType("road");
                                cells.get(c).getCellList().get(x_coor).setSpecific(point.getType());

                                if(c != y_coor_neighbour && c != y_coor)
                                {
                                    roadCell =  cells.get(c).getCellList().get(x_coor);
                                    roadCell.setX(x_coor);
                                    roadCell.setY(c);
                                    cellLink.addIntermediateCell(roadCell);
                                }
                            }
                            if(cells.get(c).getCellList().get(x_coor).getType().equals("surrounding_point") == true && c != y_coor_neighbour && c != y_coor)
                            {
                                roadCell =  cells.get(c).getCellList().get(x_coor);
                                roadCell.setX(x_coor);
                                roadCell.setY(c);
                                cellLink.addIntermediateCell(roadCell);
                            }
                        }
                    }
                } else {
                    if (y_coor_neighbour < y_coor) {
                        int y_temp = y_coor_neighbour;
                        for (int c = x_coor_neighbour; c >= x_coor; c = c - unitWorld) {
                            if (cells.get(y_temp).getCellList().get(c).getType().equals("surrounding_point") == false && cells.get(y_temp).getCellList().get(c).getType().equals("spot") == false) {
                                cells.get(y_temp).getCellList().get(c).setType("road");
                                cells.get(y_temp).getCellList().get(c).setSpecific(point.getType());

                                if(c != x_coor_neighbour && c != x_coor && y_temp != y_coor_neighbour && y_temp != y_coor)
                                {
                                    roadCell =  cells.get(y_temp).getCellList().get(c);
                                    roadCell.setX(c);
                                    roadCell.setY(y_temp);
                                    cellLink.addIntermediateCell(roadCell);
                                };
                            }
                            if (cells.get(y_temp).getCellList().get(c).getType().equals("surrounding_point") == true && c != x_coor_neighbour && c != x_coor && y_temp != y_coor_neighbour && y_temp != y_coor)
                            {
                                roadCell =  cells.get(y_temp).getCellList().get(c);
                                roadCell.setX(c);
                                roadCell.setY(y_temp);
                                cellLink.addIntermediateCell(roadCell);
                            }
                            if (y_temp != y_coor) {
                                if(c == x_coor)
                                {
                                    c = x_coor+unitWorld;
                                }
                                y_temp++;
                            }
                        }
                    } else if (y_coor_neighbour == y_coor) {
                        for (int c = x_coor_neighbour; c > x_coor; c = c - unitWorld) {
                            if (cells.get(y_coor_neighbour).getCellList().get(c).getType().equals("surrounding_point") == false && cells.get(y_coor_neighbour).getCellList().get(c).getType().equals("spot") == false) {
                                cells.get(y_coor_neighbour).getCellList().get(c).setType("road");
                                cells.get(y_coor_neighbour).getCellList().get(c).setSpecific(point.getType());

                                if(c != x_coor_neighbour && c != x_coor)
                                {
                                    roadCell =  cells.get(y_coor_neighbour).getCellList().get(c);
                                    roadCell.setX(c);
                                    roadCell.setY(y_coor_neighbour);
                                    cellLink.addIntermediateCell(roadCell);
                                }
                            }

                            if(cells.get(y_coor_neighbour).getCellList().get(c).getType().equals("surrounding_point") == true &&  c != x_coor_neighbour && c != x_coor)
                            {
                                roadCell =  cells.get(y_coor_neighbour).getCellList().get(c);
                                roadCell.setX(c);
                                roadCell.setY(y_coor_neighbour);
                                cellLink.addIntermediateCell(roadCell);
                            }
                        }
                    } else {
                        int y_temp = y_coor_neighbour;
                        for (int c = x_coor_neighbour; c >= x_coor; c = c - unitWorld) {
                            if (cells.get(y_temp).getCellList().get(c).getType().equals("surrounding_point") == false && cells.get(y_temp).getCellList().get(c).getType().equals("spot") == false) {
                                cells.get(y_temp).getCellList().get(c).setType("road");
                                cells.get(y_temp).getCellList().get(c).setSpecific(point.getType());

                                if(c != x_coor_neighbour && c != x_coor && y_temp != y_coor_neighbour && y_temp != y_coor)
                                {
                                    roadCell =  cells.get(y_temp).getCellList().get(c);
                                    roadCell.setX(c);
                                    roadCell.setY(y_temp);
                                    cellLink.addIntermediateCell(roadCell);
                                }
                            }
                            if(cells.get(y_temp).getCellList().get(c).getType().equals("surrounding_point") == true && c != x_coor_neighbour && c != x_coor && y_temp != y_coor_neighbour && y_temp != y_coor)
                            {
                                roadCell =  cells.get(y_temp).getCellList().get(c);
                                roadCell.setX(c);
                                roadCell.setY(y_temp);
                                cellLink.addIntermediateCell(roadCell);
                            }

                            if (y_temp != y_coor) {
                                if(c == x_coor)
                                {
                                    c = x_coor+unitWorld;
                                }
                                y_temp--;
                            }
                        }
                    }
                }
                cellLink = cotrolOrderLink(cellLink);
                cellLinks.add(cellLink);
            }
        }
        System.out.println("---- World is ready ----");
        /*
        System.out.println("---- Print cellLinks ----");

        for(int i = 0;i < cellLinks.size(); i++)

        {
            System.out.println("Cell index is "+i);
            cellLinks.get(i).print();
        }
        */
    }

    /**
     * Start a delivery
     *
     * @param progress
     * @return
     */
    public int[] startDelivery(int progress) {
       /* int pointA = 0;
        int pointB = 1;
        int pointC = 2;
        int totalDistance = 0;

        Cell cellA = getCellofPointName(pointA);
        Cell cellB = getCellofPointName(pointB);
        Cell cellC  = getCellofPointName(pointC);

        if (cellA == null || cellB == null || cellC == null) {
            System.out.println("No delivery available because one or two points are none existing.");
            return null;
        }

        System.out.println("cellA x " + cellA.getX() + " y " + cellA.getY());
        System.out.println("cellB x " + cellB.getX() + " y " + cellB.getY());
        System.out.println("cellC x " + cellC.getX() + " y " + cellC.getY());

        totalDistance = totalDistance + getDistancePoints(cellA,cellB);
        totalDistance = totalDistance + getDistancePoints(cellB,cellC);
        System.out.println("TOTAL DISTANCE "+totalDistance);

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
        int total_distance = distance_1 + distance_2;
        int progress_distance = total_distance * progress / 100;
        if (progress_distance < distance_1) {
            x = progress_distance;
        } else {
            x = 50;
            y = progress_distance - 50;
        }*/
        int[] distance = new int[2];
        //getDistancePoints();
        distance[0] = 0;
        distance[1] = 0;
        return distance;
    }

    /**
     * Returns the coordinates in this world of a specific point.
     * @param pointName
     * @return
     */
    public Cell getCellofPointName(int pointName) {
        Cell cell = null;
        for (int i = 0; i < dimensionX; i++) {
            for (int j = 0; j < dimensionY; j++) {

                int spot_ID = cells.get(j).getCellList().get(i).getSpotID();
                if (spot_ID == pointName) {
                    cell = cells.get(j).getCellList().get(i);
                }
            }
        }
        // coordinates[0];
        return cell;
    }

    /**
     * Returns the distance between two points on the map.
     * @return
     */

    public int[] getDistancePoints(List<Integer> jobs, int progress)
    {
        int distance = 0;

        // List<Integer> listLinkIDs = getLinkList(jobList);
        Integer listLinkIDs = getLinkIDofStartEnd(jobs.get(0), jobs.get(1));
        /*
        for(int i = 0; i < jobs.size(); i = i+2)
        {
           // System.out.println("getLinkIDofStartEnd start "+jobs.get(i) +" end "+jobs.get(i+1) );

            listLinkIDs.add(getLinkIDofStartEnd(jobs.get(i), jobs.get(i+1)));
        }
*/
        /*
        for(int i = 0; i < listLinkIDs.size(); i++)
        {
             System.out.println("ID of link list "+listLinkIDs.get(i));
        }
        */

        // For the total distance there should be one extra point added for endpoint.
        // One because the startpoint is also seen as one distance.
        int totalDistance = cellLinks.get(listLinkIDs).sizeIntermediateCells();
        //for(int i = 0 ; i < listLinkIDs.size() ; i++)
        //{
            // totalDistance = totalDistance+cellLinks.get(listLinkIDs).sizeIntermediateCells();
       // }

        // Current distance progress (remeber progress = 100,0%)
        int intermediateDistance = totalDistance*progress/100;
        // System.out.println("Total distance "+totalDistance + " current distance "+distance);

        // Remove the endpoints' size of the distance
       // int intermediateDistance = distance;// - listLinkIDs.size();
        if(intermediateDistance < 0)
        {
            intermediateDistance = 0;
        }
        int distance_temp = 0;
        Cell currentCell = null;
        int x = 0;
        int y = 0;
        //for(int l = 0 ; l< listLinkIDs.size() ; l++)
       // {
            // Add the the total distance of a link to temp_distance
            // Plus one for the start and last endPoint
            distance_temp = distance_temp + cellLinks.get(listLinkIDs).sizeIntermediateCells()+1;
            // System.out.println("Cell link start "+ cellLinks.get(listLinkIDs.get(l)).getStartCell().getSpotID() + "size intermediate "+ cellLinks.get(listLinkIDs.get(l)).sizeIntermediateCells() +" end "+cellLinks.get(listLinkIDs.get(l)).getEndCell().getSpotID() +" progress "+progress +" distance_temp "+distance_temp);
            // If intermediate distance is lower than distance_temp than this is current x and y are set in this link.
           // if(intermediateDistance <= distance_temp)
           // {
                // Distance from the start of this link
                //    int distance_start = distance_temp-cellLinks.get(listLinkIDs).sizeIntermediateCells();
                // Cell index
                int cellIndexOfLink = intermediateDistance;// - distance_start;


                // System.out.println("listLinkIDs " + listLinkIDs.get(l)+ " cellIndexOfLink " + cellIndexOfLink +" intermediateDistance " +intermediateDistance+" distance_start "+distance_start);
                // If the index is equal to the size of intermediate cells, then the endpoint is reached of
                // of this link
                if(progress == 100)
                {
                    currentCell = cellLinks.get(listLinkIDs).getEndCell();
                }else {
                    if ((cellIndexOfLink == 0)) {
                        currentCell = cellLinks.get(listLinkIDs).getLinkCells().get(0);
                    } else if (cellIndexOfLink >= cellLinks.get(listLinkIDs).getLinkCells().size()) {
                        // System.out.println(" Size of links "+cellLinks.get(listLinkIDs.get(l)).getLinkCells().size());
                        currentCell = cellLinks.get(listLinkIDs).getEndCell();
                    } else if (cellIndexOfLink < 0) {
                        // System.out.println(" A cellLink smaller the 0 has been calculated. Value is adjusted to start cell.");
                        currentCell = cellLinks.get(listLinkIDs).getStartCell();
                    } else {
                        currentCell = cellLinks.get(listLinkIDs).getLinkCells().get(cellIndexOfLink);
                    }
                }
                x = currentCell.getX();
                y = currentCell.getY();
                // Leave the loop if the x and y values are set for the current progress
                //l = listLinkIDs.size();
           // }
       // }
        int[] coordinates = new int[2];
        coordinates[0] = x;
        coordinates[1] = y;
        //System.out.println("Distance of  intermediate cells "+distance);
        //System.out.println(" x "+currentCell.getX()+" y "+currentCell.getY());
        return coordinates;
    }

    /**
     * Search for the right link of a job's start-and endpoint.
     * It returns the amount of intermediate cells between these two points.
     * @param job
     * @return
     */
    public int getCellDistance(Job job)
    {
        int distance = -1;

        for(int i = 0; i < cellLinks.size() ;i++)
        {
            if(cellLinks.get(i).getStartCell().getSpotID() == Math.toIntExact(job.getIdStart()) && cellLinks.get(i).getEndCell().getSpotID() == Math.toIntExact(job.getIdEnd()))
            {
                distance = cellLinks.get(i).sizeIntermediateCells();
                return distance;
            }
        }
        return distance;
    }

    /**
     * Stores the id of the links needed for a specific job in a list.
     * @param jobList
     * @return
     */
    public List<Integer> getLinkList(JobList jobList)
    {
        List<Integer> listLinkIds = new ArrayList<Integer>();
        List< Job> jobs = jobList.getJobs();
        for(int i = 0; i < jobs.size() ;i++)
        {
            for(int c = 0; c < cellLinks.size() ;c++)
            {
                if(cellLinks.get(c).getStartCell().getSpotID() == jobs.get(i).getIdStart() && cellLinks.get(c).getEndCell().getSpotID() == jobs.get(i).getIdEnd())
                {
                    listLinkIds.add(c);
                }
            }
        }
        return listLinkIds;
    }


    /**
     * Controls if the cells of a link are in the right order.
     * If not than change the order.
     * @param cellLink
     * @return
     */
    public CellLink cotrolOrderLink(CellLink cellLink)
    {
        // System.out.println("CellLink start "+cellLink.getStartCell().getSpotID() + " end "+cellLink.getEndCell().getSpotID()+ " size links "+cellLink.getLinkCells().size());
        int x_start = cellLink.getStartCell().getX();
        int y_start = cellLink.getStartCell().getY();
        int x_end = cellLink.getEndCell().getX();
        int y_end = cellLink.getEndCell().getY();

        int differenceX_start = Math.abs(x_start - cellLink.getLinkCells().get(0).getX());
        int differenceY_start = Math.abs(y_start - cellLink.getLinkCells().get(0).getY());
        int differenceX_end = Math.abs(x_end - cellLink.getLinkCells().get(cellLink.getLinkCells().size()-1).getX());
        int differenceY_end = Math.abs(y_end - cellLink.getLinkCells().get(cellLink.getLinkCells().size()-1).getY());
        if((differenceX_start  ==  differenceX_end && differenceX_start != 1) || (differenceY_start  ==  differenceY_end &&  differenceY_start!= 1))
        {
            List<Cell> intermediate_temp = new ArrayList<Cell>();
            for(int i = cellLink.getLinkCells().size()-1 ; i > -1;i--)
            {
                intermediate_temp.add(cellLink.getLinkCells().get(i));
            }
            cellLink.setLinkCells(intermediate_temp);

        }
        return cellLink;
    }

    public int getLinkIDofStartEnd(int startID, int endID) {
        for(int i = 0 ;i < cellLinks.size();i++)
        {
            if(cellLinks.get(i).getStartCell().getSpotID() == startID &&  cellLinks.get(i).getEndCell().getSpotID() ==  endID)
            {
                // System.out.println("Retrieve id "+i+" of link with start "+ startID +" and end "+endID);
                return i;
            }
        }
        return -1;
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

    public List<CellLink> getCellLinks() {
        // System.out.println("cellLinks world "+ cellLinks.size());
        return this.cellLinks;
    }

    public void setCellLinks(List<CellLink> cellLinks) {
        this.cellLinks = cellLinks;
    }

    public String getWorld_ID() {
        return world_ID;
    }

    public void setWorld_ID(String world_ID) {
        this.world_ID = world_ID;
    }
}
