package com.example.psd;

import java.awt.geom.Point2D;
import java.util.List;

public class Data {


    private String c1; //first criteria
    private String c2; //second criteria
    private int n; // number of records
    private List<Point2D> points; // points used to build the tree

    public Data() {
    }

    public String getC1() {
        return c1;
    }

    public void setC1(String c1) {
        this.c1 = c1;
    }

    public String getC2() {
        return c2;
    }

    public void setC2(String c2) {
        this.c2 = c2;
    }

    public int getN() {
        return n;
    }

    public void setN(int n) {
        this.n = n;
    }

    public List<Point2D> getPoints() {
        return points;
    }

    public void setPoints(List<Point2D> points) {
        this.points = points;
    }

    @Override
    public String toString() {
        return "Data{" +
                "c1=" + c1 +
                ", c2=" + c2 +
                ", n=" + n +
                ", points=" + points +
                '}';
    }
}
