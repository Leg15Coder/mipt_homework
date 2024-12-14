package blog.posts.requests;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public record CommentGetRequest(Long article) {
  @JsonCreator
  public CommentGetRequest(@JsonProperty("article") Long article) {
    this.article = article;
  }
}

