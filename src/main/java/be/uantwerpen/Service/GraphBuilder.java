package be.uantwerpen.Service;

import be.uantwerpen.Models.Cost;
import be.uantwerpen.Models.Link;
import be.uantwerpen.Models.Point;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;


/**
 * Created by dries on 10/05/2017.
 */
public class GraphBuilder {
    @Value("${sc.core.ip:localhost}")
    private String serverCoreIP;

    @Value("#{new Integer(${sc.core.port}) ?: 1994}")
    private int serverCorePort;

    @Value("${sc.drone.ip:localhost}")
    private String droneCoreIP;
    @Value("${sc.car.ip:localhost}")
    private String carCoreIP;
    @Value("${sc.robot.ip:localhost}")
    private String robotCoreIP;

    @Value("#{new Integer(${sc.drone.port}) ?: 1994}")
    private String droneCorePort;
    @Value("#{new Integer(${sc.car.port}) ?: 1994}")
    private String carCorePort;
    @Value("#{new Integer(${sc.robot.port}) ?: 1994}")
    private String robotCorePort;

    Link[] linkList;
    Point[] pointList;
    ArrayList<Cost> bestCostList = new ArrayList<>();
    ArrayList<Link> walkLinks = new ArrayList<>();


    public void getMap()
    {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Point[]> responseList;
        responseList = restTemplate.getForEntity("http://" + serverCoreIP + ":" + serverCorePort + "/point/", Point[].class);
        pointList = responseList.getBody();

        restTemplate = new RestTemplate();
        ResponseEntity<Link[]> responseList2;
        responseList2 = restTemplate.getForEntity("http://" + serverCoreIP + ":" + serverCorePort + "/link/", Link[].class);
        linkList = responseList2.getBody();
        for(Link link: linkList)
        {
            if(link.getVehicle().equals("walk"))
            {
                link.setWeight(new Long(0));
                walkLinks.add(link);
            }
        }

    }

