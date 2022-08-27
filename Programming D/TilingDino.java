/**
 * CS4102 Spring 2022 -- Unit D Programming
 *********************************
 * Collaboration Policy: You are encouraged to collaborate with up to 3 other
 * students, but all work submitted must be your own independently written
 * solution. List the computing ids of all of your collaborators in the comment
 * at the top of your java or python file. Do not seek published or online
 * solutions for any assignments. If you use any published or online resources
 * (which may not include solutions) when completing this assignment, be sure to
 * cite them. Do not submit a solution that you are unable to explain orally to a
 * member of the course staff.
 *********************************
 * Your Computing ID: as9vd
 * Collaborators: tv9fm, rm6jj, wfm8jns
 * Sources: Introduction to Algorithms, Cormen
 **************************************/

import org.jgrapht.Graph;
import org.jgrapht.alg.flow.EdmondsKarpMFImpl;
import org.jgrapht.alg.interfaces.MaximumFlowAlgorithm;
import org.jgrapht.graph.DefaultDirectedWeightedGraph;
import org.jgrapht.graph.DirectedAcyclicGraph;
import org.jgrapht.graph.DirectedWeightedMultigraph;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import java.util.List;
import java.util.ArrayList;

public class TilingDino {
    public ArrayList<Node> nodes = new ArrayList<>();
    public ArrayList<Edge> edges = new ArrayList<>();
    public ArrayList<Node> chosenNodes = new ArrayList<>();

    public ArrayList<Edge> sourceEdges = new ArrayList<>();
    public ArrayList<Edge> sinkEdges = new ArrayList<>();

    /**
     * This is the method that should set off the computation
     * of tiling dino.  It takes as input a list lines of input
     * as strings.  You should parse that input, find a tiling,
     * and return a list of strings representing the tiling
     *
     * @return the list of strings representing the tiling
     */
    public List<String> compute(List<String> fileLines) {
        int columnCount = fileLines.get(0).length();
        int rowCount = fileLines.size();

        Node source = new Node(-69, -69); Node sink = new Node(-68, -68);

        // LOOK, this adds all the right nodes.
        // Thank Goodness for that.
        for (int r = 0; r < rowCount; r++) {
            for (int c = 0; c < columnCount; c++) {
                if (fileLines.get(r).charAt(c) == '#') {
                    Node node = new Node(r, c);
                    nodes.add(node);
                }
            }
        }

        // Left or below is the move I reckon. It's good we move through x first, however.
        // LOOK, this works properly. All the right edges are present, albeit in a different order than the PDF.
        // We also distribute sink/source status properly.
        for (int i = 0; i < nodes.size(); i++) {
            if (!(chosenNodes.contains(nodes.get(i)))) {
                Node above = new Node(nodes.get(i).y - 1, nodes.get(i).x);
                Node left = new Node(nodes.get(i).y, nodes.get(i).x - 1);

                 if (nodes.contains(above) && !(chosenNodes.contains(above))) {
                    if (nodes.get(i).status.equals("Odd")) {
                        edges.add(new Edge(nodes.get(i), above));

                        sourceEdges.add(new Edge(source, nodes.get(i)));
                        sinkEdges.add(new Edge(above, sink));
                    } else {
                        edges.add(new Edge(above, nodes.get(i)));

                        sinkEdges.add(new Edge(nodes.get(i), sink));
                        sourceEdges.add(new Edge(source, above));
                    }

                    chosenNodes.add(nodes.get(i));
                    chosenNodes.add(above);
                } else if (nodes.contains(left) && !(chosenNodes.contains(left))) {
                    if (nodes.get(i).status.equals("Odd")) {
                        edges.add(new Edge(nodes.get(i), left));

                        sourceEdges.add(new Edge(source, nodes.get(i)));
                        sinkEdges.add(new Edge(left, sink));
                    } else {
                        edges.add(new Edge(left, nodes.get(i)));

                        sinkEdges.add(new Edge(nodes.get(i), sink));
                        sourceEdges.add(new Edge(source, left));
                    }

                    chosenNodes.add(nodes.get(i));
                    chosenNodes.add(left);
                }
            }
        }

        List<String> result = new ArrayList<>();

        // Make a directed graph and add nodes/edges from the lists above.
        // Add a source + sink afterwards and connect all evens to one and odds to another.
        // Make sure the capacity of each edge is 1.
        Graph<Node, Edge> graph = new SimpleDirectedWeightedGraph(Edge.class);

        for (int i = 0; i < edges.size(); i++) {
            graph.addVertex(edges.get(i).n1);
            graph.addVertex(edges.get(i).n2);
        }

        graph.addVertex(source); graph.addVertex(sink);

        // LOOK, this accurately sets the weight/capacity of the edges to be 1.
        for (int i = 0; i < edges.size(); i++) {
            Node n1 = edges.get(i).n1;
            Node n2 = edges.get(i).n2;

            graph.addEdge(n1, n2, edges.get(i));

            // We assign the source to the even one and the sink to the odd one.
            if (n1.status.equals("Even")) {
                graph.addEdge(n1, sink, sinkEdges.get(i));
                graph.addEdge(source, n2, sourceEdges.get(i));
            } else if (n1.status.equals("Odd")) {
                graph.addEdge(source, n1, sourceEdges.get(i));
                graph.addEdge(n2, sink, sinkEdges.get(i));
            }
        }

        MaximumFlowAlgorithm<Node, Edge> mf = new EdmondsKarpMFImpl<>(graph);

        if (mf.getMaximumFlow(source, sink).getValue() == nodes.size() / 2) {
            for (int i = 0; i < edges.size(); i++) {
                result.add(edges.get(i).toString());
            }
        } else {
            result.add("impossible");
        }

        return result;
    }
}
