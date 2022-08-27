import java.util.Hashtable;

public class DisjointSet {
    public Hashtable<String, String> disjointsets; // We will pair a Node with its parent.

    public DisjointSet() {
        disjointsets = new Hashtable<>();
    }

    // Dictionary: key is node; value is root.
    String findSet(String i) { // We're looking for node i's set that it belongs to.
        // Parent of the input node; if it equals itself, then we've found the set.
        String parent = disjointsets.get(i);

        if (!parent.equals(i)) {
            parent = findSet(parent);
        }

        return parent;
    }

    void union(String i, String j) {
        String label1 = findSet(i);
        String label2 = findSet(j);

        disjointsets.replace(label1, label2);
    }

}
