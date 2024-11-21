package blog.posts.responses;

import com.fasterxml.jackson.annotation.JsonCreator;

public record ArticleUpdateResponse() {
  @JsonCreator
  public ArticleUpdateResponse() {}
}