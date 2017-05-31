package be.uantwerpen.localization.astar;

import be.uantwerpen.model.Job;
import be.uantwerpen.model.Link;
import be.uantwerpen.model.JobList;
import be.uantwerpen.services.GraphBuilder;
import be.uantwerpen.services.JobService;
import be.uantwerpen.services.JobListService;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * Created by Revil on 10/05/2017.
 */

public class JobDispatching {

    @Autowired
    private JobService jobService;
    @Autowired
    private JobListService jobListService;


    public JobDispatching (JobService jobService, JobListService jobListService) {
        this.jobService = jobService;
        this.jobListService = jobListService;

    }

    public JobDispatching (JobService jobService, JobListService jobListService, String path, GraphBuilder graphBuilder) {
        this.jobService = jobService;
        this.jobListService = jobListService;
        testdispatchOrders(path, graphBuilder);
    }

    public void testdispatchOrders (String path, GraphBuilder graphBuilder ) {
        long k = 0;         //
        path = path.substring(1);           // eerste karakter weghalen
        path = path.substring(0,path.length()-1);       // laatste karakter weghalen
        String[] pathSplit =  path.split(", ", -1);
        Link[] listOfEdges = graphBuilder.getLinkList();
        // need to delete the first item since it was random generated
        //jobService.delete(Long.valueOf(1));

        // keep initeal Jobservice counter
 //       long tempInitCount = jobService.getSize();
        JobList joblist = new JobList();
        for (int i = 0; i < pathSplit.length - 1; i++) {
            for (int j = 0; j < graphBuilder.getLinkList().length; j++) {
                // TODO: als een link ID = -1 retourneert moet er een error volgen!
                Link link = graphBuilder.getCertainLink(Long.valueOf(pathSplit[i]), Long.valueOf(pathSplit[i + 1]));
                Link previous = link;
                if (listOfEdges[j].getId().equals(link.getId())) {
                    System.out.println("Edge found: " + listOfEdges[j].getId());
                    System.out.println(" cost of edge: " + listOfEdges[j].getWeight());
                    if (listOfEdges[j].getVehicle().equals("walk")){
                        //don't add job!
                    }
                    else {
                        Job job = new Job();
                        job.setIdStart(Long.valueOf(pathSplit[i]).longValue());
                        job.setIdEnd(Long.valueOf(pathSplit[i + 1]).longValue());


                        jobService.save(job);
                        if (joblist.isEmpty() == true) {
                            joblist.setStartPoint(job.getIdStart());
                        }


                        // to avoid the problem of changing vehicles of a simular type on the same platform, we are keeping the same ID

                        if(previous.getStopPoint().equals(job.getIdStart())){
                            job.setIdVehicle(previous.getVehicleID());
                        }
                        else {
                            job.setIdVehicle(listOfEdges[j].getVehicleID());
                        }
                        joblist.addJob(job);
                        jobService.save(job);

                        // update last endpoint of joblist to the last added endpoint
                        joblist.setEndPoint(job.getIdEnd());

                        previous = link;
                    }
                } else {
                    // niets doen omdat de correcte edge niet gevonden is
                    //System.out.println("failed to find link");
                }
            }
        }

        //joblist.setStartPoint(joblist.getJobs());
        jobListService.saveOrder(joblist);
        System.out.println("starting Order input");
        printJobList();
    }

    /*public void printJobList() {
        System.out.println("lijst van jobs afprinten");
        for (int x = 0; x < jobService.findAll().size(); x++) {
            System.out.println("jobID: " + jobService.findAll().get(x).getIdJob() + ";   startPos :" + jobService.findAll().get(x).getIdStart() + ";   endPos :" + jobService.findAll().get(x).getIdEnd() + ";   vehicleID :" + jobService.findAll().get(x).getIdVehicle());
        }
    }*/
    public void printJobList() {
 /*       System.out.println("lijst van jobs afprinten");
        for (Job j: jobService.findAll()) {
            System.out.println("jobID: " + j.getId() + ";   startPos :" + j.getIdStart() + ";   endPos :" + j.getIdEnd() + ";   vehicleID :" + j.getIdVehicle());
        }
*/

        System.out.println(" Lijst van Orders afdrukken");
        for (JobList jl: jobListService.findAll()) {
            System.out.println(" Order #" + jl.getId());
            for(int x = 0; x<jl.getJobs().size(); x++) {
                System.out.println("jobID: " + jl.getJobs().get(x).getId() + ";   startPos :" + jl.getJobs().get(x).getIdStart() + ";   endPos :" + jl.getJobs().get(x).getIdEnd() + ";   vehicleID :" + jl.getJobs().get(x).getIdVehicle());
            }
        }
    }

    // check to make sure you don't change from 1 drone to another drone the same platform
    public void OptimiseVehicleUsage () {
        for (Job j: jobService.findAll()){
            //TODO zorg ervoor dat indien het eenzelfde vehicle is, dat je niet overstapt
            System.out.println("jobID: " + j.getId() + ";   startPos :" + j.getIdStart() + ";   endPos :" + j.getIdEnd() + ";   vehicleID :" + j.getIdVehicle());
        }
    }


}
