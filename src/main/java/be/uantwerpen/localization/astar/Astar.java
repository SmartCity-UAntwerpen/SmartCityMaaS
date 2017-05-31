package be.uantwerpen.localization.astar;

import java.util.ArrayList;
import java.util.List;


import be.uantwerpen.model.Point;
import be.uantwerpen.model.Link;
import be.uantwerpen.services.GraphBuilder;
import be.uantwerpen.services.JobService;
import be.uantwerpen.services.JobListService;
import org.graphstream.algorithm.AStar;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.Path;
import org.graphstream.graph.implementations.SingleGraph;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class Astar {

    @Autowired
    private JobService jobService;
    @Autowired
    private JobListService jobListService;
    GraphBuilder graphBuilder;

    //     B-(1)-C
    //    /       \
    //  (1)       (10)
    //  /           \
    // A             F
    //  \           /
    //  (1)       (1)
    //    \       /
    //     D-(1)-E
    /*static String my_graph = 
            "DGS004\n" 
            + "my 0 0\n" 
            + "an A xy: 0,1\n" 
            + "an B xy: 1,2\n"
            + "an C xy: 2,2\n"
            + "an D xy: 1,0\n"
            + "an E xy: 2,0\n"
            + "an F xy: 3,1\n"
            + "ae AB A B weight:1 \n"
            + "ae AD A D weight:1 \n"
            + "ae BC B C weight:1 \n"
            + "ae CF C F weight:10 \n"
            + "ae FC F C weight:1 \n"
            + "ae ED E D weight:10 \n"
            + "ae DE D E weight:1 \n"
            + "ae EF E F weight:1 \n"
            ;
            
                    public static void main(String[] args) throws IOException {
                Graph graph = new DefaultGraph("A* Test");
                StringReader reader = new StringReader(my_graph);
 
                FileSourceDGS source = new FileSourceDGS();
                source.addSink(graph);
                source.readAll(reader);
 
                AStar astar = new AStar(graph);
                //astar.setCosts(new DistanceCosts());
                astar.compute("C", "F");
 
                System.out.println(astar.getShortestPath());
        }*/


    public Astar() {

        /*graphBuilder = new GraphBuilder();
        graphBuilder.setUpTest();
        graphBuilder.setLinkCosts();*/
    }

    public void init(JobService jobService, JobListService jobListService) {
        this.jobService = jobService;
        this.jobListService = jobListService;
        graphBuilder = new GraphBuilder();
        graphBuilder.setUpTest();
    }

     /*   public static void main(String[] args) throws IOException {
    }*/


    public void startAStar() {
        Graph graph = new SingleGraph("A* Test");

 /*       //testfiles met apparte dummy methodes voor genereren van Graph
        testMakeNode(graph);
        testMakeEdge(graph);
        testDeterminePath(graph, "A", "K");*/

        //Testfiles met correcte graaf
        makeNode(graph, graphBuilder);
        makeEdge(graph, graphBuilder);
        testDeterminePath(graph, "1004", "1015");
        testDeterminePath(graph, "1014", "1002");

    }

/*    public void makeNode(List<Point> namenodes, Graph graph) {
        List<Point> nodes = new ArrayList<Point>();
        // all the nodes
        for (int i = 0; i < namenodes.size(); i++) {
            nodes.add(graph.addNode(namenodes.get(i).getId().toString()));
            graph.getNode(i).setAttribute("xy", namenodes.get(i).getX(), namenodes.get(i).getY());
        }
    }*/

    public void makeNode(Graph graph, GraphBuilder graphbuilder) {
        Point[] listOfPoints = graphbuilder.getPointList();
        List<Node> nodes = new ArrayList<Node>();
        // provide all the nodes
        for (int i = 0; i < listOfPoints.length; i++) {
            nodes.add(graph.addNode(listOfPoints[i].getId().toString()));
            graph.getNode(i).setAttribute("xy", listOfPoints[i].getX(), listOfPoints[i].getY());
        }
        // TODO: boolean mss returnen voor succes of failure?
    }

    public void testMakeNode( Graph graph) {
        List<Knoop> knopen = testMakeNodeList();
        List<Node> nodes = new ArrayList<Node>();
        // all the nodes
        for (int i = 0; i < knopen.size(); i++) {
            nodes.add(graph.addNode(knopen.get(i).getName()));
            graph.getNode(i).setAttribute("xy", knopen.get(i).getX(), knopen.get(i).getY());
        }
    }

/*    public void makeEdge(Graph graph, List<Link> links) {
        for (int i = 0; i < links.size(); i++) {
            graph.addEdge(links.get(i).getId().toString(), links.get(i).getStartPoint().getId().toString(), links.get(i).getStopPoint().getId().toString(), true);
            graph.getEdge(links.get(i).getId().toString()).setAttribute("weight", links.get(i).getWeight());
        }
    }*/

    public void makeEdge(Graph graph, GraphBuilder graphbuilder) {
        Link[] listOfEdges = graphbuilder.getLinkList();
        for (int i = 0; i < listOfEdges.length; i++){
            graph.addEdge(listOfEdges[i].getId().toString(), listOfEdges[i].getStartPoint().getId().toString(), listOfEdges[i].getStopPoint().getId().toString(), true);
            graph.getEdge(listOfEdges[i].getId().toString()).setAttribute("weight", listOfEdges[i].getWeight());
        }
    }


    public void testMakeEdge(Graph graph) {
        List<Links> linken = testMakeEdgeList();
        for (int i = 0; i < linken.size(); i++) {
            graph.addEdge(linken.get(i).getName(), linken.get(i).getStartPos(), linken.get(i).getEndPos(), true);
            graph.getEdge(linken.get(i).getName()).setAttribute("weight", linken.get(i).getWeight());
            // type voertuig insteken. nog na te kijken.
            // graph.getEdge(links(i).getName()).setAttribute("weight", links(i).getWeight());
        }
    }

    public void testDeterminePath(Graph graph, String startPos, String endPos) {
        AStar astar = new AStar(graph);
        astar.compute(startPos, endPos);
        System.out.println(astar.getShortestPath());
        //TODO: verder werken naar jobdispatching van hier uit
        Path path = astar.getShortestPath();
        JobDispatching jd = new JobDispatching(jobService, jobListService, path.toString(), graphBuilder);



    }
/*    public String determinePath(Graph graph, String startPos, String endPos) {
        AStar astar = new AStar(graph);
        //astar.setCosts(new DistanceCosts());
        astar.compute(startPos, endPos);
        Path path = astar.getShortestPath();
        return path.toString();
    }*/

    /* making test methodes that should later be taken over by code from Dries
    methodes to be removed:
    makeNodeList
     */

    private List<Knoop> testMakeNodeList() {
        Knoop a = new Knoop("A", 4, 1);
        Knoop b = new Knoop("B", 2, 1);
        Knoop c = new Knoop("C", 0, 0);
        Knoop d = new Knoop("D", 4, 7);
        Knoop e = new Knoop("E", 2, 6);
        Knoop f = new Knoop("F", 0, 6);
        Knoop g = new Knoop("G", 4, 47);
        Knoop h = new Knoop("H", 2, 47);
        Knoop i = new Knoop("I", 0, 46);
        Knoop j = new Knoop("J", 4, 53);
        Knoop k = new Knoop("K", 2, 52);
        Knoop l = new Knoop("L", 0, 52);

        List<Knoop> knopen = new ArrayList<Knoop>();
        knopen.add(a);knopen.add(b);knopen.add(c);knopen.add(d);knopen.add(e);knopen.add(f);knopen.add(g);knopen.add(h);knopen.add(i);knopen.add(j);knopen.add(k);knopen.add(l);

        return knopen;
    }

    private List<Links> testMakeEdgeList() {

// all connections from node A to all neighbouring nodes, along with the weight of those edge

        List<Links> linken = new ArrayList<Links>();
        // alle links A
        Links ab = new Links("AB", "A", "B", (long)11.5);
        Links ba = new Links("BA", "B", "A", (long)16.5);
        Links ac = new Links("AC", "A", "C", (long)10.5);
        Links ca = new Links("CA", "C", "A", (long)15.5);
        Links ad = new Links("AD", "A", "D", (long)5);
        Links da = new Links("DA", "D", "A", (long)8);
        Links ae = new Links("AE", "A", "E", (long)6);
        Links ea = new Links("EA", "E", "A", (long)9);
        Links af = new Links("AF", "A", "F", (long)8.5);
        Links fa = new Links("FA", "F", "A", (long)8.5);
        linken.add(ab);linken.add(ba);linken.add(ac);linken.add(ca);linken.add(ad);linken.add(da);linken.add(ae);linken.add(ea);linken.add(af);linken.add(fa);

        // alle links B
        Links bc = new Links("BC", "B", "C", (long)2);
        Links cb = new Links("CB", "C", "B", (long)2);
        Links bd = new Links("BD", "B", "D", (long)18.5);
        Links db = new Links("DB", "D", "B", (long)18.5);
        Links be = new Links("BE", "B", "E", (long)19.5);
        Links eb = new Links("EB", "E", "B", (long)19.5);
        Links bf = new Links("BF", "B", "F", (long)9);
        Links fb = new Links("FB", "F", "B", (long)6);
        linken.add(bc);linken.add(cb);linken.add(bd);linken.add(db);linken.add(be);linken.add(eb);linken.add(bf);linken.add(fb);

        // alle links C
        Links cd = new Links("CD", "C", "D", (long)17.5);
        Links dc = new Links("DC", "D", "C", (long)17.5);
        Links ce = new Links("CE", "C", "E", (long)18.5);
        Links ec = new Links("EC", "E", "C", (long)18.5);
        Links cf = new Links("CF", "C", "F", (long)8);
        Links fc = new Links("FC", "F", "C", (long)5);
        linken.add(cd);linken.add(dc);linken.add(ce);linken.add(ec);linken.add(cf);linken.add(fc);

        // alle links D
        Links de = new Links("DE", "D", "E", (long)2);
        Links ed = new Links("ED", "E", "D", (long)2);
        Links df = new Links("DF", "D", "F", (long)15.5);
        Links fd = new Links("FD", "F", "D", (long)10.5);
        Links dg = new Links("DG", "D", "G", (long)40);
        Links gd = new Links("GD", "G", "D", (long)40);
        Links dh = new Links("DH", "D", "H", (long)50);
        Links hd = new Links("HD", "H", "D", (long)50);
        linken.add(de);linken.add(ed);linken.add(df);linken.add(fd);linken.add(dg);linken.add(gd);linken.add(dh);linken.add(hd);

        // alle links E
        Links ef = new Links("EF", "E", "F", (long)16.5);
        Links fe = new Links("FE", "F", "E", (long)11.5);
        Links eg = new Links("EG", "E", "G", (long)50);
        Links ge = new Links("GE", "G", "E", (long)50);
        Links eh = new Links("EH", "E", "H", (long)40);
        Links he = new Links("HE", "H", "E", (long)40);
        Links ei = new Links("EI", "E", "I", (long)50);
        Links ie = new Links("IE", "I", "E", (long)50);
        linken.add(ef);linken.add(fe);linken.add(eg);linken.add(ge);linken.add(eh);linken.add(he);linken.add(ei);linken.add(ie);

        //alle links F
        Links fh = new Links("FH", "F", "H", (long)40);
        Links hf = new Links("HF", "H", "F", (long)40);
        Links fi = new Links("FI", "F", "I", (long)50);
        Links if1 = new Links("IF", "I", "F", (long)50);
        linken.add(fh);linken.add(hf);linken.add(fi);linken.add(if1);

        // alle links G
        Links gh = new Links("GH", "G", "H", (long)11.5);
        Links hg = new Links("HG", "H", "G", (long)16.5);
        Links gi = new Links("GI", "G", "I", (long)10.5);
        Links ig = new Links("IG", "I", "G", (long)15.5);
        Links gj = new Links("GJ", "G", "J", (long)5);
        Links jg = new Links("JG", "J", "G", (long)8);
        Links gk = new Links("GK", "G", "K", (long)6);
        Links kg = new Links("KG", "K", "G", (long)9);
        Links gl = new Links("GL", "G", "L", (long)8.5);
        Links lg = new Links("LG", "L", "G", (long)8.5);
        linken.add(gh);linken.add(hg);linken.add(gi);linken.add(ig);linken.add(gj);linken.add(jg);linken.add(gk);linken.add(kg);linken.add(gl);linken.add(lg);

        // alle links G
        Links hi = new Links("HI", "H", "I", (long)2);
        Links ih = new Links("IH", "I", "H", (long)2);
        Links hj = new Links("HJ", "H", "J", (long)18.5);
        Links jh = new Links("JH", "J", "H", (long)18.5);
        Links hk = new Links("HK", "H", "K", (long)19.5);
        Links kh = new Links("KH", "K", "H", (long)19.5);
        Links hl = new Links("HL", "H", "L", (long)9);
        Links lh = new Links("LH", "L", "H", (long)6);
        linken.add(hi);linken.add(ih);linken.add(hj);linken.add(jh);linken.add(hk);linken.add(kh);linken.add(hl);linken.add(lh);

        // alle links I
        Links ij = new Links("IJ", "I", "J", (long)17.5);
        Links ji = new Links("JI", "J", "I", (long)17.5);
        Links ik = new Links("IK", "I", "K", (long)18.5);
        Links ki = new Links("KI", "K", "I", (long)18.5);
        Links il = new Links("IL", "I", "L", (long)8);
        Links li = new Links("LI", "L", "I", (long)5);
        linken.add(ij);linken.add(ji);linken.add(ik);linken.add(ki);linken.add(il);linken.add(li);

        // alle links j
        Links jk = new Links("JK", "J", "K", (long)2);
        Links kj = new Links("KJ", "K", "J", (long)2);
        Links jl = new Links("JL", "J", "L", (long)15.5);
        Links lj = new Links("LJ", "L", "J", (long)10.5);
        linken.add(jk);linken.add(kj);linken.add(jl);linken.add(lj);

        // alle links k
        Links kl = new Links("KL", "K", "L", (long)15.5);
        Links lk = new Links("LK", "L", "K", (long)10.5);
        linken.add(kl);linken.add(lk);

        return linken;
    }
}
