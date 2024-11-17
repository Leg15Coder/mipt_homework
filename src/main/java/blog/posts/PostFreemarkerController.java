package blog.posts;

import blog.Controller;
import blog.posts.articles.Article;
import blog.posts.comments.Comment;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Service;
import spark.template.freemarker.FreeMarkerEngine;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PostFreemarkerController implements Controller {
  private final Service service;
  private final PostService postService;
  private final FreeMarkerEngine freeMarkerEngine;

  public PostFreemarkerController(
      Service service,
      PostService postService,
      FreeMarkerEngine freeMarkerEngine
  ) {
    this.service = service;
    this.postService = postService;
    this.freeMarkerEngine = freeMarkerEngine;
  }

  @Override
  public void initializeEndpoints() {
    getAllPosts();
    getPost();
  }

  private void getAllPosts() {
    service.get(
        "/",
        (Request request, Response response) -> {
          response.type("text/html; charset=utf-8");
          List<Article> posts = postService.getAll();
          List<Map<String, String>> postMapList =
              posts.stream()
                  .map(article -> Map.of(
                      "header", article.getHeader(),
                      "tags", article.getTags().toString(),
                      "comments_count", article.getComments().size() + "",
                      "id", article.getId().getId().toString()))
                  .toList();

          Map<String, Object> model = new HashMap<>();
          model.put("posts", postMapList);
          return freeMarkerEngine.render(new ModelAndView(model, "index.ftl"));
        }
    );
  }

  private void getPost() {
    service.get(
        "/article/:id",
        (Request request, Response response) -> {
          response.type("text/html; charset=utf-8");

          long articleId = Long.parseLong(request.params("id"));
          Article currentArticle = postService.findArticleById(articleId);

          List<Comment> comments = currentArticle.getComments();
          List<Map<String, String>> commentsMapList =
              comments.stream()
                  .map(comment -> Map.of("text", comment.getText()))
                  .toList();

          Map<String, String> articleMap = Map.of(
              "header", currentArticle.getHeader(),
              "tags", currentArticle.getTags().toString()
          );

          Map<String, Object> model = new HashMap<>();
          model.put("comments", commentsMapList);
          model.put("article", articleMap);

          return freeMarkerEngine.render(new ModelAndView(model, "article.ftl"));
        }
    );
  }
}
