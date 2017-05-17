package be.uantwerpen.localization.astar;

import be.uantwerpen.Models.Job;
import be.uantwerpen.Models.Link;
import be.uantwerpen.Service.GraphBuilder;
import be.uantwerpen.repositories.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Revil on 10/05/2017.
 */

public class JobDispatching {

    @Autowired
    private JobRepository jobRepository;

    public JobDispatching () {

    }

    public JobDispatching (String path, GraphBuilder graphBuilder) {
        testdispatchOrders(path, graphBuilder);
    }

    public void testdispatchOrders (String path, GraphBuilder graphBuilder ) {
        long k = 0;         //
        path = path.substring(1);           // eerste karakter weghalen
        path = path.substring(0,path.length()-1);       // laatste karakter weghalen
        String[] pathSplit =  path.split(", ", -1);
        Link[] listOfEdges = graphBuilder.getLinkList();

        for (int i = 0; i < pathSplit.length - 1; i++) {
            for (int j = 0; j < graphBuilder.getLinkList().length; j++) {
                // TODO: zorg ervoor dat via de functie van Dries met begin en eindpunt een id van de link wordt opgehaald!
                // TODO: nakijken of de getlinkID functie niet ergens beter geschreven kan worden dan in GraphBuilder
                // TODO: als een link ID = -1 retourneert moet er een error volgen!
                long lid = graphBuilder.getCertainLink(Long.valueOf(pathSplit[i]), Long.valueOf(pathSplit[i + 1]));
                if (listOfEdges[j].getId().equals(lid)) {
                    System.out.println("Edge found: " + listOfEdges[j].getId());
                    System.out.println(" cost of edge: " + listOfEdges[j].getWeight());
                    if (listOfEdges[j].getVehicle().equals("walk")){
                        //don't add job!
                    }
                    else {
                        Job job = new Job();
                        job.setIdStart(Long.valueOf(pathSplit[i]).longValue());
                        job.setIdEnd(Long.valueOf(pathSplit[i + 1]).longValue());
                        job.setIdVehicle(listOfEdges[j].getVehicleID());
                        jobRepository.save(job);
                    }
                } else {
                    // niets doen omdat de correcte edge niet gevonden is
                    //System.out.println("failed to find link");
                }
            }
        }

    }

    public void printJobList() {
        System.out.println("lijst van jobs afprinten");
        for (int x = 0; x < jobRepository.findAll().size(); x++) {
            System.out.println("jobID: " + jobRepository.findAll().get(x).getIdJob() + ";   startPos :" + jobRepository.findAll().get(x).getIdStart() + ";   endPos :" + jobRepository.findAll().get(x).getIdEnd() + ";   vehicleID :" + jobRepository.findAll().get(x).getIdVehicle());
        }
    }
}
