package com.yarukov.backend.jmx;

import java.beans.ConstructorProperties;

public class UserStatistic {
    private final String username;
    private long all;
    private long misses;
    private long streakMisses;
    private long firstQuarterHits;
    private long secondQuarterHits;
    private long fourthQuarterHits;

    @ConstructorProperties({"username", "all", "misses", "streakMisses", "firstQuarterHits", "secondQuarterHits", "fourthQuarterHits"})
    public UserStatistic(String username, long all, long misses, long streakMisses,
                         long firstQuarterHits, long secondQuarterHits, long fourthQuarterHits) {
        this.username = username;
        this.all = all;
        this.misses = misses;
        this.streakMisses = streakMisses;
        this.firstQuarterHits = firstQuarterHits;
        this.secondQuarterHits = secondQuarterHits;
        this.fourthQuarterHits = fourthQuarterHits;
    }

    public UserStatistic(String username) {
        this(username, 0, 0, 0, 0, 0, 0);
    }

    public synchronized void registerPoint(boolean isHit, int quarter) {
        this.all++;
        if (isHit) {
            this.streakMisses = 0;
            if (quarter == 1) {
                this.firstQuarterHits++;
            } else if (quarter == 2) {
                this.secondQuarterHits++;
            } else if (quarter == 4) {
                this.fourthQuarterHits++;
            }
        } else {
            this.misses++;
            this.streakMisses++;
        }
    }

    public String getUsername() { return username; }
    public long getAll() { return all; }
    public long getMisses() { return misses; }
    public long getStreakMisses() { return streakMisses; }
    public long getFirstQuarterHits() { return firstQuarterHits; }
    public long getSecondQuarterHits() { return secondQuarterHits; }
    public long getFourthQuarterHits() { return fourthQuarterHits; }
}
