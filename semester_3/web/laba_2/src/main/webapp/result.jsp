<%@ page import="com.yarukov.model.Result" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Результат проверки</title>
    <link rel="stylesheet" href="css/style.css">
    <script src="js/validation.js"></script>
    <script src="js/graph.js"></script>
</head>
<body>
<div class="container">
    <h1>Результат проверки попадания</h1>

    <%
        Result result = (Result) request.getAttribute("lastResult");
        if (result != null) {
    %>
    <div class="result <%= result.isPopalIliNet() ? "hit" : "miss" %>">
        <h2><%= result.isPopalIliNet() ? "Точка попала в область!" : "Точка не попала в область" %></h2>
        <p><strong>Координаты:</strong> X=<%= result.getX() %>, Y=<%= result.getY() %></p>
        <p><strong>Радиус:</strong> R=<%= result.getR() %></p>
        <p><strong>Время проверки:</strong> <%= result.getCurTime() %></p>
        <p><strong>Время выполнения:</strong> <%= result.getWorkTime() %></p>
    </div>
    <% } else { %>
    <p>Результат не найден</p>
    <% } %>

    <a href="controller" class="back-link">Вернуться к форме</a>
</div>
</body>
</html>