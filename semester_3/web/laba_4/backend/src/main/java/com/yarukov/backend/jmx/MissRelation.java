package com.yarukov.backend.jmx;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@ManagedResource(objectName = "com.yarukov.backend.jmx:type=MissPercentage,name=missPercentage")
public class MissRelation implements MissRelationMXBean {

    private final PointCounter pointCounter;

    @Autowired
    public MissRelation(PointCounter pointCounter) {
        this.pointCounter = pointCounter;
    }

    @Override
    public double getTotalMissRelation() {
        long total = pointCounter.getAllPoints();
        if (total == 0) {
            return 0.0;
        }
        return ((double) pointCounter.getMissPoints() / total) * 100.0;
    }

    @Override
    public Map<String, Double> getMissRelation() {
        Map<String, Double> result = new HashMap<>();
        for (Map.Entry<String, UserStatistic> entry : pointCounter.getUsersStats().entrySet()) {
            UserStatistic stats = entry.getValue();
            long all = stats.getAll();
            double relation = (all == 0) ? 0.0 : ((double) stats.getMisses() / all) * 100.0;
            result.put(entry.getKey(), relation);
        }
        return result;
    }
}