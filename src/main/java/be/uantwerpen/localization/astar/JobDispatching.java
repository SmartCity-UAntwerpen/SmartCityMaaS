package be.uantwerpen.localization.astar;

import be.uantwerpen.model.Jobs;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Revil on 10/05/2017.
 */
public class JobDispatching {

    private List<Jobs> joblist;

    public JobDispatching () {

    }

    public JobDispatching (String path, List<Links> links) {
        this.joblist = new ArrayList<Jobs>();
        dispatchOrders(path, links);
    }

    public void dispatchOrders (String path, List<Links> links ) {
        path = path.substring(1);           // eerste karakter weghalen
        path = path.substring(0,path.length()-1);       // laatste karakter weghalen
        String[] pathSplit =  path.split(", ", -1);

            for (int i = 0; i < pathSplit.length - 1; i++) {
                for (int j = 0; j < links.size(); j++) {
                String checkEdge = pathSplit[i].concat(pathSplit[i + 1]);
                if (links.get(j).getName().equals(checkEdge)) {
                    System.out.println("test works");
                    System.out.println(checkEdge);
                    // haal die Edge eruit, haal idvoertuig & zorg ervoor dat een Job gedispatched wordt
                    //long idVehicle = 0;
                    //Jobs newJobs = new Jobs(pathSplit[i], pathSplit[i+1], idVehicle, 0,0);
                    //Joblist.add(newjob);
                } else {
                    // print niets
                }
            }
        }
    }
}
