package blog.exceptions;

public class CommentCreateException extends Exception {
  public CommentCreateException(String message, Throwable e) {
    super(message, e);
  }
}
