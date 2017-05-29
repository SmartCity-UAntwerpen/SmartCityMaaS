package be.uantwerpen.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Revil on 29/05/2017.
 */
@Entity
public class Order extends MyAbstractPersistable<Long> {

    @OneToMany(mappedBy = "order")
    private List<Job> jobs;
    @Column(name="SP", unique = false, nullable = false)
    private String startPoint;
    @Column(name = "EP", unique = false, nullable = false)
    private String endPoint;

    public void addJob(Job job)
    {
        jobs.add(job);
    }

    public Order() {
        this.jobs = new ArrayList<Job>();
    }

    public Order(List<Job> jobs) {
        this.jobs = jobs;
    }

    public List<Job> getJobs() {
        return jobs;
    }

    public void setJobs(List<Job> jobs) {
        this.jobs = jobs;
    }

    public String getStartPoint() {
        return startPoint;
    }

    public void setStartPoint(String startPoint) {
        this.startPoint = startPoint;
    }

    public String getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(String endPoint) {
        this.endPoint = endPoint;
    }
}
