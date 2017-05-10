package be.uantwerpen.localization.astar;

import java.io.IOException;
import java.io.StringReader;
 



import org.graphstream.algorithm.AStar;
import org.graphstream.algorithm.AStar.DistanceCosts;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
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
    public void startAStar(){
            Graph graph = new SingleGraph("A* Test");
            
            // all the nodes
            Node a = graph.addNode("A");
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
            
            System.out.println(astar.getShortestPath());
            
    }
    }
