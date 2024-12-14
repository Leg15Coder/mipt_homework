package blog.posts.responses;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public record ErrorResponse(String message) {
  @JsonCreator
  public ErrorResponse(@JsonProperty("message") String message) {
    this.message = message;
  }
}