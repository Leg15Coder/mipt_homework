package blog.posts.articles;

import blog.posts.exceptions.ArticleIdDublicationException;
import blog.posts.exceptions.ArticleNotFoundException;

import java.util.List;

public interface ArticlesRepository {
  ArticleId generateId();

  List<Article> getAll();

  Article findById(ArticleId id) throws ArticleNotFoundException;

  void add(Article article) throws ArticleIdDublicationException;

  void update(Article article) throws ArticleNotFoundException;

  void delete(ArticleId article) throws ArticleNotFoundException;
}
