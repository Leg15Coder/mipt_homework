package blog.exceptions;

public class ArticleUpdateException extends Exception {
  public ArticleUpdateException(String message, Throwable e) {
    super(message, e);
  }
}
