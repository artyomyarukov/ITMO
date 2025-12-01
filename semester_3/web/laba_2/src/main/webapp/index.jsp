<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.yarukov.model.Result" %>
<%@ page import="java.util.List" %>
<html>
<head>
    <title>Вторая лаба </title>
    <link rel="stylesheet" href="css/style.css">
    <script src="js/validation.js"></script>
</head>
<body>
<div class="container">
    <h1>Лабораторная работа №2</h1>
    <p><strong>Студент:</strong> Яруков Артём Дмитриевич</p>
    <p><strong>Группа:</strong> P3212 </p>
    <p><strong>Вариант:</strong> 470506 </p>

    <% if (request.getAttribute("error") != null) { %>
    <div class="error">
        <%= request.getAttribute("error") %>
    </div>
    <% } %>

    <div class="form-section">
        <h2>Введите координаты точки и радиус:</h2>
        <form id="pointForm" action="controller" method="GET">
            <div class="form-group">
                <label>Координата X:</label>
                <input type="text" name="x" placeholder="От -5 до 5" required>
                <small>Число от -5 до 5</small>
            </div>

            <div class="form-group">
                <label>Координата Y:</label>
                <input type="text" name="y" placeholder="От -3 до 5" required>
                <small>Число от -3 до 5</small>
            </div>

            <div class="form-group">
                <label>Радиус R:</label>
                <div class="radius-buttons">
                    <input type="radio" name="r" value="1" id="r1" required>
                    <label for="r1" class="radius-btn">1</label>

                    <input type="radio" name="r" value="1.5" id="r1.5">
                    <label for="r1.5" class="radius-btn">1.5</label>

                    <input type="radio" name="r" value="2" id="r2">
                    <label for="r2" class="radius-btn">2</label>

                    <input type="radio" name="r" value="2.5" id="r2.5">
                    <label for="r2.5" class="radius-btn">2.5</label>

                    <input type="radio" name="r" value="3" id="r3">
                    <label for="r3" class="radius-btn">3</label>
                </div>
            </div>


            <input type="hidden" name="canvasX" id="canvasX">
            <input type="hidden" name="canvasY" id="canvasY">

            <button type="submit" onclick="prepareFormSubmission()">Проверить попадание</button>
        </form>


        </form>
    </div>

    <div class="graph-section">
        <h2>Область попадания</h2>
        <canvas id="graphCanvas" width="400" height="400"></canvas>
        <p class="canvas-hint">Выберите радиус R и кликните на график для проверки точки</p>
    </div>

    <div class="results-section">
        <h2>История проверок</h2>
        <div style="margin-bottom: 15px;">
            <button onclick="clearResults()" class="clear-button">Очистить таблицу</button>
        </div>

        <%
            ServletContext context = request.getServletContext();
            List<Result> results = (List<Result>) context.getAttribute("results");
            if (results != null && !results.isEmpty()) {
        %>
        <input type="hidden" id="resultsData" value='<%=
        results.stream()
            .map(r -> "{\"canvasX\":" + (r.getCanvasX() != null ? r.getCanvasX() : "null") +
                     ",\"canvasY\":" + (r.getCanvasY() != null ? r.getCanvasY() : "null") +
                     ",\"r\":" + r.getR() +
                     ",\"hit\":" + r.isPopalIliNet() + "}")
            .collect(java.util.stream.Collectors.joining(",", "[", "]"))
    %>'>
        <table>
            <thead>
            <tr>
                <th>X</th>
                <th>Y</th>
                <th>R</th>
                <th>Результат</th>
                <th>Время</th>
                <th>Время выполнения</th>
            </tr>
            </thead>
            <tbody>
            <% for (Result result : results) { %>
            <tr>
                <td><%= result.getX() %>
                </td>
                <td><%= result.getY() %>
                </td>
                <td><%= result.getR() %>
                </td>
                <td class="<%= result.isPopalIliNet() ? "hit" : "miss" %>">
                    <%= result.isPopalIliNet() ? "ПОПАЛ" : "НЕ ПОПАЛ" %>
                </td>
                <td><%= result.getCurTime() %>
                </td>
                <td><%= result.getWorkTime() %>
                </td>
            </tr>
            <% } %>
            </tbody>
        </table>
        <% } else { %>
        <p>Пока нет результатов проверок</p>
        <% } %>
    </div>
</div>
<script src="js/validation.js"></script>
<script src="js/graph.js"></script>

</body>
</html>