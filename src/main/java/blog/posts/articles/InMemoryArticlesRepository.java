package blog.posts.articles;

import blog.exceptions.ArticleIdDublicationException;
import blog.exceptions.ArticleNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class InMemoryArticlesRepository implements ArticlesRepository {
  private final Map<ArticleId, Article> articles = new ConcurrentHashMap<>();
  private final AtomicLong nextId = new  AtomicLong(0);

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
  public synchronized void add(Article article) throws ArticleIdDublicationException {
    if (articles.containsKey(article.getId())) {
      throw new ArticleIdDublicationException("Такая статья уже есть");
    }
    articles.put(article.getId(), article);
  }

  @Override
  public synchronized void update(Article article) throws ArticleNotFoundException {
    if (!articles.containsKey(article.getId())) {
      throw new ArticleNotFoundException("Невозможно обновить: такой статьи нет");
    }
    articles.put(article.getId(), article);
  }

  @Override
  public synchronized void delete(ArticleId article) throws ArticleNotFoundException {
    if (!articles.containsKey(article)) {
      throw new ArticleNotFoundException("Невозможно удалить: такой статьи нет");
    }
    articles.remove(article);
  }
}
