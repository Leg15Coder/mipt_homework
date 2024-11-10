package blog.articles;

import blog.exceptions.ArticleIdDublicationException;
import blog.exceptions.ArticleNotFoundException;

public interface ArticlesRepository {
  Article findById(ArticleId id) throws ArticleNotFoundException;

  void add(Article comment) throws ArticleIdDublicationException;

  void update(Article comment) throws ArticleNotFoundException;

  void delete(Article comment) throws ArticleNotFoundException;
}
