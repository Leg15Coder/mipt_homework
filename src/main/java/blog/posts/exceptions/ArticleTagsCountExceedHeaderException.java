package blog.posts.exceptions;

public class ArticleTagsCountExceedHeaderException extends Exception {
  public ArticleTagsCountExceedHeaderException(String message) {
    super(message);
  }
}
