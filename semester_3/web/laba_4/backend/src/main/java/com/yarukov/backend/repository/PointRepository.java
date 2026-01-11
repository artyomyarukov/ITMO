package com.yarukov.backend.repository;

import com.yarukov.backend.model.Point;
import com.yarukov.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PointRepository extends JpaRepository<Point, Long> {
    List<Point> findAllByOwnerOrderByExecutionTimeDesc(User owner);
}