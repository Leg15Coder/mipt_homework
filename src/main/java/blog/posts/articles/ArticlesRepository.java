package blog.posts.articles;

import blog.posts.exceptions.*;

import java.util.List;

public interface ArticlesRepository {
  ArticleId generateId();

  List<Article> getAll();

  Article findById(ArticleId id) throws ArticleNotFoundException;

  void add(Article article) throws ArticleIdDublicationException, ArticleHeaderExceedHeaderException, ArticleTagsCountExceedHeaderException, ArticleTagLengthExceedHeaderException;

  void update(Article article) throws ArticleNotFoundException, ArticleTagsCountExceedHeaderException, ArticleTagLengthExceedHeaderException, ArticleHeaderExceedHeaderException;

  void delete(ArticleId article) throws ArticleNotFoundException;
}
