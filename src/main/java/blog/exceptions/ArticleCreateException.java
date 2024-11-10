package blog.exceptions;

public class ArticleCreateException extends Exception {
  public ArticleCreateException(String message, Throwable e) {
    super(message, e);
  }
}
