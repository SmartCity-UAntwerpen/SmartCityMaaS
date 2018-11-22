package be.uantwerpen.localization.astar;

import be.uantwerpen.model.Job;
import be.uantwerpen.model.Link;
import be.uantwerpen.model.JobList;
import be.uantwerpen.services.GraphBuilder;
import be.uantwerpen.services.JobService;
import be.uantwerpen.services.JobListService;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * A Star Class (will be used as a Service)
 * This class will provide a path, while building up a graph in which the path will be calculated from input it gets from the Graphbuilder service.
 * VN 2018
 */

public class JobDispatching {
    private static final Logger logger = LogManager.getLogger(JobDispatching.class);
    private JobService jobService;

    private JobListService jobListService;

    private GraphBuilder graphBuilder;


    public JobDispatching( String path, String idDelivery, GraphBuilder gb ) {
        //dispatchOrders2( path, idDelivery );
        this.graphBuilder = gb;
    }

    public JobDispatching (JobService jobService, JobListService jobListService) {
        this.jobService = jobService;
        this.jobListService = jobListService;

    }

    public JobDispatching (JobService jobService, JobListService jobListService, String path, GraphBuilder graphBuilder) {
        this.jobService = jobService;
        this.jobListService = jobListService;
        dispatchOrders(path, graphBuilder);
    }

    public JobDispatching (JobService jobService, JobListService jobListService, String path, GraphBuilder graphBuilder, String idDelivery) {
        this.jobService = jobService;
        this.jobListService = jobListService;
        this.graphBuilder = graphBuilder;
    }

    /**
     * USED FOR DEBUGGING
     * Dispatch orders function will use a path as parameter and convert it into a JobList, which consists of Jobs containing all the information required to send to the cores.
     * At the end of the function, there will be a call to the dispatch2core function, to start communication in the
     * @param path  (String) path computed by Astar Algorithm
     * @param graphBuilder  (Graphbuilder Service)
     */
    public void dispatchOrders (String path, GraphBuilder graphBuilder ) {
        long k = 0;         //
        path = path.substring(1);           // remove first character '['
        path = path.substring(0,path.length()-1);       // remove last character ']'
        String[] pathSplit =  path.split(", ", -1);        // Split string up into Edges
        Link[] listOfEdges = graphBuilder.getLinkList();
        Link previous = listOfEdges[0];         // will be needed later on to prevent simular vehicletype switching
        JobList joblist = new JobList();
        for (int i = 0; i < pathSplit.length - 1; i++) {
            for (int j = 0; j < graphBuilder.getLinkList().length; j++) {
                Link link = graphBuilder.getCertainLink(Long.valueOf(pathSplit[i]), Long.valueOf(pathSplit[i + 1]));
                // If the Link has been found, start creating a job with all relevant information
                if (listOfEdges[j].getId().equals(link.getId())) {
                    System.out.println("Edge found: " + listOfEdges[j].getId());
                    System.out.println(" cost of edge: " + listOfEdges[j].getWeight());
                    // if we are changing vehicle types, then we will walk from 1 vehicle to another. No need to add a Job then
                    if (listOfEdges[j].getVehicle().equals("wait")){
                        //don't add job!
                    }
                    // Transport in a vehicle --> new job needs to be added.
                    else {
                        Job job = new Job();
                        job.setIdStart(Long.valueOf(pathSplit[i]).longValue());             // set start ID from job
                        job.setIdEnd(Long.valueOf(pathSplit[i + 1]).longValue());           // set stop ID from job
                        job.setTypeVehicle((listOfEdges[j].getVehicle()));                  // set vehicle type
                        job.setStatus("ready");                                             // set status (3 status: ready, busy or done. since this is the creating part, we'll init them on ready)
                        // for the joblist, we want to keep track of the starting ID!
                        if (joblist.isEmpty() == true) {
                            joblist.setStartPoint(job.getIdStart());
                        }
                        // to avoid the problem of changing vehicles of a simular type on the same platform, we are keeping the same ID
                        if(previous.getStopPoint().getId().equals(link.getStartPoint().getId())){
                            job.setIdVehicle(previous.getVehicleID());
                        }
                        else {
                            System.out.println("Vehicle ID =" + listOfEdges[j].getVehicleID());
                            job.setIdVehicle(listOfEdges[j].getVehicleID());
                        }
                        // Add it to the joblist (all jobs within 1 delivery) and then to the jobservice (list of all jobs, regardless of to which delivery it belongs)
                        joblist.addJob(job);
                        jobService.save(job);

                        // update last endpoint of joblist to the last added endpoint
                        joblist.setEndPoint(job.getIdEnd());
                        // previous needed to avoid needless changing of vehicles.
                        previous = link;
                    }
                }
            }
        }
        jobListService.saveOrder(joblist);
        System.out.println("starting Order input");
        printJobList();
        jobListService.dispatch2Core();
    }

