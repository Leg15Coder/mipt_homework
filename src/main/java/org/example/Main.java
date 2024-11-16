package org.example;

import blog.Application;
import blog.posts.PostController;
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
        Application application = new Application(
            List.of(
                new PostController(
                    service,
                    new PostService(
                        new InMemoryArticlesRepository(),
                        new InMemoryCommentsRepository()
                    ),
                    objectMapper
                )
            )
        );
        application.start();
    }
}
