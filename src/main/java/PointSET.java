import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;

import java.util.Collection;
import java.util.LinkedList;

public class PointSET {

    private final SET<Point2D> set;

    public PointSET() {                               // construct an empty set of points
        set = new SET<>();
    }

    public boolean isEmpty() {                      // is the set empty?
        return set.isEmpty();
    }

    public int size() {                        // number of points in the set
        return set.size();
    }

    public void insert(Point2D p) {             // add the point to the set (if it is not already in the set)
        checkNotNull(p);
        set.add(p);
    }

    public boolean contains(Point2D p) {           // does the set contain point p?
        checkNotNull(p);
        return set.contains(p);
    }

    public void draw() {                        // draw all points to standard draw
        for (Point2D point : set) {
            point.draw();
        }
    }

    public Iterable<Point2D> range(RectHV rect) {            // all points that are inside the rectangle
        checkNotNull(rect);
        Collection<Point2D> pointsInRect = new LinkedList<>();
        for (Point2D point : set) {
            if (rect.contains(point)) {
                pointsInRect.add(point);
            }
        }
        return pointsInRect;
    }

    public Point2D nearest(Point2D p) {            // a nearest neighbor in the set to point p; null if the set is empty
        checkNotNull(p);
        Point2D nearestPoint = null;
        double nearestDistance = Math.sqrt(2);
        double distance;
        for (Point2D point : set) {
            distance = point.distanceTo(p);
            if (distance <= nearestDistance) {
                nearestDistance = distance;
                nearestPoint = point;
            }
        }
        return nearestPoint;
    }

    private void checkNotNull(Object o) {
        if (o == null) {
            throw new NullPointerException();
        }
    }

    public static void main(String[] args) {                 // unit testing of the methods (optional)
    }
}
