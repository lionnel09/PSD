package com.example.psd;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static java.lang.System.out;

public class KDTree {



    private Node root;

    public KDTree() {
    }

    private Node innerTreeBuilder(List<Point2D> points, int depth) {

        Direction currentDirection;
        Node node;


        if (points == null || points.size() == 0) return null;

        List<Point2D> leftPoints = new ArrayList<>(points.size() - 1);
        List<Point2D> rightPoints = new ArrayList<>(points.size() - 1);



        if (depth % 2 == 0) {
            currentDirection = Direction.Vertical;
            points.sort(Comparator.comparing(Point2D::getX));

        } else {
            currentDirection = Direction.Horizontal;
            points.sort(Comparator.comparing(Point2D::getY));
        }
        if (points.size() == 1) {
            currentDirection = Direction.Leaf;
        }


        int medianIndex = points.size() / 2;
        node = new Node(null, null, new NodeData(currentDirection, points.get(medianIndex), depth));

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

    //build T
    public void buildTree(Data data) {
        this.setRoot(innerTreeBuilder(data.getPoints(), 0));
    }

    public void insert(Point2D point){
        if (point == null){
            return;
        }
        innerInsert(point,root,null);
    }


    private void innerInsert(Point2D point , Node node, Node parent){
        if (contains(point)){
            return;
        }
        if (node == null && parent !=null) {
            setParentNode(parent,point);
            return;
        }

        if(node.getNodeData().getDirection().equals(Direction.Leaf)){

            if (node.getNodeData().getDepth() % 2 == 0){

                node.getNodeData().setDirection(Direction.Vertical);

                if (point.getX() <= node.getNodeData().getPoint().getX() )
                    node.setLeftChild(new Node(null, null, node, new NodeData(Direction.Leaf, point, node.getNodeData().getDepth() + 1)));

                else
                    node.setRightChild(new Node(null, null, node, new NodeData(Direction.Leaf, point, node.getNodeData().getDepth() + 1)));

                out.println("New Node inserted" );

            }else {

                node.getNodeData().setDirection(Direction.Horizontal);

                if (node.getNodeData().getPoint().getY() <= point.getY())
                    node.setLeftChild(new Node(null, null, node, new NodeData(Direction.Leaf, point, node.getNodeData().getDepth() + 1)));

                else
                    node.setRightChild(new Node(null, null, node, new NodeData(Direction.Leaf, point, node.getNodeData().getDepth() + 1)));
                out.println("New Node inserted" );
            }
        }

        else if(node.getNodeData().getDirection().equals(Direction.Vertical)){

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

    private void setParentNode(Node parent,Point2D point){
        if(parent.getNodeData().getDirection().equals(Direction.Vertical)){

            if(point.getX() <= parent.getNodeData().getPoint().getX())
                parent.setLeftChild(new Node(null,null,parent,new NodeData(Direction.Leaf,point,parent.getNodeData().getDepth()+1)));

            else
                parent.setRightChild(new Node(null,null,parent,new NodeData(Direction.Leaf,point,parent.getNodeData().getDepth()+1)));

        }else {

            if(point.getY() <= parent.getNodeData().getPoint().getY())
                parent.setLeftChild(new Node(null,null,parent,new NodeData(Direction.Leaf,point,parent.getNodeData().getDepth()+1)));
            else
                parent.setRightChild(new Node(null,null,parent,new NodeData(Direction.Leaf,point,parent.getNodeData().getDepth()+1)));

        }
    }

    public boolean contains(Point2D point) {
        if (point == null) throw new IllegalArgumentException();
        return contains(root, point);
    }

    private boolean contains(Node node, Point2D point) {
        if (node == null) return false;

        if (node.getNodeData().getDirection().equals(Direction.Leaf))
            if (point.getX() == node.getNodeData().getPoint().getX() && point.getY() == node.getNodeData().getPoint().getY())
                return true;


        if (node.getNodeData().getDirection().equals(Direction.Horizontal)) {

            if (point.getY() <= node.getNodeData().getPoint().getY() && point.getX() != node.getNodeData().getPoint().getX())
                return contains(node.getLeftChild(), point);

            else if (point.getY() > node.getNodeData().getPoint().getY())
                return contains(node.getRightChild(), point);

            else
                return true;


        } else if (node.getNodeData().getDirection().equals(Direction.Vertical)) {

            if (point.getX() <= node.getNodeData().getPoint().getX() && point.getY() != node.getNodeData().getPoint().getY())
                return contains(node.getLeftChild(), point);

            else if (point.getX() > node.getNodeData().getPoint().getX())
                return contains(node.getRightChild(), point);
            else
                return true;

        }
        return false;
    }

    public void displayTree() {

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
     * @param d Dimension, d=0: X, d=1: Y
     * @return The minimum valued point in dimension d
     */
    private static Point2D min(Point2D p1, Point2D p2, Point2D p3, int d) {
        return getPoint(p1, p2, p3, d, p1.getX() <= p2.getX(), p1.getX() <= p3.getX(), p2.getX() <= p1.getX(), p2.getX() <= p3.getX(), p1.getY() <= p2.getY(), p1.getY() <= p3.getY(), p2.getY() <= p1.getY(), p2.getY() <= p3.getY());
    }

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


    public Node getRoot() {
        return root;
    }

    public void setRoot(Node root) {
        this.root = root;
    }

    @Override
    public String toString() {
        return "KDTree{" +
                "root=" + root +
                '}';
    }
}
