package com.yarukov.backend.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PointRequest {

    @NotNull(message = "X не может быть пустым")
    private double x;

    @NotNull(message = "Y не может быть пустым")
    private double y;

    @NotNull(message = "R не может быть пустым")

    @DecimalMin("0.5")
    @DecimalMax("5.0")
    private double r;

    @NotNull
    private String username;
}