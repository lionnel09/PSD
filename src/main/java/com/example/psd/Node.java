package com.example.psd;

import java.awt.geom.Point2D;

public class Node {

    private Node leftChild;

    private Node rightChild;

    private Node parent;

    private NodeData nodeData;

    public Node() {
        this.leftChild = null;
        this.rightChild = null;
        this.parent = null;
        this.nodeData = null;
    }

    public Node(NodeData nodeData) {
        this();
        this.nodeData = nodeData;
    }

    public Node(Node leftChild, Node rightChild, Node parent, NodeData nodeData) {
        this.leftChild = leftChild;
        this.rightChild = rightChild;
        this.parent = parent;
        this.nodeData = nodeData;
    }

    public Node(Node leftChild, Node rightChild, NodeData nodeData) {
        this(nodeData);
        this.leftChild = leftChild;
        this.rightChild = rightChild;
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

    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public NodeData getNodeData() {
        return nodeData;
    }

    public void setNodeData(NodeData nodeData) {
        this.nodeData = nodeData;
    }

    public boolean equals(Object o) {
        if(o instanceof Node node) {
            return node.getNodeData().toString().equals(this.getNodeData().toString());
        }
        return false;
    }

    int count() {
        return 1 + (leftChild != null ? leftChild.count() : 0) + (rightChild != null ? rightChild.count() : 0);
    }


    private String depthToStr(int depth) {
        return ". ".repeat(Math.max(0, depth));
    }

    @Override
    public String toString() {
        Direction direction = nodeData.getDirection();
        Point2D pointIntersecting = nodeData.getPoint();
        int depth = nodeData.getDepth();
        if (direction == Direction.Leaf) {
            return String.format("%s(%s: (%s, %s))", depthToStr(depth), direction, pointIntersecting.getX(), pointIntersecting.getY());
        } else { // Vertical or Horizontal cut
            return String.format("%s(%s: (%s))", depthToStr(depth), direction, direction == Direction.Vertical ?  nodeData.pointFormatter() :  nodeData.pointFormatter());
        }

    }


}
