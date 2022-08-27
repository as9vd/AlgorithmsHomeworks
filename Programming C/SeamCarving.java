import java.util.ArrayList;

/**
 * CS4102 Spring 2022 -- Unit C Programming
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
 * Sources: Introduction to Algorithms, Cormen; https://stackoverflow.com/questions/5958186/multidimensional-arrays-lengths-in-java
 **************************************/

// We're doing bottom-up.
public class SeamCarving {
    public ArrayList<String> bottomRow;
    public double[][] energies;
    public double[][] coolEnergies;

    public int[][][] originalImage;

    double sum;
    int smallestIndex;

    public SeamCarving() {
        bottomRow = new ArrayList<>();
    }

    /**
     * This method is the one you should implement.  It will be called to perform
     * the seam carving.  You may create any additional data structures as fields
     * in this class or write any additional methods you need.
     * 
     * @return the seam's weight
     */
    public double run(int[][][] image) {
        // We might also have to bin off the Pixel class. Sad.
        int xLength = image[1].length; // This is "m".
        int yLength = image.length; // This is "n".

        originalImage = image;

        this.energies = new double[yLength][xLength];
        this.coolEnergies = new double[yLength][xLength]; // "Cumulative energies."

        // LOOK, we initialise the energies properly (assuming the formula is right).
        for (int x = 0; x < xLength; x++) { // x
            for (int y = 0; y < yLength; y++) { // y
                int red = image[y][x][0];
                int green = image[y][x][1];
                int blue = image[y][x][2];

                energies[y][x] = energyCalculation(x, y, red, green, blue);
            }
        }

        // LOOK, this works as well. The coolEnergies's bottom row is just the bottom row's base energy.
        for (int x = 0; x < xLength; x++) { // We initialise the bottom with their original energies.
            coolEnergies[yLength - 1][x] = energies[yLength - 1][x];
        }

        // The coolEnergies matrix is correct.
        solveTheProblem();

        return sum;
    }

    public void solveTheProblem() {
        int xLength = originalImage[1].length; // This is "m".
        int yLength = originalImage.length; // This is "n".

        // It seems only the bottom row and the second-to-bottom row update properly with the right values.
        for (int y = yLength - 2; y >= 0; y--) {
            for (int x = 0; x < xLength ; x++) {
                int xBelow = x; int yBelow = y + 1;
                int xLeft = x - 1; int yLeft = y + 1;
                int xRight = x + 1; int yRight = y + 1;

                if (x == 0) { // Left
                    coolEnergies[y][x] = energies[y][x] + Math.min(coolEnergies[yBelow][xBelow], coolEnergies[yRight][xRight]); // The energy of the pixel itself, plus the minimum of the one below it or to the right.
                } else if (x == xLength - 1) { // Right
                    coolEnergies[y][x] = energies[y][x] + Math.min(coolEnergies[yBelow][xBelow], coolEnergies[yLeft][xLeft]);
                } else { // Normal
                    coolEnergies[y][x] = energies[y][x] + Math.min(Math.min(coolEnergies[yBelow][xBelow], coolEnergies[yLeft][xLeft]), coolEnergies[yRight][xRight]);
                }
            }
        }

        double smallest = 99999999;
        int smallestIndex = 0;
        for (int x = 0; x < xLength; x++) {
            if (coolEnergies[0][x] < smallest) {
                smallest = coolEnergies[0][x];
                smallestIndex = x;
            }
        }

        this.sum = smallest;
        this.smallestIndex = smallestIndex;
    }

