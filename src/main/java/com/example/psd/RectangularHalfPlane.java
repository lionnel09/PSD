package com.example.psd;

import java.awt.geom.Point2D;

public class RectangularHalfPlane {
    private double minX, minY, maxX, maxY;

    /**
     * Returns half plane's ranges as string
     * @return Formatted String
     */
    public String toString() {
        return String.format("%s<x<=%s, %s<y<=%s", minX,maxX,minY,maxY);
    }

    /**
     * Constructor.
     * @param minX Left bound
     * @param minY Bottom bound
     * @param maxX Right bound
     * @param maxY Top bound
     */
    public RectangularHalfPlane(double minX, double minY, double maxX, double maxY) {
        this.minX = minX;
        this.minY = minY;
        this.maxX = maxX;
        this.maxY = maxY;
    }

    /**
     * default constructor
     */
    public RectangularHalfPlane() {
        this.minX = -Double.MAX_VALUE;
        this.minY = -Double.MAX_VALUE;
        this.maxX = Double.MAX_VALUE;
        this.maxY = Double.MAX_VALUE;
    }

    /**
     * Intersects line's left(or down) side and this half plane
     * @param line The point that a line passes through
     * @param d Dimension of line. d=0: Vertical, d=1 Horizontal
     * @return An intersection area with line and half plane
     */
    public RectangularHalfPlane intersectToLeft(Point2D line, int d) {
        RectangularHalfPlane temp = new RectangularHalfPlane();
        if( d == 0) { // Vertical line // Leftside
            temp.minX = this.minX;
            temp.maxX = line.getX();
            temp.minY = this.minY;
            temp.maxY = this.maxY;
        } else { // Horizontal line // Lowerside
            temp.minX = this.minX;
            temp.maxX = this.maxX;
            temp.maxY = line.getY();
            temp.minY = this.minY;
        }
        return temp;
    }

    /**
     * Intersects line's right(or up) side and this half plane
     * @param line The point that a line passes through
     * @param d Dimension of line. d=0: Vertical, d=1 Horizontal
     * @return An intersection area with line and half plane
     */
    public RectangularHalfPlane intersectToRight(Point2D line, int d) {
        RectangularHalfPlane temp = new RectangularHalfPlane();
        if( d == 0) { // Vertical line
            temp.minX = line.getX();
            temp.maxX = this.maxX;
            temp.minY = this.minY;
            temp.maxY = this.maxY;
        } else { // Horizontal line
            temp.minX = this.minX;
            temp.maxX = this.maxX;
            temp.maxY = this.maxY;
            temp.minY = line.getY();
        }
        return temp;
    }

    /**
     * Checks if given point as parameter is contained in this half plane (closed)
     * @param point Point to be checked
     * @return True if point is contained, false otherwise
     */
    public boolean contains(Point2D point) {
        return (point.getX() <= this.maxX && point.getX() > this.minX && point.getY() <= this.maxY && point.getY() > this.minY);
    }

    /**
     * Checks if given half plane as parameter is contained in this half plane (closed)
     * @param halfPlane Half plane to be checked
     * @return True if half plane is contained, false otherwise
     */
    public boolean contains(RectangularHalfPlane halfPlane) {
        return (halfPlane.maxX <= this.maxX && halfPlane.minX >= this.minX && halfPlane.maxY <= this.maxY && halfPlane.minY >= this.minY);
    }

    /**
     * Checks if given half plane as parameter intersects with this half plane (closed)
     * @param halfPlane Half plane to be checked
     * @return True if half planes intersect, false otherwise
     */
    public boolean intersects(RectangularHalfPlane halfPlane) {
        return !(halfPlane.maxX < this.minX || halfPlane.minX > this.maxX || halfPlane.maxY < this.minY || halfPlane.minY > this.maxY);
    }
}