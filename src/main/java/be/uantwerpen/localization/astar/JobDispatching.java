package be.uantwerpen.localization.astar;

import be.uantwerpen.Models.Job;
import be.uantwerpen.Models.Link;
import be.uantwerpen.repositories.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;

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

    public JobDispatching (String path, List<Links> linken) {
        testdispatchOrders(path, linken);
    }

 /*   public JobDispatching (String path, List<Link> links) {
        this.joblist = new ArrayList<Jobs>();
        dispatchOrders(path, links);
    }*/

    public void testdispatchOrders (String path, List<Links> links ) {
        long k = 0;         //
        path = path.substring(1);           // eerste karakter weghalen
        path = path.substring(0,path.length()-1);       // laatste karakter weghalen
        String[] pathSplit =  path.split(", ", -1);

            for (int i = 0; i < pathSplit.length - 1; i++) {
                for (int j = 0; j < links.size(); j++) {
                    // TODO: zorg ervoor dat via de functie van Dries met begin en eindpunt een id van de link wordt opgehaald!
                    // TODO: nakijken of de getlinkID functie niet ergens beter geschreven kan worden dan in GraphBuilder
                    // TODO: als een link ID = -1 retourneert moet er een error volgen!
                    /* long lid = getlinkID(pathSplit[i], pathSplit[i+1]);
                     if (links.get(j).getName().equals(lid)) {
                        Link templink = Link.get(lid)
                        Job job = new Job (Long.valueOf(pathSplit[i]).longValue(), Long.valueOf(pathSplit[i+1]).longValue(), getVehicleID());
                        jobRepository.save(job);
                    }


                String checkEdge = pathSplit[i].concat(pathSplit[i + 1]);
                if (links.get(j).getName().equals(checkEdge)) {
                    System.out.println("test works");
                    System.out.println(checkEdge);
                    Links tempLink = links.get(j);

                    Job job = new Job(Long.valueOf(1), checkEdge.charAt(0), checkEdge.charAt(1), Long.valueOf(1));
                    jobRepository.save(job);
                } else {
                    // print niets
                }*/
            }
        }
    }
}
