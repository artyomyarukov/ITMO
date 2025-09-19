package com.artyomyarukov.web;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class Params {
    private final double x;
    private final double y;
    private final double r;

    public Params(String query) throws ValidationException {
        if (query == null || query.isEmpty()) {
            throw new ValidationException("Missing parameters");
        }

        Map<String, String> params = splitQuery(query);
        validateParams(params);

        this.x = Double.parseDouble(params.get("x"));
        this.y = Double.parseDouble(params.get("y"));
        this.r = Double.parseDouble(params.get("r"));
    }

    private static Map<String, String> splitQuery(String query) {
        return Arrays.stream(query.split("&"))
                .map(pair -> pair.split("="))
                .collect(
                        Collectors.toMap(
                                pairParts -> URLDecoder.decode(pairParts[0], StandardCharsets.UTF_8),
                                pairParts -> URLDecoder.decode(pairParts[1], StandardCharsets.UTF_8),
                                (a, b) -> b,
                                HashMap::new
                        )
                );
    }

    private static void validateParams(Map<String, String> params) throws ValidationException {
        // Проверка X
        String x = params.get("x");
        if (x == null || x.isEmpty()) {
            throw new ValidationException("Parameter 'x' is required");
        }
        try {
            double xx = Double.parseDouble(x);
            if (xx < -5 || xx > 5) {
                throw new ValidationException("X must be between -5 and 5");
            }
        } catch (NumberFormatException e) {
            throw new ValidationException("X must be a number");
        }

        // Проверка Y
        String y = params.get("y");
        if (y == null || y.isEmpty()) {
            throw new ValidationException("Parameter 'y' is required");
        }
        try {
            double yy = Double.parseDouble(y);
            if (yy < -5 || yy > 3) {
                throw new ValidationException("Y must be between -5 and 3");
            }
        } catch (NumberFormatException e) {
            throw new ValidationException("Y must be a number");
        }

        // Проверка R
        String r = params.get("r");
        if (r == null || r.isEmpty()) {
            throw new ValidationException("Parameter 'r' is required");
        }
        try {
            double rr = Double.parseDouble(r);
            if (rr < 1 || rr > 3) {
                throw new ValidationException("R must be between 1 and 3");
            }
        } catch (NumberFormatException e) {
            throw new ValidationException("R must be a number");
        }
    }

    public double getX() { return x; }
    public double getY() { return y; }
    public double getR() { return r; }
}