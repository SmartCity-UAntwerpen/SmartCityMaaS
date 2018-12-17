package be.uantwerpen.localization.astar;

import be.uantwerpen.sc.models.Job;
import be.uantwerpen.sc.models.JobList;
import be.uantwerpen.model.Link;
import be.uantwerpen.sc.models.JobState;
import be.uantwerpen.services.GraphBuilder;
import be.uantwerpen.services.JobListService;
import be.uantwerpen.services.JobService;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;


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

    public JobDispatching(JobService jobService, JobListService jobListService, GraphBuilder graphBuilder) {
        this.jobService = jobService;
        this.jobListService = jobListService;
        this.graphBuilder = graphBuilder;
    }

    /**
     * Dispatch orders function will use a path as parameter and convert it into a JobList, which consists of Jobs containing all the information required to send to the cores.
     * At the end of the function, there will be a call to the dispatch2core function, to start communication in the
     *
     * @param path       (String) path computed by Astar Algorithm
     * @param idDelivery (String) idDelivery waaraan het Order wordt vastgekoppeld
     */
    public void dispatchOrders2(String path, String idDelivery) {
        // todo graphbuilder from backbone
        path = path.substring(1); // remove first character '['
        path = path.substring(0, path.length() - 1); // remove last character ']'
        String[] pathSplit = path.split(", ", -1); // Split string up into Edges
        if (graphBuilder == null) {
            logger.error("graphbuilder is null");
            return;
        }
        List<Link> linkList = new ArrayList<>(pathSplit.length - 1);
        for (int i = 0; i < pathSplit.length - 1; ++i)
            graphBuilder.getCertainLink(Long.parseLong(pathSplit[i]), Long.parseLong(pathSplit[i + 1])).ifPresent(linkList::add);

        Link previous = null;         // will be needed later on to prevent similar vehicle type switching
        //Link previous = graphBuilder.getCertainLink(Long.valueOf(pathSplit[0]), Long.valueOf(pathSplit[1]));
        logger.debug("Linklist count: " + graphBuilder.getLinkList().size());
        JobList joblist = new JobList(); // New empty joblist
        for (int i = 0; i < linkList.size(); i++) {
            Link link = linkList.get(i);
            Job job;
            // if we are changing vehicle types, then we will walk from 1 vehicle to another. No need to add a Job then
            if (link.getVehicle().equals("wait") && i < linkList.size() - 1) {
                // Add job for next vehicle to rendezvous at transit point
                job = new Job();
                job.setIdStart(link.getStopPoint().getId());
                job.setIdEnd(link.getStopPoint().getId());
                job.setStatus(JobState.TODO);
//                job.setIdVehicle(linkList.get[j + 1].getVehicleID());
                logger.info("Rendezvous job: \tStart: " + job.getIdStart() + "\tEnd: " + job.getIdEnd());
            } else {
                // We DO need a vehicle so let's make a job
                job = new Job();
                job.setIdStart(link.getStartPoint().getId()); // set start ID from job
                job.setIdEnd(link.getStopPoint().getId()); // set stop ID from job
                job.setStatus(JobState.TODO); // set status (3 status: ready, busy or done. since this is the creating part, we'll init them on ready)
//                job.setIdVehicle(link.getVehicleID());
                logger.info("Job initialized: \tStart: " + job.getIdStart() + "\tEnd: " + job.getIdEnd());

                // to avoid the problem of changing vehicles of a similar type on the same platform, we are keeping the same ID
                if (previous != null) {
                    if (previous.getStopPoint().getId().equals(link.getStartPoint().getId())) {
                        logger.debug("voertuig ID previous nemen = " + previous.getVehicleID());
//                        job.setIdVehicle(previous.getVehicleID());
                    } else {
                        logger.debug("voertuig ID overstap nemen = " + link.getVehicleID());
//                        job.setIdVehicle(listOfEdges[j].getVehicleID());
                    }
                }
                // Else: nothing to be done, this means we're executing the first link, so we have to take the first vehicle
                // previous needed to avoid needless changing of vehicles.
                previous = link;
            }

            // Add it to the joblist (all jobs within 1 delivery) and then to the jobservice (list of all jobs, regardless of to which delivery it belongs)
            joblist.addJob(job);
            if (jobService == null) {
                logger.error("ERROR: jobService is null ");
                continue;
            }
            jobService.save(job);
        }

        joblist.setIdDelivery(idDelivery);
        jobListService.saveOrder(joblist);
        logger.info("starting Order input");
        printJobList();
        // Dispatch method will be done on the BackBone
        jobListService.dispatchToCore();
    }

    /**
     * Print function. Suited for debug purposes so that it's clear that is to be found in the orders and joblisservice etc.
     */
    public void printJobList() {
        logger.debug(" JOBLISTS: ");
        for (JobList jl : jobListService.findAll()) {
            logger.debug(" JOBLIST ID" + jl.getId());
            for (int x = 0; x < jl.getJobs().size(); x++) {
                logger.debug("jobID: " + jl.getJobs().get(x).getId() + ";   startPos :" + jl.getJobs().get(x).getIdStart() + ";   endPos :" + jl.getJobs().get(x).getIdEnd() + ";   Status :" + jl.getJobs().get(x).getStatus());
            }
        }
    }
}