    // Check for top, left, bottom, etc. stuff in here since Pixel class is dead.
    // LOOK, this is right, since the test cases return the right energies too.
    public double energyCalculation(int x, int y, int red, int green, int blue) {
        if (x == 0) { // Narrows down to #1 (top left), #8 (left), and #7 (bottom left).
            if (y == originalImage.length - 1) { // Bottom left (#7); deals with top, rightDiagTop, and right.
                return (difference(red, green, blue, originalImage[y - 1][x][0], originalImage[y - 1][x][1], originalImage[y - 1][x][2]) // top
                                + difference(red, green, blue, originalImage[y - 1][x + 1][0], originalImage[y - 1][x + 1][1], originalImage[y - 1][x + 1][2]) // rightDiagTop
                                + difference(red, green, blue, originalImage[y][x + 1][0], originalImage[y][x + 1][1], originalImage[y][x + 1][2])/3); // right
            } else if (y == 0) { // Top left (#1); deals with bottom, rightDiagBottom, and right.
                return (difference(red, green, blue, originalImage[y + 1][x][0], originalImage[y + 1][x][1], originalImage[y + 1][x][2]) // bottom
                                + difference(red, green, blue, originalImage[y + 1][x + 1][0], originalImage[y + 1][x + 1][1], originalImage[y + 1][x + 1][2]) // rightDiagBottom
                                + difference(red, green, blue, originalImage[y][x + 1][0], originalImage[y][x + 1][1], originalImage[y][x + 1][2])
                )/3; // right
            } else { // Regular left (#8); deals with top, rightDiagTop, right, rightDiagBottom, and bottom.
                return (difference(red, green, blue, originalImage[y - 1][x][0], originalImage[y - 1][x][1], originalImage[y - 1][x][2]) // top
                        + difference(red, green, blue, originalImage[y - 1][x + 1][0], originalImage[y - 1][x + 1][1], originalImage[y - 1][x + 1][2]) // rightDiagTop
                        + difference(red, green, blue, originalImage[y][x + 1][0], originalImage[y][x + 1][1], originalImage[y][x + 1][2]) // right
                        + difference(red, green, blue, originalImage[y + 1][x + 1][0], originalImage[y + 1][x + 1][1], originalImage[y + 1][x + 1][2]) // rightDiagBottom
                        + difference(red, green, blue, originalImage[y + 1][x][0], originalImage[y + 1][x][1], originalImage[y + 1][x][2]) // bottom
                    )/5;
            }
        } else if (x == originalImage[1].length - 1) { // Narrows down to #3, #4, and #5.
            if (y == originalImage.length - 1) { // Bottom right (#5); deals with top, leftDiagTop, and left.
                return (difference(red, green, blue, originalImage[y - 1][x][0], originalImage[y - 1][x][1], originalImage[y - 1][x][2]) // top
                        + difference(red, green, blue, originalImage[y - 1][x - 1][0], originalImage[y - 1][x - 1][1], originalImage[y - 1][x - 1][2]) // leftDiagTop
                        + difference(red, green, blue, originalImage[y][x - 1][0], originalImage[y][x - 1][1], originalImage[y][x - 1][2]) // left
                        )/3;
            } else if (y == 0) { // Top right (#3); deals with bottom, leftDiagBottom, and left.
                return (difference(red, green, blue, originalImage[y + 1][x][0], originalImage[y + 1][x][1], originalImage[y + 1][x][2]) // bottom
                        + difference(red, green, blue, originalImage[y + 1][x - 1][0], originalImage[y + 1][x - 1][1], originalImage[y + 1][x - 1][2]) // leftDiagBottom
                        + difference(red, green, blue, originalImage[y][x - 1][0], originalImage[y][x - 1][1], originalImage[y][x - 1][2]) // left
                        )/3;
            } else { // Regular right (#4); deals with top, leftDiagTop, left, leftDiagBottom, and bottom.
                return (difference(red, green, blue, originalImage[y - 1][x][0], originalImage[y - 1][x][1], originalImage[y - 1][x][2]) // top
                        + difference(red, green, blue, originalImage[y - 1][x - 1][0], originalImage[y - 1][x - 1][1], originalImage[y - 1][x - 1][2]) // leftDiagTop
                        + difference(red, green, blue, originalImage[y][x - 1][0], originalImage[y][x - 1][1], originalImage[y][x - 1][2]) // left
                        + difference(red, green, blue, originalImage[y + 1][x - 1][0], originalImage[y + 1][x - 1][1], originalImage[y + 1][x - 1][2]) // leftDiagBottom
                        + difference(red, green, blue, originalImage[y + 1][x][0], originalImage[y + 1][x][1], originalImage[y + 1][x][2]) // bottom
                        )/5;
            }
        } else if (y == 0) { // Top (#2); deals with left, leftDiagBottom, bottom, rightDiagBottom, and right.
            return (difference(red, green, blue, originalImage[y][x - 1][0], originalImage[y][x - 1][1], originalImage[y][x - 1][2]) // left
                    + difference(red, green, blue, originalImage[y + 1][x - 1][0], originalImage[y + 1][x - 1][1], originalImage[y + 1][x - 1][2]) // leftDiagBottom
                    + difference(red, green, blue, originalImage[y + 1][x][0], originalImage[y + 1][x][1], originalImage[y + 1][x][2]) // bottom
                    + difference(red, green, blue, originalImage[y + 1][x + 1][0], originalImage[y + 1][x + 1][1], originalImage[y + 1][x + 1][2]) // rightDiagBottom
                    + difference(red, green, blue, originalImage[y][x + 1][0], originalImage[y][x + 1][1], originalImage[y][x + 1][2]) // right
                    )/5;
        } else if (y == originalImage.length - 1) { // Bottom (#6); deals with left, leftDiagTop, top, rightDiagTop, and right.
            return (difference(red, green, blue, originalImage[y][x - 1][0], originalImage[y][x - 1][1], originalImage[y][x - 1][2]) // left
                    + difference(red, green, blue, originalImage[y - 1][x - 1][0], originalImage[y - 1][x - 1][1], originalImage[y - 1][x - 1][2]) // leftDiagTop
                    + difference(red, green, blue, originalImage[y - 1][x][0], originalImage[y - 1][x][1], originalImage[y - 1][x][2]) // top
                    + difference(red, green, blue, originalImage[y - 1][x + 1][0], originalImage[y - 1][x + 1][1], originalImage[y - 1][x + 1][2]) // rightDiagTop
                    + difference(red, green, blue, originalImage[y][x + 1][0], originalImage[y][x + 1][1], originalImage[y][x + 1][2]) // right
                    )/5;
        } else { // Just a normal pixel, which deals with everything.
            return (difference(red, green, blue, originalImage[y][x - 1][0], originalImage[y][x - 1][1], originalImage[y][x - 1][2]) // left
                    + difference(red, green, blue, originalImage[y - 1][x - 1][0], originalImage[y - 1][x - 1][1], originalImage[y - 1][x - 1][2]) // leftDiagTop
                    + difference(red, green, blue, originalImage[y + 1][x - 1][0], originalImage[y + 1][x - 1][1], originalImage[y + 1][x - 1][2]) // leftDiagBottom
                    + difference(red, green, blue, originalImage[y][x + 1][0], originalImage[y][x + 1][1], originalImage[y][x + 1][2]) // right
                    + difference(red, green, blue, originalImage[y - 1][x + 1][0], originalImage[y - 1][x + 1][1], originalImage[y - 1][x + 1][2]) // rightDiagTop
                    + difference(red, green, blue, originalImage[y + 1][x + 1][0], originalImage[y + 1][x + 1][1], originalImage[y + 1][x + 1][2]) // rightDiagBottom
                    + difference(red, green, blue, originalImage[y - 1][x][0], originalImage[y - 1][x][1], originalImage[y - 1][x][2]) // top
                    + difference(red, green, blue, originalImage[y + 1][x][0], originalImage[y + 1][x][1], originalImage[y + 1][x][2]) // bottom
                    )/8;
        }
    }

