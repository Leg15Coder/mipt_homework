<html lang="ru">
<head>
  <meta charset="UTF-8">
  <title>Главная страница</title>
  <link rel="stylesheet"
        href="https://cdn.jsdelivr.net/gh/yegor256/tacit@gh-pages/tacit-css-1.6.0.min.css"/>
</head>

<body>

<h1>Посты</h1>
<table>
  <tr>
    <th>Название</th>
    <th>Теги</th>
    <th>Количество комментариев</th>
  </tr>
    <#list posts as post>
      <tr>
        <td>${post.header}</td>
        <td>${post.tags}</td>
        <td>${post.comments_count}</td>
      </tr>
    </#list>
</table>

</body>

</html>