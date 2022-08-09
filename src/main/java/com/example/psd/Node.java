package com.example.psd;

import java.awt.geom.Point2D;

public class Node {

    //defines left-child node
    private Node leftChild;

    //defines right-child node
    private Node rightChild;

    //defines parent node
    private Node parent;

    //defines data into node
    private final NodeData nodeData;

    /**
     * default constructor
     */
    public Node() {
        this.leftChild = null;
        this.rightChild = null;
        this.parent = null;
        this.nodeData = null;
    }

    /**
     * constructor
     * @param leftChild left-child
     * @param rightChild right-child
     * @param parent parent
     * @param nodeData data into node
     */
    public Node(Node leftChild, Node rightChild, Node parent, NodeData nodeData) {
        this.leftChild = leftChild;
        this.rightChild = rightChild;
        this.parent = parent;
        this.nodeData = nodeData;
    }

    public Node getLeftChild() {
        return leftChild;
    }

    public void setLeftChild(Node leftChild) {
        if(leftChild!=null)
            leftChild.setParent(this);
        this.leftChild = leftChild;
    }

    public Node getRightChild() {
        return rightChild;
    }

    public void setRightChild(Node rightChild) {
        if(rightChild!=null)
            rightChild.setParent(this);
        this.rightChild = rightChild;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public NodeData getNodeData() {
        return nodeData;
    }

    public boolean equals(Object o) {
        if(o instanceof Node node) {
            return node.getNodeData().toString().equals(this.getNodeData().toString());
        }
        return false;
    }

    /**
     * @return depth count
     */
    int count() {
        return 1 + (leftChild != null ? leftChild.count() : 0) + (rightChild != null ? rightChild.count() : 0);
    }

    /**
     * @param depth depth
     * @return Formatted string of point
     */
    private String depthToStr(int depth) {
        return ". ".repeat(Math.max(0, depth));
    }

    /**
     * @return Formatted String to save in saving file
     */
    @Override
    public String toString() {
        Cut direction = nodeData.getDirection();
        Point2D pointIntersecting = nodeData.getPoint();
        int depth = nodeData.getDepth();
        if (direction == Cut.Leaf) {
            return String.format("%s(%s: (%s, %s))", depthToStr(depth), direction, pointIntersecting.getX(), pointIntersecting.getY());
        } else { // Vertical or Horizontal cut
            return String.format("%s(%s: (%s))", depthToStr(depth), direction, nodeData.pointFormatter());
        }

    }


}
