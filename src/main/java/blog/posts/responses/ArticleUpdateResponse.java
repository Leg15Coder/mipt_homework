package blog.posts.responses;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public record ArticleUpdateResponse(Long id, String message) {
  @JsonCreator
  public ArticleUpdateResponse(@JsonProperty("id") Long id, @JsonProperty("message") String message) {
    this.message = message;
    this.id = id;
  }
}