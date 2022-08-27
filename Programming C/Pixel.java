public class Pixel {
    public int x;
    public int y;

    public int red;
    public int blue;
    public int green;

    public double energyMeasure;
    public String status = "normal";

    int[][][] image;

    int[] left; int[] right; int[] top; int[] bottom;
    int[] leftDiagTop; int[] rightDiagTop; int[] leftDiagBottom; int[] rightDiagBottom;

    double leftDifference; double rightDifference; double topDifference; double bottomDifference;
    double leftDiagTopDifference; double rightDiagTopDifference; double leftDiagBottomDifference; double rightDiagBottomDifference;

    public Pixel(int x, int y, int red, int green, int blue, int[][][] image) {
        this.x = x;
        this.y = y;
        this.red = red;
        this.blue = blue;
        this.green = green;
        this.image = image;

        sortStatus();
    }

    // https://stackoverflow.com/questions/5958186/multidimensional-arrays-lengths-in-java
    public void sortStatus() {
        if (this.x == this.image[1].length - 1) { // Narrows this down to 3, 4, and 5.
            if (this.y == 0) { // #3.
                this.setStatus("top right");
            } else if (this.y == this.image.length - 1) { // #5.
                this.setStatus("bottom right");
            } else { // #4.
                this.setStatus("right");
            }
        } else if (this.x == 0) { // Narrows this down to 1, 8, and 7.
            if (this.y == 0) {
                this.setStatus("top left"); // #1.
            } else if (this.y == this.image.length - 1) { // #7.
                this.setStatus("bottom left");
            } else {
                this.setStatus("left"); // #8.
            }
        } else if (this.y == 0) { // #2.
            this.setStatus("top");
        } else if (this.y == this.image.length - 1) { // #6.
            this.setStatus("bottom");
        } else { // If this is just a normal pixel within the middle area (e.g. #9).
            this.setStatus("normal");
        }
    }

    public double difference(int[] pixel) {
        return Math.sqrt(
                (Math.pow(pixel[0] - red, 2))
                + (Math.pow(pixel[1] - green, 2))
                + (Math.pow(pixel[2] - blue, 2)));
    }

    public void setStatus(String status) {
        this.status = status;

        if (status.equals("top left")) { // #1. right, rightDiagBottom, bottom.
            right = image[this.y][this.x + 1];
            rightDiagBottom = image[this.y + 1][this.x + 1];
            bottom = image[this.y + 1][this.x];

            rightDifference = difference(right);
            rightDiagBottomDifference = difference(rightDiagBottom);
            bottomDifference = difference(bottom);

            energyMeasure = (rightDifference + rightDiagBottomDifference + bottomDifference) / 3;
        } else if (status.equals("top right")) { // #3. left, leftDiagBottom, bottom.
            left = image[this.y][this.x - 1];
            leftDiagBottom = image[this.y + 1][this.x - 1];
            bottom = image[this.y + 1][this.x];;

            leftDifference = difference(left);
            leftDiagBottomDifference = difference(leftDiagBottom);
            bottomDifference = difference(bottom);

            energyMeasure = (leftDifference + leftDiagBottomDifference + bottomDifference) / 3;
        } else if (status.equals("top")) { // #2. left, leftDiagBottom, bottom, rightDiagBottom, right.
            left = image[this.y][this.x - 1];
            leftDiagBottom = image[this.y + 1][this.x - 1];
            bottom = image[this.y + 1][this.x];
            right = image[this.y][this.x + 1];
            rightDiagBottom = image[this.y + 1][this.x + 1];

            leftDifference = difference(left);
            leftDiagBottomDifference = difference(leftDiagBottom);
            bottomDifference = difference(bottom);
            rightDifference = difference(right);
            rightDiagBottomDifference = difference(rightDiagBottom);

            energyMeasure = (leftDifference + leftDiagBottomDifference + bottomDifference + rightDifference + rightDiagBottomDifference) / 5;
        } else if (status.equals("right")) { // #4. top, leftDiagTop, left, leftDiagBottom, bottom.
            top = image[this.y - 1][this.x];
            left = image[this.y][this.x - 1];
            leftDiagBottom = image[this.y + 1][this.x - 1];
            leftDiagTop = image[this.y - 1][this.x - 1];
            bottom = image[this.y + 1][this.x];

            topDifference = difference(top);
            leftDifference = difference(left);
            leftDiagBottomDifference = difference(leftDiagBottom);
            leftDiagTopDifference = difference(leftDiagTop);
            bottomDifference = difference(bottom);

            energyMeasure = (topDifference + leftDifference + leftDiagBottomDifference + leftDiagTopDifference + bottomDifference) / 5;
        } else if (status.equals("bottom right")) { // #5. left, leftDiagTop, top.
            top = image[this.y - 1][this.x];
            left = image[this.y][this.x - 1];
            leftDiagTop = image[this.y - 1][this.x - 1];

            topDifference = difference(top);
            leftDifference = difference(left);
            leftDiagTopDifference = difference(leftDiagTop);

            energyMeasure = (topDifference + leftDifference + leftDiagTopDifference) / 3;
        } else if (status.equals("bottom")) { // #6. left, leftDiagTop, top, rightDiagTop, right.
            top = image[this.y - 1][this.x];
            left = image[this.y][this.x - 1];
            leftDiagTop = image[this.y - 1][this.x - 1];
            rightDiagTop = image[this.y - 1][this.x + 1];
            right = image[this.y][this.x + 1];

            topDifference = difference(top);
            leftDifference = difference(left);
            leftDiagTopDifference = difference(leftDiagTop);
            rightDiagTopDifference = difference(rightDiagTop);
            rightDifference = difference(right);

            energyMeasure = (topDifference + leftDifference + leftDiagTopDifference + rightDiagTopDifference + rightDifference) / 5;
        } else if (status.equals("bottom left")) { // #7. top, rightDiagTop, right.
            top = image[this.y - 1][this.x];
            rightDiagTop = image[this.y - 1][this.x + 1];
            right = image[this.y][this.x + 1];

            topDifference = difference(top);
            rightDiagTopDifference = difference(rightDiagTop);
            rightDifference = difference(right);

            energyMeasure = (topDifference + rightDiagTopDifference + rightDifference) / 3;
        } else if (status.equals("left")) { // #8. top, rightDiagTop, right, rightDiagBottom, bottom.
            top = image[this.y - 1][this.x];
            rightDiagTop = image[this.y - 1][this.x + 1];
            right = image[this.y][this.x + 1];
            rightDiagBottom = image[this.y + 1][this.x + 1];
            bottom = image[this.y + 1][this.x];

            topDifference = difference(top);
            rightDiagTopDifference = difference(rightDiagTop);
            rightDifference = difference(right);
            rightDiagBottomDifference = difference(rightDiagBottom);
            bottomDifference = difference(bottom);

            energyMeasure = (topDifference + rightDiagTopDifference + rightDifference + rightDiagBottomDifference + bottomDifference) / 5;
        } else { // Normal pixel.
            top = image[this.y - 1][this.x];
            bottom = image[this.y + 1][this.x];
            left = image[this.y][this.x - 1];
            right = image[this.y][this.x + 1];
            leftDiagTop = image[this.y - 1][this.x - 1];
            rightDiagTop = image[this.y - 1][this.x + 1];
            leftDiagBottom = image[this.y + 1][this.x - 1];
            rightDiagBottom = image[this.y + 1][this.x + 1];

            topDifference = difference(top);
            bottomDifference = difference(bottom);
            leftDifference = difference(left);
            rightDifference = difference(right);
            leftDiagTopDifference = difference(leftDiagTop);
            rightDiagTopDifference = difference(rightDiagTop);
            leftDiagBottomDifference = difference(leftDiagBottom);
            rightDiagBottomDifference = difference(rightDiagBottom);

            energyMeasure = (topDifference + bottomDifference + leftDifference + rightDifference + leftDiagTopDifference + rightDiagTopDifference + leftDiagBottomDifference + rightDiagBottomDifference) / 8;
        }
    }

    public String toString() {
        return "X: " + this.x + ", Y: " + this.y;
    }

}
