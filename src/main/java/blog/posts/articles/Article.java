package blog.posts.articles;

import blog.posts.comments.Comment;
import blog.exceptions.CommentNotFoundException;

import java.util.*;

public class Article {
  private final ArticleId id;

  final String header;
  final Set<String> tags;
  final List<Comment> comments;

  public Article(ArticleId id, String header, Set<String> tags, List<Comment> comments) {
    this.id = id;
    this.header = header;
    this.tags = tags;
    this.comments = comments;
  }

  public ArticleId getId() {
    return this.id;
  }

  public Article addTag(String tag) {
    Set<String> newTags = new HashSet<>(this.tags);
    newTags.add(tag);
    return new Article(this.id, this.header, newTags, this.comments);
  }

  public Article addTags(Set<String> tags) {
    Set<String> newTags = new HashSet<>(this.tags);
    newTags.addAll(tags);
    return new Article(this.id, this.header, newTags, this.comments);
  }

  public Article removeTag(String tag) {
    Set<String> newTags = new HashSet<>(this.tags);
    newTags.remove(tag);
    return new Article(this.id, this.header, newTags, this.comments);
  }

  public Article removeTags(Set<String> tags) {
    Set<String> newTags = new HashSet<>(this.tags);
    newTags.removeAll(tags);
    return new Article(this.id, this.header, newTags, this.comments);
  }

  public Article switchHeader(String header) {
    return new Article(this.id, header, this.tags, this.comments);
  }

  public Article addComment(Comment comment) {
    List<Comment> newComments = new ArrayList<>(this.comments);
    newComments.add(comment);
    return new Article(this.id, this.header, this.tags, newComments);
  }

  public Article removeComment(Comment comment) throws CommentNotFoundException {
    if (!this.comments.contains(comment)) {
      throw new CommentNotFoundException("Комментарий не найден");
    }
    List<Comment> newComments = new ArrayList<>(this.comments);
    newComments.remove(comment);
    return new Article(this.id, this.header, this.tags, newComments);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Article article = (Article) o;
    return Objects.equals(id, article.id);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id);
  }
}
