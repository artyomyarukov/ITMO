package com.artyomyarukov.web;

import com.fastcgi.FCGIInterface;

import java.io.BufferedOutputStream;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Locale;
import java.util.Scanner;

public class Main {

    private static final String HTTP_RESPONSE = """
            Content-Type: application/json
            Content-Length: %d
            
            %s
            """;

    private static final String HTTP_ERROR = """
            Content-Type: application/json
            Content-Length: %d
            
            %s
            """;


    public static void main(String[] args) {
        FCGIInterface fcgi = new FCGIInterface();
        PrintStream output = new PrintStream(new BufferedOutputStream(new FileOutputStream(FileDescriptor.out), 128), true);


        System.out.println("Starting FastCGI server...");

        while (fcgi.FCGIaccept() >= 0) {
            try {
                // Получаем параметры из FastCGI
                Scanner sc = new Scanner(System.in);
                String queryString = sc.nextLine();
                if (queryString == null) {
                    queryString = "";
                }

                output.println("получили параметры");
                output.println("Query String: " + queryString);


                // Парсим параметры
                Params params = new Params(queryString);

                // Замер времени выполнения
                Instant startTime = Instant.now();
                boolean result = AreaChecker.checkHit(params.getX(), params.getY(), params.getR());
                output.println("распарсился");
                Instant endTime = Instant.now();
                double executionTime = ChronoUnit.NANOS.between(startTime, endTime);

                // Формируем JSON ответ
                String json = String.format(
                        Locale.US,
                        "{\"success\":true,\"result\":%b,\"x\":%.2f,\"y\":%.2f,\"r\":%.2f,\"timestamp\":\"%s\",\"executionTime\":%f}",
                        result, params.getX(), params.getY(), params.getR(),
                        LocalDateTime.now(), executionTime / 1000000.0
                );

                output.println("json сформировался");

                // Формируем HTTP ответ
                String response = String.format(
                        HTTP_RESPONSE,
                        json.getBytes(StandardCharsets.UTF_8).length,
                        json
                );
                output.println(response);

                // Отправляем ответ
                System.out.print(response);

            } catch (ValidationException e) {
                // Обработка ошибок валидации
                String errorJson = String.format(
                        "{\"success\":false,\"message\":\"%s\"}",
                        e.getMessage()
                );

                String errorResponse = String.format(
                        HTTP_ERROR,
                        errorJson.getBytes(StandardCharsets.UTF_8).length,
                        errorJson
                );

                System.out.print(errorResponse);
            } catch (Exception e) {
                // Обработка других ошибок
                String errorJson = "{\"success\":false,\"message\":\"Internal server error\"}";
                String errorResponse = String.format(
                        HTTP_ERROR,
                        errorJson.getBytes(StandardCharsets.UTF_8).length,
                        errorJson
                );

                System.out.print(errorResponse);
            }
        }
    }
}
