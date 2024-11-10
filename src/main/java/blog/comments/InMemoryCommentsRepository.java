package blog.comments;

import blog.exceptions.CommentIdDublicationException;
import blog.exceptions.CommentNotFoundException;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryCommentsRepository implements CommentsRepository {
  private final Map<CommentId, Comment> comments = new ConcurrentHashMap<>();

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
  public synchronized void delete(Comment comment) throws CommentNotFoundException {
    if (!comments.containsKey(comment.getId())) {
      throw new CommentNotFoundException("Невозможно удалить: такого коментария нет");
    }
    comments.remove(comment.getId());
  }
}
