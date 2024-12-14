package blog.posts.exceptions;

public class CommentException extends Exception {
  public CommentException(String message, Throwable e) {
    super(message, e);
  }
}
