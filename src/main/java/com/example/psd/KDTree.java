package com.example.psd;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static java.lang.System.out;

/**
 * class that implements all method use to interact with the tree
 */
public class KDTree {


    // root of tree
    private Node root;

    public KDTree() {
    }

    /**
     * internal method to build a 2DTree
     * @param points points using to construct tree
     * @param depth is the depth for the current node
     * @return a 2DTree
     */
    private Node innerTreeBuilder(List<Point2D> points, int depth) {

        Cut currentCut;
        Node node;


        if (points == null || points.size() == 0) return null;

        List<Point2D> leftPoints = new ArrayList<>(points.size() - 1);
        List<Point2D> rightPoints = new ArrayList<>(points.size() - 1);



        if (even(depth)) {
            currentCut = Cut.Vertical;
            points.sort(Comparator.comparing(Point2D::getX));

        } else {
            currentCut = Cut.Horizontal;
            points.sort(Comparator.comparing(Point2D::getY));
        }
        if (points.size() == 1) {
            currentCut = Cut.Leaf;
        }


        int medianIndex = points.size() / 2;
        node = new Node(null, null, new NodeData(currentCut, points.get(medianIndex), depth));

        //Process list to see where each non-median point lies
        int bound = points.size();
        for (int i = 0; i < bound; i++) {
            if (i != medianIndex) {
                Point2D p = points.get(i);
                if (i < medianIndex)
                    leftPoints.add(p);

                else
                    rightPoints.add(p);
            }
        }

        if (leftPoints.size() > 0) {

            node.setLeftChild(innerTreeBuilder(leftPoints, depth + 1));
            node.getLeftChild().setParent(node);
        }
        if (rightPoints.size() > 0) {

            node.setRightChild(innerTreeBuilder(rightPoints, depth + 1));
            node.getRightChild().setParent(node);
        }

        return node;
    }

    /**
     * build  a 2DTree
     * @param data data loaded after file readed
     */
    public void buildTree(Data data) {
        this.setRoot(innerTreeBuilder(data.getPoints(), 0));
        this.calculateRegions();

    }

    /**
     * method to calculate the region of every point using his splitting line
     */
    private void calculateRegions() {
        RectangularHalfPlane halfPlane = new RectangularHalfPlane(); // Parent area to be intersected is whole area
        root.getNodeData().setLeftRegion(halfPlane.intersectToLeft(root.getNodeData().getPoint(), 0));
        root.getNodeData().setRightRegion(halfPlane.intersectToRight(root.getNodeData().getPoint(), 0));
        innerCalculateRegions(root);
    }

    /**
     * Intersects current line node's regions with its child node's regions. Store results in child nodes.
     * @param root The node whose child's regions are going to be calculated
     */
    private void innerCalculateRegions(Node root) {
        if (root.getLeftChild() != null) { // Calculate left child's regions
            Node ls = root.getLeftChild();
            RectangularHalfPlane latestArea = root.getNodeData().getLeftRegion();
            ls.getNodeData().setLeftRegion(latestArea.intersectToLeft(ls.getNodeData().getPoint(), ls.getNodeData().getDepth() % 2));
            ls.getNodeData().setRightRegion(latestArea.intersectToRight(ls.getNodeData().getPoint(), ls.getNodeData().getDepth() % 2));
        }

        if (root.getRightChild() != null) { // Calculate right child's regions
            Node rs = root.getRightChild();
            RectangularHalfPlane latestArea = root.getNodeData().getRightRegion();
            rs.getNodeData().setLeftRegion(latestArea.intersectToLeft(rs.getNodeData().getPoint(), rs.getNodeData().getDepth() % 2));
            rs.getNodeData().setRightRegion(latestArea.intersectToRight(rs.getNodeData().getPoint(), rs.getNodeData().getDepth() % 2));
        }

        //traversing the tree
        if (root.getLeftChild() != null) {
            innerCalculateRegions(root.getLeftChild());
        }

        if (root.getRightChild() != null) {
            innerCalculateRegions(root.getRightChild());
        }
    }

    /**
     * print the tree range of search
     * @param rectangularHalfPlane defines the search's range
     */
    public void printRange(RectangularHalfPlane rectangularHalfPlane) {
        Search2DTree(root, rectangularHalfPlane);
        out.println();
    }

    /**
     * method using to insert new point into the tree
     * @param point defines the points to insert
     */
    public void insert(Point2D point){
        if (point == null){
            return;
        }
        innerInsert(point, root,null);

        // re-calculate region after insertion of new point
        innerCalculateRegions(root);
    }

