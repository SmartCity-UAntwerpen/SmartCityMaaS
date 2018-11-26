package be.uantwerpen.services;

import be.uantwerpen.localization.astar.Astar;
import be.uantwerpen.model.Job;
import be.uantwerpen.model.JobList;
import be.uantwerpen.repositories.JobListRepository;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.*;


/**
 * Service based off the JobListRepository (formerly known as OrderRepository) in which JobList (Orders) will be saved
 * aswell as all functions needed to dispatch jobs to the cores will be forseen.
 *  NV 2018
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
    private Astar astar;

    public Iterable<JobList> findAll() {
        return this.jobListRepository.findAll();
    }

    public void saveOrder(final JobList joblist)
    {
        this.jobListRepository.save(joblist);
    }

    /**
     * a print function, mainly written for debug purposes in the console to make certain that the objects are correctly
     * being saved & that the correct information is written into each object
     */
    public void printJobList() {
        logger.info("Print job list.");
        for (JobList jl: this.jobListRepository.findAll()) {
            logger.info(" Order #" + jl.getId());
            for(int x = 0; x<jl.getJobs().size(); x++) {
                logger.info("jobID: " + jl.getJobs().get(x).getId() + ";   startPos :" + jl.getJobs().get(x).getIdStart() + ";   endPos :" + jl.getJobs().get(x).getIdEnd() + ";   vehicleID :" + jl.getJobs().get(x).getIdVehicle() + ";   VehicleType :" + jl.getJobs().get(x).getTypeVehicle()+ ";   Status :" + jl.getJobs().get(x).getStatus());
            }
        }
    }

    /**
     * Dispatch2core method is a method that will iterate over the Orders & will dispatch the first job from each order
     * to the respective core so that the job can be performed. Once a successful message has been retrieved, the status
     * of the job will be changed to avoid multiple dispatches of the same job while it is being carried out
     */
    public void dispatch2Core() {
        for (JobList jl : this.jobListRepository.findAll()) {
            // iterate over all orders
            if (jl.getJobs().get(0).getStatus().equals("ready")) {
                //check type of vehicle, to determine which core needs to be addressed. first case: communication required with drone core
                String url = "http://";
                String typeVehicle = jl.getJobs().get(0).getTypeVehicle().toUpperCase();
                switch (typeVehicle){
                    case "DRONETOP":
                        url += droneCoreIP + ":" + droneCorePort + "/executeJob/";
                        logger.info("DroneDispatch: " + url);
                        break;
                    case "CARTOP":
                        url += carCoreIP + ":" + carCorePort + "/carmanager/executeJob/";
                        logger.info("CarDispatch: " + url);
                        break;
                    case "ROBOTTOP":
                        url += robotCoreIP + ":" + robotCorePort + "/job/executeJob/";
                        logger.info("RobotDispatch: " + url);
                        break;
                    default: logger.warn("No correct type dispatch2Core function");
                }
                // if succesfully dispatched, update status of the job
                if (dispatch(jl.getJobs().get(0).getId(), jl.getJobs().get(0).getIdStart(), jl.getJobs().get(0).getIdEnd(), jl.getJobs().get(0).getIdVehicle(), url)){
                    jl.getJobs().get(0).setStatus("busy");
                }
                // an error has occured. Rerun the calculations for paths.
                else {
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
        }
    }


    public void dispatchToCore( JobList jl ) {
        //check type of vehicle, to determine which core needs to be addressed. first case: communication required with drone core
        String url = "http://";
        String typeVehicle = jl.getJobs().get(0).getTypeVehicle().toUpperCase();
        switch (typeVehicle){
            case "DRONETOP":
                url += droneCoreIP + ":" + droneCorePort + "/executeJob/";
                logger.info("DroneDispatch: " + url);
                break;
            case "CARTOP":
                url += carCoreIP + ":" + carCorePort + "/carmanager/executeJob/";
                logger.info("CarDispatch: " + url);
                break;
            case "ROBOTTOP":
                url += robotCoreIP + ":" + robotCorePort + "/job/executeJob/";
                logger.info("RobotDispatch: " + url);
                break;
            default: logger.warn("No correct type dispatchToCore function");
        }
        // if succesfully dispatched, update status of the job
        if (dispatch(jl.getJobs().get(0).getId(), jl.getJobs().get(0).getIdStart(), jl.getJobs().get(0).getIdEnd(), jl.getJobs().get(0).getIdVehicle(), url)){
            jl.getJobs().get(0).setStatus("busy");
        }
        // an error has occured. Rerun the calculations for paths.
        else {
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
     * @param idJob             (long) Id from the job that is being dispatched
     * @param idStart           (long) id from the startposition
     * @param idEnd             (long) id from the endposition
     * @param idVehicle         (long) id from the vehicle
     * @param temp              (String) url to which is should be send
     * @return                  (boolean) true if succesfully sent. False if error occured
     */
    private Boolean dispatch(long idJob, long idStart, long idEnd, long idVehicle, String temp)
    {
        boolean status = true;
        temp += (String.valueOf(idJob) + "/" + String.valueOf(idVehicle) + "/" + String.valueOf(idStart) + "/" + String.valueOf(idEnd));
        logger.info("the url is: " + temp);
        try {
            URL url = new URL(temp);
            HttpURLConnection conn;
            conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("GET");
            // for debugging purposes
            /*logger.info("responsecode " + conn.getResponseCode());
            logger.info("responsmsg " + conn.getResponseMessage());*/
            //succesfull transmission
             if (conn.getResponseCode() == 200) {
                 logger.info(conn.getResponseCode());
                /*String msgresponse = conn.getResponseMessage();
                /*if (msgresponse.equals("ACK")) {
                    //TODO: doet iets met de ACK code
                    logger.info(msgresponse);
                }*/
                conn.disconnect();
                status = true;
            }
            // an error has occured
            else{
                /*String msgresponse = conn.getResponseMessage();
                logger.info(msgresponse);
                switch (msgresponse) {
                    case "idVehicleError":
                        Slogger.info(msgresponse);
                        break;
                    default: logger.info(msgresponse);
                }*/
                 logger.error("ERROR WHILE DISPATCHING JOB, job ID: " + idJob + " for vehicle " + idVehicle);
                conn.disconnect();
                status = false;
            }
        } catch (IOException e) {
            logger.error("Can't get file", e);
        }
        return status;
    }

    /**
     * function to check if order is empty or not
     * @param id        (long) id of the order
     * @return          (boolean) true if Order is empty
     */
    public boolean isEmpty(long id) {
        if (this.jobListRepository.findOne(id).getJobs().size() == 0) {
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * delete an order
     * @param id    (long) id from the order that needs to be deleted
     */
    public void deleteOrder (long id) {
        this.jobListRepository.delete(id);
    }

    public void deleteAll() { this.jobListRepository.deleteAll(); }

    /**
     * recalculate the order for which an error occured during the dispatch2core
     * @param idJob             (long) id from the job in which an error occured
     * @param idDelivery        (string) id from delivery which needs to be saved when making a new order with correct input
     */
    public void recalculatePathAfterError (long idJob, String idDelivery){
        for (JobList jl: this.jobListRepository.findAll()){
            // iterate over all orders untill the correct one is found
            if (jl.getJobs().get(0).getId().equals(idJob)) {
                String sPos =  Long.toString(jl.getJobs().get(0).getIdStart());
                String ePos =  Long.toString(jl.getEndPoint());
                deleteOrder(jl.getId());
                astar.determinePath2(sPos,ePos,idDelivery);
            }
        }
    }

    /**
     * function to find a delivery
     * @param idDelivery        (String) Id from the delivery
     * @return Job              (Job) first job from the order that is found mathcing the delivery ID
     */
    public Job findDelivery(String idDelivery) {
        Job found = new Job();
        boolean foundUpdated = false;
        for (JobList jl: this.jobListRepository.findAll()){
            if (idDelivery.equals(jl.getIdDelivery())) {
                foundUpdated = true;
                found = jl.getJobs().get(0);
            }
        }
        if (!foundUpdated) {
            return null;
        }
        else {
            return found;
        }
    }

}



