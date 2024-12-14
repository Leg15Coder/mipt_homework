package blog;

import blog.posts.PostController;
import blog.posts.PostService;
import blog.posts.articles.PostgresArticlesRepository;
import blog.posts.comments.PostgresCommentsRepository;
import blog.posts.responses.ArticleCreateResponse;
import blog.posts.responses.ArticleGetResponse;
import blog.posts.responses.CommentCreateResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.flywaydb.core.Flyway;
import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import spark.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashSet;
import java.util.List;

import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Testcontainers
class ApplicationTest {
  @Container
  public static final PostgreSQLContainer<?> POSTGRES = new PostgreSQLContainer<>("postgres:13");

  static {
    POSTGRES.start();
  }

  private static Jdbi jdbi;
  private Service service;

  @BeforeAll
  static void beforeAll() {
    String postgresJdbcUrl = POSTGRES.getJdbcUrl();
    Flyway flyway =
        Flyway.configure()
            .outOfOrder(true)
            .locations("classpath:db/migrations")
            .dataSource(postgresJdbcUrl, POSTGRES.getUsername(), POSTGRES.getPassword())
            .load();
    flyway.migrate();
    jdbi = Jdbi.create(postgresJdbcUrl, POSTGRES.getUsername(), POSTGRES.getPassword());

  }

    @BeforeEach
  void beforeEach() {
      jdbi.inTransaction((Handle bothHandle) -> {
            jdbi.useTransaction(handle -> handle.createUpdate("DELETE FROM articles").execute());
            jdbi.useTransaction(handle -> handle.createUpdate("DELETE FROM comment").execute());
            return null;
          }
      );
    service = Service.ignite();
  }

  @AfterEach
  void afterEach() {
    service.stop();
    service.awaitStop();
  }

  @Test
  void TestThrows() throws Exception {
    ObjectMapper objectMapper = new ObjectMapper();
    Application application = new Application(
        List.of(
            new PostController(
                service,
                new PostService(
                    new PostgresArticlesRepository(jdbi),
                    new PostgresCommentsRepository(jdbi)
                ),
                objectMapper
            )
        )
    );

    application.start();
    service.awaitInitialization();

    HttpResponse<String> response;

    response = HttpClient.newHttpClient()
        .send(
            HttpRequest.newBuilder()
                .GET()
                .uri(URI.create("http://localhost:%d/api/articles/13".formatted(service.port())))
                .build(),
            HttpResponse.BodyHandlers.ofString(UTF_8)
        );
    assertEquals(404, response.statusCode());

    response = HttpClient.newHttpClient()
        .send(
            HttpRequest.newBuilder()
                .GET()
                .uri(URI.create("http://localhost:%d/api/comments/13".formatted(service.port())))
                .build(),
            HttpResponse.BodyHandlers.ofString(UTF_8)
        );
    assertEquals(404, response.statusCode());

    response = HttpClient.newHttpClient()
        .send(
            HttpRequest.newBuilder()
                .DELETE()
                .uri(URI.create("http://localhost:%d/api/comments/13".formatted(service.port())))
                .build(),
            HttpResponse.BodyHandlers.ofString(UTF_8)
        );
    assertEquals(404, response.statusCode());

    response = HttpClient.newHttpClient()
        .send(
            HttpRequest.newBuilder()
                .DELETE()
                .uri(URI.create("http://localhost:%d/api/articles/13".formatted(service.port())))
                .build(),
            HttpResponse.BodyHandlers.ofString(UTF_8)
        );
    assertEquals(404, response.statusCode());

    response = HttpClient.newHttpClient()
        .send(
            HttpRequest.newBuilder()
                .PUT(
                    HttpRequest.BodyPublishers.ofString(
                    """
                        { "header": "updated article", "tags": ["newest", "the best"] }
                        """
                ))
                .uri(URI.create("http://localhost:%d/api/articles/13".formatted(service.port())))
                .build(),
            HttpResponse.BodyHandlers.ofString(UTF_8)
        );
    assertEquals(404, response.statusCode());

    response = HttpClient.newHttpClient()
        .send(
            HttpRequest.newBuilder()
                .POST(
                    HttpRequest.BodyPublishers.ofString(
                        """
                            { "header": "test", "tags": ["test"] }
                            """
                    ))
                .uri(URI.create("http://localhost:%d/api/articles".formatted(service.port())))
                .build(),
            HttpResponse.BodyHandlers.ofString(UTF_8)
        );
    assertEquals(201, response.statusCode());

    ArticleCreateResponse articleCreateResponse = objectMapper.readValue(
        response.body(),
        ArticleCreateResponse.class
    );
    long newArticleId = articleCreateResponse.id();

    response = HttpClient.newHttpClient()
        .send(
            HttpRequest.newBuilder()
                .POST(
                    HttpRequest.BodyPublishers.ofString(
                        """
                        { "article": 13, "text": "Hi, how are u?" }
                        """
                    ))
                .uri(URI.create("http://localhost:%d/api/comments".formatted(service.port())))
                .build(),
            HttpResponse.BodyHandlers.ofString(UTF_8)
        );
    assertEquals(400, response.statusCode());

    response = HttpClient.newHttpClient()
        .send(
            HttpRequest.newBuilder()
                .POST(
                    HttpRequest.BodyPublishers.ofString(
                        """
                        { "article": %d, "text": "Hi, how are u?" }
                        """.formatted(newArticleId)
                    ))
                .uri(URI.create("http://localhost:%d/api/comments".formatted(service.port())))
                .build(),
            HttpResponse.BodyHandlers.ofString(UTF_8)
        );
    assertEquals(201, response.statusCode());

    response = HttpClient.newHttpClient()
        .send(
            HttpRequest.newBuilder()
                .DELETE()
                .uri(URI.create("http://localhost:%d/api/articles/%d".formatted(service.port(), newArticleId)))
                .build(),
            HttpResponse.BodyHandlers.ofString(UTF_8)
        );
    assertEquals(200, response.statusCode());
  }

