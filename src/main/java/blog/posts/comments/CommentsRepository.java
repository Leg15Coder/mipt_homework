package blog.posts.comments;

import blog.posts.articles.ArticleId;
import blog.posts.exceptions.CommentIdDublicationException;
import blog.posts.exceptions.CommentNotFoundException;

import java.util.List;

public interface CommentsRepository {
  CommentId generateId();

  List<Comment> getAll();

  List<Comment> findAllByArticleId(ArticleId articleId);

  Comment findById(CommentId id) throws CommentNotFoundException;

  void add(Comment comment) throws CommentIdDublicationException;

  void update(Comment comment) throws CommentNotFoundException;

  void delete(CommentId comment) throws CommentNotFoundException;
}
