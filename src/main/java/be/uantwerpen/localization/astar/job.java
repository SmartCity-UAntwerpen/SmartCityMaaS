package be.uantwerpen.localization.astar;

/**
 * Created by Revil on 10/05/2017.
 */
public class job {

    private String startpos;
    private String endPos;
    private long idVehicle;
    private int amountOfPassengers;             // amount of persons
    private int waitTime;           // in minutes

    public job (){
        this.startpos = "start";
        this.endPos = "end";
        this.idVehicle = 123456789;
        this.amountOfPassengers = 0;
        this.waitTime = 0;
    }

    public job (String startpos, String endPos, long idVehicle, int amountOfPassengers, int waitTime) {
        this.startpos = startpos;
        this.endPos = endPos;
        this.idVehicle = idVehicle;
        this.amountOfPassengers = amountOfPassengers;
        this.waitTime = waitTime;
    }

    public String getStartpos() {
        return startpos;
    }

    public void setStartpos(String startpos) {
        this.startpos = startpos;
    }

    public String getEndPos() {
        return endPos;
    }

    public void setEndPos(String endPos) {
        this.endPos = endPos;
    }

    public long getIdVehicle() {
        return idVehicle;
    }

    public void setIdVehicle(long idVehicle) {
        this.idVehicle = idVehicle;
    }

    public int getAmountOfPassengers() {
        return amountOfPassengers;
    }

    public void setAmountOfPassengers(int amountOfPassengers) {
        this.amountOfPassengers = amountOfPassengers;
    }

    public int getWaitTime() {
        return waitTime;
    }

    public void setWaitTime(int waitTime) {
        this.waitTime = waitTime;
    }
}