  @Test
  void generale2eTest() throws Exception {
    var rep = new PostgresCommentsRepository(jdbi);

    ObjectMapper objectMapper = new ObjectMapper();
    Application application = new Application(
        List.of(
            new PostController(
                service,
                new PostService(
                    new PostgresArticlesRepository(jdbi),
                    rep
                ),
                objectMapper
            )
        )
    );

    application.start();
    service.awaitInitialization();

    HttpResponse<String> response;

    response = HttpClient.newHttpClient()
        .send(
            HttpRequest.newBuilder()
                .POST(
                    HttpRequest.BodyPublishers.ofString(
                        """
                            { "header": "first article", "tags": ["new", "best", "first"] }
                            """
                    )
                )
                .uri(URI.create("http://localhost:%d/api/articles".formatted(service.port())))
                .build(),
            HttpResponse.BodyHandlers.ofString(UTF_8)
        );
    assertEquals(201, response.statusCode());

    ArticleCreateResponse articleCreateResponse = objectMapper.readValue(
        response.body(),
        ArticleCreateResponse.class
    );
    long newArticleId = articleCreateResponse.id();

    response = HttpClient.newHttpClient()
        .send(
            HttpRequest.newBuilder()
                .POST(
                    HttpRequest.BodyPublishers.ofString(
                        String.format(
                            """
                            { "article": %d, "text": "Hi, how are u?" }
                            """,
                            newArticleId
                        )
                    )
                )
                .uri(URI.create("http://localhost:%d/api/comments".formatted(service.port())))
                .build(),
            HttpResponse.BodyHandlers.ofString(UTF_8)
        );
    assertEquals(201, response.statusCode());

    CommentCreateResponse commentCreateResponse = objectMapper.readValue(
        response.body(),
        CommentCreateResponse.class
    );
    long newCommentId = commentCreateResponse.id();

    response = HttpClient.newHttpClient()
        .send(
            HttpRequest.newBuilder()
                .PUT(
                    HttpRequest.BodyPublishers.ofString(
                        """
                            { "header": "updated article", "tags": ["newest", "the best"] }
                            """
                    )
                )
                .uri(URI.create("http://localhost:%d/api/articles/%d".formatted(service.port(), newArticleId)))
                .build(),
            HttpResponse.BodyHandlers.ofString(UTF_8)
        );
    assertEquals(204, response.statusCode());

    response = HttpClient.newHttpClient()
        .send(
            HttpRequest.newBuilder()
                .DELETE()
                .uri(URI.create("http://localhost:%d/api/comments/%d".formatted(service.port(), newCommentId)))
                .build(),
            HttpResponse.BodyHandlers.ofString(UTF_8)
        );
    assertEquals(200, response.statusCode());

    response = HttpClient.newHttpClient()
        .send(
            HttpRequest.newBuilder()
                .GET()
                .uri(URI.create("http://localhost:%d/api/articles".formatted(service.port())))
                .build(),
            HttpResponse.BodyHandlers.ofString(UTF_8)
        );
    assertEquals(200, response.statusCode());

    response = HttpClient.newHttpClient()
        .send(
            HttpRequest.newBuilder()
                .GET()
                .uri(URI.create("http://localhost:%d/api/articles/%d".formatted(service.port(), newArticleId)))
                .build(),
            HttpResponse.BodyHandlers.ofString(UTF_8)
        );
    assertEquals(200, response.statusCode());

    ArticleGetResponse articleGetResponse = objectMapper.readValue(
        response.body(),
        ArticleGetResponse.class
    );
    assertEquals("updated article", articleGetResponse.header());
    assertEquals(new HashSet<>(List.of("newest", "the best")), articleGetResponse.tags());
    assertEquals(0, articleGetResponse.comments().size());

    response = HttpClient.newHttpClient()
        .send(
            HttpRequest.newBuilder()
                .GET()
                .uri(URI.create("http://localhost:%d/api/comments".formatted(service.port())))
                .build(),
            HttpResponse.BodyHandlers.ofString(UTF_8)
        );
    assertEquals(200, response.statusCode());

    response = HttpClient.newHttpClient()
        .send(
            HttpRequest.newBuilder()
                .POST(
                    HttpRequest.BodyPublishers.ofString(
                        """
                            [
                              {
                                "header": "Article 1 Title",
                                "tags": ["tag1", "tag2", "tag3"]
                              },
                              {
                                "header": "Article 2 Title",
                                "tags": ["tagA", "tagB"]
                              },
                              {
                                "header": "Article 3 Title",
                                "tags": ["tagX", "tagY", "tagZ"]
                              }
                            ]
                            """
                    )
                )
                .uri(URI.create("http://localhost:%d/api/articles/many".formatted(service.port())))
                .build(),
            HttpResponse.BodyHandlers.ofString(UTF_8)
        );
    assertEquals(200, response.statusCode());
  }
}