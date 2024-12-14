<!DOCTYPE html>
<html lang="ru">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Главная страница</title>
  <link rel="stylesheet"
        href="https://cdn.jsdelivr.net/gh/yegor256/tacit@gh-pages/tacit-css-1.6.0.min.css"/>
  <style>
    body {
      font-family: 'Orbitron', sans-serif;
      margin: 0;
      padding: 0;
      background: linear-gradient(120deg, #0f2027, #203a43, #2c5364);
      color: #e0e0e0;
      overflow-x: hidden;
    }

    h1 {
      text-align: center;
      font-size: 3rem;
      color: #00e5ff;
      margin-top: 20px;
      margin-bottom: 30px;
      text-shadow: 0 0 10px #00e5ff, 0 0 20px #00e5ff;
      animation: glow 2s infinite;
    }

    table {
      margin: 0 auto;
      border-collapse: collapse;
      width: 80%;
      box-shadow: 0 4px 15px rgba(0, 229, 255, 0.5);
      background: rgba(15, 32, 39, 0.9);
      border-radius: 8px;
      overflow: hidden;
    }

    th, td {
      padding: 15px;
      text-align: left;
      border-bottom: 1px solid rgba(224, 224, 224, 0.2);
      color: #e0e0e0;
    }

    th {
      background-color: rgba(0, 229, 255, 0.3);
      color: #00e5ff;
      font-size: 1.2rem;
      text-transform: uppercase;
    }

    tr:hover {
      background-color: rgba(32, 58, 67, 0.7);
    }

    a {
      color: #00e5ff;
      text-decoration: none;
      font-weight: bold;
    }

    a:hover {
      text-decoration: underline;
    }

    @keyframes glow {
      0% {
        text-shadow: 0 0 5px #00e5ff, 0 0 10px #00e5ff;
      }
      50% {
        text-shadow: 0 0 15px #00e5ff, 0 0 30px #00e5ff;
      }
      100% {
        text-shadow: 0 0 5px #00e5ff, 0 0 10px #00e5ff;
      }
    }
  </style>
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
      <td><a href="/article/${post.id}">${post.header}</a></td>
      <td>${post.tags}</td>
      <td>${post.comments_count}</td>
    </tr>
  </#list>
</table>

</body>
</html>
