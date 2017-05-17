package be.uantwerpen.Models;

import be.uantwerpen.model.MyAbstractPersistable;

import javax.persistence.*;

/**
 * Created by Revil on 11/05/2017.
 */
@Entity
public class Job extends MyAbstractPersistable<Long> {

    private long idJob;
    private long idStart;
    private long idEnd;
    private long idVehicle;

    public Job () {

    }
    /*public Job() {
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
    }*/


 /*   @Column(name = "jid")
    @GeneratedValue(strategy = GenerationType.AUTO)
    //TODO nog nakijken voor duplicate ID's!
    public long getIdJob() {
        return idJob;
    }

    public void setIdJob(long idJob) {
        this.idJob = idJob;
    }
*/
    @Basic
    @Column(name = "start_direction")
    public long getIdStart() {
        return idStart;
    }

    public void setIdStart(long idStart) {
        this.idStart = idStart;
    }

    @Basic
    @Column(name = "stop_direction")
    public long getIdEnd() {
        return idEnd;
    }

    public void setIdEnd(long idEnd) {
        this.idEnd = idEnd;
    }

    @Basic
    @Column(name = "vehicleID")
    public long getIdVehicle() {
        return idVehicle;
    }

    public void setIdVehicle(long idVehicle) {
        this.idVehicle = idVehicle;
    }
}
