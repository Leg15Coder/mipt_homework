package blog.posts;

import blog.exceptions.*;
import blog.posts.articles.Article;
import blog.posts.articles.ArticleId;
import blog.posts.articles.ArticlesRepository;
import blog.posts.comments.Comment;
import blog.posts.comments.CommentId;
import blog.posts.comments.CommentsRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class PostService {
  private final ArticlesRepository articlesRepository;
  private final CommentsRepository commentsRepository;

  public PostService(ArticlesRepository articlesRepository, CommentsRepository commentsRepository) {
    this.articlesRepository = articlesRepository;
    this.commentsRepository = commentsRepository;
  }

  public List<Article> getAll() {
    return this.articlesRepository.getAll();
  }

  public Article findArticleById(long id) throws ArticleFindException {
    try {
      return this.articlesRepository.findById(new ArticleId(id));
    } catch (ArticleNotFoundException e) {
      throw new ArticleFindException("Не удалось найти статью с ID=" + id, e);
    }
  }

  public void updateArticle(long id, String header, Set<String> tags, List<Comment> comments) throws ArticleUpdateException {
    try {
      ArticleId articleId = new ArticleId(id);
      Article articleToUpdate = this.articlesRepository.findById(articleId);
      Article updatedArticle = new Article(
          articleId,
          header,
          tags,
          comments
      );
      this.articlesRepository.update(updatedArticle);
    } catch (ArticleNotFoundException e) {
      throw new ArticleUpdateException("Не удалось обнавить статью с ID=" + id, e);
    }
  }

  public long createArticle(String header, Set<String> tags) throws ArticleCreateException {
    Article newArticle = new Article(
        articlesRepository.generateId(),
        header,
        tags,
        new ArrayList<>()
        );
    try {
      this.articlesRepository.add(newArticle);
      return newArticle.getId().getId();
    } catch (ArticleIdDublicationException e) {
      throw new ArticleCreateException("Не удалось добавить статью, так как она уже существует", e);
    }
  }

  public void deleteArticle(long id) throws ArticleDeleteException {
    try {
      this.articlesRepository.delete(new ArticleId(id));
    } catch (ArticleNotFoundException e) {
      throw new ArticleDeleteException("Не удалось удалить статью с ID=" + id, e);
    }
  }

  public long createComment(long articleId, String text) throws CommentCreateException {
    Comment newComment = new Comment(
        commentsRepository.generateId(),
        new ArticleId(articleId),
        text
    );
    try {
      this.commentsRepository.add(newComment);
      Article articleWithNewComment = findArticleById(articleId);
      Article updatedArticle = articleWithNewComment.addComment(newComment);
      articlesRepository.update(updatedArticle);
    } catch (CommentIdDublicationException | ArticleFindException | ArticleNotFoundException e) {
      throw new CommentCreateException("Не удалось создать комментарий", e);
    }
    return newComment.getId().getId();
  }

  public void deleteComment(long id) throws CommentDeleteException {
    try {
      CommentId commentId = new CommentId(id);
      Comment commentToDelete = this.commentsRepository.findById(commentId);
      commentsRepository.delete(commentId);
      ArticleId articleId = commentToDelete.getArticle();
      Article articleWithDeletedComment = this.articlesRepository.findById(articleId);
      Article updatedArticle = articleWithDeletedComment.removeComment(commentToDelete);
      articlesRepository.update(updatedArticle);
    } catch (ArticleNotFoundException | CommentNotFoundException e) {
      throw new CommentDeleteException("Не удалось удалить комментарий с ID=" + id, e);
    }
  }
}
