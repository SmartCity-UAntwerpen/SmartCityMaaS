package be.uantwerpen.services;

import be.uantwerpen.localization.astar.Astar;
import be.uantwerpen.sc.models.Job;
import be.uantwerpen.sc.models.JobList;
import be.uantwerpen.repositories.JobListRepository;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


/**
 * Service based off the JobListRepository (formerly known as OrderRepository) in which JobList (Orders) will be saved
 * aswell as all functions needed to dispatch jobs to the cores will be forseen.
 * NV 2018
 */
@Service
public class JobListService {
    private static final Logger logger = LogManager.getLogger(JobListService.class);

    @Value("${sc.core.ip:localhost}")
    private String serverCoreIP;

    @Value("#{new Integer(${core.port}) ?: 1994}")
    private int serverCorePort;

    @Value("${drone.ip:localhost}")
    private String droneCoreIP;
    @Value("${car.ip:localhost}")
    private String carCoreIP;
    @Value("${robot.ip:localhost}")
    private String robotCoreIP;

    @Value("#{new Integer(${drone.port}) ?: 1994}")
    private String droneCorePort;
    @Value("#{new Integer(${car.port}) ?: 1994}")
    private String carCorePort;
    @Value("#{new Integer(${robot.port}) ?: 1994}")
    private String robotCorePort;

    @Autowired
    private JobListRepository jobListRepository;

    @Autowired
    private JobService jobService;

    @Autowired
    private Astar astar;

    public Iterable<JobList> findAll() {
        return this.jobListRepository.findAll();
    }

    public void saveOrder(final JobList joblist) {
        this.jobListRepository.save(joblist);
    }

    /**
     * a print function, mainly written for debug purposes in the console to make certain that the objects are correctly
     * being saved & that the correct information is written into each object
     */
    public void printJobList() {
        logger.info("Print job list.");
        for (JobList jl : this.jobListRepository.findAll()) {
            logger.info(" Order #" + jl.getId());
            for (int x = 0; x < jl.getJobs().size(); x++) {
                logger.info("jobID: " + jl.getJobs().get(x).getId() + ";   startPos :" + jl.getJobs().get(x).getIdStart() + ";   endPos :" + jl.getJobs().get(x).getIdEnd() + ";   vehicleID :" + jl.getJobs().get(x).getIdVehicle() + ";   VehicleType :" + jl.getJobs().get(x).getTypeVehicle() + ";   Status :" + jl.getJobs().get(x).getStatus());
            }
        }
    }

    public void dispatchToCore(JobList jl) {
        // TODO: move to backbone
        Job job = jl.getJobs().get(0);

        // if successfully dispatched, update status of the job
        if (job.getStatus().equals("busy")) {
            // probably not reached rendezvous point yet: wait
            return;
        }

        if (dispatch(job)) {
            job.setStatus("busy");
            jobService.save(job);

            if (jl.getJobs().size() > 1 && !jl.getJobs().get(1).getTypeVehicle().equals(jl.getJobs().get(0).getTypeVehicle())) {
                Job nextJob = jl.getJobs().get(1);

                if (dispatch(nextJob)) {
                    nextJob.setStatus("busy");
                    jobService.save(nextJob);
                }
            }
        } else {
            // an error has occurred. Rerun the calculations for paths.
            recalculatePathAfterError(jl.getJobs().get(0).getId(), jl.getIdDelivery());
            // for debug purposes
                    /* logger.info(" Lijst van Orders afdrukken");
                    for (JobList jl2: jobListRepository.findAll()) {
                        logger.info(" Order #" + jl2.getId());
                        for(int x = 0; x<jl2.getJobs().size(); x++) {
                            logger.info("jobID: " + jl2.getJobs().get(x).getId() + ";   startPos :" + jl2.getJobs().get(x).getIdStart() + ";   endPos :" + jl2.getJobs().get(x).getIdEnd() + ";   vehicleID :" + jl2.getJobs().get(x).getIdVehicle()+ ";   VehicleType :" + jl2.getJobs().get(x).getTypeVehicle()+ ";   Status :" + jl2.getJobs().get(x).getStatus());
                        }
                    }*/
        }
    }

