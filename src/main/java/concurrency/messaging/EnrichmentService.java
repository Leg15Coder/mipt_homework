package concurrency.messaging;

import concurrency.users.UsersRepository;

import java.util.HashMap;
import java.util.Map;

public class EnrichmentService {
  private final Map<Message.EnrichmentType, Enrichment> enrichmentSource;

  public EnrichmentService(UsersRepository usersRepository) {
    this.enrichmentSource = new HashMap<>();
    this.enrichmentSource.put(Message.EnrichmentType.MSISDN, new EnrichmentByMSISDN(usersRepository));
    this.enrichmentSource.put(null, new EnrichmentByMSISDN(usersRepository));
  }

  public Message enrich(Message message) {
    if (message == null) {
      throw new IllegalArgumentException("message не может быть null");
    }
    if (!this.enrichmentSource.containsKey(message.getEnrichmentType())) {
      throw new IllegalArgumentException("Переданное сообщение содержит недопустимый enrichmentType");
    }
    synchronized (message) {
      Map<String, String> content = message.getContent();
      content = this.enrichmentSource.get(message.getEnrichmentType()).enrich(content);
      message.rewriteContent(content);
      return message;
    }
  }
}