    /**
     * internal method that operate to insert the point into the tree
     * @param point defines the point to insert
     * @param node defines current node during the traversing
     * @param parent defines parent of current node . it's used to set the direction of splitting line
     */
    private void innerInsert(Point2D point , Node node, Node parent){
        //verify if point already exist into the tree
        if (contains(point)){
            return;
        }

        if (node == null && parent !=null) {
            setParentNode(parent,point);
            return;
        }

        if(node.getNodeData().getDirection().equals(Cut.Leaf)){

            if (even(node.getNodeData().getDepth())){

                node.getNodeData().setDirection(Cut.Vertical); // set current node direction if depth is even to insert point

                if (point.getX() <= node.getNodeData().getPoint().getX() ) // setting child
                    node.setLeftChild(new Node(null, null, node, new NodeData(Cut.Leaf, point, node.getNodeData().getDepth() + 1)));

                else
                    node.setRightChild(new Node(null, null, node, new NodeData(Cut.Leaf, point, node.getNodeData().getDepth() + 1)));

                out.println("New Node inserted" );

            }else {

                node.getNodeData().setDirection(Cut.Horizontal); //set current node direction if depth is odd to insert point

                if (node.getNodeData().getPoint().getY() <= point.getY()) // setting child
                    node.setLeftChild(new Node(null, null, node, new NodeData(Cut.Leaf, point, node.getNodeData().getDepth() + 1)));

                else
                    node.setRightChild(new Node(null, null, node, new NodeData(Cut.Leaf, point, node.getNodeData().getDepth() + 1)));
                out.println("New Node inserted" );
            }
        }

        // continue traversing
        else if(node.getNodeData().getDirection().equals(Cut.Vertical)){

            if(point.getX() <= node.getNodeData().getPoint().getX())
                innerInsert(point,node.getLeftChild(),node);
            else
                innerInsert(point,node.getRightChild(),node);
        }
        else {
            if(point.getY() <= node.getNodeData().getPoint().getY())
                innerInsert(point,node.getLeftChild(),node);

            else
                innerInsert(point,node.getRightChild(),node);
        }

    }

    /**
     * method to insert an empty node into tree (can be ameliorated )
     * @param parent parent's node to set
     * @param point point to add
     */
    private void setParentNode(Node parent,Point2D point){
        if(parent.getNodeData().getDirection().equals(Cut.Vertical)){

            if(point.getX() <= parent.getNodeData().getPoint().getX())
                parent.setLeftChild(new Node(null,null,parent,new NodeData(Cut.Leaf,point,parent.getNodeData().getDepth()+1)));

            else
                parent.setRightChild(new Node(null,null,parent,new NodeData(Cut.Leaf,point,parent.getNodeData().getDepth()+1)));

        }else {

            if(point.getY() <= parent.getNodeData().getPoint().getY())
                parent.setLeftChild(new Node(null,null,parent,new NodeData(Cut.Leaf,point,parent.getNodeData().getDepth()+1)));

            else
                parent.setRightChild(new Node(null,null,parent,new NodeData(Cut.Leaf,point,parent.getNodeData().getDepth()+1)));

        }
    }

    /**
     * public method to verify if point contains
     * @param point defines the point to search
     * @return true is point is reached
     */
    public boolean contains(Point2D point) {
        if (point == null) throw new IllegalArgumentException();
        return innerContains(root, point);
    }

    /**
     *
     * @param node defines the root tree
     * @param point defines points to insert
     * @return true if contains
     */
    private boolean innerContains(Node node, Point2D point) {
        if (node == null) return false;

        if (node.getNodeData().getDirection().equals(Cut.Leaf))
            if (point.getX() == node.getNodeData().getPoint().getX() && point.getY() == node.getNodeData().getPoint().getY())
                return true;


        if (node.getNodeData().getDirection().equals(Cut.Horizontal)) {

            if (point.getY() <= node.getNodeData().getPoint().getY() && point.getX() != node.getNodeData().getPoint().getX())
                return innerContains(node.getLeftChild(), point);

            else if (point.getY() > node.getNodeData().getPoint().getY())
                return innerContains(node.getRightChild(), point);

            else
                return true;


        } else if (node.getNodeData().getDirection().equals(Cut.Vertical)) {

            if (point.getX() <= node.getNodeData().getPoint().getX() && point.getY() != node.getNodeData().getPoint().getY())
                return innerContains(node.getLeftChild(), point);

            else if (point.getX() > node.getNodeData().getPoint().getX())
                return innerContains(node.getRightChild(), point);
            else
                return true;

        }
        return false;
    }

    /**
     * draw the three
     */
    public void draw() {

        if (root == null) {
            out.println("Tree is empty!");
            return;
        }
        TreePrinter.printTree(root);
    }

    /**
     * Searches the tree for minimum x or y valued point.
     * @param d Search dimension, d=0: C1, d=1: C2
     * @return Found point
     */
    public Point2D findMin(int d) {
        return innerFindMin(root, d, 0);
    }

    private Point2D innerFindMin(Node node, int d, int depth) {
        if (node == null) {
            return new Point2D.Double(Double.MAX_VALUE, Double.MAX_VALUE);
        }
        if (depth % 2 == d) {
            if (node.getLeftChild() == null) {
                return node.getNodeData().getPoint();
            }
            return innerFindMin(node.getLeftChild(), d, depth + 1);
        } else {
            return min(node.getNodeData().getPoint(), innerFindMin(node.getLeftChild(), d, depth + 1), innerFindMin(node.getRightChild(), d, depth + 1), d);
        }
    }

