package be.uantwerpen.visualization.model;

import be.uantwerpen.model.Job;
import be.uantwerpen.model.JobList;

import java.util.ArrayList;
import java.util.DoubleSummaryStatistics;
import java.util.List;

/**
 * Created by Frédéric Melaerts on 10/05/2017.
 */
public class World {
    private String world_ID;
    private List<CellRow> cells;
    private int dimensionX;
    private int dimensionY;
    private int unitWorld;
    private List<CellLink> cellLinks;
    // Defines the diameter around a location point.
    private int surround_layer = 2;

    public World(int dimensionX, int dimensionY) {
        this.dimensionX = dimensionX;
        this.dimensionY = dimensionY;
        this.unitWorld = 1; //1 meter
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


    public void surroundPoint(int y, int x, String specific, int spotID) {
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
                        } else {
                            cells.get(i).getCellList().get(j).setType("spot");
                            cells.get(i).getCellList().get(j).setSpecific(specific);
                            cells.get(i).getCellList().get(j).setSur_x(x);
                            cells.get(i).getCellList().get(j).setSur_y(y);
                            cells.get(i).getCellList().get(j).setSpotID(spotID);
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
        DummyMap dummyMap = new DummyMap();
        int x_coor = 0;
        int y_coor = 0;
        String specific = "drone";
        System.out.println("Start pasing world");
        //List<DummyPoint> pointsList =  dummyMap.getDummyPoints();
        List<DummyPoint> pointsList = dummyList;
        cellLinks = new ArrayList<CellLink>();
        for (int m = 0; m < pointsList.size(); m++) {
            DummyPoint point = pointsList.get(m);
            x_coor = point.getPhysicalPoisionX() * dummyMap.getUnit();
            y_coor = point.getPhysicalPoisionY() * dummyMap.getUnit();

            // Start point for the link
            Cell startPoint = new Cell(x_coor,y_coor);
            startPoint.setSpotID(point.getPointName());

            //cells.get(y_coor).getCellList().get(x_coor).setType(2);
            surroundPoint(y_coor, x_coor, specific, point.getPointName());
            System.out.println("set road point on column " + x_coor + " and row " + y_coor);
            for (int n = 0; n < point.getNeighbours().size(); n++) {
                CellLink cellLink = new CellLink();
                cellLink.setStartCell(startPoint);



                int neighbourPoint = point.getNeighbours().get(n);
                int x_coor_neighbour = pointsList.get(neighbourPoint).getPhysicalPoisionX() * dummyMap.getUnit();
                int y_coor_neighbour = pointsList.get(neighbourPoint).getPhysicalPoisionY() * dummyMap.getUnit();
                // End point for the link
                Cell endPoint = new Cell(x_coor_neighbour,y_coor_neighbour);
                endPoint.setSpotID(pointsList.get(neighbourPoint).getPointName());
                cellLink.setEndCell(endPoint);

                System.out.println("Check " + neighbourPoint + " real name " + pointsList.get(neighbourPoint).getPointName());
                if (cells.get(y_coor).getCellList().get(x_coor).getType().equals("spot") == false) {
                    cells.get(y_coor_neighbour).getCellList().get(x_coor_neighbour).setType("spot");
                    cells.get(y_coor_neighbour).getCellList().get(x_coor_neighbour).setSpecific(specific);
                    cells.get(y_coor_neighbour).getCellList().get(x_coor_neighbour).setSpotID(point.getPointName());
                    System.out.println(" kuuukelkekuu : " + cells.get(y_coor_neighbour).getCellList().get(x_coor_neighbour).getSpotID());
                }
                double tempPow_x = Math.pow(Math.abs(x_coor - x_coor_neighbour), 2);
                double tempPow_y = Math.pow(Math.abs(y_coor - y_coor_neighbour), 2);
                double temp_interpolate = Math.sqrt(tempPow_x + tempPow_y);
                int interpolate = (int) temp_interpolate;
                Cell roadCell = new Cell();
                if (x_coor_neighbour < x_coor) {
                    if (y_coor_neighbour < y_coor) {
                        int y_temp = y_coor_neighbour;

                        for (int c = x_coor_neighbour; c < x_coor; c = c + unitWorld) {
                            if (cells.get(y_temp).getCellList().get(c).getType().equals("surrounding_point") == false && cells.get(y_temp).getCellList().get(c).getType().equals("spot") == false) {
                                cells.get(y_temp).getCellList().get(c).setType("road");
                                if(c != x_coor_neighbour && c != x_coor && y_temp != y_coor_neighbour && y_temp != y_coor)
                                {
                                    roadCell =  cells.get(c).getCellList().get(y_temp);
                                    cellLink.addIntermediateCell(roadCell);
                                    if(point.getPointName() == 1)
                                    {
                                        System.out.println("SpecialV x"+ c+ " y "+c);
                                    }
                                }
                            }else{
                                if(cells.get(y_temp).getCellList().get(c).getType().equals("surrounding_point") == true  && c != x_coor_neighbour && c != x_coor && y_temp != y_coor_neighbour && y_temp != y_coor)
                                {
                                    roadCell =  cells.get(c).getCellList().get(y_temp);
                                    cellLink.addIntermediateCell(roadCell);
                                    if(point.getPointName() == 1)
                                    {
                                        System.out.println("SpecialV x"+ c+ " y "+c);
                                    }
                                }
                            }
                            if (y_temp != y_coor) {
                                y_temp++;
                            }
                        }
                    } else if (y_coor_neighbour == y_coor) {
                        for (int c = x_coor_neighbour; c < x_coor; c = c + unitWorld) {
                            if (cells.get(y_coor_neighbour).getCellList().get(c).getType().equals("surrounding_point") == false && cells.get(y_coor_neighbour).getCellList().get(c).getType().equals("spot") == false) {
                                cells.get(y_coor_neighbour).getCellList().get(c).setType("road");
                                if(c != x_coor_neighbour && c != x_coor)
                                {
                                    roadCell =  cells.get(c).getCellList().get(y_coor_neighbour);
                                    cellLink.addIntermediateCell(roadCell);
                                    if(point.getPointName() == 1)
                                    {
                                        System.out.println("SpecialZ x"+ c+ " y "+c);
                                    }
                                }
                            }
                            if (cells.get(y_coor_neighbour).getCellList().get(c).getType().equals("surrounding_point") == true && c != x_coor_neighbour && c != x_coor)
                            {
                                roadCell =  cells.get(c).getCellList().get(y_coor_neighbour);
                                cellLink.addIntermediateCell(roadCell);
                                if(point.getPointName() == 1)
                                {
                                    System.out.println("SpecialZ x"+ c+ " y "+c);
                                }
                            }
                        }
                    } else {
                        int y_temp = y_coor_neighbour;
                        for (int c = x_coor_neighbour; c < x_coor; c = c + unitWorld) {
                            if (cells.get(y_temp).getCellList().get(c).getType().equals("surrounding_point") == false && cells.get(y_temp).getCellList().get(c).getType().equals("spot") == false) {
                                cells.get(y_temp).getCellList().get(c).setType("road");
                                if(c != x_coor_neighbour && c != x_coor && y_temp != y_coor_neighbour && y_temp != y_coor)
                                {
                                    roadCell =  cells.get(c).getCellList().get(y_temp);
                                    cellLink.addIntermediateCell(roadCell);
                                }
                                if(point.getPointName() == 1)
                                {
                                    System.out.println("SpecialQ x"+ c+ " y "+c);
                                }
                            }
                            if(cells.get(y_temp).getCellList().get(c).getType().equals("surrounding_point") == true && c != x_coor_neighbour && c != x_coor && y_temp != y_coor_neighbour && y_temp != y_coor)
                            {
                                roadCell =  cells.get(c).getCellList().get(y_temp);
                                cellLink.addIntermediateCell(roadCell);
                                if(point.getPointName() == 1)
                                {
                                    System.out.println("SpecialQ x"+ c+ " y "+c);
                                }
                            }
                            if (y_temp != y_coor) {
                                y_temp--;
                            }
                        }
                    }
                } else if (x_coor_neighbour == x_coor) {
                    if (y_coor_neighbour < y_coor) {
                        for (int c = y_coor_neighbour; c < y_coor; c = c + unitWorld) {
                            if (cells.get(c).getCellList().get(x_coor).getType().equals("surrounding_point") == false && cells.get(c).getCellList().get(x_coor).getType().equals("spot")== false) {
                                cells.get(c).getCellList().get(x_coor).setType("road");

                                if(c != y_coor_neighbour && c != y_coor)
                                {
                                    /*if(point.getPointName() == 1)
                                    {
                                        System.out.println("Special x"+ x_coor + " y "+c);
                                        System.out.println("In roadcellDDD x"+ x_coor+ " y "+c);
                                        roadCell =  cells.get(c).getCellList().get(x_coor);
                                        cellLink.addIntermediateCell(roadCell);

                                    }else
                                    {*/
                                        roadCell =  cells.get(c).getCellList().get(x_coor);
                                        cellLink.addIntermediateCell(roadCell);
                                   // }
                                }
                            }
                            if(cells.get(c).getCellList().get(x_coor).getType().equals("surrounding_point") == true && c != y_coor_neighbour && c != y_coor)
                            {
                                /*if(point.getPointName() == 1)
                                {
                                    System.out.println("Special x"+ x_coor + " y "+c);
                                    System.out.println("In roadcellZZZ x"+ x_coor+ " y "+c);
                                    roadCell =  cells.get(c).getCellList().get(x_coor);
                                    cellLink.addIntermediateCell(roadCell);

                                }else {*/
                                    roadCell = cells.get(c).getCellList().get(x_coor);
                                    cellLink.addIntermediateCell(roadCell);
                                //}
                            }
                        }
                    } else if (y_coor_neighbour == y_coor) {

                    } else {
                        for (int c = y_coor_neighbour; c > y_coor; c = c - unitWorld) {

                            if (cells.get(c).getCellList().get(x_coor).getType().equals("surrounding_point") == false && cells.get(c).getCellList().get(x_coor).getType().equals("spot") == false) {
                                cells.get(c).getCellList().get(x_coor).setType("road");
                                if(point.getPointName() == 1)
                                {
                                    System.out.println("SpecialA x"+ c+ " y "+c);
                                }
                                if(c != y_coor_neighbour && c != y_coor)
                                {
                                    roadCell =  cells.get(x_coor).getCellList().get(c);
                                    cellLink.addIntermediateCell(roadCell);
                                }
                            }
                            if(cells.get(c).getCellList().get(x_coor).getType().equals("surrounding_point") == true && c != y_coor_neighbour && c != y_coor)
                            {
                                if(point.getPointName() == 1)
                                {
                                    System.out.println("SpecialA x"+ c+ " y "+c);
                                }
                                roadCell =  cells.get(x_coor).getCellList().get(c);
                                cellLink.addIntermediateCell(roadCell);
                            }
                        }
                    }
                } else {
                    if (y_coor_neighbour < y_coor) {
                        int y_temp = y_coor_neighbour;
                        for (int c = x_coor_neighbour; c > x_coor; c = c - unitWorld) {
                            if (cells.get(y_temp).getCellList().get(c).getType().equals("surrounding_point") == false && cells.get(y_temp).getCellList().get(c).getType().equals("spot") == false) {
                                cells.get(y_temp).getCellList().get(c).setType("road");
                                if(c != x_coor_neighbour && c != x_coor && y_temp != y_coor_neighbour && y_temp != y_coor)
                                {
                                    roadCell =  cells.get(c).getCellList().get(y_temp);
                                    cellLink.addIntermediateCell(roadCell);
                                };
                                if(point.getPointName() == 1)
                                {
                                    System.out.println("SpecialB x"+ c+ " y "+c);
                                }
                            }
                            if (cells.get(y_temp).getCellList().get(c).getType().equals("surrounding_point") == true && c != x_coor_neighbour && c != x_coor && y_temp != y_coor_neighbour && y_temp != y_coor)
                            {
                                roadCell =  cells.get(c).getCellList().get(y_temp);
                                cellLink.addIntermediateCell(roadCell);
                                if(point.getPointName() == 1)
                                {
                                    System.out.println("SpecialB x"+ c+ " y "+c);
                                }
                            }
                            if (y_temp != y_coor) {
                                y_temp++;
                            }
                        }
                    } else if (y_coor_neighbour == y_coor) {
                        for (int c = x_coor_neighbour; c > x_coor; c = c - unitWorld) {
                            if (cells.get(y_coor_neighbour).getCellList().get(c).getType().equals("surrounding_point") == false && cells.get(y_coor_neighbour).getCellList().get(c).getType().equals("spot") == false) {
                                cells.get(y_coor_neighbour).getCellList().get(c).setType("road");
                                if(c != x_coor_neighbour && c != x_coor)
                                {
                                    roadCell =  cells.get(c).getCellList().get(y_coor_neighbour);
                                    cellLink.addIntermediateCell(roadCell);
                                }
                                if(point.getPointName() == 1)
                                {
                                    System.out.println("SpecialC x"+ c+ " y "+c);
                                }
                            }

                            if(cells.get(y_coor_neighbour).getCellList().get(c).getType().equals("surrounding_point") == true &&  c != x_coor_neighbour && c != x_coor)
                            {
                                roadCell =  cells.get(c).getCellList().get(y_coor_neighbour);
                                cellLink.addIntermediateCell(roadCell);
                                if(point.getPointName() == 1)
                                {
                                    System.out.println("SpecialC x"+ c+ " y "+c);
                                }
                            }
                        }
                    } else {
                        int y_temp = y_coor_neighbour;
                        for (int c = x_coor_neighbour; c > x_coor; c = c - unitWorld) {
                            if (cells.get(y_temp).getCellList().get(c).getType().equals("surrounding_point") == false && cells.get(y_temp).getCellList().get(c).getType().equals("spot") == false) {
                                cells.get(y_temp).getCellList().get(c).setType("road");
                                if(c != x_coor_neighbour && c != x_coor && y_temp != y_coor_neighbour && y_temp != y_coor)
                                {
                                    roadCell =  cells.get(c).getCellList().get(y_temp);
                                    cellLink.addIntermediateCell(roadCell);
                                }
                                if(point.getPointName() == 1)
                                {
                                    System.out.println("SpecialD x"+ c+ " y "+c);
                                }
                            }
                            if(cells.get(y_temp).getCellList().get(c).getType().equals("surrounding_point") == true && c != x_coor_neighbour && c != x_coor && y_temp != y_coor_neighbour && y_temp != y_coor)
                            {
                                roadCell =  cells.get(c).getCellList().get(y_temp);
                                cellLink.addIntermediateCell(roadCell);
                                if(point.getPointName() == 1)
                                {
                                    System.out.println("SpecialD x"+ c+ " y "+c);
                                }
                            }

                            if (y_temp != y_coor) {
                                y_temp--;
                            }
                        }
                    }
                }
                cellLink = cotrolOrderLink(cellLink);
                cellLinks.add(cellLink);
            }
        }
        System.out.println("---- Print cellLinks ----");
        for(CellLink cl: cellLinks)
        {
            cl.print();
        }
        System.out.println("Life is a bitch");
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
    public int[] getDistancePoints(int progress)
    {
        int distance = 0;
        /*
        int x_1 = startCell.getX();
        int y_1 =  startCell.getY();
        int x_2 =  endCell.getX();
        int y_2 = endCell.getY();
        double tempPow_x = Math.pow(Math.abs(x_1 - x_2), 2);
        double tempPow_y = Math.pow(Math.abs(y_1 - y_2), 2);
        Double temp_interpolate = Math.sqrt(tempPow_x + tempPow_y);
        distance = temp_interpolate.intValue();*/

        // Mock object jobList
        List<Job> jobs = new ArrayList<Job>();
        Job job1 = new Job();
        job1.setIdStart(0);
        job1.setIdEnd(1);
        Job job2 = new Job();
        job2.setIdStart(1);
        job2.setIdEnd(2);
        Job job3 = new Job();
        job3.setIdStart(2);
        job3.setIdEnd(0);
        Job job4 = new Job();
        job4.setIdStart(0);
        job4.setIdEnd(3);
        Job job5 = new Job();
        job5.setIdStart(3);
        job5.setIdEnd(1);
        JobList jobList = new JobList();
        jobList.addJob(job1);
        jobList.addJob(job2);
        jobList.addJob(job3);
        jobList.addJob(job4);
        jobList.addJob(job5);
        List<Integer> listLinkIDs = getLinkList(jobList);
        // For the total distance there should be one extra point added for endpoint.
        // One because the startpoint is also seen as one distance.
        int totalDistance = 1;
        for(int i = 0 ; i < listLinkIDs.size() ; i++)
        {
            totalDistance = totalDistance+cellLinks.get(listLinkIDs.get(i)).sizeIntermediateCells()+1;
        }
        // Current distance progress (remeber progress = 100,0%)
        distance = totalDistance*progress/1000;
        // Remove the endpoints' size of the distance
        int intermediateDistance = distance;// - listLinkIDs.size();
        if(intermediateDistance < 0)
        {
            intermediateDistance = 0;
        }
        int distance_temp = 0;
        Cell currentCell = null;
        int x = 0;
        int y = 0;
        for(int l = 0 ; l< listLinkIDs.size() ; l++)
        {
            // Add the the total distance of a link to temp_distance
            // Plus one for the last endPoint
            distance_temp = distance_temp + cellLinks.get(listLinkIDs.get(l)).sizeIntermediateCells()+1;
            if(intermediateDistance <= distance_temp)
            {
                int modulo = intermediateDistance % distance_temp;
                // Distance from the start of this link
                int distance_start = distance_temp-cellLinks.get(listLinkIDs.get(l)).sizeIntermediateCells();
                // Cell index
                int cellIndexOfLink = intermediateDistance - distance_start;


                System.out.println("cellIndexOfLink " + cellIndexOfLink +" intermediateDistance " +intermediateDistance+" distance_start "+distance_start);
                // If the index is equal to the size of intermediate cells, then the endpoint is reached of
                // of this link
                if((cellIndexOfLink == 0))
                {
                    currentCell = cellLinks.get(listLinkIDs.get(l)).getLinkCells().get(0);
                }else if(cellIndexOfLink == cellLinks.get(listLinkIDs.get(l)).getLinkCells().size())
                {
                    System.out.println(" Size of links "+cellLinks.get(listLinkIDs.get(l)).getLinkCells().size());
                    currentCell = cellLinks.get(listLinkIDs.get(l)).getEndCell();
                }else
                {
                    currentCell = cellLinks.get(listLinkIDs.get(l)).getLinkCells().get(cellIndexOfLink);
                }
                x = currentCell.getX();
                y = currentCell.getY();
                // Leave the loop if the x and y values are set for the current progress
                l = listLinkIDs.size();
            }
        }
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

    public void getCoordinatesDistance(int total_distance,int distance, Cell cell1, Cell cell2)
    {
        int x = 0;
        int y = 0;
        int x_high = 0;
        int x_low = 0;
        int y_high = 0;
        int y_low = 0;

        if(cell1.getX()<cell2.getX())
        {
            x_high = cell2.getX();
            x_low = cell1.getX();
        }else
        {
            x_high = cell1.getX();
            x_low = cell2.getX();
        }
        if(cell1.getY()<cell2.getY())
        {
            y_high = cell2.getY();
            y_low = cell1.getY();
        }else
        {
            y_high = cell1.getY();
            y_low = cell2.getY();
        }

        for(int i = 0 ; i < total_distance;i++)
        {
           // if()

        }

        //return
    }

    /**
     * Controls if the cells of a link are in the right order.
     * If not than change the order.
     * @param cellLink
     * @return
     */
    public CellLink cotrolOrderLink(CellLink cellLink)
    {
        int x_start = cellLink.getStartCell().getX();
        int y_start = cellLink.getStartCell().getY();
        int x_end = cellLink.getEndCell().getX();
        int y_end = cellLink.getEndCell().getY();

        int differenceX_start = Math.abs(x_start - cellLink.getLinkCells().get(0).getX());
        int differenceY_start = Math.abs(x_start - cellLink.getLinkCells().get(0).getY());
        int differenceX_end = Math.abs(x_start - cellLink.getLinkCells().get(0).getX());
        int differenceY_end = Math.abs(x_start - cellLink.getLinkCells().get(0).getY());
        if((differenceX_start  ==  differenceX_end && differenceX_start != 0)|| (differenceY_start  ==  differenceY_end &&  differenceY_start!= 0))
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
