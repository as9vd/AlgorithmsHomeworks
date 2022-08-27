public class Node {
    String name;
    String type;

    Node disCenter;

    public Node() {}

    public Node(String name, String type) {
        this.name = name;
        this.type = type;
    }

    public void setDistributionCenter(Node disCenter) {
        this.disCenter = disCenter;
    }

    @Override public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Node))
            return false;
        Node node = (Node) o;
        return name.equals(node.name) && type.equals(node.type);
    }

    public String toString() {
        return "Name: " + this.name + ", Type: " + this.type;
    }

}
