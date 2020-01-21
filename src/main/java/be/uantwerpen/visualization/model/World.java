package be.uantwerpen.visualization.model;

import be.uantwerpen.sc.models.Job;
import be.uantwerpen.sc.models.JobList;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.DoubleSummaryStatistics;
import java.util.List;

/**
 * Created by Frédéric Melaerts on 10/05/2017.
 *
 * The world contains the total physical map of the smartcity and is used to represent the smartcity
 * on the MaaS webapp.
 * The world is a list of CellRow that results in a matrix where avery cell has a specific x- and y-index.
 * The dimensions od the world and the unit of the world define the total width and length of the physical world.
 * The unitMap is the unit used on the received map from the backbone core and must be equal or bigger than the
 * world's unit.
 * The CellLinks is a list that contains the cell that are part of a link between two points.
 * Both start and end point are part of a cellLink.
 *
 *
 */
public class World {
    private static final Logger logger = LogManager.getLogger(World.class);

    private String world_ID;
    private List<CellRow> cells;
    private int dimensionX;
    private int dimensionY;
    private int unitWorld;
    private int unitMap;
    private List<CellLink> cellLinks;
    public List<Link> links;
    public List<Link> droneLinks;
    public List<Link> carLinks;

    // Defines the diameter of cells around a location point.
    private int surround_layer = 1;

    private List<DummyPoint> points;

    /**
     * Constructor of the world with specified world dimensions.
     * @param dimensionX
     * @param dimensionY
     */
    public World(int dimensionX, int dimensionY) {
        this.dimensionX = 0;
        this.dimensionY = 0;
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


    public int[] getDistance(int mapId, int startX, int startY, int endX, int endY, int startID, int endID, double progress){

        boolean beginYaxis = true; //start in Y direction

        int[] coordinates = new int[2];
        int distX = (endX - startX);
        int distY = (endY - startY);

        switch (mapId) {
            case 1: //RACECAR
                if (startID < endID && (startID + 1) < endID) {
                    Tile inter = searchForIntermediatePoint(mapId, startID, endID);
                    if (inter != null) {
                        if (progress >= 0.5) {
                            if (inter.y < startY) {
                                coordinates[0] = (int)(inter.x + ((endX - inter.x) * progress));
                                coordinates[1] = (int)(inter.y - ((endY - inter.y) * progress));
                            } else {
                                coordinates[0] = (int)(inter.x + ((endX - inter.x) * progress));
                                coordinates[1] = (int)(inter.y + ((endY - inter.y) * progress));
                            }
                        } else {
                            if (inter.y < startY) {
                                coordinates[0] = (int)(startX + ((inter.x - startX) * progress));
                                coordinates[1] = (int)(startY - ((inter.y - startY) * progress));
                            } else {
                                coordinates[0] = (int)(startX + ((inter.x - startX) * progress));
                                coordinates[1] = (int)(startY + ((inter.y - startY) * progress));
                            }


                        }
                    }
                    else {
                        coordinates[0] = (int)(startX + (distX * progress));
                        coordinates[1] = (int)(startY + (distY * progress));
                    }
                } else if (startID > endID && (endID + 1) < startID) {
                    Tile inter = searchForIntermediatePoint(mapId, startID, endID);
                    if (inter != null) {
                        if (progress >= 0.5) {
                            if (inter.x < startY) {
                                coordinates[0] = (int)(inter.x - ((endX - inter.x) * progress));
                                coordinates[1] = (int)(inter.y + ((endY - inter.y) * progress));
                            } else {
                                coordinates[0] = (int)(inter.x + ((endX - inter.x) * progress));
                                coordinates[1] = (int)(inter.y + ((endY - inter.y) * progress));
                            }
                        } else {
                            if (inter.x < startY) {
                                coordinates[0] = (int)(startX - ((inter.x - startX) * progress));
                                coordinates[1] = (int)(startY + ((inter.y - startY) * progress));
                            } else {
                                coordinates[0] = (int)(startX + ((inter.x - startX) * progress));
                                coordinates[1] = (int)(startY + ((inter.y - startY) * progress));
                            }


                        }
                    }
                    else {
                        coordinates[0] = (int)(startX + (distX * progress));
                        coordinates[1] = (int)(startY + (distY * progress));
                    }
                } else {
                    coordinates[0] = (int)(startX + (distX * progress));
                    coordinates[1] = (int)(startY + (distY * progress));
                }
                break;
            default:
                coordinates[0] = (int)(startX + (distX * progress));
                coordinates[1] = (int)(startY + (distY * progress));
                break;
        }

        logger.info(" Progress Distance = x " +  coordinates[0] + " y = " + coordinates[1]);

        return coordinates;
    }

    public Tile searchForIntermediatePoint(int mapId, int startID, int endID){

        ArrayList<Tile> tempPoints = new ArrayList<>();
        switch (mapId) {
            case 1: //RACECAR
                Tile intermediateStart = new Tile();
                Tile intermediateEnd = new Tile();
                boolean startFound = false;
                boolean endFound = false;
                for (int i = 0; i < carLinks.size(); i++) {
                    Link link = carLinks.get(i);
                    if (Integer.parseInt(link.destination.pointName) == endID || Integer.parseInt(link.destination.pointName) == startID || Integer.parseInt(link.start.pointName) == startID || Integer.parseInt(link.start.pointName) == endID) {
                        if (Integer.parseInt(link.destination.pointName) == endID || Integer.parseInt(link.destination.pointName) == startID) {
                            tempPoints.add(link.start);
                        } else {
                            tempPoints.add(link.destination);
                        }
                    }
                    }
                if (tempPoints.size() > 1) {
                    if (tempPoints.get(0).pointName.equals(tempPoints.get(1).pointName)) {
                        if (tempPoints.get(0).id == null) {
                            return tempPoints.get(1);
                        } else {
                            return tempPoints.get(0);
                        }
                    }
                }

                break;

        }
        return null;
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
        // logger.info("cellLinks world "+ cellLinks.size());
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

    public void setPoints(List<DummyPoint> points){
        for(int i= 0; i < points.size(); i++){
            if(points.get(i).getPhysicalPoisionX() > this.dimensionX){
                this.dimensionX = points.get(i).getPhysicalPoisionX(); 
            }
            if(points.get(i).getPhysicalPoisionY() > this.dimensionY){
                this.dimensionY = points.get(i).getPhysicalPoisionY();
            }
        }
        this.dimensionY += 6;
        this.dimensionX += 6;
        this.points = points;
    }

    public List<DummyPoint> getPoints(){
        return this.points;
    }
}
