package be.uantwerpen.Models;

import be.uantwerpen.model.MyAbstractPersistable;

import javax.persistence.Entity;

/**
 * Created by Revil on 11/05/2017.
 */
@Entity
public class Job extends MyAbstractPersistable<Long> {
    private long idJob;
    private long idStart;
    private long idEnd;
    private long idVehicle;

    public Job() {
        this.idJob = (long)-1;
        this.idStart = (long)-1;
        this.idEnd = (long)-1;
        this.idVehicle = (long)-1;
    }

    public Job(Long idJob, Long idStart, Long idEnd, Long idVehicle){
        this.idJob = idJob;
        this.idStart = idStart;
        this.idEnd = idEnd;
        this.idVehicle = idVehicle;
    }

    public long getIdJob() {
        return idJob;
    }

    public void setIdJob(long idJob) {
        this.idJob = idJob;
    }

    public long getIdStart() {
        return idStart;
    }

    public void setIdStart(long idStart) {
        this.idStart = idStart;
    }

    public long getIdEnd() {
        return idEnd;
    }

    public void setIdEnd(long idEnd) {
        this.idEnd = idEnd;
    }

    public long getIdVehicle() {
        return idVehicle;
    }

    public void setIdVehicle(long idVehicle) {
        this.idVehicle = idVehicle;
    }
}
