package com.yarukov.model;

import java.io.Serializable;

public class Result implements Serializable {
    private double x;
    private double y;
    private double r;
    private String curTime;
    private String workTime;
    private boolean popalIliNet;
    private Double canvasX;  // координаты на canvas
    private Double canvasY;


    public Result(double x, double y, double r, String curTime, String workTime, boolean popalIliNet) {
        this.x = x;
        this.y = y;
        this.r = r;
        this.curTime = curTime;
        this.workTime = workTime;
        this.popalIliNet = popalIliNet;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getR() {
        return r;
    }

    public String getCurTime() {
        return curTime;
    }

    public String getWorkTime() {
        return workTime;
    }

    public boolean isPopalIliNet() {
        return popalIliNet;
    }

    public Double getCanvasX() {
        return canvasX;
    }

    public Double getCanvasY() {
        return canvasY;
    }

    public void setCanvasX(Double canvasX) {
        this.canvasX = canvasX;
    }

    public void setCanvasY(Double canvasY) {
        this.canvasY = canvasY;
    }
}