    public void setUpTest()
    {
        pointList[0] = new Point(new Long(1000), 0, 0, "car");
        pointList[1] = new Point(new Long(1001), 1, 0, "robot");
        pointList[2] = new Point(new Long(1002), 0, 1, "drone");
        pointList[3] = new Point(new Long(1003), 20, 0, "car");
        pointList[4] = new Point(new Long(1004), 20, 1, "robot");
        pointList[5] = new Point(new Long(1005), 20, 20, "robot");
        pointList[6] = new Point(new Long(1006), 20, 21, "drone");
        pointList[7] = new Point(new Long(1007), 0, 19, "car");
        pointList[8] = new Point(new Long(1008), 0, 20, "drone");
        pointList[9] = new Point(new Long(1009), 1, 20, "robot");
        pointList[10] = new Point(new Long(1010), 10, 30, "drone");
        pointList[11] = new Point(new Long(1011), 20, 40, "robot");
        pointList[12] = new Point(new Long(1012), 20, 41, "drone");
        pointList[13] = new Point(new Long(1013), 0, 35, "drone");
        pointList[14] = new Point(new Long(1014), 0, 36, "car");
        pointList[15] = new Point(new Long(1015), 10, 51, "drone");
        pointList[16] = new Point(new Long(1016), 10, 50, "car");
        pointList[17] = new Point(new Long(1017), 10, 29, "robot");

        linkList[0] = new Link(new Long(2000), pointList[0], pointList[3], "car");
        linkList[1] = new Link(new Long(2001), pointList[3], pointList[0], "car");
        linkList[2] = new Link(new Long(2002), pointList[0], pointList[7], "car");
        linkList[3] = new Link(new Long(2003), pointList[7], pointList[0], "car");
        linkList[4] = new Link(new Long(2004), pointList[7], pointList[14], "car");
        linkList[5] = new Link(new Long(2005), pointList[14], pointList[7], "car");
        linkList[6] = new Link(new Long(2006), pointList[14], pointList[16], "car");
        linkList[7] = new Link(new Long(2007), pointList[16], pointList[14], "car");

        linkList[8] = new Link(new Long(2008), pointList[2], pointList[8], "drone");
        linkList[9] = new Link(new Long(2009), pointList[8], pointList[2], "drone");
        linkList[10] = new Link(new Long(2010), pointList[8], pointList[10], "drone");
        linkList[11] = new Link(new Long(2011), pointList[10], pointList[8], "drone");
        linkList[12] = new Link(new Long(2012), pointList[10], pointList[6], "drone");
        linkList[13] = new Link(new Long(2013), pointList[6], pointList[10], "drone");
        linkList[14] = new Link(new Long(2014), pointList[10], pointList[12], "drone");
        linkList[15] = new Link(new Long(2015), pointList[12], pointList[10], "drone");
        linkList[16] = new Link(new Long(2016), pointList[12], pointList[15], "drone");
        linkList[17] = new Link(new Long(2017), pointList[15], pointList[12], "drone");
        linkList[18] = new Link(new Long(2018), pointList[10], pointList[13], "drone");
        linkList[19] = new Link(new Long(2019), pointList[13], pointList[10], "drone");

        linkList[20] = new Link(new Long(2020), pointList[1], pointList[4], "robot");
        linkList[21] = new Link(new Long(2021), pointList[4], pointList[1], "robot");
        linkList[22] = new Link(new Long(2022), pointList[1], pointList[5], "robot");
        linkList[23] = new Link(new Long(2023), pointList[5], pointList[1], "robot");
        linkList[24] = new Link(new Long(2024), pointList[1], pointList[9], "robot");
        linkList[25] = new Link(new Long(2025), pointList[9], pointList[1], "robot");
        linkList[26] = new Link(new Long(2026), pointList[4], pointList[5], "robot");
        linkList[27] = new Link(new Long(2027), pointList[5], pointList[4], "robot");
        linkList[28] = new Link(new Long(2028), pointList[4], pointList[9], "robot");
        linkList[29] = new Link(new Long(2029), pointList[9], pointList[4], "robot");
        linkList[30] = new Link(new Long(2030), pointList[5], pointList[9], "robot");
        linkList[31] = new Link(new Long(2031), pointList[9], pointList[5], "robot");
        linkList[32] = new Link(new Long(2032), pointList[5], pointList[17], "robot");
        linkList[33] = new Link(new Long(2033), pointList[17], pointList[9], "robot");
        linkList[34] = new Link(new Long(2034), pointList[17], pointList[11], "robot");
        linkList[35] = new Link(new Long(2035), pointList[11], pointList[17], "robot");

        linkList[36] = new Link(new Long(2036), pointList[0], pointList[1], "walk");
        linkList[37] = new Link(new Long(2037), pointList[0], pointList[2], "walk");
        linkList[38] = new Link(new Long(2038), pointList[1], pointList[0], "walk");
        linkList[39] = new Link(new Long(2039), pointList[1], pointList[2], "walk");
        linkList[40] = new Link(new Long(2040), pointList[2], pointList[0], "walk");
        linkList[41] = new Link(new Long(2041), pointList[2], pointList[1], "walk");
        linkList[42] = new Link(new Long(2042), pointList[3], pointList[4], "walk");
        linkList[43] = new Link(new Long(2043), pointList[4], pointList[3], "walk");
        linkList[44] = new Link(new Long(2044), pointList[5], pointList[6], "walk");
        linkList[45] = new Link(new Long(2045), pointList[6], pointList[5], "walk");
        linkList[46] = new Link(new Long(2046), pointList[7], pointList[8], "walk");
        linkList[47] = new Link(new Long(2047), pointList[7], pointList[9], "walk");
        linkList[48] = new Link(new Long(2048), pointList[8], pointList[7], "walk");
        linkList[49] = new Link(new Long(2049), pointList[8], pointList[9], "walk");
        linkList[50] = new Link(new Long(2050), pointList[9], pointList[7], "walk");
        linkList[51] = new Link(new Long(2051), pointList[9], pointList[8], "walk");
        linkList[52] = new Link(new Long(2052), pointList[10], pointList[17], "walk");
        linkList[53] = new Link(new Long(2053), pointList[17], pointList[10], "walk");
        linkList[54] = new Link(new Long(2054), pointList[13], pointList[14], "walk");
        linkList[55] = new Link(new Long(2055), pointList[14], pointList[13], "walk");
        linkList[56] = new Link(new Long(2056), pointList[11], pointList[12], "walk");
        linkList[57] = new Link(new Long(2057), pointList[12], pointList[11], "walk");
        linkList[58] = new Link(new Long(2058), pointList[15], pointList[16], "walk");
        linkList[59] = new Link(new Long(2059), pointList[16], pointList[15], "walk");
    }

