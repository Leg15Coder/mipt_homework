package blog.posts.comments;

import blog.posts.exceptions.CommentIdDublicationException;
import blog.posts.exceptions.CommentNotFoundException;

import blog.posts.articles.ArticleId;
import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;

import java.sql.ResultSet;
import java.util.List;

public class PostgresCommentsRepository implements CommentsRepository {
  private final Jdbi jdbi;

  public PostgresCommentsRepository(Jdbi jdbi) {
    this.jdbi = jdbi;
  }

  private Comment commentFromMap(ResultSet rs) {
    try {
      return new Comment(
          new CommentId(rs.getLong("comment_id")),
          new ArticleId(rs.getLong("article_id")),
          rs.getString("content")
      );
    } catch (Exception e) {
      throw new RuntimeException("Ошибка при маппинге комментария из ResultSet", e);
    }
  }

  @Override
  public CommentId generateId() {
    long value;

    try {
      value = jdbi.withHandle(handle ->
          handle.createQuery("SELECT max(comment_id) FROM comment")
              .mapTo(Long.class)
              .one()
      ) + 1;
    } catch (IllegalStateException | NullPointerException e) {
      value = 0L;
    }

    return new CommentId(value);
  }

  @Override
  public List<Comment> getAll() {
    return jdbi.withHandle(handle ->
        handle.createQuery(
                "SELECT comment_id, article_id, content FROM comment")
            .map((rs, ctx) -> commentFromMap(rs))
            .list()
    );
  }

  @Override
  public List<Comment> findAllByArticleId(ArticleId articleId) {
    return jdbi.withHandle(handle ->
        handle.createQuery("SELECT comment_id, article_id, content FROM comment WHERE article_id = :article_id")
            .bind("article_id", articleId.getId())
            .map((rs, ctx) -> commentFromMap(rs))
            .list()
    );
  }

  @Override
  public Comment findById(CommentId id) throws CommentNotFoundException {
    return jdbi.withHandle(handle ->
        handle.createQuery("SELECT comment_id, article_id, content FROM comment WHERE comment_id = :comment_id")
            .bind("comment_id", id.getId())
            .map((rs, ctx) -> commentFromMap(rs))
            .findOne()
            .orElseThrow(() -> new CommentNotFoundException("Комментарий с ID=" + id.getId() + " не найден"))
    );
  }

  @Override
  public synchronized void add(Comment comment) throws CommentIdDublicationException {
    try {
      jdbi.inTransaction((Handle transHandle) -> {
        jdbi.useHandle(handle ->
            handle.createUpdate("INSERT INTO comment (comment_id, article_id, content) VALUES (:comment_id, :article_id, :content)")
                .bind("comment_id", comment.getId().getId())
                .bind("article_id", comment.getArticle().getId())
                .bind("content", comment.getText())
                .execute()
        );
        return null;
      });
    } catch (Exception e) {
      throw new CommentIdDublicationException("Комментарий с ID=" + comment.getId().getId() + " уже существует");
    }
  }

  @Override
  public synchronized void update(Comment comment) throws CommentNotFoundException {
    int rowsUpdated = jdbi.inTransaction((Handle transHandle) -> {
      return jdbi.withHandle(handle ->
          handle.createUpdate("UPDATE comment SET content = :content WHERE comment_id = :comment_id")
              .bind("comment_id", comment.getId().getId())
              .bind("content", comment.getText())
              .execute()
      );
    });

    if (rowsUpdated == 0) {
      throw new CommentNotFoundException("Невозможно обновить: комментарий с ID=" + comment.getId().getId() + " не найден");
    }
  }

  @Override
  public synchronized void delete(CommentId commentId) throws CommentNotFoundException {
    int rowsDeleted = jdbi.inTransaction((Handle transHandle) -> {
      return jdbi.withHandle(handle ->
          handle.createUpdate("DELETE FROM comment WHERE comment_id = :comment_id")
              .bind("comment_id", commentId.getId())
              .execute()
      );
    });

    if (rowsDeleted == 0) {
      throw new CommentNotFoundException("Невозможно удалить: комментарий с ID=" + commentId.getId() + " не найден");
    }
  }
}
