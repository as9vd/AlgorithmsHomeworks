/**
 * CS4102 Spring 2022 - Unit B Programming
 *********************************************
 * Collaboration Policy: You are encouraged to collaborate with up to 3 other
 * students, but all work submitted must be your own independently written
 * solution. List the computing ids of all of your collaborators in the
 * comments at the top of each submitted file. Do not share written notes,
 * documents (including Google docs, Overleaf docs, discussion notes, PDFs), or
 * code. Do not seek published or online solutions, including pseudocode, for
 * this assignment. If you use any published or online resources (which may not
 * include solutions) when completing this assignment, be sure to cite them. Do
 * not submit a solution that you are unable to explain orally to a member of
 * the course staff. Any solutions that share similar text/code will be
 * considered in breach of this policy. Please refer to the syllabus for a
 * complete description of the collaboration policy.
 *********************************
 * Your Computing ID: as9vd
 * Collaborators: tv9fm, rm6jj, wfm8jns
 * Sources: Introduction to Algorithms, Cormen
 **************************************/
import java.util.ArrayList;
import java.util.List;

// Fix the stuff where stores immediately listed after a distribution center are associated with it.
public class Supply {
    public ArrayList<Node> allNodes = new ArrayList<>();
    public ArrayList<Edge> allEdges = new ArrayList<>();

    public ArrayList<Edge> chosenEdges = new ArrayList<>();

    /**
     # This is the method that should set off the computation
     # of the supply chain problem.  It takes as input a list containing lines of input
     # as strings.  You should parse that input and then call a
     # subroutine that you write to compute the total edge-weight sum
     # and return that values from this method
     #
     # @return the total edge-weight sum of a tree that connects nodes as described
     # in the problem statement
     */
    public int compute(List<String> fileData) {
        String numbers = fileData.get(0); // First line of the input text file.
        int numNodes = Integer.parseInt(numbers.split(" ")[0]);

        Node potentialDist = new Node();
        boolean debounce = false;

        for (int i = 1; i <= numNodes; i++) { // Do something here to check for N = 0 and/or L = 0.
            Node node = new Node(fileData.get(i).split(" ")[0], fileData.get(i).split(" ")[1]);

            // Do something here to assign stores to most recent dist-Centers.
            if (node.type.equals("dist-center")) {
                potentialDist = node;
                debounce = true;
            } else if ((node.type.equals("store")) && (debounce)) {
                node.setDistributionCenter(potentialDist);
            } else {
                debounce = false;
            }

            allNodes.add(node);
        }

        for (int j = numNodes + 1; j < fileData.size(); j++) {
            Node node1 = new Node();
            Node node2 = new Node();

            if (fileData.get(j).isBlank()) {
                continue;
            }

            for (int x = 0; x < allNodes.size(); x++) { // This is just setting node1 and node2.
                if (allNodes.get(x).name.equals(fileData.get(j).split(" ")[0])) {
                    node1 = allNodes.get(x);
                } else if (allNodes.get(x).name.equals(fileData.get(j).split(" ")[1])) {
                    node2 = allNodes.get(x);
                }
            }

            if ((node1.type.equals("store") && (node2.type.equals("dist-center")) && (node1.disCenter == null))) { // Setting a non-disCenter node's disCenter.
                node1.setDistributionCenter(node2);
            } else if ((node2.type.equals("store") && (node1.type.equals("dist-center")) && (node2.disCenter == null))) { // Setting a non-disCenter node's disCenter.
                node2.setDistributionCenter(node1);
            }
            else if ((node2.type.equals("store") && (node1.type.equals("dist-center")) && !(node2.disCenter.equals(node1)))) { // If the store already has a disCenter, continue.
                continue;
            } else if ((node1.type.equals("store") && (node2.type.equals("dist-center")) && !(node1.disCenter.equals(node2)))) { // If the store already has a disCenter, continue.
                continue;
            }

            int weight = Integer.parseInt(fileData.get(j).split(" ")[2]);

            Edge edge = new Edge(node1, node2, weight);

            if (!(edge.node1 == null)) { // e.g. if this is a valid edge.
                allEdges.add(edge);
            }
        }

        int edgeWeightSum = goodKruskals();

        return edgeWeightSum;
    }

    public int goodKruskals() {
        int edgesAccepted = 0;
        int NUM_VERTICES = allNodes.size();

        DisjointSet s = new DisjointSet(); // We update this list as we iterate the vertices.

        for (Node n: allNodes) {
            s.disjointsets.put(n.name, n.name);
        }

        // Why does s2 mistakenly get assigned rh2 instead of rh1? This is the cause of the infinite loop.
        // This could not be mistakenly, idiot; it could be right. Let's do some test cases and find out.
        while (edgesAccepted < NUM_VERTICES - 1) {
            Edge e = getSmallestEdge(); // e = smallest weight edge not deleted yet;

            // edge e = (u, v)
            Node u = e.node1;
            Node v = e.node2;

            // We are checking if the labels of the two vertices are the same.
            String uset = s.findSet(u.name);
            String vset = s.findSet(v.name);

            if (!uset.equals(vset)) { // This "error" shit could actually be giving the right answer, we just need to do something else.
                edgesAccepted++;
                s.union(e.node1.name, e.node2.name);
                deleteSmallestEdge(true);

            } else { // Because this actually keeps going/printing.
                deleteSmallestEdge(false);
                continue;
            }
        }

        int sum = 0;

        for (Edge ed: chosenEdges) {
            sum = sum + ed.weight;
        }

        return sum;
    }

    public Edge getSmallestEdge() {
        int x = 99999;
        Edge returnEdge = new Edge();

        for (Edge edge: allEdges) {
            if (edge.weight < x) {
                x = edge.weight;
                returnEdge = edge;
            }
        }

        return returnEdge;
    }

    public void deleteSmallestEdge(boolean addOrNo) {
        int x = 99999;
        Edge returnEdge = new Edge();

        for (Edge edge: allEdges) {
            if (edge.weight < x) {
                x = edge.weight;
                returnEdge = edge;
            }
        }

        allEdges.remove(returnEdge);

        if (addOrNo) {
            chosenEdges.add(returnEdge);
        }
    }

}
