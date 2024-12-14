package blog.posts.requests;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Set;

public record ArticleCreateRequest(String header, Set<String> tags) {
  @JsonCreator
  public ArticleCreateRequest(@JsonProperty("header") String header, @JsonProperty("tags") Set<String> tags) {
    this.header = header;
    this.tags = tags;
  }
}

