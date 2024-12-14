package blog.posts.comments;

import blog.posts.articles.ArticleId;
import blog.posts.exceptions.CommentIdDublicationException;
import blog.posts.exceptions.CommentNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class InMemoryCommentsRepository implements CommentsRepository {
  private final Map<CommentId, Comment> comments = new ConcurrentHashMap<>();
  private final AtomicLong nextId = new AtomicLong(0);

  @Override
  public CommentId generateId() {
    return new CommentId(nextId.getAndIncrement());
  }

  @Override
  public List<Comment> getAll() {
    return new ArrayList<>(comments.values());
  }

  @Override
  public List<Comment> findAllByArticleId(ArticleId articleId) {
    return List.of(); // todo later
  }

  @Override
  public Comment findById(CommentId id) throws CommentNotFoundException {
    if (!comments.containsKey(id)) {
      throw new CommentNotFoundException("Невозможно найти: нет комментария с ID " + id.getId());
    }
    return comments.get(id);
  }

  @Override
  public synchronized void add(Comment comment) throws CommentIdDublicationException {
    if (comments.containsKey(comment.getId())) {
      throw new CommentIdDublicationException("Такой комментарий уже есть");
    }
    comments.put(comment.getId(), comment);
  }

  @Override
  public synchronized void update(Comment comment) throws CommentNotFoundException {
    if (!comments.containsKey(comment.getId())) {
      throw new CommentNotFoundException("Невозможно обновить: такого коментария нет");
    }
    comments.put(comment.getId(), comment);
  }

  @Override
  public synchronized void delete(CommentId comment) throws CommentNotFoundException {
    if (!comments.containsKey(comment)) {
      throw new CommentNotFoundException("Невозможно удалить: такого коментария нет");
    }
    comments.remove(comment);
  }
}
