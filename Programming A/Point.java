public class Point implements Comparable<Point> {
    double x;
    double y;

    public Point(String x, String y) {
        this.x = Float.parseFloat(x);
        this.y = Float.parseFloat(y);
    }

    @Override
    public String toString() {
        return "X: " + getX() + ", Y: " + getY();
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }

        /* Check if o is an instance of Complex or not
          "null instanceof [type]" also returns false */
        if (!(o instanceof Point)) {
            return false;
        }

        // typecast o to Complex so that we can compare data members
        Point c = (Point) o;

        // Compare the data members and return accordingly
        return (this.x == c.getX()) && (this.y == c.getY());
    }

    @Override
    public int compareTo(Point o) {
        if (this.x > o.getX()) {
            return 1;
        } else if (this.x < o.getX()) {
            return -1;
        }

        return 0;
    }

    @Override public int hashCode() {
        return ((int) this.x + (int) this.y);
    }
}
