<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>MyVote</title>
    <link rel="stylesheet" href="/style.css">
    <script src="/jquery-3.6.0.slim.min.js"></script>
</head>
<body>
<ul class="special">
    <li>
        <a href="/admin/clear">RESET</a>
    </li>
    <li>
        <form action="/admin/change" method="get">
            <input type="number" class="participantes" name="totalParticipantes" value="${totalParticipantes}"/>
            <input type="submit" class="participantes" title="Cambiar"></input>
        </form>
    </li>

</ul>

<ul>
    <li>
        <a class="admin yes" href="/admin/vote/YES">YES</a>
    </li>
    <li>
        <a class="admin no" href="/admin/vote/NO">NO</a>
    </li>
</ul>

<div class="progress" data-label="${totalVotes}/${totalParticipantes}">
    <span class="value"></span>
</div>


<c:if test="${message != null}">
    <div class="msg">${message}</div>
</c:if>
<c:if test="${totalVotes != null}">
    <div>Total votos: ${totalVotes}</div>
</c:if>
<c:if test="${results != null}">
    <div>Resultados
        <div class="results">${results}</div>
    </div>
</c:if>
<div class="winner"><p>${winner}</p></div>

</body>
</html>