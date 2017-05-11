package be.uantwerpen.model;

import be.uantwerpen.Models.Job;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Revil on 11/05/2017.
 */
public class Jobs extends Job {
    //TODO ALLES HERSCHRIJVEN
    private int passengers;

    public Jobs (){
        super();
        this.jobs = new ArrayList<Job>();
        this.passengers = -1;
    }

    public Jobs(Long idJob, Long idStart, Long idEnd, Long idVehicle, int passengers) {
        super(idJob, idStart, idEnd, idVehicle);
        this.jobs = new ArrayList<Jobs>();
        this.passengers = passengers;
    }

    public List<Jobs> getJobs() {
        return jobs;
    }

    public void setJobs(List<Jobs> jobs) {
        this.jobs = jobs;
    }


    public int getPassengers() {
        return passengers;
    }

    public void setPassengers(int passengers) {
        this.passengers = passengers;
    }
}
