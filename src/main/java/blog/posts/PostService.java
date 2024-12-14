package blog.posts;

import blog.posts.exceptions.*;
import blog.posts.articles.Article;
import blog.posts.articles.ArticleId;
import blog.posts.articles.ArticlesRepository;
import blog.posts.comments.Comment;
import blog.posts.comments.CommentId;
import blog.posts.comments.CommentsRepository;
import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;

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

      String updatedHeader = header != null ? header : articleToUpdate.getHeader();
      Set<String> updatedTags = tags != null ? tags : articleToUpdate.getTags();
      List<Comment> updatedComments = comments != null ? comments : articleToUpdate.getComments();

      Article updatedArticle = new Article(
          articleId,
          updatedHeader,
          updatedTags,
          updatedComments
      );
      this.articlesRepository.update(updatedArticle);
    } catch (ArticleNotFoundException e) {
      throw new ArticleUpdateException("Не удалось обновить статью с ID=" + id + ": статья не найдена.", e);
    } catch (ArticleTagsCountExceedHeaderException | ArticleTagLengthExceedHeaderException | ArticleHeaderExceedHeaderException e) {
      throw new ArticleUpdateException("Превышения лимит переданных данных", e);
    } catch (Exception e) {
      throw new ArticleUpdateException("Произошла ошибка при обновлении статьи с ID=" + id, e);
    }
  }

  public long createArticle(String header, Set<String> tags) throws ArticleCreateException {
    if (header == null || tags == null) {
      throw new ArticleCreateException("Не удалось добавить статью, так как передан элемент типа null", new Exception());
    }

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
    } catch (ArticleTagsCountExceedHeaderException | ArticleTagLengthExceedHeaderException | ArticleHeaderExceedHeaderException e) {
      throw new ArticleCreateException("Превышения лимит переданных данных", e);
    }
  }

  public void deleteArticle(long id) throws ArticleDeleteException {
    try {
      Article article = this.articlesRepository.findById(new ArticleId(id));

      for (var comment : article.getComments()) {
        try {
          this.commentsRepository.delete(comment.getId());
        } catch (CommentNotFoundException e) {
          continue; // Комментария уже нет
        }
      }
      this.articlesRepository.delete(new ArticleId(id));
    } catch (ArticleNotFoundException e) {
      throw new ArticleDeleteException("Не удалось удалить статью с ID=" + id, e);
    }
  }

  public long createComment(long articleId, String text) throws CommentCreateException {
    if (text == null) {
      throw new CommentCreateException("Не удалось создать комментарий, передан пустой контент", new Exception());
    }

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
    } catch (CommentIdDublicationException | ArticleFindException | ArticleNotFoundException | ArticleTagsCountExceedHeaderException | ArticleTagLengthExceedHeaderException | ArticleHeaderExceedHeaderException e) {
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
      Article updatedArticle;
      try {
        updatedArticle = articleWithDeletedComment.removeComment(commentToDelete);
      } catch (CommentNotFoundException e) {
        updatedArticle = articleWithDeletedComment;
      }

      articlesRepository.update(updatedArticle);
    } catch (ArticleNotFoundException | CommentNotFoundException | ArticleTagsCountExceedHeaderException | ArticleTagLengthExceedHeaderException | ArticleHeaderExceedHeaderException e) {
      throw new CommentDeleteException("Не удалось удалить комментарий с ID=" + id, e);
    }
  }
}
