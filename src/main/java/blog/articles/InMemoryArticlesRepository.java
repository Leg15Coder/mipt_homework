package blog.articles;

import blog.exceptions.ArticleIdDublicationException;
import blog.exceptions.ArticleNotFoundException;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryArticlesRepository implements ArticlesRepository {
  private final Map<ArticleId, Article> articles = new ConcurrentHashMap<>();

  @Override
  public Article findById(ArticleId id) throws ArticleNotFoundException {
    if (!articles.containsKey(id)) {
      throw new ArticleNotFoundException("Невозможно найти: нет комментария с ID " + id.getId());
    }
    return articles.get(id);
  }

  @Override
  public synchronized void add(Article article) throws ArticleIdDublicationException {
    if (articles.containsKey(article.getId())) {
      throw new ArticleIdDublicationException("Такой комментарий уже есть");
    }
    articles.put(article.getId(), article);
  }

  @Override
  public synchronized void update(Article article) throws ArticleNotFoundException {
    if (!articles.containsKey(article.getId())) {
      throw new ArticleNotFoundException("Невозможно обновить: такого коментария нет");
    }
    articles.put(article.getId(), article);
  }

  @Override
  public synchronized void delete(Article article) throws ArticleNotFoundException {
    if (!articles.containsKey(article.getId())) {
      throw new ArticleNotFoundException("Невозможно удалить: такого коментария нет");
    }
    articles.remove(article.getId());
  }
}
