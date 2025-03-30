<%--
  Created by IntelliJ IDEA.
  User: Unfortunately_Redis
  Date: 30.03.2025
  Time: 19:02
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Currencies</title>
</head>
<body>
<h1>Currencies</h1>
<ul>
    <c:forEach items="${currencies}" var="currency">
        <li>${currency.code} - ${currency.fullName} (${currency.sign})</li>
    </c:forEach>
</ul>
</body>
</html>
