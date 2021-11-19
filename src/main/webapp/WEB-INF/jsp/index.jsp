<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>MyVote</title>
    <link rel="stylesheet" href="/style.css">
    <script src="/jquery-3.6.0.slim.min.js"></script>
</head>
<body>
<c:choose>
    <c:when test='${message != null}'>
        <a href="/" >
            <img src="https://assets.dryicons.com/uploads/icon/svg/2518/refresh.svg" alt="actualizar votación">
        </a>
        <script>

        </script>
    </c:when>
    <c:otherwise>
        <ul class="vote">
            <li>
                <a class="yes" href="/vote/YES">YES</a>
            </li>
            <li>
                <a class="no" href="/vote/NO">NO</a>
            </li>
        </ul>
    </c:otherwise>
</c:choose>



<div class="progress" data-label="${totalVotes}/${totalParticipantes}">
    <span class="value"></span>
</div>


<c:if test="${message != null}">
    <div class="msg">${message}</div>
</c:if>
<c:if test="${results != null}">
    <div>Resultados
        <div class="results">${results}</div>
    </div>
</c:if>
<div class="winner"><p>${winner}</p></div>


</body>
</html>