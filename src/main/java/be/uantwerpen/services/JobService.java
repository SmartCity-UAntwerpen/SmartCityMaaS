package be.uantwerpen.services;

import be.uantwerpen.Models.Job;
import be.uantwerpen.repositories.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.Convert;
import java.util.List;

/**
 * Created by Revil on 17/05/2017.
 */
@Service
public class JobService {

    @Autowired
    private JobRepository jobRepository;

    public Iterable<Job> findAll() {
        return this.jobRepository.findAll();
    }

    // add function
    public Job saveJob (Job job){
        return jobRepository .save(job);
    }

    public Job findOne(Long id) {
        return this.jobRepository.findOne(id);
    }

   public Job getJob (Long id){
        return jobRepository.findOne(id);
   }

    public void delete(Long id) {
        this.jobRepository.delete(id);
    }

   public List<Job> getAllJobs () {
       return jobRepository.findAll();
   }

   public boolean deleteJob (Long id){
       if(this.getJob(id) == null){
           //could not find job
           return false;
       }
       else {
           jobRepository.delete(id);
           return true;
       }
   }

    public void saveSomeAttributes(Job job) {
        Job tempJob = (((Long)job.getIdJob() == null) ? null : getJob(job.getIdJob()));
        if (tempJob != null){
            tempJob.setIdStart(job.getIdStart());
            tempJob.setIdEnd(job.getIdEnd());
            tempJob.setIdVehicle(job.getIdVehicle());
            jobRepository.save(tempJob);
        }
        else{
            jobRepository.save(job);
        }
    }

   //TODO stuur alle jobs door naar backbone
}
