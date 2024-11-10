package blog.exceptions;

public class CommentDeleteException extends Exception {
  public CommentDeleteException(String message, Throwable e) {
    super(message, e);
  }
}
