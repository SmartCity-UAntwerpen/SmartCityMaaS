package be.uantwerpen.visualization.model;

import be.uantwerpen.model.Job;
import be.uantwerpen.model.JobList;
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


    /**
     * Returns current point on the world of a vehicle that is specified in the job together with its start
     * and end point.
     * The progress is used to determine its and distance on the link between these two points.
     * @return
     */

    public int[] getDistancePoints(List<Integer> jobs, int progress, String type)
    {
        return getDistance(jobs.get(0),jobs.get(1),(double)(progress)/100.0, type);
    }

    public int[] getDistance(int startID, int endID, double progress, String type){
        int startX = 0;
        int startY = 0;

        int endX = 0;
        int endY = 0;

        boolean beginYaxis = true; //start in Y direction
        int startPointId = 0;
        //logger.info("start ID = " + startID + " end ID = " + endID + " progress = "  + progress);
        for(int i = 0; i < points.size(); i++){
            if(points.get(i).getPointName() == startID){
                startX = points.get(i).getPhysicalPoisionX();
                startY = points.get(i).getPhysicalPoisionY();
                startPointId = i;
            }
            if(points.get(i).getPointName() == endID){
                endX = points.get(i).getPhysicalPoisionX();
                endY = points.get(i).getPhysicalPoisionY();

            }

            if( startX != 0 && endX != 0 ) break;

        }
        //logger.info(" Points on map Start: x = "+startX + " y " + startY + " End: x = " + endX + " y = " + endY );

        int[] coordinates = new int[2];
        int distX = (endX - startX);
        int distY = (endY - startY);

        switch (type) {
            case "robot":
                //90 graden
                if(startX != endX && startY != endY) {
                    // control corner direction: check if start has neighbour intersections not to pass
                    if(endID != points.get(startPointId+1).getPointName()){
                        if(points.get(startPointId+1).getPhysicalPoisionY() == endY){
                            beginYaxis = false;
                        }

                    } else if(endID != points.get(startPointId-1).getPointName()){
                        if(points.get(startPointId-1).getPhysicalPoisionY() == endY){
                            beginYaxis = false;
                        }
                    }

                    if (progress <= 0.5) {
                        if(beginYaxis) { // start in Y direction
                            coordinates[0] = startX;
                            coordinates[1] = (int) (startY + (distY * progress * 2));
                        } else { // start in X direction
                            coordinates[0] = (int) (startX + (distX * progress * 2));
                            coordinates[1] = startY;
                        }
                    } else {
                        if(beginYaxis) {
                            coordinates[0] = (int) (endX - (distX * (1 - progress) * 2));
                            coordinates[1] = endY;
                        } else {
                            coordinates[0] = endX;
                            coordinates[1] = (int) (endY - (endY * (1 - progress) * 2));
                        }
                    }
                }
                break;
            case "car":
                // curve with 2 points
                float distXpiece = (float) distX/8;
                float distYpiece = (float) distY/8;
                double pointX;
                double pointY;
                // LENGTH OF SEPERATE LINE PIECES (Pythagoras):
                double line1 =  Math.sqrt(Math.pow(Math.abs(5*distXpiece), 2) +  Math.pow(Math.abs(distYpiece),2));
                double line2 =  Math.sqrt(Math.pow(Math.abs(2*distXpiece), 2) +  Math.pow(Math.abs(2*distYpiece),2));
                double line3 =  Math.sqrt(Math.pow(Math.abs(distXpiece), 2) +  Math.pow(Math.abs(5*distYpiece),2));
                // HOW MUCH PERCENT OF TOTAL LINE IS THIS LINE:
                double totalLine = line1 + line2 + line3;
                double line1prog = line1/totalLine;
                double line2prog = line2/totalLine;
                double line3prog = line3/totalLine;

                if(startID < endID) { // neighbour to the right (map is going to draw from 1st ID to last)
                    if (progress < line1prog) {
                        //progress = progress * (1 / 0.625); // change progress to between start and first kink: 0-0.625 TO 0-100
                        progress = progress * (1/line1prog);
                        pointX = 5 * distXpiece; // 5/8
                        pointY = distYpiece; // 1/8
                    } else if (progress < (line1prog+line2prog)) {
                        startX = (int) (startX + (5 * distXpiece));
                        startY = (int) (startY + distYpiece);
                        progress = (progress-line1prog)*(1/line2prog); // change progress to between the two kinks: 0.625-0.875 TO 0-100
                        pointX = (2 * distXpiece); // 7/8
                        pointY = (2 * distYpiece); // 3/8
                    } else {
                        startX = (int)(startX + (7 * distXpiece));
                        startY = (int)(startY + (3 * distYpiece));
                        progress = (progress-(line1prog+line2prog))*(1/line3prog);
                        pointX = distXpiece; // 8/8
                        pointY = (5 * distYpiece); // 8/8
                    }
                } else {
                    if (progress < line3prog) {
                        progress = progress  * (1/line3prog);
                        pointX = (distXpiece);
                        pointY = (5 * distYpiece);
                    } else if (progress < (line3prog+line2prog)) {
                        startX = (int)(startX + distXpiece);
                        startY = (int)(startY + (5*distYpiece));
                        progress = (progress-line3prog)*(1/line2prog);
                        pointX = (2 * distXpiece);
                        pointY = (2 * distYpiece);
                    } else {
                        startX = (int)(startX + (3 * distXpiece));
                        startY = (int)(startY + (7 * distYpiece));
                        progress = (progress-(line3prog+line2prog))*(1/line1prog);
                        pointX = 5 * distXpiece; // 8/8
                        pointY = distYpiece; // 8/8
                    }
                }
                coordinates[0] = (int)(startX + (pointX * progress));
                coordinates[1] = (int)(startY + (pointY * progress));
                break;

            default:
                coordinates[0] = (int)(startX + (distX * progress));
                coordinates[1] = (int)(startY + (distY * progress));
                break;
        }
        //logger.info(" Progress Distance = x " +  coordinates[0] + " y = " + coordinates[1]);
        return coordinates;
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