    public void setLinkCosts()
    {
        linkList[0].setWeight(new Long(10));
        linkList[0].setVehicleID(new Long(1001));
        linkList[1].setWeight(new Long(10));
        linkList[1].setVehicleID(new Long(1001));
        linkList[2].setWeight(new Long(10));
        linkList[2].setVehicleID(new Long(1001));
        linkList[3].setWeight(new Long(10));
        linkList[3].setVehicleID(new Long(1001));
        linkList[4].setWeight(new Long(15));
        linkList[4].setVehicleID(new Long(1001));
        linkList[5].setWeight(new Long(15));
        linkList[5].setVehicleID(new Long(1002));
        linkList[6].setWeight(new Long(15));
        linkList[6].setVehicleID(new Long(1002));
        linkList[7].setWeight(new Long(15));
        linkList[7].setVehicleID(new Long(1002));

        linkList[8].setWeight(new Long(5));
        linkList[8].setVehicleID(new Long(2001));
        linkList[9].setWeight(new Long(5));
        linkList[9].setVehicleID(new Long(2001));
        linkList[10].setWeight(new Long(10));
        linkList[10].setVehicleID(new Long(2001));
        linkList[11].setWeight(new Long(10));
        linkList[11].setVehicleID(new Long(2002));
        linkList[12].setWeight(new Long(10));
        linkList[12].setVehicleID(new Long(2002));
        linkList[13].setWeight(new Long(10));
        linkList[13].setVehicleID(new Long(2002));
        linkList[14].setWeight(new Long(10));
        linkList[14].setVehicleID(new Long(2002));
        linkList[15].setWeight(new Long(10));
        linkList[15].setVehicleID(new Long(2002));
        linkList[16].setWeight(new Long(10));
        linkList[16].setVehicleID(new Long(2002));
        linkList[17].setWeight(new Long(10));
        linkList[17].setVehicleID(new Long(2002));
        linkList[18].setWeight(new Long(10));
        linkList[18].setVehicleID(new Long(2002));
        linkList[19].setWeight(new Long(10));
        linkList[19].setVehicleID(new Long(2002));

        linkList[20].setWeight(new Long(20));
        linkList[20].setVehicleID(new Long(0002));
        linkList[21].setWeight(new Long(20));
        linkList[21].setVehicleID(new Long(0002));
        linkList[22].setWeight(new Long(30));
        linkList[22].setVehicleID(new Long(0002));
        linkList[23].setWeight(new Long(30));
        linkList[23].setVehicleID(new Long(0001));
        linkList[24].setWeight(new Long(20));
        linkList[24].setVehicleID(new Long(0002));
        linkList[25].setWeight(new Long(20));
        linkList[25].setVehicleID(new Long(0002));
        linkList[26].setWeight(new Long(20));
        linkList[26].setVehicleID(new Long(0002));
        linkList[27].setWeight(new Long(20));
        linkList[27].setVehicleID(new Long(0001));
        linkList[28].setWeight(new Long(30));
        linkList[28].setVehicleID(new Long(0002));
        linkList[29].setWeight(new Long(30));
        linkList[29].setVehicleID(new Long(0001));
        linkList[30].setWeight(new Long(20));
        linkList[30].setVehicleID(new Long(0001));
        linkList[31].setWeight(new Long(20));
        linkList[31].setVehicleID(new Long(0001));
        linkList[32].setWeight(new Long(20));
        linkList[32].setVehicleID(new Long(0001));
        linkList[33].setWeight(new Long(20));
        linkList[33].setVehicleID(new Long(0001));
        linkList[34].setWeight(new Long(20));
        linkList[34].setVehicleID(new Long(0001));
        linkList[35].setWeight(new Long(20));
        linkList[35].setVehicleID(new Long(0001));

        linkList[36].setWeight(new Long(0));
        linkList[37].setWeight(new Long(5));
        linkList[38].setWeight(new Long(0));
        linkList[39].setWeight(new Long(5));
        linkList[40].setWeight(new Long(0));
        linkList[41].setWeight(new Long(0));
        linkList[42].setWeight(new Long(20));
        linkList[43].setWeight(new Long(10));
        linkList[44].setWeight(new Long(10));
        linkList[45].setWeight(new Long(0));
        linkList[46].setWeight(new Long(0));
        linkList[47].setWeight(new Long(20));
        linkList[48].setWeight(new Long(10));
        linkList[49].setWeight(new Long(20));
        linkList[50].setWeight(new Long(10));
        linkList[51].setWeight(new Long(0));
        linkList[52].setWeight(new Long(20));
        linkList[53].setWeight(new Long(0));
        linkList[54].setWeight(new Long(0));
        linkList[55].setWeight(new Long(10));
        linkList[56].setWeight(new Long(10));
        linkList[57].setWeight(new Long(40));
        linkList[58].setWeight(new Long(10));
        linkList[59].setWeight(new Long(20));
    }

