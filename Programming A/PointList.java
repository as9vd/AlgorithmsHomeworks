import java.util.ArrayList;

public class PointList extends ArrayList {
    ArrayList<Point> pointList;

    ArrayList<Integer> xList;
    ArrayList<Integer> yList;

    public PointList() {
        this.pointList = new ArrayList<>();
    }



    public void add(Point e) {
        this.pointList.add(e);
    }

}
