package week03_collinear;

import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

public class FastCollinearPoints {

    private Point[] points;
    private Point[] sortPoints;
    private Collection<LineSegment> segmentsColl;

    public FastCollinearPoints(Point[] points) {
        // throw NPE if points == null or any item in points == null
        if (points == null) {
            throw new NullPointerException("points array is null");
        }

        // Checking repeated points
        for (int i = 0; i < points.length; i++) {
            for (int j = 0; j < points.length; j++) {
                if (i != j && (points[i].slopeTo(points[j]) == Double.NEGATIVE_INFINITY)) {
                    throw new IllegalArgumentException("points array contains repeated members");
                }
            }
        }

        this.points = Arrays.copyOf(points, points.length);
        sortPoints = Arrays.copyOf(points, points.length);
        segmentsColl = new LinkedList<>();
        solve();
    }

    private void solve() {
        // Iterate all over the points and every time sort the sortedPoints with respect to the currently selected point
        if (points.length < 4) {
            return;
        }
        Arrays.sort(points);
        for (int index = 0; index < points.length; index++) {
            Point p0 = points[index];
            Comparator<Point> comparator = new FirstCollinearThenNaturalPointsOrderComparator(p0);
            Arrays.sort(sortPoints, comparator);

            double currentSlope;
            double rangeSlope = p0.slopeTo(sortPoints[1]);
            Point currentPoint;
            List<Point> slopeList = new LinkedList<>();
            List<Point> finishedList = new LinkedList<>();
            boolean slopesAreEqual;
            initializeSlopeList(p0, sortPoints[1], slopeList);
            for (int sortedIndex = 2; sortedIndex < sortPoints.length; sortedIndex++) {
                currentPoint = sortPoints[sortedIndex];
                currentSlope = p0.slopeTo(currentPoint);

                slopesAreEqual = currentSlope == rangeSlope;
                if (slopesAreEqual) {
                    slopeList.add(currentPoint);
                }
                if (!slopesAreEqual || sortedIndex == sortPoints.length - 1) {
                    // The slope range is over and needs to be checked for length
                    if (slopeList.size() >= 4) {
                        // Trying to add segment
                        finishedList.clear();
                        finishedList.addAll(slopeList);
                        tryAddSegment(finishedList);
                    }
                    initializeSlopeList(p0, currentPoint, slopeList);
                    rangeSlope = currentSlope;
                }
            }
        }
    }

    private void initializeSlopeList(Point p0, Point currentPoint, List<Point> slopeList) {
        slopeList.clear();
        slopeList.add(p0);
        slopeList.add(currentPoint);
    }

    private void tryAddSegment(List<Point> finishedList) {
        // Compare the first point in the list with the second and the last ones. The first one must be either
        // greater than or equal or less than or equal to both of them (to create a new segment)
        Point p0 = finishedList.get(0);
        Point p1 = finishedList.get(1);
        Point pLast = finishedList.get(finishedList.size() - 1);
        int compare1 = p0.compareTo(p1);
        int compare2 = p0.compareTo(pLast);

        if (compare1 <= 0 && compare2 <= 0) {
            segmentsColl.add(new LineSegment(p0, pLast));
        }
    }

    public int numberOfSegments() {
        return segmentsColl.size();
    }

    public LineSegment[] segments() {
        return segmentsColl.toArray(new LineSegment[segmentsColl.size()]);
    }

    private static class FirstCollinearThenNaturalPointsOrderComparator implements Comparator<Point> {

        private Point zero;

        public FirstCollinearThenNaturalPointsOrderComparator(Point zero) {
            this.zero = zero;
        }

        @Override
        public int compare(Point o1, Point o2) {
            double slope1 = zero.slopeTo(o1);
            double slope2 = zero.slopeTo(o2);
            if (slope1 < slope2) {
                return -1;
            }
            if (slope1 > slope2) {
                return 1;
            }
            return o1.compareTo(o2);
        }

    }


}
