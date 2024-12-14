package blog.posts.responses;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public record CommentCreateResponse(Long id, String message) {
  @JsonCreator
  public CommentCreateResponse(@JsonProperty("id") Long id, @JsonProperty("message") String message) {
    this.message = message;
    this.id = id;
  }
}