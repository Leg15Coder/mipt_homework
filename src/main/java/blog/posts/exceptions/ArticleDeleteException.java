package blog.posts.exceptions;

public class ArticleDeleteException extends ArticleException {
  public ArticleDeleteException(String message, Throwable e) {
    super(message, e);
  }
}
