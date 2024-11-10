package blog.exceptions;

public class ArticleFindException extends Exception {
  public ArticleFindException(String message, Throwable e) {
    super(message, e);
  }
}
