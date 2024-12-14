package blog.posts.requests;

import blog.posts.comments.Comment;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Set;

public record ArticleUpdateRequest(String header, Set<String> tags, List<Comment> comments) {
  @JsonCreator
  public ArticleUpdateRequest(@JsonProperty("header") String header, @JsonProperty("tags") Set<String> tags, @JsonProperty("comments") List<Comment> comments) {
    this.header = header;
    this.tags = tags;
    this.comments = comments;
  }
}

