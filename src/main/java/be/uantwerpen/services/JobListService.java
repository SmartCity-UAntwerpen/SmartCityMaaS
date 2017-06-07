package be.uantwerpen.services;

import be.uantwerpen.localization.astar.Astar;
import be.uantwerpen.model.Job;
import be.uantwerpen.model.JobList;
import be.uantwerpen.repositories.JobListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.*;



/**
 * Created by Revil on 29/05/2017.
 */
@Service
public class JobListService {

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


    public void printJobList() {
        System.out.println(" Lijst van Orders afdrukken");
        for (JobList jl: this.jobListRepository.findAll()) {
            System.out.println(" Order #" + jl.getId());
            for(int x = 0; x<jl.getJobs().size(); x++) {
                System.out.println("jobID: " + jl.getJobs().get(x).getId() + ";   startPos :" + jl.getJobs().get(x).getIdStart() + ";   endPos :" + jl.getJobs().get(x).getIdEnd() + ";   vehicleID :" + jl.getJobs().get(x).getIdVehicle() + ";   VehicleType :" + jl.getJobs().get(x).getTypeVehicle()+ ";   Status :" + jl.getJobs().get(x).getStatus());
            }
        }
    }

    public void dispatch2Core() {
        for (JobList jl : this.jobListRepository.findAll()) {
            // iterate over all orders
            if (jl.getJobs().get(0).getStatus().equals("ready")) {
                //check type of vehicle, to determine which core needs to be addressed. first case: communication required with drone core
                String url = "";
                if (jl.getJobs().get(0).getTypeVehicle().equals("drone")){
                    url = "http://" + droneCoreIP + ":" + droneCorePort + "/executeJob/";
                    //temp += (String.valueOf(idJob) + "/" + String.valueOf(idVehicle) + "/" + String.valueOf(idStart) + "/" + String.valueOf(idEnd));
                    url += ("911/78/0/2");
                    System.out.println("DroneDispatch");
                    System.out.println(url);
                } else if(jl.getJobs().get(0).getTypeVehicle().equals("car")) {
                    url = "http://" + carCoreIP + ":" + carCorePort + "/carmanager/executeJob/";
                    //temp=temp+(String.valueOf(idJob) + "/" + String.valueOf(idVehicle) + "/" + String.valueOf(idStart) + "/" + String.valueOf(idEnd));
                    url+=("0/0/9/10");
                    System.out.println(url);
                } else if(jl.getJobs().get(0).getTypeVehicle().equals("robot")) {
                    //String temp = "143.129.39.112:1949/executeJob/";
                    url = "http://" + robotCoreIP + ":" + robotCorePort + "/executeJob/";
                    System.out.println("RobotDispatch");
                    System.out.println(url);
                }

                if (dispatch(jl.getJobs().get(0).getId(), jl.getJobs().get(0).getIdStart(), jl.getJobs().get(0).getIdEnd(), jl.getJobs().get(0).getIdVehicle(), url)){
                    jl.getJobs().get(0).setStatus("busy");
                }
                else {
                    recalculatePathAfterError(jl.getJobs().get(0).getId(), jl.getIdDelivery());
                }
            }
        }
    }

    private Boolean dispatch(long idJob, long idStart, long idEnd, long idVehicle, String temp)
    {
        boolean status = true;
        temp += (String.valueOf(idJob) + "/" + String.valueOf(idVehicle) + "/" + String.valueOf(idStart) + "/" + String.valueOf(idEnd));
        //temp += ("911/78/0/2");
        try {
            URL url = new URL(temp);
            HttpURLConnection conn;
            conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("GET");
            //an error has occured
            if (conn.getResponseCode() == 200) {
                String msgresponse = conn.getResponseMessage();
                if (msgresponse.equals("ACK")) {
                    //TODO: doet iets met de ACK code
                    System.out.println(msgresponse);
                }
                conn.disconnect();
                status = true;
            }
            // an error has occured
            else{
                String msgresponse = conn.getResponseMessage();
                System.out.println(msgresponse);
                switch (msgresponse) {
                    case "idVehicleError":
                        //TODO: doet iets met de error code
                        System.out.println(msgresponse);
                        break;
                    default: System.out.println(msgresponse);
                }
                conn.disconnect();
                status = false;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return status;
    }

    public boolean isEmpty(long id) {
        if (this.jobListRepository.findOne(id).getJobs().size() == 0) {
            return true;
        }
        else {
            return false;
        }
    }

    public void deleteOrder (long id) {
        this.jobListRepository.delete(id);
    }

    public void recalculatePathAfterError (long idJob, String idDelivery){
        for (JobList jl: this.jobListRepository.findAll()){
            if (jl.getJobs().get(0).getId().equals(idJob)) {
                String sPos =  Long.toString(jl.getJobs().get(0).getIdStart());
                String ePos =  Long.toString(jl.getEndPoint());
                deleteOrder(jl.getId());
                astar.determinePath2(sPos,ePos,idDelivery);
            }
            else {
                // do nothing for now
            }
        }
    }

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



