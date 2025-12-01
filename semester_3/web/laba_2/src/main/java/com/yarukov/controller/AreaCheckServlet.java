package com.yarukov.controller;

import com.yarukov.model.Result;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


@WebServlet("/area-check")
public class AreaCheckServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    private void processRequest(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String xstr = req.getParameter("x");
        String ystr = req.getParameter("y");
        String rstr = req.getParameter("r");
        String canvasXstr = req.getParameter("canvasX");
        String canvasYstr = req.getParameter("canvasY");

        try {
            double x = Double.parseDouble(xstr);
            double y = Double.parseDouble(ystr);
            double r = Double.parseDouble(rstr);

            String curTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
            double startTime = System.nanoTime();
            boolean popalIliNet = chechHit(x, y, r);
            double endTime = System.nanoTime();
            String workTime = String.valueOf((endTime - startTime)/1000) + " мс";

            Result result = new Result(x, y, r, curTime, workTime, popalIliNet);


            if (canvasXstr != null && !canvasXstr.isEmpty() &&
                    canvasYstr != null && !canvasYstr.isEmpty()) {
                result.setCanvasX(Double.parseDouble(canvasXstr));
                result.setCanvasY(Double.parseDouble(canvasYstr));
            }

            ServletContext context = getServletContext();
            List<Result> results = (List<Result>) context.getAttribute("results");
            if (results == null) {
                results = new ArrayList<>();
                context.setAttribute("results", results);
            }
            results.add(result);

            req.setAttribute("lastResult", result);
            req.getRequestDispatcher("/result.jsp").forward(req, res);

        } catch (NumberFormatException e) {
            req.setAttribute("error", "Что то не то с введёными данными(((");
            req.getRequestDispatcher("/index.jsp").forward(req, res);
        }
    }

    private boolean chechHit(double x, double y, double r) {
        boolean popalIliNet = false;
        if (x <= 0 && y >= 0 && x >= -r && y <= r) {
            popalIliNet = true;
        }
        if (x >= 0 && y >= 0 && (x * x + y * y) <= r * r) {
            popalIliNet = true;
        }
        if (x >= 0 && y <= 0 && x <= r / 2 && y >= 2 * x - r) {
            popalIliNet = true;
        }

        return popalIliNet;
    }


}
