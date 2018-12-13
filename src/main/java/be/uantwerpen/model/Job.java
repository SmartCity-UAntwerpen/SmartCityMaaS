package be.uantwerpen.model;

import be.uantwerpen.model.MyAbstractPersistable;

import javax.persistence.*;
import java.util.List;

import static javax.persistence.GenerationType.IDENTITY;

/**
 *  NV 2018
 */
@Entity

public class Job extends MyAbstractPersistable<Long> {


    private long idStart;
    private long idEnd;
    private long idVehicle;
    private String typeVehicle;
    private String Status;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="ID_ORDER")
    private JobList joblist;


    public Job() {

    }


    public Job(Long idStart, Long idEnd, Long idVehicle) {
        this.idStart = idStart;
        this.idEnd = idEnd;
        this.idVehicle = idVehicle;
    }

    public String toString(){
        return "[ start: "+idStart+" - end:"+idEnd+" ] vehicle:" + idVehicle;
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

    public String getTypeVehicle() {
        return typeVehicle;
    }

    public void setTypeVehicle(String typeVehicle) {
        this.typeVehicle = typeVehicle;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

}