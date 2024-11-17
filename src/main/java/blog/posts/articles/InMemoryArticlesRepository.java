package blog.posts.articles;

import blog.posts.exceptions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class InMemoryArticlesRepository implements ArticlesRepository {
  static final long MAX_HEADER_LENGTH = 1024L;
  static final long MAX_TAGS_COUNT = 256L;
  static final long MAX_TAG_LENGTH = 256L;

  private final Map<ArticleId, Article> articles = new ConcurrentHashMap<>();
  private final AtomicLong nextId = new AtomicLong(0);

  private void checkArticleData(Article article)
      throws ArticleTagLengthExceedHeaderException, ArticleTagsCountExceedHeaderException, ArticleHeaderExceedHeaderException {
    if (article.getHeader().length() > MAX_HEADER_LENGTH) {
      throw new ArticleHeaderExceedHeaderException("Слишком большой размер заголовка");
    }
    if (article.getTags().size() > MAX_TAGS_COUNT) {
      throw new ArticleTagsCountExceedHeaderException("Превышено допустимое количество текгов на одну статью");
    }
    for (var tag : article.getTags()) {
      if (tag.length() > MAX_TAG_LENGTH) {
        throw new ArticleTagLengthExceedHeaderException("Слишком большой размер тега");
      }
    }
  }

  @Override
  public ArticleId generateId() {
    return new ArticleId(nextId.getAndIncrement());
  }

  @Override
  public List<Article> getAll() {
    return new ArrayList<>(articles.values());
  }

  @Override
  public Article findById(ArticleId id) throws ArticleNotFoundException {
    if (!articles.containsKey(id)) {
      throw new ArticleNotFoundException("Невозможно найти: нет статьи с ID " + id.getId());
    }
    return articles.get(id);
  }

  @Override
  public synchronized void add(Article article)
      throws ArticleIdDublicationException, ArticleHeaderExceedHeaderException, ArticleTagsCountExceedHeaderException, ArticleTagLengthExceedHeaderException {
    checkArticleData(article);
    if (articles.containsKey(article.getId())) {
      throw new ArticleIdDublicationException("Такая статья уже есть");
    }
    articles.put(article.getId(), article);
  }

  @Override
  public synchronized void update(Article article)
      throws ArticleNotFoundException, ArticleTagsCountExceedHeaderException, ArticleTagLengthExceedHeaderException, ArticleHeaderExceedHeaderException {
    checkArticleData(article);
    if (!articles.containsKey(article.getId())) {
      throw new ArticleNotFoundException("Невозможно обновить: такой статьи нет");
    }
    articles.put(article.getId(), article);
  }

  @Override
  public synchronized void delete(ArticleId articleId) throws ArticleNotFoundException {
    if (!articles.containsKey(articleId)) {
      throw new ArticleNotFoundException("Невозможно удалить: такой статьи нет");
    }
    articles.remove(articleId);
  }
}
