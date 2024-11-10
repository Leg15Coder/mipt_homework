package blog.comments;

import java.util.Objects;

public class CommentId {
  private final Long id;

  public CommentId(long id) {
    this.id = id;
  }

  public Long getId() {
    return this.id;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof CommentId commentId)) return false;
    return Objects.equals(id, commentId.id);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id);
  }
}
