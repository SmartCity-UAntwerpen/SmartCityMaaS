package be.uantwerpen.localization.astar;

import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;


import org.graphstream.algorithm.AStar;
import org.graphstream.algorithm.AStar.DistanceCosts;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.Path;
import org.graphstream.graph.implementations.DefaultGraph;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.stream.file.FileSourceDGS;

public class Astar {

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

    }

     /*   public static void main(String[] args) throws IOException {
    }*/


    public void startAStar() {
        Graph graph = new SingleGraph("A* Test");

            /*List<Node> nodes = new ArrayList<Node>();
            // all the nodes
            for(int i = 0; i < 15; i++)
            {
                    nodes.add(graph.addNode(Integer.toString(i)));
            }*/
 /*       Node a = graph.addNode("A");
        Node b = graph.addNode("B");
        Node c = graph.addNode("C");
        Node d = graph.addNode("D");
        Node e = graph.addNode("E");
        Node f = graph.addNode("F");
        Node g = graph.addNode("G");
        Node h = graph.addNode("H");
        Node i = graph.addNode("I");
        Node j = graph.addNode("J");
        Node k = graph.addNode("K");
        Node l = graph.addNode("L");


        // all coordinates from all the nodes (GPS coordinates)
        a.setAttribute("xy", 4, 1);
        b.setAttribute("xy", 2, 1);
        c.setAttribute("xy", 0, 0);
        d.setAttribute("xy", 4, 7);
        e.setAttribute("xy", 2, 6);
        f.setAttribute("xy", 0, 6);
        g.setAttribute("xy", 4, 47);
        h.setAttribute("xy", 2, 47);
        i.setAttribute("xy", 0, 46);
        j.setAttribute("xy", 4, 53);
        k.setAttribute("xy", 2, 52);
        l.setAttribute("xy", 0, 52);

        // all connections from node A to all neighbouring nodes, along with the weight of those edge
        graph.addEdge("AB", "A", "B", true);
        graph.getEdge("AB").setAttribute("weight", 11.5);
        graph.addEdge("BA", "B", "A", true);
        graph.getEdge("BA").setAttribute("weight", 16.5);
        graph.addEdge("AC", "A", "C", true);
        graph.getEdge("AC").setAttribute("weight", 10.5);
        graph.addEdge("CA", "C", "A", true);
        graph.getEdge("CA").setAttribute("weight", 15.5);
        graph.addEdge("AD", "A", "D", true);
        graph.getEdge("AD").setAttribute("weight", 5);
        graph.addEdge("DA", "D", "A", true);
        graph.getEdge("DA").setAttribute("weight", 8);
        graph.addEdge("AE", "A", "E", true);
        graph.getEdge("AE").setAttribute("weight", 6);
        graph.addEdge("EA", "E", "A", true);
        graph.getEdge("EA").setAttribute("weight", 9);
        graph.addEdge("AF", "A", "F", true);
        graph.getEdge("AF").setAttribute("weight", 8.5);
        graph.addEdge("FA", "F", "A", true);
        graph.getEdge("FA").setAttribute("weight", 8.5);

        // all connections from node B to all neighbouring nodes, along with the weight of those edges
        graph.addEdge("BC", "B", "C", true);
        graph.getEdge("BC").setAttribute("weight", 2);
        graph.addEdge("CB", "C", "B", true);
        graph.getEdge("CB").setAttribute("weight", 2);
        graph.addEdge("BD", "B", "D", true);
        graph.getEdge("BD").setAttribute("weight", 18.5);
        graph.addEdge("DB", "D", "B", true);
        graph.getEdge("DB").setAttribute("weight", 18.5);
        graph.addEdge("BE", "B", "E", true);
        graph.getEdge("BE").setAttribute("weight", 19.5);
        graph.addEdge("EB", "E", "B", true);
        graph.getEdge("EB").setAttribute("weight", 19.5);
        graph.addEdge("BF", "B", "F", true);
        graph.getEdge("BF").setAttribute("weight", 9);
        graph.addEdge("FB", "F", "B", true);
        graph.getEdge("FB").setAttribute("weight", 6);

        // all connections from node C to all neighbouring nodes, along with the weight of those edges
        graph.addEdge("CD", "C", "D", true);
        graph.getEdge("CD").setAttribute("weight", 17.5);
        graph.addEdge("DC", "D", "C", true);
        graph.getEdge("DC").setAttribute("weight", 17.5);
        graph.addEdge("CE", "C", "E", true);
        graph.getEdge("CE").setAttribute("weight", 18.5);
        graph.addEdge("EC", "E", "C", true);
        graph.getEdge("EC").setAttribute("weight", 18.5);
        graph.addEdge("CF", "C", "F", true);
        graph.getEdge("CF").setAttribute("weight", 8);
        graph.addEdge("FC", "F", "C", true);
        graph.getEdge("FC").setAttribute("weight", 5);

        // all connections from node D to all neighbouring nodes, along with the weight of those edges
        graph.addEdge("DE", "D", "E", true);
        graph.getEdge("DE").setAttribute("weight", 2);
        graph.addEdge("ED", "E", "D", true);
        graph.getEdge("ED").setAttribute("weight", 2);
        graph.addEdge("DF", "D", "F", true);
        graph.getEdge("DF").setAttribute("weight", 15.5);
        graph.addEdge("FD", "F", "D", true);
        graph.getEdge("FD").setAttribute("weight", 10.5);
        graph.addEdge("DG", "D", "G", true);
        graph.getEdge("DG").setAttribute("weight", 40);
        graph.addEdge("GD", "G", "D", true);
        graph.getEdge("GD").setAttribute("weight", 40);
        graph.addEdge("DH", "D", "H", true);
        graph.getEdge("DH").setAttribute("weight", 50);
        graph.addEdge("HD", "H", "D", true);
        graph.getEdge("HD").setAttribute("weight", 50);

        // all connections from node E to all neighbouring nodes, along with the weight of those edges
        graph.addEdge("EF", "E", "F", true);
        graph.getEdge("EF").setAttribute("weight", 16.5);
        graph.addEdge("FE", "F", "E", true);
        graph.getEdge("FE").setAttribute("weight", 11.5);
        graph.addEdge("EG", "E", "G", true);
        graph.getEdge("EG").setAttribute("weight", 50);
        graph.addEdge("GE", "G", "E", true);
        graph.getEdge("GE").setAttribute("weight", 50);
        graph.addEdge("EH", "E", "H", true);
        graph.getEdge("EH").setAttribute("weight", 40);
        graph.addEdge("HE", "H", "E", true);
        graph.getEdge("HE").setAttribute("weight", 40);
        graph.addEdge("EI", "E", "I", true);
        graph.getEdge("EI").setAttribute("weight", 50);
        graph.addEdge("IE", "I", "E", true);
        graph.getEdge("IE").setAttribute("weight", 50);

        // all connections from node F to all neighbouring nodes, along with the weight of those edges
        graph.addEdge("FH", "F", "H", true);
        graph.getEdge("FH").setAttribute("weight", 50);
        graph.addEdge("HF", "H", "F", true);
        graph.getEdge("HF").setAttribute("weight", 50);
        graph.addEdge("FI", "F", "I", true);
        graph.getEdge("FI").setAttribute("weight", 40);
        graph.addEdge("IF", "I", "F", true);
        graph.getEdge("IF").setAttribute("weight", 40);

        // all connections from node G to all neighbouring nodes, along with the weight of those edges
        graph.addEdge("GH", "G", "H", true);
        graph.getEdge("GH").setAttribute("weight", 11.5);
        graph.addEdge("HG", "H", "G", true);
        graph.getEdge("HG").setAttribute("weight", 16.5);
        graph.addEdge("GI", "G", "I", true);
        graph.getEdge("GI").setAttribute("weight", 10.5);
        graph.addEdge("IG", "I", "G", true);
        graph.getEdge("IG").setAttribute("weight", 15.5);
        graph.addEdge("GJ", "G", "J", true);
        graph.getEdge("GJ").setAttribute("weight", 5);
        graph.addEdge("JG", "J", "G", true);
        graph.getEdge("JG").setAttribute("weight", 8);
        graph.addEdge("GK", "G", "K", true);
        graph.getEdge("GK").setAttribute("weight", 6);
        graph.addEdge("KG", "K", "G", true);
        graph.getEdge("KG").setAttribute("weight", 9);
        graph.addEdge("GL", "G", "L", true);
        graph.getEdge("GL").setAttribute("weight", 8.5);
        graph.addEdge("LG", "L", "G", true);
        graph.getEdge("LG").setAttribute("weight", 8.5);

        // all connections from node H to all neighbouring nodes, along with the weight of those edges
        graph.addEdge("HI", "H", "I", true);
        graph.getEdge("HI").setAttribute("weight", 2);
        graph.addEdge("IH", "I", "H", true);
        graph.getEdge("IH").setAttribute("weight", 2);
        graph.addEdge("HJ", "H", "J", true);
        graph.getEdge("HJ").setAttribute("weight", 18.5);
        graph.addEdge("JH", "J", "H", true);
        graph.getEdge("JH").setAttribute("weight", 18.5);
        graph.addEdge("HK", "H", "K", true);
        graph.getEdge("HK").setAttribute("weight", 19.5);
        graph.addEdge("KH", "K", "H", true);
        graph.getEdge("KH").setAttribute("weight", 19.5);
        graph.addEdge("HL", "H", "L", true);
        graph.getEdge("HL").setAttribute("weight", 9);
        graph.addEdge("LH", "L", "H", true);
        graph.getEdge("LH").setAttribute("weight", 6);

        // all connections from node I to all neighbouring nodes, along with the weight of those edges
        graph.addEdge("IJ", "I", "J", true);
        graph.getEdge("IJ").setAttribute("weight", 17.5);
        graph.addEdge("JI", "J", "I", true);
        graph.getEdge("JI").setAttribute("weight", 17.5);
        graph.addEdge("IK", "I", "K", true);
        graph.getEdge("IK").setAttribute("weight", 18.5);
        graph.addEdge("KI", "K", "I", true);
        graph.getEdge("KI").setAttribute("weight", 18.5);
        graph.addEdge("IL", "I", "L", true);
        graph.getEdge("IL").setAttribute("weight", 8);
        graph.addEdge("LI", "L", "I", true);
        graph.getEdge("LI").setAttribute("weight", 5);

        // all connections from node J to all neighbouring nodes, along with the weight of those edges
        graph.addEdge("JK", "J", "K", true);
        graph.getEdge("JK").setAttribute("weight", 2);
        graph.addEdge("KJ", "K", "J", true);
        graph.getEdge("KJ").setAttribute("weight", 2);
        graph.addEdge("JL", "J", "L", true);
        graph.getEdge("JL").setAttribute("weight", 15.5);
        graph.addEdge("LJ", "L", "J", true);
        graph.getEdge("LJ").setAttribute("weight", 10.5);

        // all connections from node K to all neighbouring nodes, along with the weight of those edges
        graph.addEdge("KL", "K", "L", true);
        graph.getEdge("KL").setAttribute("weight", 16.5);
        graph.addEdge("LK", "L", "K", true);
        graph.getEdge("LK").setAttribute("weight", 11.5);

        AStar astar = new AStar(graph);
        //astar.setCosts(new DistanceCosts());
        astar.compute("A", "K");

        System.out.println(astar.getShortestPath());*/

        testMakeNode(graph);
        testMakeEdge(graph);
        testDeterminePath(graph, "A", "K");

    }

 /*   public void makeNode(List<String> namenodes, Graph graph) {
        List<Node> nodes = new ArrayList<Node>();
        // all the nodes
        for (int i = 0; i < namenodes.size(); i++) {
            nodes.add(graph.addNode(namenodes.get(i).getName()));
            graph.getNode(i).setAttribute("xy", namenodes.get(i).getX(), namenodes.get(i).getY());
        }
    }*/

    public void testMakeNode( Graph graph) {
        List<Knoop> knopen = testMakeNodeList();
        List<Node> nodes = new ArrayList<Node>();
        // all the nodes
        for (int i = 0; i < knopen.size(); i++) {
            nodes.add(graph.addNode(knopen.get(i).getName()));
            graph.getNode(i).setAttribute("xy", knopen.get(i).getX(), knopen.get(i).getY());
        }
    }

/*    public void makeEdge(Graph graph, List<Links> links) {
        for (int i = 0; i < links.size(); i++) {
            graph.addEdge(links(i).getName(), links(i).getStart(), links(i).getEnd(), true);
            graph.getEdge(links(i).getName()).setAttribute("weight", links(i).getWeight());
            // type voertuig insteken. nog na te kijken.
            // graph.getEdge(links(i).getName()).setAttribute("weight", links(i).getWeight());
        }
    }*/


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
        astar.compute("B", "L");
        System.out.println(astar.getShortestPath());
        Path path = astar.getShortestPath();
        List<Links> linken = testMakeEdgeList();
        JobDispatching jd = new JobDispatching(path.toString(), linken);



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
