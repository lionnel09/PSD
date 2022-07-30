package com.example.psd;

import java.awt.geom.Point2D;

public class NodeData {

    private Direction direction;
    private final Point2D point; // The point of node
    private RectangularHalfPlane leftRegion; // The region of left-side of the line (closed)
    private RectangularHalfPlane rightRegion; // The region of right-side of the line (open)
    private final int depth; // depth of the node in tree

    public NodeData(Direction direction, Point2D point, int depth) {
        this.direction = direction;
        this.point = point;
        this.leftRegion = null;
        this.rightRegion = null;
        this.depth = depth;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public Point2D getPoint() {
        return point;
    }

    public RectangularHalfPlane getLeftRegion() {
        return leftRegion;
    }

    public void setLeftRegion(RectangularHalfPlane leftRegion) {
        this.leftRegion = leftRegion;
    }

    public RectangularHalfPlane getRightRegion() {
        return rightRegion;
    }

    public void setRightRegion(RectangularHalfPlane rightRegion) {
        this.rightRegion = rightRegion;
    }

    public int getDepth() {
        return depth;
    }

    @Override
    public String toString() {

        return String.format("(%s,%s) : %s", point.getX(), point.getY(),direction);
    }
    public String pointFormatter(){
        return String.format("%s, %s", point.getX(), point.getY());
    }
}
