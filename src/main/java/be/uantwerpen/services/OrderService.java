package be.uantwerpen.services;

import be.uantwerpen.model.Job;
import be.uantwerpen.model.JobList;
import be.uantwerpen.repositories.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Revil on 29/05/2017.
 */
@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    public Iterable<JobList> findAll() {
        return this.orderRepository.findAll();
    }

    public void saveOrder(JobList joblist)
    {
        this.orderRepository.save(joblist);
    }


    public void printJobList() {
        System.out.println(" Lijst van Orders afdrukken");
        for (JobList jl: this.orderRepository.findAll()) {
            System.out.println(" Order #" + jl.getId());
            for(int x = 0; x<jl.getJobs().size(); x++) {
                System.out.println("jobID: " + jl.getJobs().get(x).getId() + ";   startPos :" + jl.getJobs().get(x).getIdStart() + ";   endPos :" + jl.getJobs().get(x).getIdEnd() + ";   vehicleID :" + jl.getJobs().get(x).getIdVehicle());
            }
        }
    }
}