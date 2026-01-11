package com.yarukov.backend.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity
@Table(name = "points")
public class Point {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double x;
    private double y;
    private double r;
    private boolean hit;

    @Column(name = "execution_time")
    private LocalDateTime executionTime;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User owner;
}