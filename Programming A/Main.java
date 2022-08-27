import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Main {
	public static void main(String[] args) {
        List<String> lines = new ArrayList<>();
		try (BufferedReader br = new BufferedReader(new FileReader("80k vertical.txt"))) {
			String line;
			while ((line = br.readLine()) != null) {
			    lines.add(line.trim());
            }
			//			ArrayList<Point> pointList = new ArrayList<>();
//
//			for (int i = 0; i < lines.size(); i++) {
//				String x = lines.get(i).split(" ")[0];
//				String y = lines.get(i).split(" ")[1];
//
//				Point point = new Point(x, y);
//
//				pointList.add(point);
//			}
//			Collections.sort(pointList);
            Long start = System.currentTimeMillis();
            ClosestPair cp = new ClosestPair();
			//			System.out.println("Shortest: " + cp.bruteForce(pointList)[0] + ", Second Shortest: " + cp.bruteForce(pointList)[1]);
//
			System.out.println("Shortest: " + cp.compute(lines)[0] + ", Second Shortest: " + cp.compute(lines)[1]);


            Long end = System.currentTimeMillis();
            System.out.println("time: " + ((end - start) / 1000.0));
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Error occurred when reading file");
		}
	}

	public void bruteForce() {

	}
}
