package blog.posts.articles;

import blog.posts.comments.Comment;
import blog.posts.comments.CommentId;
import blog.posts.exceptions.*;
import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;

import java.sql.Array;
import java.sql.ResultSet;
import java.util.*;

public class PostgresArticlesRepository implements ArticlesRepository {
  static final long MAX_HEADER_LENGTH = 1024L;
  static final long MAX_TAGS_COUNT = 256L;
  static final long MAX_TAG_LENGTH = 256L;

  private final Jdbi jdbi;

  public PostgresArticlesRepository(Jdbi jdbi) {
    this.jdbi = jdbi;
  }

  private void checkArticleData(Article article)
      throws ArticleTagLengthExceedHeaderException, ArticleTagsCountExceedHeaderException, ArticleHeaderExceedHeaderException {
    if (article.getHeader().length() > MAX_HEADER_LENGTH) {
      throw new ArticleHeaderExceedHeaderException("Слишком большой размер заголовка");
    }
    if (article.getTags().size() > MAX_TAGS_COUNT) {
      throw new ArticleTagsCountExceedHeaderException("Превышено допустимое количество текгов на одну статью");
    }
    for (var tag : article.getTags()) {
      if (tag.length() > MAX_TAG_LENGTH) {
        throw new ArticleTagLengthExceedHeaderException("Слишком большой размер тега");
      }
    }
  }

  private Article articleFromMap(ResultSet rs) {
    try {
      return new Article(
          new ArticleId(rs.getLong("article_id")),
          rs.getString("header"),
          extractTags(rs.getArray("tags")),
          getCommentsByArticleId(rs.getLong("article_id"))
      );
    } catch (Exception e) {
      throw new RuntimeException("Ошибка при маппинге статьи из ResultSet", e);
    }
  }

  private Set<String> extractTags(Array sqlArray) {
    if (sqlArray == null) {
      return Set.of();
    }
    try {
      Object[] array = (Object[]) sqlArray.getArray();
      Set<String> tags = new HashSet<>();
      for (Object tag : array) {
        tags.add(tag.toString());
      }
      return tags;
    } catch (Exception e) {
      throw new RuntimeException("Ошибка при извлечении тегов", e);
    }
  }

  private List<Comment> getCommentsByArticleId(long articleId) {
    return jdbi.withHandle(handle ->
        handle.createQuery(
                "SELECT comment_id, content FROM comment WHERE article_id = :article_id")
            .bind("article_id", articleId)
            .map((rs, ctx) -> new Comment(
                new CommentId(rs.getLong("comment_id")),
                new ArticleId(articleId),
                rs.getString("content")
            ))
            .list()
    );
  }

  @Override
  public ArticleId generateId() {
    Long value;

    try {
      value = jdbi.withHandle(handle ->
          handle.createQuery("SELECT max(article_id) FROM articles")
                .mapTo(Long.class)
                .one()
      ) + 1;
    } catch (IllegalStateException | NullPointerException e) {
      value = 0L;
    }

    return new ArticleId(value);
  }

  @Override
  public List<Article> getAll() {
    return jdbi.withHandle(handle ->
        handle.createQuery(
                "SELECT article_id, header, trending, tags FROM articles")
            .map((rs, ctx) -> articleFromMap(rs))
            .list()
    );
  }

  @Override
  public Article findById(ArticleId id) throws ArticleNotFoundException {
    return jdbi.withHandle(handle ->
        handle.createQuery(
                "SELECT article_id, header, trending, tags FROM articles WHERE article_id = :article_id")
            .bind("article_id", id.getId())
            .map((rs, ctx) -> articleFromMap(rs))
            .findOne()
            .orElseThrow(() -> new ArticleNotFoundException("Невозможно найти: нет статьи с ID " + id.getId()))
    );
  }

  @Override
  public synchronized void add(Article article)
      throws ArticleIdDublicationException, ArticleHeaderExceedHeaderException, ArticleTagsCountExceedHeaderException, ArticleTagLengthExceedHeaderException {
    checkArticleData(article);

    try {
      jdbi.inTransaction((Handle transHandle) -> {
        jdbi.useHandle(handle ->
            handle.createUpdate(
                    "INSERT INTO articles (article_id, header, trending, tags) " +
                        "VALUES (:article_id, :header, :trending, :tags)")
                .bind("article_id", article.getId().getId())
                .bind("header", article.getHeader())
                .bind("trending", article.trending)
                .bind("tags", article.getTags().toArray(new String[0]))
                .execute()
        );
        return null;
      });
    } catch (Exception e) {
      throw new ArticleIdDublicationException("Такая статья уже есть");
    }
  }

  @Override
  public synchronized void update(Article article)
      throws ArticleNotFoundException, ArticleTagsCountExceedHeaderException, ArticleTagLengthExceedHeaderException, ArticleHeaderExceedHeaderException {
    checkArticleData(article);

    int rowsUpdated = jdbi.inTransaction((Handle transHandle) -> {
      return jdbi.withHandle(handle ->
          handle.createUpdate(
                "UPDATE articles SET header = :header, trending = :trending, tags = :tags WHERE article_id = :article_id")
            .bind("article_id", article.getId().getId())
            .bind("header", article.getHeader())
            .bind("trending", article.trending)
            .bind("tags", article.getTags().toArray(new String[0]))
            .execute()
      );
    });

    if (rowsUpdated == 0) {
      throw new ArticleNotFoundException("Невозможно обновить: такой статьи нет");
    }
  }

  @Override
  public synchronized void delete(ArticleId articleId) throws ArticleNotFoundException {
    int rowsDeleted =jdbi.inTransaction((Handle transHandle) -> {
        return jdbi.withHandle(handle ->
            handle.createUpdate("DELETE FROM articles WHERE article_id = :article_id")
                .bind("article_id", articleId.getId())
                .execute()
          );
      });

    if (rowsDeleted == 0) {
      throw new ArticleNotFoundException("Невозможно удалить: такой статьи нет");
    }
  }
}
