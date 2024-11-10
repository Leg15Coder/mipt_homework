package blog.comments;

import blog.exceptions.CommentIdDublicationException;
import blog.exceptions.CommentNotFoundException;

public interface CommentsRepository {
  Comment findById(CommentId id) throws CommentNotFoundException;

  void add(Comment comment) throws CommentIdDublicationException;

  void update(Comment comment) throws CommentNotFoundException;

  void delete(Comment comment) throws CommentNotFoundException;
}
