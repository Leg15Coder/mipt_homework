package blog.exceptions;

public class ArticleDeleteException extends Exception {
  public ArticleDeleteException(String message, Throwable e) {
    super(message, e);
  }
}
