package com.example.psd;

import java.awt.geom.Point2D;

public class NodeData {

    //Cut Direction
    private Cut direction;
    // The point of node
    private Point2D point;
    // The region of left-side of the line (closed)
    private RectangularHalfPlane leftRegion;
    // The region of right-side of the line (open)
    private RectangularHalfPlane rightRegion;
    // depth of the node into tree
    private final int depth;

    public NodeData(Cut direction, Point2D point, int depth) {
        this.direction = direction;
        this.point = point;
        this.leftRegion = null;
        this.rightRegion = null;
        this.depth = depth;
    }

    public Cut getDirection() {
        return direction;
    }

    public void setDirection(Cut direction) {
        this.direction = direction;
    }

    public Point2D getPoint() {
        return point;
    }
     public void setPoint(Point2D point){
        this.point = point;
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
        return String.format("(%s, %s)", point.getX(), point.getY());
    }
}
