package blog.comments;

import blog.articles.ArticleId;

import java.util.Objects;

public class Comment {
  private final CommentId id;
  private final ArticleId article;

  String text;

  public Comment(CommentId id, ArticleId article, String text) {
    this.id = id;
    this.article = article;
    this.text = text;
  }

  public Comment updateText(String text) {
    return new Comment(this.id, this.article, text);
  }

  public CommentId getId() {
    return this.id;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Comment comment = (Comment) o;
    return Objects.equals(id, comment.id);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id);
  }
}