    /**
     * communication to the cores
     *
     * @param job The job to dispatch
     * @return (boolean) true if successfully sent. False if error occurred
     */
    private Boolean dispatch(Job job) {
        logger.info("Dispatch " + job.getId() + " - vehicle " + job.getTypeVehicle());
        // TODO: dispatch to backbone
        return true;
//        String stringUrl = "http://";
//        String typeVehicle = job.getTypeVehicle().toUpperCase();
//        switch (typeVehicle) {
//            case "DRONETOP":
//                stringUrl += droneCoreIP + ":" + droneCorePort + "/executeJob/";
//                logger.info("DroneDispatch: " + stringUrl);
//                break;
//            case "CARTOP":
//                stringUrl += carCoreIP + ":" + carCorePort + "/carmanager/executeJob/";
//                logger.info("CarDispatch: " + stringUrl);
//                break;
//            case "ROBOTTOP":
//                stringUrl += robotCoreIP + ":" + robotCorePort + "/job/executeJob/";
//                logger.info("RobotDispatch: " + stringUrl);
//                break;
//            default:
//                logger.warn("No correct type dispatchToCore function");
//        }
//
//        boolean status = true;
//        stringUrl += (String.valueOf(job.getId()) + "/" + String.valueOf(job.getIdVehicle()) + "/" + String.valueOf(job.getIdStart()) + "/" + String.valueOf(job.getIdEnd()));
//        logger.info("the url is: " + stringUrl);
//        try {
//            URL url = new URL(stringUrl);
//            HttpURLConnection conn;
//            conn = (HttpURLConnection) url.openConnection();
//            conn.setDoOutput(true);
//            conn.setRequestMethod("GET");
//            // for debugging purposes
//            /*logger.info("responsecode " + conn.getResponseCode());
//            logger.info("responsmsg " + conn.getResponseMessage());*/
//            //succesfull transmission
//            if (conn.getResponseCode() == 200) {
//                logger.info(conn.getResponseCode());
//                /*String msgresponse = conn.getResponseMessage();
//                /*if (msgresponse.equals("ACK")) {
//                    //TODO: doet iets met de ACK code
//                    logger.info(msgresponse);
//                }*/
//                conn.disconnect();
//            }
//            // an error has occured
//            else {
//                /*String msgresponse = conn.getResponseMessage();
//                logger.info(msgresponse);
//                switch (msgresponse) {
//                    case "idVehicleError":
//                        logger.info(msgresponse);
//                        break;
//                    default: logger.info(msgresponse);
//                }*/
//                logger.error("ERROR WHILE DISPATCHING JOB, job ID: " + job.getId() + " for vehicle " + job.getIdVehicle());
//                conn.disconnect();
//                status = false;
//            }
//        } catch (IOException e) {
//            logger.error("Can't get file", e);
//        }
//        return status;
    }

    /**
     * function to check if order is empty or not
     *
     * @param id (long) id of the order
     * @return (boolean) true if Order is empty
     */
    public boolean isEmpty(long id) {
        return this.jobListRepository.findOne(id).getJobs().isEmpty();
    }

    /**
     * delete an order
     *
     * @param id (long) id from the order that needs to be deleted
     */
    public void deleteOrder(long id) {
        this.jobListRepository.delete(id);
    }

    public void deleteAll() {
        this.jobListRepository.deleteAll();
    }

    /**
     * recalculate the order for which an error occured during the dispatch2core
     *
     * @param idJob      (long) id from the job in which an error occured
     * @param idDelivery (string) id from delivery which needs to be saved when making a new order with correct input
     */
    public void recalculatePathAfterError(long idJob, String idDelivery) {
        for (JobList jl : this.jobListRepository.findAll()) {
            // iterate over all orders untill the correct one is found
            if (jl.getJobs().get(0).getId().equals(idJob)) {
                String sPos = Long.toString(jl.getJobs().get(0).getIdStart());
                String ePos = Long.toString(jl.getEndPoint());
                deleteOrder(jl.getId());
                astar.determinePath2(sPos, ePos, idDelivery);
            }
        }
    }

    /**
     * function to find a delivery
     *
     * @param idDelivery (String) Id from the delivery
     * @return Job              (Job) first job from the order that is found mathcing the delivery ID
     */
    public Job findDelivery(String idDelivery) {
        Job found = new Job();
        boolean foundUpdated = false;
        for (JobList jl : this.jobListRepository.findAll()) {
            if (idDelivery.equals(jl.getIdDelivery())) {
                foundUpdated = true;
                found = jl.getJobs().get(0);
            }
        }
        if (!foundUpdated) {
            return null;
        } else {
            return found;
        }
    }

}



