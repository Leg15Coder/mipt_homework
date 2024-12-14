package blog.posts.requests;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public record CommentCreateRequest(Long article, String text) {
  @JsonCreator
  public CommentCreateRequest(@JsonProperty("article") Long article, @JsonProperty("text") String text) {
    this.article = article;
    this.text = text;
  }
}

