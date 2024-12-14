package blog.posts.responses;

import blog.posts.comments.Comment;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Set;

public record ArticleGetResponse(String header, Set<String> tags, List<Comment> comments) {
  @JsonCreator
  public ArticleGetResponse(@JsonProperty("header") String header, @JsonProperty("tags") Set<String> tags, @JsonProperty("comments") List<Comment> comments) {
    this.header = header;
    this.tags = tags;
    this.comments = comments;
  }
}