import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class KdTree {

    private static final RectHV UNIT_RECT = new RectHV(0, 0, 1, 1);
    private Node root;
    private int count;

    public KdTree() {                               // construct an empty set of points
    }

    public boolean isEmpty() {                      // is the set empty?
        return count == 0;
    }

    public int size() {                        // number of points in the set
        return count;
    }

    public void insert(Point2D p) {             // add the point to the set (if it is not already in the set)
        checkNotNull(p);
        if (root == null) {
            root = new Node(p);
            count++;
        } else {
            insertInner(root, UNIT_RECT, p, true);
        }
    }

    public boolean contains(Point2D p) {           // does the set contain point p?
        checkNotNull(p);
        return containsInner(p, root, true);
    }

    public void draw() {                        // draw all points to standard draw
        StdDraw.clear();
        drawInner(root, UNIT_RECT, true);
        StdDraw.show();
    }

    public Iterable<Point2D> range(RectHV rect) {            // all points that are inside the rectangle
        checkNotNull(rect);
        List<Point2D> pointList = new LinkedList<>();
        if (root != null) {
            rangeInner(root, true, UNIT_RECT, rect, pointList);
        }
        return pointList;
    }

    public Point2D nearest(Point2D p) {            // a nearest neighbor in the set to point p; null if the set is empty
        checkNotNull(p);
        return root == null ? null : nearestInner(root, true, UNIT_RECT, p, null);
    }

    /**
     * @param parentNode a node to which a new node may be added as a child
     * @param p a point, which may be the value of new node
     * @param axisX
     */
    private void insertInner(Node parentNode, RectHV parentRect, Point2D p, boolean axisX) {
        int comp = comparePointsByAxis(axisX, p, parentNode.p);
        if (comp == 0 && parentNode.p.compareTo(p) == 0) {
            return;
        }
        if (comp >= 0) {
            if (parentNode.rt != null) {
                RectHV rect = subRect(axisX, parentRect, parentNode.p, false);
                insertInner(parentNode.rt, rect, p, !axisX);
            } else {
                // Create a new node and add it to right-top link
                parentNode.rt = new Node(p);
                count++;
            }
        } else {  // if (comp < 0) {
            if (parentNode.lb != null) {
                RectHV rect = subRect(axisX, parentRect, parentNode.p, true);
                insertInner(parentNode.lb, rect, p, !axisX);
            } else {
                // Create a new node and add it to left-bottom link
                parentNode.lb = new Node(p);
                count++;
            }
        }
    }

    private boolean containsInner(Point2D p, Node node, boolean axisX) {
        if (node == null) {
            return false;
        }
        Point2D nodePoint = node.p;
        int comp = comparePointsByAxis(axisX, p, nodePoint);
        if (comp < 0) {
            return containsInner(p, node.lb, !axisX);
        }
        if (comp == 0 && nodePoint.compareTo(p) == 0) {
            return true;
        }
        return containsInner(p, node.rt, !axisX);
    }

    private void drawInner(Node node, RectHV rect, boolean axisX) {
        if (node == null || rect == null) {
            return;
        }
        // draw itself
        /// draw the axis
        StdDraw.setPenRadius(0.002);
        if (axisX) {
            double nodePX = node.p.x();
            Point2D first = new Point2D(nodePX, rect.ymin());
            Point2D second = new Point2D(nodePX, rect.ymax());
            StdDraw.setPenColor(StdDraw.RED);
            first.drawTo(second);
        } else {
            double nodePY = node.p.y();
            Point2D first = new Point2D(rect.xmin(), nodePY);
            Point2D second = new Point2D(rect.xmax(), nodePY);
            StdDraw.setPenColor(StdDraw.BLUE);
            first.drawTo(second);
        }
        /// draw the point on top of the axis
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.006);
        node.p.draw();
        // call left
        if (node.lb != null) {
            int comp = comparePointsByAxis(axisX, node.lb.p, node.p);
            RectHV leftRect = subRect(axisX, rect, node.p, comp < 0);
            drawInner(node.lb, leftRect, !axisX);
        }
        // call right
        if (node.rt != null) {
            int comp = comparePointsByAxis(axisX, node.rt.p, node.p);
            RectHV rightRect = subRect(axisX, rect, node.p, comp < 0);
            drawInner(node.rt, rightRect, !axisX);
        }
    }

    private void rangeInner(Node node, boolean axisX, RectHV nodeRect, RectHV searchRect, List<Point2D> pointList) {
        if (searchRect.contains(node.p)) {
            pointList.add(node.p);
        }
        rangeInnerForChild(node.p, node.lb, axisX, nodeRect, searchRect, pointList);
        rangeInnerForChild(node.p, node.rt, axisX, nodeRect, searchRect, pointList);
    }

    private void rangeInnerForChild(Point2D parentPoint, Node childNode, boolean axisX, RectHV parentRect,
                                    RectHV searchRect, List<Point2D> pointList) {
        if (childNode != null) {
            int comp = comparePointsByAxis(axisX, childNode.p, parentPoint);
            RectHV childRect = subRect(axisX, parentRect, parentPoint, comp < 0);
            if (childRect.intersects(searchRect)) {
                rangeInner(childNode, !axisX, childRect, searchRect, pointList);
            }
        }
    }

    private Point2D nearestInner(Node node, boolean axisX, RectHV rect, Point2D searchPoint, Point2D nearestPoint) {
        // Compare current nearest point to the node point
        nearestPoint = nearestPointFromTwo(searchPoint, nearestPoint, node.p);
        if (node.lb == null && node.rt == null) {
            return nearestPoint;
        }

        double nearestDistance = nearestDistance(searchPoint, nearestPoint);

        // Find preferred link of the node to further search
        double distanceToLeft = Double.POSITIVE_INFINITY;
        double distanceToRight = Double.POSITIVE_INFINITY;
        RectHV subRectLeft = null, subRectRight = null;
        if (node.lb != null) {
            int comp = comparePointsByAxis(axisX, node.lb.p, node.p);
            subRectLeft = subRect(axisX, rect, node.p, comp < 0);
            distanceToLeft = subRectLeft.distanceSquaredTo(searchPoint);
        }
        if (node.rt != null) {
            int comp = comparePointsByAxis(axisX, node.rt.p, node.p);
            subRectRight = subRect(axisX, rect, node.p, comp < 0);
            distanceToRight = subRectRight.distanceSquaredTo(searchPoint);
        }

        if (node.lb == null) {
            if (distanceToRight >= nearestDistance) {
                return nearestPoint;
            }
            // distanceToRight < nearestDistance
            return nearestPointFromTwo(searchPoint, nearestPoint,
                                       nearestInner(node.rt, !axisX, subRectRight, searchPoint, nearestPoint));
        } else if (node.rt == null) {
            if (distanceToLeft >= nearestDistance) {
                return nearestPoint;
            }
            // distanceToLeft < nearestDistance
            return nearestPointFromTwo(searchPoint, nearestPoint,
                                       nearestInner(node.lb, !axisX, subRectLeft, searchPoint, nearestPoint));
        }

        // here the both sublinks are not null
        if (distanceToLeft < distanceToRight) {
            if (distanceToLeft < nearestDistance) {
                Point2D leftNearest = nearestInner(node.lb, !axisX, subRectLeft, searchPoint, nearestPoint);
                nearestPoint = nearestPointFromTwo(searchPoint, nearestPoint, leftNearest);
                nearestDistance = nearestDistance(searchPoint, nearestPoint);
                if (distanceToRight < nearestDistance) {
                    Point2D rightNearest = nearestInner(node.rt, !axisX, subRectRight, searchPoint, nearestPoint);
                    nearestPoint = nearestPointFromTwo(searchPoint, nearestPoint, rightNearest);
                }
            }
        } else {
            if (distanceToRight < nearestDistance) {
                Point2D rightNearest = nearestInner(node.rt, !axisX, subRectRight, searchPoint, nearestPoint);
                nearestPoint = nearestPointFromTwo(searchPoint, nearestPoint, rightNearest);
                nearestDistance = nearestDistance(searchPoint, nearestPoint);
                if (distanceToLeft < nearestDistance) {
                    Point2D leftNearest = nearestInner(node.lb, !axisX, subRectLeft, searchPoint, nearestPoint);
                    nearestPoint = nearestPointFromTwo(searchPoint, nearestPoint, leftNearest);
                }
            }
        }
        return nearestPoint;
    }

    private Point2D nearestPointFromTwo(Point2D searchPoint, Point2D p1, Point2D p2) {
        if (p1 == null) {
            return p2;
        }
        if (p2 == null) {
            return p1;
        }
        if (p1.equals(p2)) {
            return p1;
        }
        double d1 = nearestDistance(searchPoint, p1);
        double d2 = nearestDistance(searchPoint, p2);
        if (d1 == Double.POSITIVE_INFINITY && d2 == Double.POSITIVE_INFINITY) {
            return null;
        }
        return d1 < d2 ? p1 : p2;
    }

    private static double nearestDistance(Point2D searchPoint, Point2D nearestPoint) {
        return nearestPoint == null ? Double.POSITIVE_INFINITY : searchPoint.distanceSquaredTo(nearestPoint);
    }

    private static void checkNotNull(Object o) {
        if (o == null) {
            throw new NullPointerException();
        }
    }

    private static int comparePointsByAxis(boolean isAxisX, Point2D childPoint, Point2D parentPoint) {
        // compare if the parent axis is X
        return isAxisX
                ? Point2D.X_ORDER.compare(childPoint, parentPoint)
                : Point2D.Y_ORDER.compare(childPoint, parentPoint);
    }

    private static RectHV subRect(boolean axisX, RectHV rect, Point2D curPoint, boolean isLess) {
        if (axisX) {
            double x = curPoint.x();
            double xmin = isLess ? rect.xmin() : x;
            double xmax = isLess ? x : rect.xmax();
            return new RectHV(xmin, rect.ymin(), xmax, rect.ymax());
        } else {
            double y = curPoint.y();
            double ymin = isLess ? rect.ymin() : y;
            double ymax = isLess ? y : rect.ymax();
            return new RectHV(rect.xmin(), ymin, rect.xmax(), ymax);
        }
    }

    public static void main(String[] args) {                 // unit testing of the methods (optional)
        KdTree kdTree = new KdTree();
        Collection<Point2D> points = Arrays.asList(new Point2D(0.1, 0.1), new Point2D(0.1, 0.2), new Point2D(0.1, 0.3),
                                                   new Point2D(0.1, 0.1));
        for (Point2D point : points) {
            kdTree.insert(point);
        }
        System.out.println(kdTree.size());
        System.out.println("kdTree.contains(new Point2D(0.1, 0.1)): " + kdTree.contains(new Point2D(0.1, 0.1)));
        System.out.println("kdTree.contains(new Point2D(0.1, 0.2)): " + kdTree.contains(new Point2D(0.1, 0.2)));
        System.out.println("kdTree.contains(new Point2D(0.3, 0.5)): " + kdTree.contains(new Point2D(0.3, 0.5)));
        System.out.println("kdTree.contains(new Point2D(0.3, 0.6)): " + kdTree.contains(new Point2D(0.3, 0.6)));
    }

    private static class Node {
        private Point2D p;
        private Node lb, rt;

        Node(Point2D p) {
            checkNotNull(p);
            this.p = p;
        }
    }
}