    public double difference(int originalRed, int originalGreen, int originalBlue, int red, int green, int blue) {
        return Math.sqrt(
                Math.pow(originalRed - red, 2) + Math.pow(originalGreen - green, 2) + Math.pow(originalBlue - blue, 2)
        );
    }

    /**
     * Get the seam, in order from top to bottom, where the top-left corner of the
     * image is denoted (0,0).
     * 
     * Since the y-coordinate (row) is determined by the order, only return the x-coordinate
     * 
     * @return the ordered list of x-coordinates (column number) of each pixel in the seam
     */
    public int[] getSeam() {
        int seam[] = new int[originalImage.length];

        // This is the first index we start at from the top.
        seam[0] = smallestIndex;

        int i = 1;
        for (int y = 1; y < originalImage.length; y++) {
            // Check here for whichever is smallest out of the left, middle, and right columns.
            if (this.smallestIndex == 0) { // Left; we check middle and right columns.
                if (coolEnergies[y][smallestIndex + 1] < coolEnergies[y][smallestIndex]) { // If the right energy is smaller than the middle one.
                    smallestIndex++;
                }
            } else if (this.smallestIndex == originalImage[1].length - 1) { // Right; we check middle and left columns.
                if (coolEnergies[y][smallestIndex - 1] < coolEnergies[y][smallestIndex]) { // If the left energy is smaller than the middle one.
                    smallestIndex--;
                }
            } else { // Middle; we check all the columns.
                if (coolEnergies[y][smallestIndex - 1] < coolEnergies[y][smallestIndex]) { // If the left energy is smaller than the middle one.
                    if (coolEnergies[y][smallestIndex - 1] <= coolEnergies[y][smallestIndex + 1]) { // If the left energy is smaller than or equal to the right one.
                        smallestIndex--;
                    } else {
                        smallestIndex++;
                    }
                } else if (coolEnergies[y][smallestIndex - 1] >= coolEnergies[y][smallestIndex]) { // If the middle energy is smaller than the left energy.
                    if (coolEnergies[y][smallestIndex + 1] < coolEnergies[y][smallestIndex]) { // If the right energy is smaller than the middle energy.
                        smallestIndex++;
                    }
                }
            }

            seam[i] = smallestIndex;

            i++;
        }

        return seam;
    }

}
