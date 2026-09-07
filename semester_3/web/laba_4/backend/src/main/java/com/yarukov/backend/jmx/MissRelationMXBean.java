package com.yarukov.backend.jmx;

import java.util.Map;

public interface MissRelationMXBean {
    double getTotalMissRelation();
    Map<String, Double> getMissRelation();
}