import java.util.Objects;

public class Distance implements Comparable<Distance> {
    Point point1;
    Point point2;
    double distance;

    public Distance(Point point1, Point point2, double distance) {
        if (point1.getX() < point2.getX()) {
            this.point1 = point1;
            this.point2 = point2;
        } else if (point1.getX() > point2.getX()) {
            this.point2 = point1;
            this.point1 = point2;
        } else {
            if (point1.getY() < point2.getY()) {
                this.point1 = point1;
                this.point2 = point2;
            } else {
                this.point2 = point1;
                this.point1 = point2;
            }
        }

        this.distance = distance;
    }

    @Override public String toString() {
        return "[" + point1 + ", " + point2 + "]: " + distance;
    }

    @Override public int compareTo(Distance o) {
        if (this.distance > o.distance) {
            return 1;
        } else if (this.distance < o.distance) {
            return -1;
        }

        return 0;
    }

    @Override public int hashCode() {
        return Objects.hash(point1, point2);
    }

    @Override public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Distance))
            return false;
        Distance distance1 = (Distance) o;

        boolean equals1;
        boolean equals2;

        if (point1.equals(distance1.point1)) {
            equals1 = true;

            if (point2.equals(distance1.point2)) {
                equals2 = true;
            } else {
                equals2 = false;
            }
        } else if (point1.equals(distance1.point2)) {
            equals1 = true;

            if (point2.equals(distance1.point1)) {
                equals2 = true;
            } else {
                equals2 = false;
            }
        } else {
            equals1 = false;
            equals2 = false; // doesn't really matter la
        }

//        DecimalFormat df = new DecimalFormat("#.#######");
//        String d1 = df.format(distance);
//        String d2 = df.format(distance1.distance);
//
//        if (d1.equals(d2)) {
//            distanceEq = true;
//        } else {
//            distanceEq = false;
//        }
        return ((equals1) && (equals2));
    }

}
