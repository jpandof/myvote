<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>MyVote</title>
</head>
<style>

    body {
        background: linear-gradient(
                45deg
                , #02001F, #1F1B4E);

        margin: 0;
        min-height: 100vh;
        align-items: center;
        justify-content: center;
        font-family: Helvetica, Sans-serif;
    }
    ul {
        margin: 0;
        padding: 0;
    }
    a {
        text-decoration: none;
        color: #FFF;
    }

    li {
        list-style: none;
    }

    li > a {
        width: auto;
        height: 300px;
        background-image: linear-gradient(90deg, #00C0FF 0%, #ff0000 49%, #6a0000 80%, #00C0FF 100%);
        border-radius: 5px;
        display: flex;
        align-items: center;
        justify-content: center;
        text-transform: uppercase;
        font-size: 60px;
        font-weight: bold;
    }
    ul.special {
        margin: 50px 0 0 0;
    }
    ul.special > li > a {
        width: auto;
        height: 200px;
        font-size: 60px;
        background: cadetblue;
    }
    li > a:hover {
        animation: slidebg 2s linear infinite;
    }
    div {
        font-size: 60px;
        color: antiquewhite;
    }
    .winner{
        font-size: 100px;
        color: gold;
    }

    @keyframes slidebg {
        to {
            background-position: 20vw;
        }
    }
</style>
<body>

<ul>
    <li>
        <a href="/vote/YES">YES</a>
    </li>
    <li>
        <a href="/vote/NO">NO</a>
    </li>
</ul>

<ul class="special">
    <li>
        <a href="/result">RESULTS</a>
    </li>
    <li>
        <a href="/clear">RESET</a>
    </li>
</ul>

<div>${message}</div>
<div>Total votos: ${totalVotes}</div>
<div>Resultado:
    <div>${results}</div>
</div>
<div class="winner">Winner: <p class="">${winner}</p></div>

</body>
</html>