    public void getLinkCost()
    {
        for(Link link: linkList)
        {
            Long startPoint = link.getStartPoint().getId();
            Long endPoint = link.getStopPoint().getId();
            String vehicle = link.getVehicle();
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<Cost[]> responseList;
            Cost[] costs;
            String url = "http://";
            if(vehicle.equals("robot")) {
                url += robotCoreIP + ":" + robotCorePort;
            } else if(vehicle.equals("drone")) {
                url += droneCoreIP + ":" + droneCorePort;
            } else if(vehicle.equals("car")) {
                url += carCoreIP + ":" + carCorePort;
            } else if(vehicle.equals("walk")) {
                //link.setWeight(new Long(0));
            } else {
                System.out.println("no supported vehicle was given. See graphbuilder class");
            }

            if(!vehicle.equals("walk")) {
                url += "/calcWeight/" + startPoint + "/to/" + endPoint;
                responseList = restTemplate.getForEntity(url, Cost[].class);
                costs = responseList.getBody();
                Long lowestCost = (costs[0].getWeight() + costs[0].getWeightToStart());
                //Long vehicleID = costs[0].getIdVehicle();
                Cost bestCost = costs[0];
                for (Cost cost : costs) {
                    if(cost.getWeightToStart()+cost.getWeight() < lowestCost)
                    {
                        lowestCost = cost.getWeightToStart()+cost.getWeight();
                        bestCost = cost;
                    }
                }
                for(Link walkLink : walkLinks)
                {
                    if(walkLink.getStopPoint().getId()== endPoint)
                    {
                        walkLink.setWeight(bestCost.getWeightToStart());
                    }
                }
                link.setWeight(bestCost.getWeight());
                link.setVehicleID(bestCost.getIdVehicle());
                bestCostList.add(bestCost);
            }
        }
    }

    public Link[] getLinkList()
    {
        return linkList;
    }

    public Point[] getPointList()
    {
            return pointList;
    }

    //public ArrayList<Cost> getBestCostList() {return bestCostList;}

    public Long getCertainLink(Long startPoint, Long endPoint)
    {
        Link foundLink = new Link(new Long(-1), pointList[0], pointList[1], "none");
        for(Link link : linkList)
        {
            if(link.getStartPoint().getId() == startPoint && link.getStopPoint().getId() == endPoint)
            {
                foundLink = link;
            }
        }
        return foundLink.getId();
    }
}
