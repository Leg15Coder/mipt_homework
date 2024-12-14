package blog.posts.responses;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record ManyArticlesCreateResponse(List<String> results, String message) {
  @JsonCreator
  public ManyArticlesCreateResponse(@JsonProperty("results") List<String> results, @JsonProperty("message") String message) {
    this.message = message;
    this.results = results;
  }
}