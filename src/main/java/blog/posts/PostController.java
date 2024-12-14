package blog.posts;

import blog.posts.articles.Article;
import blog.posts.comments.Comment;
import blog.posts.exceptions.ArticleException;
import blog.posts.exceptions.CommentException;
import blog.posts.requests.*;
import blog.posts.responses.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import blog.Controller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Service;
import spark.Request;
import spark.Response;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PostController implements Controller {
  private static final Logger LOG = LoggerFactory.getLogger(PostController.class);

  private final Service service;
  private final PostService postService;
  private final ObjectMapper objectMapper;

  public PostController(Service service, PostService postService, ObjectMapper objectMapper) {
    this.service = service;
    this.postService = postService;
    this.objectMapper = objectMapper;
  }

  @Override
  public void initializeEndpoints() {
    getAllArticles();
    getArticleByIdWithComments();
    createArticles();
    updateArticleById();
    deleteArticleById();
    createArticle();
    getAllComments();
    createComment();
    deleteComment();
    LOG.info("Проинициализирован PostController");
  }

  private void getAllArticles() {
    service.get(
        "/api/articles",
        (Request request, Response response) -> {
          response.type("application/json");
          try {
            List<Article> articles = postService.getAllArticles();

            response.status(200);
            LOG.debug("Выведены все статьи");
            return objectMapper.writeValueAsString(new ArticleGetListResponse(articles));
          } catch (Exception e) {
            LOG.error("Ошибка обработки (get) /api/articles : {}", e.toString());
            response.status(500);
            return objectMapper.writeValueAsString(new ErrorResponse(e.toString()));
          }
        }
    );
  }

  private void getArticleByIdWithComments() {
    service.get(
        "/api/articles/:id",
        (Request request, Response response) -> {
          response.type("application/json");
          try {
            long id = Long.parseLong(request.params("id"));

            Article article = postService.findArticleById(id);
            List<Comment> comments = article.getComments();

            response.status(200);

            LOG.debug("successfully get article with id={}", id);
            return objectMapper.writeValueAsString(new ArticleGetResponse(
                article.getHeader(),
                article.getTags(),
                comments
            ));
          } catch (ArticleException e) {
            LOG.warn("Ошибка {} обработки (get) /api/articles/:id : {}", e.getClass(), e.toString());
            response.status(404);
            return objectMapper.writeValueAsString(new ErrorResponse(e.toString()));
          } catch (Exception e) {
            LOG.error("Ошибка обработки (get) /api/articles/:id : {}", e.toString());
            response.status(500);
            return objectMapper.writeValueAsString(new ErrorResponse(e.toString()));
          }
        }
    );
  }

  private void updateArticleById() {
    service.put(
        "/api/articles/:id",
        (Request request, Response response) -> {
          response.type("application/json");
          try {
            long id = Long.parseLong(request.params("id"));

            ArticleUpdateRequest updateRequest = objectMapper.readValue(
                request.body(),
                ArticleUpdateRequest.class
            );

            postService.updateArticle(
                id,
                updateRequest.header(),
                updateRequest.tags(),
                updateRequest.comments()
            );

            response.status(204);

            LOG.debug("successfully update article with id={}", id);
            return objectMapper.writeValueAsString(new ArticleUpdateResponse());
          } catch (ArticleException e) {
            LOG.warn("Ошибка {} обработки (put) /api/articles/:id : {}", e.getClass(), e.toString());
            response.status(404);
            return objectMapper.writeValueAsString(new ErrorResponse(e.toString()));
          } catch (Exception e) {
            LOG.error("Ошибка обработки (put) /api/articles/:id : {}", e.toString());
            response.status(500);
            return objectMapper.writeValueAsString(new ErrorResponse(e.toString()));
          }
        }
    );
  }

  private void deleteArticleById() {
    service.delete(
        "/api/articles/:id",
        (Request request, Response response) -> {
          response.type("application/json");
          try {
            long id = Long.parseLong(request.params("id"));

            postService.deleteArticle(id);

            response.status(200);

            LOG.debug("Successfully deleted article with id={}", id);
            return objectMapper.writeValueAsString(new ArticleDeleteResponse(
                id,
                "SUCCESS DELETE"
            ));
          } catch (ArticleException e) {
            LOG.warn("Ошибка {} обработки (delete) /api/articles/:id : {}", e.getClass(), e.toString());
            response.status(404);
            return objectMapper.writeValueAsString(new ErrorResponse(e.toString()));
          } catch (Exception e) {
            LOG.error("Ошибка обработки (delete) /api/articles/:id : {}", e.toString());
            response.status(500);
            return objectMapper.writeValueAsString(new ErrorResponse(e.toString()));
          }
        }
    );
  }

  private void createArticle() {
    service.post(
        "/api/articles",
        (Request request, Response response) -> {
          response.type("application/json");
          try {
            ArticleCreateRequest createRequest = objectMapper.readValue(
                request.body(),
                ArticleCreateRequest.class
            );

            long newArticleId = postService.createArticle(
                createRequest.header(),
                createRequest.tags()
            );

            response.status(201);

            LOG.debug("Создан новый пост с ID={}", newArticleId);
            return objectMapper.writeValueAsString(new ArticleCreateResponse(
                newArticleId,
                "SUCCESSFULLY CREATED"
            ));
          } catch (ArticleException e) {
            LOG.warn("Ошибка {} обработки (post) /api/articles/ : {}", e.getClass(), e.toString());
            response.status(409);
            return objectMapper.writeValueAsString(new ErrorResponse(e.toString()));
          } catch (Exception e) {
            LOG.error("Ошибка обработки (post) /api/articles/ : {}", e.toString());
            response.status(500);
            return objectMapper.writeValueAsString(new ErrorResponse(e.toString()));
          }
        }
    );
  }

  private void createArticles() {
    service.post(
        "/api/articles/many",
        (Request request, Response response) -> {
          response.type("application/json");
          try {
            List<ArticleCreateRequest> createRequests = objectMapper.readValue(
                request.body(),
                objectMapper.getTypeFactory().constructCollectionType(List.class, ArticleCreateRequest.class)
            );

            List<String> responses = new ArrayList<>();

            for (ArticleCreateRequest subRequest : createRequests) {
              String subResponse;

              try {
                long newArticleId = postService.createArticle(
                    subRequest.header(),
                    subRequest.tags()
                );

                LOG.debug("Создан новый (частичный) пост с ID={}", newArticleId);
                subResponse = objectMapper.writeValueAsString(new ArticleCreateResponse(
                    newArticleId,
                    "SUCCESSFULLY CREATED"
                ));
              } catch (ArticleException e) {
                LOG.warn("Ошибка {} обработки элемента запроса (post) /api/articles/many : {}", e.getClass(), e.toString());
                subResponse = objectMapper.writeValueAsString(new ErrorResponse(e.toString()));
              } catch (Exception e) {
                LOG.error("Ошибка обработки элемента запроса (post) /api/articles/many : {}", e.toString());
                subResponse = objectMapper.writeValueAsString(new ErrorResponse(e.toString()));
              }

              responses.add(subResponse);
            }

            response.status(200);
            LOG.debug("Created new articles with IDs={}", responses);
            return objectMapper.writeValueAsString(new ManyArticlesCreateResponse(
                responses,
                "SUCCESSFULLY CREATED"
            ));
          } catch (Exception e) {
            LOG.error("Ошибка обработки (post) /api/articles/many : {}", e.toString());
            response.status(500);
            return objectMapper.writeValueAsString(new ErrorResponse(e.toString()));
          }
        }
    );
  }

  private void getAllComments() {
    service.get(
        "/api/comments",
        (Request request, Response response) -> {
          response.type("application/json");
          try {
            List<Comment> comments;
            if (!request.body().isEmpty()) {
              CommentGetRequest getRequest = objectMapper.readValue(
                  request.body(),
                  CommentGetRequest.class
              );

              if (getRequest.article() == null) {
                comments = postService.getAllComments();
              } else {
                comments = postService.getCommentsByArticleId(getRequest.article());
              }
            } else {
              comments = postService.getAllComments();
            }

            response.status(200);
            LOG.debug("Выведены комментарии по запросу");
            return objectMapper.writeValueAsString(new CommentsGetListResponse(comments));
          } catch (Exception e) {
            LOG.error("Ошибка обработки (get) /api/comments : {}", e.toString());
            response.status(500);
            return objectMapper.writeValueAsString(new ErrorResponse(e.toString()));
          }
        }
    );
  }

  private void createComment() {
    service.post(
        "/api/comments",
        (Request request, Response response) -> {
          response.type("application/json");
          try {
            CommentCreateRequest createRequest = objectMapper.readValue(
                request.body(),
                CommentCreateRequest.class
            );

            long newCommentId = postService.createComment(
                createRequest.article(),
                createRequest.text()
            );

            response.status(201);

            LOG.debug("create new comment with id={}, for article with id={}", newCommentId, createRequest.article());
            return objectMapper.writeValueAsString(new CommentCreateResponse(
                newCommentId,
                "SUCCESSFULLY CREATED"
            ));
          } catch (CommentException e) {
            LOG.warn("Ошибка {} обработки (post) /api/comments/ : {}", e.getClass(), e.toString());
            response.status(400);
            return objectMapper.writeValueAsString(new ErrorResponse(e.toString()));
          } catch (Exception e) {
            LOG.error("Ошибка обработки (post) /api/comments/ : {}", e.toString());
            response.status(500);
            return objectMapper.writeValueAsString(new ErrorResponse(e.toString()));
          }
        }
    );
  }

  private void deleteComment() {
    service.delete(
        "/api/comments/:id",
        (Request request, Response response) -> {
          response.type("application/json");
          try {
            long id = Long.parseLong(request.params("id"));

            postService.deleteComment(id);

            response.status(200);

            LOG.debug("deleted comment with id={}", id);
            return objectMapper.writeValueAsString(new CommentDeleteResponse(
                id,
                "SUCCESS DELETE"
            ));
          } catch (CommentException e) {
            LOG.warn("Ошибка {} обработки (delete) /api/comments/:id : {}", e.getClass(), e.toString());
            response.status(404);
            return objectMapper.writeValueAsString(new ErrorResponse(e.toString()));
          } catch (Exception e) {
            LOG.error("Ошибка обработки (delete) /api/comments/:id : {}", e.toString());
            response.status(500);
            return objectMapper.writeValueAsString(new ErrorResponse(e.toString()));
          }
        }
    );
  }
}
