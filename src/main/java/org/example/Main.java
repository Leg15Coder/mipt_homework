package org.example;

import blog.Application;
import blog.TemplateFactory;
import blog.posts.PostController;
import blog.posts.PostFreemarkerController;
import blog.posts.PostService;
import blog.posts.articles.InMemoryArticlesRepository;
import blog.posts.articles.PostgresArticlesRepository;
import blog.posts.comments.InMemoryCommentsRepository;
import blog.posts.comments.PostgresCommentsRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.flywaydb.core.Flyway;
import org.jdbi.v3.core.Jdbi;
import spark.Service;

import java.util.List;

public class Main {

  public static void main(String[] args) {
    Config config = ConfigFactory.load();

    Flyway flyway =
        Flyway.configure()
            .outOfOrder(true)
            .locations("classpath:db/migrations")
            .dataSource(config.getString("app.database.url"), config.getString("app.database.user"),
                config.getString("app.database.password"))
            .load();
    flyway.migrate();

    Jdbi jdbi = Jdbi.create(config.getString("app.database.url"), config.getString("app.database.user"),
        config.getString("app.database.password"));


    Service service = Service.ignite().port(config.getInt("app.server.port"));
    ObjectMapper objectMapper = new ObjectMapper();
    final var postService = new PostService(
        new PostgresArticlesRepository(jdbi),
        new PostgresCommentsRepository(jdbi)
    );

    Application application = new Application(
          List.of(
              new PostController(
                  service,
                  postService,
                  objectMapper
              ),
              new PostFreemarkerController(
                  service,
                  postService,
                  TemplateFactory.freeMarkerEngine()
              )
          )
      );

    application.start();
  }
}
