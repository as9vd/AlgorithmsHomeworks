// https://docs.google.com/document/d/1oElxXNcoO6N-X7cyuyyVY_P4_tyflgAPES1Z74d-VlY/edit

public class Edge {
    public Node node1;
    public Node node2;
    public int weight;

    public Edge() {
        this.node1 = new Node();
        this.node2 = new Node();
        this.weight = 999;
    }

    public Edge(Node node1, Node node2, int weight) {
        boolean isPort1 = node1.type.equals("port");
        boolean isPort2 = node2.type.equals("port");

        boolean isRailHub1 = node1.type.equals("rail-hub");
        boolean isRailHub2 = node2.type.equals("rail-hub");

        boolean isDistCenter1 = node1.type.equals("dist-center");
        boolean isDistCenter2 = node2.type.equals("dist-center");

        boolean isStore1 = node1.type.equals("store");
        boolean isStore2 = node2.type.equals("store");

        /* 1. Items that arrive at a port can be transported by rail to a rail hub, or by truck to a distribution
        center, but not directly to a store. */
        if ((isPort1 && isRailHub2) || (isPort1 && isDistCenter2)) {
            this.node1 = node1;
            this.node2 = node2;
            this.weight = weight;
        }

        /* 2. Items that arrive at a rail hub can be transported by rail to another rail hub, or by truck
        to a distribution center, but not directly to a store. */
        if ((isRailHub1 && isRailHub2) || (isRailHub1 && isDistCenter2) || (isRailHub1 && isPort2)) {
            this.node1 = node1;
            this.node2 = node2;
            this.weight = weight;
        }

        /* 3. Each distribution center exists to get goods to a particular set of stores.
        Goods arrive by rail or truck from a port or rail hub, and then a truck leaves the distribution
        center and goes to one or more stores that it serves. Goods will never be transported from
        one distribution center to another. */
        if (((isDistCenter1 && isStore2) && node2.disCenter.equals(node1))|| (isDistCenter1 && isRailHub2) || (isDistCenter1 && isPort2)) {
            this.node1 = node1;
            this.node2 = node2;
            this.weight = weight;
        }

        /* 4. Goods will arrive at a store from a distribution center or from another store. Each store
        can only be served by one distribution center. */

        // DistributionCenter might be sus if the disCenter edge has a larger weight than one that happens later in the text file.
        if (((isStore1 && isDistCenter2) && node1.disCenter.equals(node2)) || (isStore1 && isStore2)) {
            this.node1 = node1;
            this.node2 = node2;
            this.weight = weight;
        }
    }

    @Override public String toString() {
        return node1.name + " connects to " + node2.name + " with a cost of " + weight;
    }

}
