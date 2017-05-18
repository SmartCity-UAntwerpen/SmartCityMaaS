package be.uantwerpen.services;

import be.uantwerpen.model.Job;
import be.uantwerpen.repositories.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public void save(final Job job){

        this.jobRepository.save(job);
    }

    public Job findOne(Long id) {
        return this.jobRepository.findOne(id);
    }

    public Job getJob (Long id){
        return this.jobRepository.findOne(id);
   }

    public void delete(Long id) {
        this.jobRepository.delete(id);
    }


    public void saveSomeAttributes(Job job) {
        Job tempJob = (((Long)job.getId() == null) ? null : getJob(job.getId()));
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
