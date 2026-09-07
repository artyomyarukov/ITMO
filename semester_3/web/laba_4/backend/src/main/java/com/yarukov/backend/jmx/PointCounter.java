package com.yarukov.backend.jmx;

import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.stereotype.Component;

import javax.management.Notification;
import javax.management.NotificationBroadcasterSupport;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Component
@ManagedResource(objectName = "com.yarukov.backend.jmx:type=PointCounter,name=pointCounter")
public class PointCounter extends NotificationBroadcasterSupport implements PointCounterMXBean {

    private final Map<String, UserStatistic> userStatsMap = new ConcurrentHashMap<>();
    private final AtomicLong allPoints = new AtomicLong(0);
    private final AtomicLong allMisses = new AtomicLong(0);
    private final AtomicLong lastActiveUserStreak = new AtomicLong(0);
    private long notificationSequence = 1;


    public void registerPoint(String username, double x, double y, boolean isHit) {

        if (username == null || username.isBlank()) {
            return;
        }

        allPoints.incrementAndGet();
        if (!isHit) {
            allMisses.incrementAndGet();
        }

        int quarter = quarter(x, y);

        userStatsMap.compute(username, (user, stats) -> {
            if (stats == null) {
                stats = new UserStatistic(user);
            }
            stats.registerPoint(isHit, quarter);
            lastActiveUserStreak.set(stats.getStreakMisses());


            if (stats.getStreakMisses() == 3) {
                sendNotification(username);
            }
            return stats;
        });
    }

    private void sendNotification(String username) {
        Notification notification = new Notification(
                "user.streak.misses",
                this,
                notificationSequence++,
                System.currentTimeMillis(),
                "Пользователь '" + username + " совершил 3 промаха подряд"
        );
        super.sendNotification(notification);
        System.out.println(" JMX: оповещение о 3 промахах для: " + username);
    }

    private int quarter(double x, double y) {
        if (x >= 0 && y >= 0){
            return 1;
        }
        if (x < 0 && y >= 0){
            return 2;
        }
        if (x < 0 && y < 0) {
            return 3;
        }
        return 4;
    }

    @Override
    public long getAllPoints() {
        return allPoints.get();
    }

    @Override
    public long getMissPoints() {
        return allMisses.get();
    }

    @Override
    public long getCurrentStreak() {
        return lastActiveUserStreak.get();
    }

    @Override
    public Map<String, UserStatistic> getUsersStats() {
        return userStatsMap;
    }
}