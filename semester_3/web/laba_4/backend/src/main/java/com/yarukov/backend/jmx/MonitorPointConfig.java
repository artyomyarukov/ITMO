package com.yarukov.backend.jmx;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.stereotype.Component;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.management.monitor.GaugeMonitor;
import java.lang.management.ManagementFactory;

@Component
public class MonitorPointConfig {

    private GaugeMonitor monitor;

    @PostConstruct
    public void setupMonitor() {
        try {
            MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
            monitor = new GaugeMonitor();
            monitor.addObservedObject(new ObjectName("com.yarukov.backend.jmx:type=PointCounter,name=pointCounter"));
            monitor.setObservedAttribute("CurrentStreak");
            monitor.setThresholds(3L, 0L);
            monitor.setNotifyHigh(true);
            monitor.setNotifyLow(false);
            monitor.setGranularityPeriod(1000);
            ObjectName monitorName = new ObjectName("com.yarukov.backend.jmx:type=GaugeMonitor,name=streakMonitor");
            mbs.registerMBean(monitor, monitorName);
            monitor.start();

        } catch (Exception e) {
            System.err.println("JMX Error " + e.getMessage());
        }
    }

    @PreDestroy
    public void stopMonitoring() {
        if (monitor != null) {
            monitor.stop();
        }
    }


}