    /**
     * Returns minimum valued point in dimension d
     * @param p1 Point 1
     * @param p2 Point 2
     * @param p3 Point 3
     * @param d defines dimensions, d=0 for  X, d=1  for Y
     * @return The minimum valued point in dimension d
     */
    private static Point2D min(Point2D p1, Point2D p2, Point2D p3, int d) {
        return getPoint(p1, p2, p3, d, p1.getX() <= p2.getX(), p1.getX() <= p3.getX(), p2.getX() <= p1.getX(), p2.getX() <= p3.getX(), p1.getY() <= p2.getY(), p1.getY() <= p3.getY(), p2.getY() <= p1.getY(), p2.getY() <= p3.getY());
    }

    /**
     *public method use to find max
     * @param d defines dimension d=0 : X , d=1: Y
     * @return founded max point
     */
    public Point2D findMax(int d) {
        return innerFindMax(root, d, 0);
    }

    /**
     * Tries going to rightmost nodes in given dimension nodes. In other dimension nodes, continues searching in both children.
     * @param node Current node
     * @param d Dimension
     * @param depth Current depth
     * @return Found point
     */
    private Point2D innerFindMax(Node node, int d, int depth) {
        if (node == null) {
            return new Point2D.Double(-Double.MAX_VALUE, -Double.MAX_VALUE);
        }
        if (depth % 2 == d) {
            if (node.getRightChild() == null) {
                return node.getNodeData().getPoint();
            }
            return innerFindMax(node.getRightChild(), d, depth + 1);
        } else {
            return max(node.getNodeData().getPoint(), innerFindMax(node.getLeftChild(), d, depth + 1), innerFindMax(node.getRightChild(), d, depth + 1), d);
        }
    }

    /**
     * Returns maximum valued point in dimension d
     * @param p1 Point 1
     * @param p2 Point 2
     * @param p3 Point 3
     * @param d Dimension, d=0: X, d=1: Y
     * @return The maximum valued point in dimension d
     */
    private static Point2D max(Point2D p1, Point2D p2, Point2D p3, int d) {
        return getPoint(p1, p2, p3, d, p1.getX() >= p2.getX(), p1.getX() >= p3.getX(), p2.getX() >= p1.getX(), p2.getX() >= p3.getX(), p1.getY() >= p2.getY(), p1.getY() >= p3.getY(), p2.getY() >= p1.getY(), p2.getY() >= p3.getY());
    }

    private static Point2D getPoint(Point2D p1, Point2D p2, Point2D p3, int d, boolean b, boolean b2, boolean b3, boolean b4, boolean b5, boolean b6, boolean b7, boolean b8) {
        if (d == 0) {
            if (b && b2) {
                return p1;
            } else if (b3 && b4) {
                return p2;
            } else {
                return p3;
            }
        } else { // d == 1
            if (b5 && b6) {
                return p1;
            } else if (b7 && b8) {
                return p2;
            } else {
                return p3;
            }
        }
    }

    /**
     * method to find all point into a range
     * @param root defines root of tree
     * @param range defines the search range
     */
    private void Search2DTree(Node root, RectangularHalfPlane range) {
        if (root == null) return;


        if (root.getNodeData().getDirection() == Cut.Leaf) { // only Leaf is contained into range
            if (range.contains(root.getNodeData().getPoint()))
                out.print(root.getNodeData().pointFormatter() + "\n");
        }
        else {

            if(range.contains(root.getNodeData().getPoint())){
                out.print(root.getNodeData().pointFormatter() + "\n" );
            }

            if (range.contains(root.getNodeData().getLeftRegion())) { // Left subTree is fully contained into range, print all points.
                depthFirstPrint(root.getLeftChild());
            } else if (range.intersects(root.getNodeData().getLeftRegion())) { // Continue searching
                Search2DTree(root.getLeftChild(), range);
            }

            if (range.contains(root.getNodeData().getRightRegion())) { // Right subTree is fully contained into range, print all points.
                depthFirstPrint(root.getRightChild());
            } else if (range.intersects(root.getNodeData().getRightRegion())) { // Continue searching
                Search2DTree(root.getRightChild(), range);
            }
        }
    }

    /**
     * preOrder writing node if subtree is fully contained
     * @param root defines the root of tree of subTree
     */
    private void depthFirstPrint(Node root) {
        if(root == null) return;

        out.println(root.getNodeData().pointFormatter() + "\n");
        depthFirstPrint(root.getLeftChild());
        depthFirstPrint(root.getRightChild());

    }


    public Node getRoot() {
        return root;
    }

    public void setRoot(Node root) {
        this.root = root;
    }

    /**
     *
     * @param depth defines the current depth
     * @return true if even
     */
    private boolean even(int depth){
        return depth % 2 == 0;
    }

    @Override
    public String toString() {
        return "KDTree{" +
                "root=" + root +
                '}';
    }
}
