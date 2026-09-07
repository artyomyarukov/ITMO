package com.yarukov.backend.jmx;

import jdk.jfr.Category;
import jdk.jfr.Description;
import jdk.jfr.Event;
import jdk.jfr.Label;

@Label("Point Miss Event")
@Description("User miss record")
@Category("Point Events")
public class PointMissEvent extends Event {

    @Label("Username")
    private final String username;

    @Label("Current Streak Misses")
    private final long streak;

    @Label("X Coordinate")
    private final double x;

    @Label("Y Coordinate")
    private final double y;

    public PointMissEvent(String username, long streak, double x, double y) {
        this.username = username;
        this.streak = streak;
        this.x = x;
        this.y = y;
    }

    public String getUsername() { return username; }
    public long getStreak() { return streak; }
    public double getX() { return x; }
    public double getY() { return y; }
}