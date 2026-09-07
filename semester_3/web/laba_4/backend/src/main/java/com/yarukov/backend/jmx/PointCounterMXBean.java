package com.yarukov.backend.jmx;

import java.util.Map;

public interface PointCounterMXBean {
    long getAllPoints();
    long getMissPoints();
    long getCurrentStreak();
    Map<String, UserStatistic> getUsersStats();
}
