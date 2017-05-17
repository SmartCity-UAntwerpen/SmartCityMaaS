package be.uantwerpen.services;

import be.uantwerpen.Models.Job;
import be.uantwerpen.repositories.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Revil on 17/05/2017.
 */
@Service
public class JobService {

    @Autowired
    private JobRepository jobRepository;

    // add function
    public Job saveJob (Job job){
        return jobRepository .save(job);
    }

   public Job getJob (Long id){
        return jobRepository.findOne(id);
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

   //TODO stuur alle jobs door naar backbone
}
