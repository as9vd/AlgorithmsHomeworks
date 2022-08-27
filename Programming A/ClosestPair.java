/*
  CS4102 Spring 2022 - Unit A Programming

  Collaboration Policy: You are encouraged to collaborate with up to 3 other
  students, but all work submitted must be your own independently written
  solution. List the computing ids of all of your collaborators in the
  comments at the top of each submitted file. Do not share written notes,
  documents (including Google docs, Overleaf docs, discussion notes, PDFs), or
  code. Do not seek published or online solutions, including pseudocode, for
  this assignment. If you use any published or online resources (which may not
  include solutions) when completing this assignment, be sure to cite them. Do
  not submit a solution that you are unable to explain orally to a member of
  the course staff. Any solutions that share similar text/code will be
  considered in breach of this policy. Please refer to the syllabus for a
  complete description of the collaboration policy.

  Your Computing ID: as9vd
  Collaborators: tv9fm, rm6jj, wfm8jns
  Sources: Introduction to Algorithms, Cormen; https://stackoverflow.com/questions/21219777/the-running-time-for-arrays-sort-method-in-java
 */

import java.util.*;

public class ClosestPair {
    /**
     * This is the method that should set off the computation
     * of closest pair.  It takes as input a list containing lines of input
     * as strings.  You should parse that input and then call a
     * subroutine that you write to compute the closest pair distances
     * and return those values from this method.
     *
     * @return the distances between the closest pair and second closest pair
     * with closest at position 0 and second at position 1
     */
    public double[] compute(List<String> fileData) {
        ArrayList<Point> pointList = new ArrayList<>();

        for (String fileDatum : fileData) {
            String x = fileDatum.split(" ")[0];
            String y = fileDatum.split(" ")[1];

            Point point = new Point(x, y);

            pointList.add(point);
        }

        Collections.sort(pointList);

        Distance[] finalList = closestPoints(pointList);

        return new double[] {finalList[0].distance, finalList[1].distance};
    }

    public Distance[] bruteForce(List<Point> pointList) { // https://i.gyazo.com/c181c0e2f12b510e378b80299594805a.png: works at least.
        ArrayList<Distance> dists = new ArrayList<>();

        for (int i = 0; i < pointList.size(); i++) {
            for (int x = i + 1; x < pointList.size(); x++) {
                dists.add(distance(pointList.get(i), pointList.get(x)));
            }
        }

        Collections.sort(dists);

        Distance[] results = new Distance[2];

        if (dists.size() == 1) {
            results[0] = dists.get(0);
            results[1] = new Distance(new Point("0","0"), new Point("0","0"), 10000);
        } else {
            results[0] = dists.get(0);
            results[1] = dists.get(1);
        }

        // I don't need to remove duplicates in this function since the for loop accounts for duplicate points.

        return results;
    }

