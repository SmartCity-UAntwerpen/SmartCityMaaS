package be.uantwerpen.localization.astar;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Revil on 10/05/2017.
 */
public class JobDispatching {

    private List<job> joblist;

    public JobDispatching () {

    }

    public JobDispatching (String path, List<Links> links) {
        this.joblist = new ArrayList<job>();
        dispatchOrders(path, links);
    }

    public void dispatchOrders (String path, List<Links> links ) {
        String[] pathSplit =  path.split(",", -1);
        for (int i = 0; i < pathSplit.length; i++) {
            String checkEdge = pathSplit[i].concat(pathSplit[i + 1]);
            if (links.contains(checkEdge) == true) {
                // haal die Edge eruit, haal idvoertuig & zorg ervoor dat een job gedispatched wordt
                //long idVehicle = 0;
                //job newjob = new job(pathSplit[i], pathSplit[i+1], idVehicle, 0,0);
                //joblist.add(newjob);
            } else {
                // print error
            }
        }
    }
}
