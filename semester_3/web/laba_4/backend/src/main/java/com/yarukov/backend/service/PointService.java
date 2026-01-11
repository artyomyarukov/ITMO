package com.yarukov.backend.service;

import com.yarukov.backend.dto.PointRequest;
import com.yarukov.backend.model.Point;
import com.yarukov.backend.model.User;
import com.yarukov.backend.repository.PointRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class PointService {

    @Autowired
    private PointRepository pointRepository;

    public Point savePoint(PointRequest request, User owner) {
        boolean hit = calculateHit(request.getX(), request.getY(), request.getR());

        Point point = new Point();
        point.setX(request.getX());
        point.setY(request.getY());
        point.setR(request.getR());
        point.setOwner(owner);
        point.setHit(hit);
        point.setExecutionTime(LocalDateTime.now());

        return pointRepository.save(point);
    }

    private boolean calculateHit(double x, double y, double r) {

        if (x >= 0 && y >= 0 && x <= r / 2 && y <= r) return true;

        if (x <= 0 && y >= 0 && (x * x + y * y) <= (r * r)) return true;

        if (x >= 0 && y <= 0 && x <= r / 2 && y >= -r && y >= 2*x - r) return true;

        return false;
    }
}