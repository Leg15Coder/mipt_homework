<html lang="ru">
<head>
  <meta charset="UTF-8">
  <title>Главная страница</title>
  <link rel="stylesheet"
        href="https://cdn.jsdelivr.net/gh/yegor256/tacit@gh-pages/tacit-css-1.6.0.min.css"/>
</head>

<body>

<h1>${article.header}</h1>
<p></p>
<table>
  <tr>
    <th>Комментарии</th>
  </tr>
    <#list comments as comment>
      <tr>
        <td>${comment.text}</td>
      </tr>
    </#list>
</table>

</body>

</html>