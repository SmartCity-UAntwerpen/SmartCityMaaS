package be.uantwerpen.services;

import be.uantwerpen.model.JobList;
import be.uantwerpen.repositories.JobListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Revil on 29/05/2017.
 */
@Service
public class JobListService {

    @Autowired
    private JobListRepository jobListRepository;

    public Iterable<JobList> findAll() {
        return this.jobListRepository.findAll();
    }

    public void saveOrder(JobList joblist)
    {
        this.jobListRepository.save(joblist);
    }


    public void printJobList() {
        System.out.println(" Lijst van Orders afdrukken");
        for (JobList jl: this.jobListRepository.findAll()) {
            System.out.println(" Order #" + jl.getId());
            for(int x = 0; x<jl.getJobs().size(); x++) {
                System.out.println("jobID: " + jl.getJobs().get(x).getId() + ";   startPos :" + jl.getJobs().get(x).getIdStart() + ";   endPos :" + jl.getJobs().get(x).getIdEnd() + ";   vehicleID :" + jl.getJobs().get(x).getIdVehicle());
            }
        }
    }
}