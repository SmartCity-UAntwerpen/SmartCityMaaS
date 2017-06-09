package be.uantwerpen.services;

import be.uantwerpen.model.Job;
import be.uantwerpen.repositories.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service for the Job class. All interactions will be defined here required to perform the methods for the Job class
 * @version 2.2 17 may 2017
 * @author Oliver Nyssen, Kevin Van Tendeloo
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

    /**
     * Function to save all relevant information concernting the Job class. This is a standard function for service usage
     * @param job   (Job) class of which all information needs to be saved
     */
    public void saveSomeAttributes(Job job) {
        Job tempJob = (((Long)job.getId() == null) ? null : getJob(job.getId()));
        if (tempJob != null){
            tempJob.setIdStart(job.getIdStart());
            tempJob.setIdEnd(job.getIdEnd());
            tempJob.setIdVehicle(job.getIdVehicle());
            tempJob.setTypeVehicle(job.getTypeVehicle());
            tempJob.setStatus(job.getStatus());
            jobRepository.save(tempJob);
        }
        else{
            jobRepository.save(job);
        }
    }
}
