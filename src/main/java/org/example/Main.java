package org.example;

import blog.Application;
import blog.TemplateFactory;
import blog.posts.PostController;
import blog.posts.PostFreemarkerController;
import blog.posts.PostService;
import blog.posts.articles.InMemoryArticlesRepository;
import blog.posts.comments.InMemoryCommentsRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import spark.Service;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        Service service = Service.ignite();
        ObjectMapper objectMapper = new ObjectMapper();
      final var postService = new PostService(
          new InMemoryArticlesRepository(),
          new InMemoryCommentsRepository()
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
