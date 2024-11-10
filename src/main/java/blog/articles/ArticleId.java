package blog.articles;

import java.util.Objects;

public class ArticleId {
  private final Long id;

  public ArticleId(long id) {
    this.id = id;
  }

  public Long getId() {
    return this.id;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof ArticleId articleId)) return false;
    return Objects.equals(id, articleId.id);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id);
  }
}
