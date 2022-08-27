import org.jgrapht.graph.DefaultWeightedEdge;

public class Edge extends DefaultWeightedEdge {
    Node n1;
    Node n2;
    int capacity;

    public Edge(Node n1, Node n2) {
        this.n1 = n1;
        this.n2 = n2;

        capacity = 0;
    }

    @Override
    public String toString() {
        return this.n1 + " " + this.n2;
    }

}