    public Distance[] closestPoints(List<Point> list) {
        int length = list.size();

        // base
        if (length <= 3) {
            return bruteForce(list);
        }
        //        if (list.size() == 2) {
//            return new Distance[] {distance(list.get(0), list.get(1)), new Distance(new Point("0", "0"), new Point("0", "0"), 10000)}; // Array of 1, just the smallest value.
//        } else if (list.size() == 3) {
//            Distance p0p1 = distance(list.get(0), list.get(1)); // (P0) - (P1) - P2
//            Distance p0p2 = distance(list.get(0), list.get(2)); // (P0) - P1 - (P2)
//            Distance p1p2 = distance(list.get(1), list.get(2)); // P0 - (P1) - (P2)
//
//            if (p0p1.distance < p0p2.distance) {
//                if ()
//            }
//        }

        // divide: this works too thankfully
        int middle = length / 2;
        Distance[] deltaLeft = closestPoints(list.subList(0, middle)); // This partitioning is right.
        Distance[] deltaRight = closestPoints(list.subList(middle, length));
        ArrayList<Distance> deltaList = new ArrayList<>();
        for (int i = 0; i < deltaLeft.length; i++) {
            deltaList.add(deltaLeft[i]);
        }
        for (int i = 0; i < deltaRight.length; i++) {
            deltaList.add(deltaRight[i]);
        }
        Collections.sort(deltaList);
        double d1 = deltaList.get(0).distance;
        double d2 = deltaList.get(1).distance;

        // runway: having problems with distances being duplicated from the same points calculated above
        double median = ((list.get(middle).getX()) + (list.get(middle - 1).getX())) / 2; // returns the right median

        double upperBound = median + d2;
        double lowerBound = median - d2;

        ArrayList<Point> middlePoints = new ArrayList<>();
        ArrayList<Distance> midDistances = new ArrayList<>();

        for (int i = 0; i < length; i++) {
            double x = list.get(i).getX();

            if ((x >= lowerBound) && (x <= upperBound)) {
                middlePoints.add(list.get(i));
            }
        }

        if (middlePoints.size() >= 0) { // checking for the empty runway case
            Collections.sort(middlePoints, new PointComparator()); // correctly sorts by Y

            if (middlePoints.size() <= 7) { // this works
                Distance[] bf = bruteForce(middlePoints);

                midDistances.add(bf[0]);
                midDistances.add(bf[1]);
            } else {
                for (int i = 0; i < middlePoints.size(); i++) {
                    for (int j = i + 1; j < middlePoints.size() && (middlePoints.get(j).getY() - middlePoints.get(i).getY() < d1); j++) {
                        if (distance(middlePoints.get(i), middlePoints.get(j)).distance < d1) {
                            System.out.println(distance(middlePoints.get(i), middlePoints.get(j)).distance);
                            d1 = distance(middlePoints.get(i), middlePoints.get(j)).distance;
                        }
                    }

                    //                    List<Point> nextSeven = middlePoints.subList(i, i + 7);
//                    Distance[] bf = bruteForce(nextSeven);
//                    for (int j = 0; j < bf.length; j++) {
//                        midDistances.add(bf[j]);
//                    }

                    //                    midDistances.addAll();
//                    for (int j = 0; j < nextSeven.size(); j++) {
//                        //                        Point p1 = middlePoints.get(i);
////                        Point p2 = middlePoints.get(j);
//                        Distance bf = distance(nextSeven.get(i), nextSeven.get(i + 1));
//
//                    }
                }
                //                for (int i = 0; i < middlePoints.size() - 7; i++) {
//
//                    //                    Distance[] bf = bruteForce(middlePoints.subList(i, i + 7));
////
//                    middlePoints.get(i)
//
//                    midDistances.add(bf[0]);
//                    midDistances.add(bf[1]);
//                }
            }
        }

        deltaList.add(new Distance(new Point("0", "0"), new Point("0", "0"), d1));

        // end: putting em together
        deltaList.addAll(midDistances);

        Collections.sort(deltaList);

        Set<Distance> set = new HashSet<>(deltaList); // Removing duplicates.
        deltaList.clear();
        deltaList.addAll(set);
        Collections.sort(deltaList);

        //        System.out.println(deltaList.subList(0, deltaList.size() / 200));
//        for(int i = 0; i < deltaList.size(); i++) {
//            for (int j = i + 1; j < deltaList.size(); j++) {
//                if (deltaList.get(i).equals(deltaList.get(j))) {
//                    deltaList.remove(deltaList.get(j));
//                }
//            }
//        }

        // THERE ARE PROBLEMS HERE: RE CLEARING OUT DUPLICATES:
        // https://i.gyazo.com/5a7c5e5b6cc3bb1eeafc18416c3e0fa3.png
//        System.out.println("poof/**/");
//        System.out.println(deltaList);
        return new Distance[] {deltaList.get(0), deltaList.get(1)};
    }

    public Distance distance(Point point1, Point point2) { // https://dr282zn36sxxg.cloudfront.net/datastreams/f-d%3Af6c53449f1c70b3d208022b6911acbc7369d41cc59ddcbd456917120%2BCOVER_PAGE%2BCOVER_PAGE.1
        double p1x = point1.getX(); // point1 x
        double p1y = point1.getY(); // point1 y

        double p2x = point2.getX(); // point2 x
        double p2y = point2.getY(); // point2 y

        double term1 = Math.pow(p2x - p1x, 2);
        double term2 = Math.pow(p2y - p1y, 2);

        return new Distance(point1, point2, Math.sqrt(term1 + term2));
    }

}
