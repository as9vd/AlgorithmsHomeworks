public class Node {
    int x;
    int y;
    String status;

    public Node(int y, int x) {
        this.y = y;
        this.x = x;

        if ((x + y) % 2 == 0) {
            status = "Even";
        } else {
            status = "Odd";
        }
    }

    @Override public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Node))
            return false;
        Node node = (Node) o;
        return x == node.x && y == node.y;
    }

    @Override
    public String toString() {
        return x + " " + y;
    }

}
