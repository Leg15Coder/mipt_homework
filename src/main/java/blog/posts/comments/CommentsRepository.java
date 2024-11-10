package blog.posts.comments;

import blog.exceptions.CommentIdDublicationException;
import blog.exceptions.CommentNotFoundException;

import java.util.List;

public interface CommentsRepository {
  CommentId generateId();

  List<Comment> getAll();

  Comment findById(CommentId id) throws CommentNotFoundException;

  void add(Comment comment) throws CommentIdDublicationException;

  void update(Comment comment) throws CommentNotFoundException;

  void delete(CommentId comment) throws CommentNotFoundException;
}
