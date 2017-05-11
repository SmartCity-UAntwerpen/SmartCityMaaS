package be.uantwerpen.model;

import be.uantwerpen.Models.Job;

/**
 * Created by Revil on 11/05/2017.
 */
public class Jobs extends Job {
    private long weightToStart;
    private int passengers;

    public Jobs (){
        super();
        this.weightToStart = (long)-1;
        this.passengers = -1;
    }

    public Jobs(Long idJob, Long idStart, Long idEnd, Long idVehicle,Long weightToStart, int passengers) {
        super(idJob, idStart, idEnd, idVehicle);
        this.weightToStart = weightToStart;
        this.passengers = passengers;
    }

    public long getWeightToStart() {
        return weightToStart;
    }

    public void setWeightToStart(long weightToStart) {
        this.weightToStart = weightToStart;
    }

    public int getPassengers() {
        return passengers;
    }

    public void setPassengers(int passengers) {
        this.passengers = passengers;
    }
}
