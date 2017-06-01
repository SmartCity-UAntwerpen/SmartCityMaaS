package be.uantwerpen.model;

import be.uantwerpen.model.MyAbstractPersistable;

import javax.persistence.*;
import java.util.List;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * Created by Revil on 11/05/2017.
 */
@Entity
//@IdClass(Job.class)
/*@Table(name="jobtable", uniqueConstraints = {
        @UniqueConstraint(columnNames = "start_direction"),
        @UniqueConstraint(columnNames = "stop_direction"),
        @UniqueConstraint(columnNames = "vehicleID"),
        @UniqueConstraint(columnNames = "idOrder")
})*/
public class Job extends MyAbstractPersistable<Long> {


   // private long idJob;
    private long idStart;
    private long idEnd;
    private long idVehicle;
    private String typeVehicle;
    private String Status;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="ID_ORDER")
    private JobList joblist;
    //@ManyToOne(fetch = FetchType.LAZY)
    //@JoinColumn(name = "idOrder", unique = false, nullable = false)
    //@Column(name="idOrder")

    public Job() {

    }


    public Job(Long idStart, Long idEnd, Long idVehicle) {
       // this.idJob = idJob;
        this.idStart = idStart;
        this.idEnd = idEnd;
        this.idVehicle = idVehicle;
    }

    public JobList getJoblist() {
        return joblist;
    }

    public void setJoblist(JobList joblist) {
        this.joblist = joblist;
    }
    //TODO nog nakijken voor duplicate ID's!
/*    //@Id
    //@Column(name = "jid", unique = true, nullable = false)
    //@GeneratedValue(strategy = GenerationType.AUTO)
    //@GeneratedValue(strategy = IDENTITY)
    public long getIdJob() {
        return idJob;
    }

    public void setIdJob(long idJob) {
        this.idJob = idJob;
    }
*/
    //@Basic
//    @Column(name = "start_direction", unique = false, nullable = false)
    public long getIdStart() {
        return idStart;
    }

    public void setIdStart(long idStart) {
        this.idStart = idStart;
    }


    //@Basic
  //  @Column(name = "stop_direction", unique = false, nullable = false)
    public long getIdEnd() {
        return idEnd;
    }

    public void setIdEnd(long idEnd) {
        this.idEnd = idEnd;
    }


    //@Basic
    //@Column(name = "vehicleID", unique = false, nullable = false)
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
    //   @ManyToOne(cascade = CascadeType.REMOVE)
 //   @JoinColumn(name = "OrderID", referencedColumnName = "idOrder")
 /*   public JobList getOrder() {
        return JobList;
    }

    public void setOrder(JobList order) {
        this.order = order;
    }*/
}