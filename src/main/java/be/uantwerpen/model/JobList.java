package be.uantwerpen.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Revil on 29/05/2017.
 */
@Entity
public class JobList extends MyAbstractPersistable<Long>{

    @OneToMany
    @JoinColumn(name="ID_ORDER", referencedColumnName="ID")
    private List<Job> jobs;
    @Column(name="SP", unique = false, nullable = false)
    private long startPoint;
    @Column(name = "EP", unique = false, nullable = false)
    private long endPoint;

    public void addJob(Job job)
    {
        jobs.add(job);
    }

    public JobList() {
        this.jobs = new ArrayList<Job>();
        this.startPoint = -1;
        this.endPoint = -1;
    }

    public JobList(List<Job> jobs) {
        this.jobs = jobs;
    }

    public boolean isEmpty() {
        if (jobs.isEmpty() == true){
            return true;
        }
        else {
            return false;
        }
    }

    public int size() {
        return jobs.size();
    }
    public List<Job> getJobs() {
        return jobs;
    }

    public void setJobs(List<Job> jobs) {
        this.jobs = jobs;
    }

    public long getStartPoint() {
        return startPoint;
    }

    public void setStartPoint(long startPoint) {
        this.startPoint = startPoint;
    }

    public long getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(long endPoint) {
        this.endPoint = endPoint;
    }
}
