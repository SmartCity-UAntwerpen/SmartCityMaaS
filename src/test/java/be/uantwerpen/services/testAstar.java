package be.uantwerpen.services;

import be.uantwerpen.localization.astar.Astar;
import be.uantwerpen.model.Link;
import be.uantwerpen.model.Point;
import org.graphstream.algorithm.AStar;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Path;
import org.graphstream.graph.implementations.SingleGraph;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by Revil on 06/06/2017.
 */

@RunWith(MockitoJUnitRunner.class)
public class testAstar {

    @Mock
    private JobService jobService;
    @Mock
    private JobListService jobListService;
    @Resource
    private Graph graph;
    @Mock
    private GraphBuilder graphBuilder;
    @Mock
    private AStar testAstar;
    @InjectMocks
    private Astar eigenAstar;
    private Link[] linkList = new Link[60];
    private Point[] pointList = new Point[18];

    @Before
    public void Init() {
        //this.jobService = jobService;
        //this.jobListService = jobListService;
        graph = new SingleGraph("TestSmartCityGraph");
        eigenAstar.setGraph(graph);
        setUpTest();
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testStartAstar () {
        when(graphBuilder.getPointList()).thenReturn(pointList);
        eigenAstar.makeNode();
        when(graphBuilder.getLinkList()).thenReturn(linkList);
        eigenAstar.makeEdge();
        //testAstar.compute("1015", "1010");
        //testAstar = new AStar(graph);
        testAstar = new AStar(this.graph);
        testAstar.compute("1015", "1010");;
        Path path = testAstar.getShortestPath();
        System.out.println(path);
        assertEquals(60, eigenAstar.getGraph().getEdgeCount());
        assertEquals(18, eigenAstar.getGraph().getNodeCount());
        assertEquals("[1015, 1012, 1010]",path.toString());
    }


    public void setUpTest()
    {
        pointList[0] = new Point((long)(1000), 0, 0, "car");
        pointList[1] = new Point((long)(1001), 1, 0, "robot");
        pointList[2] = new Point((long)(1002), 0, 1, "drone");
        pointList[3] = new Point((long)(1003), 20, 0, "car");
        pointList[4] = new Point((long)(1004), 20, 1, "robot");
        pointList[5] = new Point((long)(1005), 20, 20, "robot");
        pointList[6] = new Point((long)(1006), 20, 21, "drone");
        pointList[7] = new Point((long)(1007), 0, 19, "car");
        pointList[8] = new Point((long)(1008), 0, 20, "drone");
        pointList[9] = new Point((long)(1009), 1, 20, "robot");
        pointList[10] = new Point((long)(1010), 10, 30, "drone");
        pointList[11] = new Point((long)(1011), 20, 40, "robot");
        pointList[12] = new Point((long)(1012), 20, 41, "drone");
        pointList[13] = new Point((long)(1013), 0, 35, "drone");
        pointList[14] = new Point((long)(1014), 0, 36, "car");
        pointList[15] = new Point((long)(1015), 10, 51, "drone");
        pointList[16] = new Point((long)(1016), 10, 50, "car");
        pointList[17] = new Point((long)(1017), 10, 29, "robot");

        linkList[0] = new Link((long)(2000), pointList[0], pointList[3], "car");
        linkList[1] = new Link((long)(2001), pointList[3], pointList[0], "car");
        linkList[2] = new Link((long)(2002), pointList[0], pointList[7], "car");
        linkList[3] = new Link((long)(2003), pointList[7], pointList[0], "car");
        linkList[4] = new Link((long)(2004), pointList[7], pointList[14], "car");
        linkList[5] = new Link((long)(2005), pointList[14], pointList[7], "car");
        linkList[6] = new Link((long)(2006), pointList[14], pointList[16], "car");
        linkList[7] = new Link((long)(2007), pointList[16], pointList[14], "car");

        linkList[8] = new Link((long)(2008), pointList[2], pointList[8], "drone");
        linkList[9] = new Link((long)(2009), pointList[8], pointList[2], "drone");
        linkList[10] = new Link((long)(2010), pointList[8], pointList[10], "drone");
        linkList[11] = new Link((long)(2011), pointList[10], pointList[8], "drone");
        linkList[12] = new Link((long)(2012), pointList[10], pointList[6], "drone");
        linkList[13] = new Link((long)(2013), pointList[6], pointList[10], "drone");
        linkList[14] = new Link((long)(2014), pointList[10], pointList[12], "drone");
        linkList[15] = new Link((long)(2015), pointList[12], pointList[10], "drone");
        linkList[16] = new Link((long)(2016), pointList[12], pointList[15], "drone");
        linkList[17] = new Link((long)(2017), pointList[15], pointList[12], "drone");
        linkList[18] = new Link((long)(2018), pointList[10], pointList[13], "drone");
        linkList[19] = new Link((long)(2019), pointList[13], pointList[10], "drone");

        linkList[20] = new Link((long)(2020), pointList[1], pointList[4], "robot");
        linkList[21] = new Link((long)(2021), pointList[4], pointList[1], "robot");
        linkList[22] = new Link((long)(2022), pointList[1], pointList[5], "robot");
        linkList[23] = new Link((long)(2023), pointList[5], pointList[1], "robot");
        linkList[24] = new Link((long)(2024), pointList[1], pointList[9], "robot");
        linkList[25] = new Link((long)(2025), pointList[9], pointList[1], "robot");
        linkList[26] = new Link((long)(2026), pointList[4], pointList[5], "robot");
        linkList[27] = new Link((long)(2027), pointList[5], pointList[4], "robot");
        linkList[28] = new Link((long)(2028), pointList[4], pointList[9], "robot");
        linkList[29] = new Link((long)(2029), pointList[9], pointList[4], "robot");
        linkList[30] = new Link((long)(2030), pointList[5], pointList[9], "robot");
        linkList[31] = new Link((long)(2031), pointList[9], pointList[5], "robot");
        linkList[32] = new Link((long)(2032), pointList[5], pointList[17], "robot");
        linkList[33] = new Link((long)(2033), pointList[17], pointList[9], "robot");
        linkList[34] = new Link((long)(2034), pointList[17], pointList[11], "robot");
        linkList[35] = new Link((long)(2035), pointList[11], pointList[17], "robot");

        linkList[36] = new Link((long)(2036), pointList[0], pointList[1], "wait");
        linkList[37] = new Link((long)(2037), pointList[0], pointList[2], "wait");
        linkList[38] = new Link((long)(2038), pointList[1], pointList[0], "wait");
        linkList[39] = new Link((long)(2039), pointList[1], pointList[2], "wait");
        linkList[40] = new Link((long)(2040), pointList[2], pointList[0], "wait");
        linkList[41] = new Link((long)(2041), pointList[2], pointList[1], "wait");
        linkList[42] = new Link((long)(2042), pointList[3], pointList[4], "wait");
        linkList[43] = new Link((long)(2043), pointList[4], pointList[3], "wait");
        linkList[44] = new Link((long)(2044), pointList[5], pointList[6], "wait");
        linkList[45] = new Link((long)(2045), pointList[6], pointList[5], "wait");
        linkList[46] = new Link((long)(2046), pointList[7], pointList[8], "wait");
        linkList[47] = new Link((long)(2047), pointList[7], pointList[9], "wait");
        linkList[48] = new Link((long)(2048), pointList[8], pointList[7], "wait");
        linkList[49] = new Link((long)(2049), pointList[8], pointList[9], "wait");
        linkList[50] = new Link((long)(2050), pointList[9], pointList[7], "wait");
        linkList[51] = new Link((long)(2051), pointList[9], pointList[8], "wait");
        linkList[52] = new Link((long)(2052), pointList[10], pointList[17], "wait");
        linkList[53] = new Link((long)(2053), pointList[17], pointList[10], "wait");
        linkList[54] = new Link((long)(2054), pointList[13], pointList[14], "wait");
        linkList[55] = new Link((long)(2055), pointList[14], pointList[13], "wait");
        linkList[56] = new Link((long)(2056), pointList[11], pointList[12], "wait");
        linkList[57] = new Link((long)(2057), pointList[12], pointList[11], "wait");
        linkList[58] = new Link((long)(2058), pointList[15], pointList[16], "wait");
        linkList[59] = new Link((long)(2059), pointList[16], pointList[15], "wait");
        setLinkCosts();
    }

    //for setting up the cost and vehicles of that map.
    private void setLinkCosts()
    {
        linkList[0].setWeight((long)(500));   //10
        linkList[0].setVehicleID((long)(1001));
        linkList[1].setWeight((long)(500));   //10
        linkList[1].setVehicleID((long)(1001));
        linkList[2].setWeight((long)(10));
        linkList[2].setVehicleID((long)(1001));
        linkList[3].setWeight((long)(10));
        linkList[3].setVehicleID((long)(1001));
        linkList[4].setWeight((long)(15));
        linkList[4].setVehicleID((long)(1001));
        linkList[5].setWeight((long)(15));
        linkList[5].setVehicleID((long)(1002));
        linkList[6].setWeight((long)(15));
        linkList[6].setVehicleID((long)(1002));
        linkList[7].setWeight((long)(15));
        linkList[7].setVehicleID((long)(1002));

        linkList[8].setWeight((long)(5));
        linkList[8].setVehicleID((long)(2001));
        linkList[9].setWeight((long)(5));
        linkList[9].setVehicleID((long)(2001));
        linkList[10].setWeight((long)(10));
        linkList[10].setVehicleID((long)(2001));
        linkList[11].setWeight((long)(10));
        linkList[11].setVehicleID((long)(2002));
        linkList[12].setWeight((long)(10));
        linkList[12].setVehicleID((long)(2002));
        linkList[13].setWeight((long)(10));
        linkList[13].setVehicleID((long)(2002));
        linkList[14].setWeight((long)(10));
        linkList[14].setVehicleID((long)(2002));
        linkList[15].setWeight((long)(10));
        linkList[15].setVehicleID((long)(2002));
        linkList[16].setWeight((long)(10));
        linkList[16].setVehicleID((long)(2002));
        linkList[17].setWeight((long)(10));
        linkList[17].setVehicleID((long)(2002));
        linkList[18].setWeight((long)(10));
        linkList[18].setVehicleID((long)(2002));
        linkList[19].setWeight((long)(10));
        linkList[19].setVehicleID((long)(2002));

        linkList[20].setWeight((long)(20));
        linkList[20].setVehicleID((long)(2));
        linkList[21].setWeight((long)(20));
        linkList[21].setVehicleID((long)(2));
        linkList[22].setWeight((long)(30));
        linkList[22].setVehicleID((long)(2));
        linkList[23].setWeight((long)(30));
        linkList[23].setVehicleID((long)(1));
        linkList[24].setWeight((long)(20));
        linkList[24].setVehicleID((long)(2));
        linkList[25].setWeight((long)(20));
        linkList[25].setVehicleID((long)(2));
        linkList[26].setWeight((long)(20));
        linkList[26].setVehicleID((long)(2));
        linkList[27].setWeight((long)(20));
        linkList[27].setVehicleID((long)(1));
        linkList[28].setWeight((long)(30));
        linkList[28].setVehicleID((long)(2));
        linkList[29].setWeight((long)(30));
        linkList[29].setVehicleID((long)(1));
        linkList[30].setWeight((long)(20));
        linkList[30].setVehicleID((long)(1));
        linkList[31].setWeight((long)(20));
        linkList[31].setVehicleID((long)(1));
        linkList[32].setWeight((long)(20));
        linkList[32].setVehicleID((long)(1));
        linkList[33].setWeight((long)(20));
        linkList[33].setVehicleID((long)(1));
        linkList[34].setWeight((long)(20));
        linkList[34].setVehicleID((long)(1));
        linkList[35].setWeight((long)(20));
        linkList[35].setVehicleID((long)(1));

        linkList[36].setWeight((long)(0));
        linkList[37].setWeight((long)(5));
        linkList[38].setWeight((long)(0));
        linkList[39].setWeight((long)(5));
        linkList[40].setWeight((long)(0));
        linkList[41].setWeight((long)(0));
        linkList[42].setWeight((long)(20));
        linkList[43].setWeight((long)(10));
        linkList[44].setWeight((long)(10));
        linkList[45].setWeight((long)(0));
        linkList[46].setWeight((long)(0));
        linkList[47].setWeight((long)(20));
        linkList[48].setWeight((long)(10));
        linkList[49].setWeight((long)(20));
        linkList[50].setWeight((long)(10));
        linkList[51].setWeight((long)(0));
        linkList[52].setWeight((long)(20));
        linkList[53].setWeight((long)(0));
        linkList[54].setWeight((long)(0));
        linkList[55].setWeight((long)(10));
        linkList[56].setWeight((long)(10));
        linkList[57].setWeight((long)(40));
        linkList[58].setWeight((long)(10));
        linkList[59].setWeight((long)(20));
    }
}