    /**
     * Dispatch orders function will use a path as parameter and convert it into a JobList, which consists of Jobs containing all the information required to send to the cores.
     * At the end of the function, there will be a call to the dispatch2core function, to start communication in the
     * @param path  (String) path computed by Astar Algorithm
     * @param //graphBuilder  (Graphbuilder Service)
     * @param idDelivery  (String) idDelivery waaraan het Order wordt vastgekoppeld
     */
    public void dispatchOrders2 (String path, String idDelivery ) {
        long k = 0;         //
        path = path.substring(1); // remove first character '['
        path = path.substring(0,path.length()-1); // remove last character ']'
        String[] pathSplit =  path.split(", ", -1); // Split string up into Edges
        if(graphBuilder == null) System.out.println("graphbuilder is null");
        Link[] listOfEdges = graphBuilder.getLinkList();
        System.out.println("LIST OF EDGES: " + listOfEdges.toString());
        Link previous = null;         // will be needed later on to prevent similar vehicle type switching
        //Link previous = graphBuilder.getCertainLink(Long.valueOf(pathSplit[0]), Long.valueOf(pathSplit[1]));
        System.out.println( "Previous link: "+previous );
        System.out.println( "Linklist count: " + graphBuilder.getLinkList().length );
        JobList joblist = new JobList(); // New empty joblist
        for (int i = 0; i < pathSplit.length - 1; i++) {
            for (int j = 0; j < graphBuilder.getLinkList().length; j++) {
                // Get Link of point A and B
                Link link = graphBuilder.getCertainLink(Long.valueOf(pathSplit[i]), Long.valueOf(pathSplit[i + 1]));
                //Iterate over all links to find the corresponding link (given by Backbone)
                if (listOfEdges[j].getId().equals(link.getId())) {
                    // if we are changing vehicle types, then we will walk from 1 vehicle to another. No need to add a Job then
                    if (listOfEdges[j].getVehicle().equals("wait")){
                        //don't add job! We're waaaaaalking on water
                    } else { // We DO need a vehicle so let's make a job
                        Job job = new Job();
                        job.setIdStart( Long.valueOf( link.getStartPoint().getId() )); // set start ID from job
                        job.setIdEnd( Long.valueOf( link.getStopPoint().getId() )); // set stop ID from job
                        job.setTypeVehicle( link.getVehicle() );// set vehicle type
                        job.setStatus("ready"); // set status (3 status: ready, busy or done. since this is the creating part, we'll init them on ready)
                        job.setIdVehicle( link.getVehicleID() );
                        System.out.println( "Job initialized: \n\tStart: " + job.getIdStart() + "\n\tEnd: " + job.getIdEnd() + "\n\tVehicle: " + job.getTypeVehicle() );

                        // for the joblist, we want to keep track of the starting ID!
                        if ( joblist.isEmpty() ) {
                            joblist.setStartPoint(job.getIdStart());
                        }

                        // to avoid the problem of changing vehicles of a simular type on the same platform, we are keeping the same ID
                        if( previous != null ) {
                            if (previous.getStopPoint().getId().equals(link.getStartPoint().getId())) {
                                System.out.println("voertuig ID previous nemen = " + previous.getVehicleID());
                                job.setIdVehicle(previous.getVehicleID());
                            } else {
                                System.out.println("voertuig ID overstap nemen = " + listOfEdges[j].getVehicleID());
                                job.setIdVehicle(listOfEdges[j].getVehicleID());
                            }
                        } else {
                            // Nothing to be done, this means we're executing the first link, so we have to take the first vehicle
                        }


                        // Add it to the joblist (all jobs within 1 delivery) and then to the jobservice (list of all jobs, regardless of to which delivery it belongs)
                        joblist.addJob(job);
                        if( jobService == null ) System.out.println("ERROR: jobService is null ");
                        jobService.save(job);

                        // update last endpoint of joblist to the last added endpoint
                        joblist.setEndPoint(job.getIdEnd());
                        // previous needed to avoid needless changing of vehicles.
                        previous = link;
                    }
                }
            }
        }

        joblist.setIdDelivery(idDelivery);
        jobListService.saveOrder(joblist);
        System.out.println("starting Order input");
        printJobList();
        jobListService.dispatchToCore( joblist );
    }

    /**
     * Print function. Suited for debug purposes so that it's clear that is to be found in the orders and joblisservice etc.
     */
    public void printJobList() {
        System.out.println(" JOBLISTS: ");
        for (JobList jl: jobListService.findAll()) {
            System.out.println(" JOBLIST ID" + jl.getId());
            for(int x = 0; x<jl.getJobs().size(); x++) {
                System.out.println("jobID: " + jl.getJobs().get(x).getId() + ";   startPos :" + jl.getJobs().get(x).getIdStart() + ";   endPos :" + jl.getJobs().get(x).getIdEnd() + ";   vehicleID :" + jl.getJobs().get(x).getIdVehicle()+ ";   VehicleType :" + jl.getJobs().get(x).getTypeVehicle()+ ";   Status :" + jl.getJobs().get(x).getStatus());
            }
        }
    }
}
