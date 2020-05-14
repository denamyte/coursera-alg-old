package week03_collinear;

import java.util.Arrays;

@Deprecated
public class Test {
    public static void main(String[] args) {

//        testTupleIndices();
//        testCollinearity();
//        testOppositeSlopes();
        testFastCollSorting();
//        testSurroundingPoints();
    }

    private static void testTupleIndices() {
        Point[] points = new Point[]{
                new Point(1, 2),
                new Point(2, 3),
                new Point(5, 6),
                new Point(5, 11),
                new Point(10, 10),
                new Point(11, 5),
                new Point(22, 7),
                new Point(22, 8),
                new Point(23, 9),
                new Point(23, 10)
        };
        BruteCollinearPoints bruteCollinearPoints = new BruteCollinearPoints(points);
        System.out.println(bruteCollinearPoints);
    }

    private static void testCollinearity() {
        Point[] points = new Point[]{
                new Point(1, 4),  // 1
                new Point(2, 5),  // 2
                new Point(3, 4),  // 3
                new Point(3, 6),  // 4
                new Point(4, 4),  // 5
                new Point(4, 7),  // 6
                new Point(5, 4),  // 7
                new Point(3, 2),  // 8
                new Point(3, 1),  // 9
                new Point(6, 5),  // 10
                new Point(4, 3),  // 11
                new Point(4, 1),  // 12
                new Point(5, 1)   // 13
        };
        BruteCollinearPoints brute = new BruteCollinearPoints(points);
        System.out.println("\n========== Segments: ========== ");
        for (LineSegment segment : brute.segments()) {
            System.out.println(segment);
        }

    }

    private static void testOppositeSlopes() {
        Point p1 = new Point(5, 1);
        Point p2 = new Point(4, 7);
        double slopeTo12 = p1.slopeTo(p2);
        double slopeTo21 = p2.slopeTo(p1);
        System.out.println("slopeTo12: " + slopeTo12);
        System.out.println("slopeTo21: " + slopeTo21);
    }

    private static void testFastCollSorting() {
        Point[] points = new Point[]{
                new Point(1, 3),  //  1
                new Point(7, 3),  //  6
                new Point(5, 2),  //  11
                new Point(3, 4),  //  2
                new Point(8, 3),  //  7
                new Point(6, 1),  //  12
                new Point(5, 5),  //  3
                new Point(5, 3),  //  8
                new Point(2, 5),  //  13
                new Point(7, 6),  //  4
                new Point(4, 3),  //  9
                new Point(9, 7),  //  5
                new Point(3, 3),  //  10
//                new Point(, ),  //
//                new Point(, ),  //
//                new Point(, ),  //
//                new Point(, ),  //
//                new Point(, ),  //
//                new Point(, ),  //
//                new Point(, ),  //
//                new Point(, )  //
        };
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        System.out.println(Arrays.toString(collinear.segments()));
    }

    private static void testSurroundingPoints() {
        Point[] points = new Point[]{
                new Point(0, 0),
                new Point(1, 0),
                new Point(2, 0),
                new Point(3, 0),
                new Point(4, 0),
                new Point(4, 1),
                new Point(4, 2),
                new Point(4, 3),
                new Point(4, 4),
                new Point(3, 4),
                new Point(2, 4),
                new Point(1, 4),
                new Point(0, 4),
                new Point(0, 3),
                new Point(0, 2),
                new Point(0, 1),
        };
        Point p0 = new Point(2, 2);
        for (Point point : points) {
            System.out.println("Slope " + p0 + " => " + point + " : " + p0.slopeTo(point));
        }
    }

//    private void testTupleIndices()
}
