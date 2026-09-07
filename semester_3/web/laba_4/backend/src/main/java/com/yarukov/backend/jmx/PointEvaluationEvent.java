package com.yarukov.backend.jmx;

import jdk.jfr.Category;
import jdk.jfr.Description;
import jdk.jfr.Event;
import jdk.jfr.Label;
import jdk.jfr.Threshold;
import lombok.Setter;

@Label("Point Evaluation Execution")
@Description("Record time and parameters check point")
@Category("Point Analytics")
@Threshold("10 ms")
public class PointEvaluationEvent extends Event {

    @Setter
    @Label("User Login")
    private String username;

    @Label("Coordinate X")
    private double x;

    @Label("Coordinate Y")
    private double y;

    @Label("Radius R")
    private double r;

    @Setter
    @Label("Hit Result")
    private boolean hitResult;

    public void setCoordinates(double x, double y, double r) {
        this.x = x;
        this.y = y;
        this.r = r;
    }

}