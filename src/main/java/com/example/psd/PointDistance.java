package com.example.psd;


import static java.lang.Math.pow;

public final class PointDistance {

    public static double calculate(double x1, double y1, double x2, double y2) {

        double x = x2-x1;
        double y = y2-y1;
        return Math.sqrt(pow(x,2)+ pow(y,2));
    }

}
