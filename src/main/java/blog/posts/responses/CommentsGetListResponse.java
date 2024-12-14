package blog.posts.responses;

import blog.posts.comments.Comment;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record CommentsGetListResponse(List<Comment> comments) {
  @JsonCreator
  public CommentsGetListResponse(@JsonProperty("comments") List<Comment> comments) {
    this.comments = comments;
  }
}