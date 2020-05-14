import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.ArrayList;

public class KdTreeDrawer {
    public static void main(String[] args) {

        StdDraw.enableDoubleBuffering();
        int n = Integer.parseInt(args[0]);
        KdTree kdTree = new KdTree();
        ArrayList<Point2D> points = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            double x = StdRandom.uniform(0.0, 1.0);
            double y = StdRandom.uniform(0.0, 1.0);
            Point2D p = new Point2D(x, y);
            points.add(p);
            StdDraw.clear();
            StdDraw.setPenColor(StdDraw.BLACK);
            StdDraw.setPenRadius(0.006);
            for (Point2D point : points) {
                point.draw();
            }
            kdTree.insert(p);
            StdDraw.show();
            StdDraw.pause(10);
            StdOut.printf("%8.6f %8.6f\n", x, y);
        }

        kdTree.draw();
//
//        String filename = args[0];
//        In in = new In(filename);

    }
}
