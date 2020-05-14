package week03_collinear;

import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedList;

public class BruteCollinearPoints {

    private Point[] points;
    private Collection<LineSegment> segmentsColl;

    /**
     * An array in which the indices are repeatedly being changed until they all move to
     * the end of points indices. For example, if the array points[] have 8 points, then
     * replacing in the tupleIndices array ends when all the combinations of indices are
     * done and tupleIndices contains {4, 5, 6, 7}
     */
    private final int[] tupleIndices = new int[] {0, 1, 2, 3};

    public BruteCollinearPoints(Point[] points) {
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

        this.points = points;
        segmentsColl = new LinkedList<>();
        solve();
    }

    public int numberOfSegments() {
        return segmentsColl.size();
    }

    public LineSegment[] segments() {
        return segmentsColl.toArray(new LineSegment[segmentsColl.size()]);
    }

    private void solve() {
        if (points.length < tupleIndices.length) {
            return;
        }
        Point[] collPoints = new Point[tupleIndices.length];
        do {
            if (isDotsTupleCollinear()) {
                arrangePointsAndKeepSegment(collPoints);
            }
        } while (changeTupleIndices());
    }

    /** Changes indices in tupleIndices to the next combination (if it is possible) and returns positive value
     * if such next combination is possible
     * @return true if next combination chosen, false otherwise
     */
    private boolean changeTupleIndices() {
        for (int i = tupleIndices.length - 1; i >= 0; i--) {
            // is it possible to increase this index and all remaining indices?
            if (tupleIndices[i] < points.length - tupleIndices.length + i) {
                // increasing the current index  and set all the remaining indices to new index + 1 and so on
                int newIndexValue = tupleIndices[i] + 1;
                for (int remainingIndex = i; remainingIndex < tupleIndices.length; remainingIndex++) {
                    // assigning and increasing newIndexValue
                    tupleIndices[remainingIndex] = newIndexValue++;
                }
                return true;
            }
        }
        return false;
    }

    /** Check if a current tuple of dots is collinear
     * @return true if the points are collinear, otherwise - false
     */
    private boolean isDotsTupleCollinear() {
        Comparator<Point> comparator = points[tupleIndices[0]].slopeOrder();
        for (int i = 1; i < tupleIndices.length - 1; i++) {
            if (0 != comparator.compare(points[tupleIndices[i]], points[tupleIndices[i + 1]])) {
                return false;
            }
        }
        return true;
    }

    private void arrangePointsAndKeepSegment(Point[] collPoints) {
        for (int i = 0; i < collPoints.length; i++) {
            collPoints[i] = points[tupleIndices[i]];
        }
        Arrays.sort(collPoints);
        LineSegment segment = new LineSegment(collPoints[0], collPoints[collPoints.length - 1]);
        segmentsColl.add(segment);
    }

}
