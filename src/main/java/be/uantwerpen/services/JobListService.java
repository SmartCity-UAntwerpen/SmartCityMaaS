package be.uantwerpen.services;

import be.uantwerpen.model.JobList;
import be.uantwerpen.repositories.JobListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.OutputStream;
import java.net.*;



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
            if (jl.getJobs().get(0).getStatus() == "ready") {
                //check type of vehicle, to determine which core needs to be addressed. first case: communication required with drone core
                if (jl.getJobs().get(0).getTypeVehicle() == "drone") {
                    if (dispatch2Drone(jl.getJobs().get(0).getId(), jl.getJobs().get(0).getIdStart(), jl.getJobs().get(0).getIdEnd(), jl.getJobs().get(0).getIdVehicle()) == true){
                        jl.getJobs().get(0).setStatus("busy");
                    }
                    else {
                        //Todo: schrijf delete en voer astar opnieuw uit
                    }
                }
                // communication needed with car core
                else if (jl.getJobs().get(0).getTypeVehicle() == "car") {
                    if (dispatch2Car(jl.getJobs().get(0).getId(), jl.getJobs().get(0).getIdStart(), jl.getJobs().get(0).getIdEnd(), jl.getJobs().get(0).getIdVehicle()) == true){
                        jl.getJobs().get(0).setStatus("busy");
                    }
                }
                // else event: type is robot: communicate with the robot core
                else {
                    if (dispatch2Robot(jl.getJobs().get(0).getId(), jl.getJobs().get(0).getIdStart(), jl.getJobs().get(0).getIdEnd(), jl.getJobs().get(0).getIdVehicle()) == true){
                        jl.getJobs().get(0).setStatus("busy");
                    }
                }
            }
        }
    }

    public Boolean dispatch2Drone(long idJob, long idStart, long idEnd, long idVehicle) {
        boolean status = true;
        String temp = "http://146.175.140.38:8082/executeJob/";
        //temp = temp+(String.valueOf(idJob) + "/" + String.valueOf(idVehicle) + "/" + String.valueOf(idStart) + "/" + String.valueOf(idEnd));
        temp=temp+("911/78/0/2");
        System.out.println("DroneDispatch");
        System.out.println(temp);
        try {
            URL url = new URL(temp);
            HttpURLConnection conn = null;
            conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("GET");
            //an error has occured
            if (conn.getResponseCode() == 200) {
                String msgresponse = conn.getResponseMessage();
                if (msgresponse == "ACK") {
                    //TODO: doet iets met de ACK code
                    System.out.println(msgresponse);
                }
                conn.disconnect();
                status = true;
            }
            // an error has occured
            else{
                String msgresponse = conn.getResponseMessage();
                if (msgresponse == "idVehicleError") {
                    //TODO: doet iets met de error code
                    System.out.println(msgresponse);
                }
                else if (msgresponse == "idVehicleError"){
                    //TODO: doet iets met de error code
                    System.out.println(msgresponse);
                }
                else if (msgresponse == "idVehicleError") {
                    //TODO: doet iets met de error code
                    System.out.println(msgresponse);
                }
                else {
                    //TODO: doet iets met de error code
                    System.out.println(msgresponse);
                }
                conn.disconnect();
                status = false;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return status;
    }


    public boolean dispatch2Car(long idJob, long idStart, long idEnd, long idVehicle) {
        boolean status = true;
        String temp = "http://143.129.39.151:8081/carmanager/executeJob/";
        //temp=temp+(String.valueOf(idJob) + "/" + String.valueOf(idVehicle) + "/" + String.valueOf(idStart) + "/" + String.valueOf(idEnd));
        temp=temp+("0/0/9/10");
        System.out.println(temp);
        try {
            URL url = new URL(temp);
            HttpURLConnection conn = null;
            conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("GET");
            //an error has occured
            if (conn.getResponseCode() == 200) {
                String msgresponse = conn.getResponseMessage();
                if (msgresponse == "ACK") {
                    //TODO: doet iets met de ACK code
                    System.out.println(msgresponse);
                }
                conn.disconnect();
                status = true;
            }
            // an error has occured
            else{
                String msgresponse = conn.getResponseMessage();
                if (msgresponse == "idVehicleError") {
                    //TODO: doet iets met de error code
                    System.out.println(msgresponse);
                }
                else if (msgresponse == "idVehicleError"){
                    //TODO: doet iets met de error code
                    System.out.println(msgresponse);
                }
                else if (msgresponse == "idVehicleError") {
                    //TODO: doet iets met de error code
                    System.out.println(msgresponse);
                }
                else {
                    //TODO: doet iets met de error code
                    System.out.println(msgresponse);
                }
                conn.disconnect();
                status = false;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return status;
    }


    public boolean dispatch2Robot(long idJob, long idStart, long idEnd, long idVehicle) {
        boolean status = true;
        //String temp = "143.129.39.112:1949/executeJob/";
        String temp = "http://146.175.140.154:1949/executeJob/";
        temp = temp+(String.valueOf(idJob) + "/" + String.valueOf(idVehicle) + "/" + String.valueOf(idStart) + "/" + String.valueOf(idEnd));
        System.out.println("RobotDispatch");
        System.out.println(temp);
        try {
            URL url = new URL(temp);
            HttpURLConnection conn = null;
            conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("GET");
            //an error has occured
            if (conn.getResponseCode() == 200) {
                String msgresponse = conn.getResponseMessage();
                if (msgresponse == "ACK") {
                    //TODO: doet iets met de ACK code
                    System.out.println(msgresponse);
                }
                conn.disconnect();
                status = true;
            }
            // an error has occured
            else{
                String msgresponse = conn.getResponseMessage();
                if (msgresponse == "idVehicleError") {
                    //TODO: doet iets met de error code
                    System.out.println(msgresponse);
                }
                else if (msgresponse == "idVehicleError"){
                    //TODO: doet iets met de error code
                    System.out.println(msgresponse);
                }
                else if (msgresponse == "idVehicleError") {
                    //TODO: doet iets met de error code
                }
                else {
                    //TODO: doet iets met de error code
                    System.out.println(msgresponse);
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
}