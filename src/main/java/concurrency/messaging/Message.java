package concurrency.messaging;

import java.util.Map;

public class Message {
  private Map<String, String> content;
  private final EnrichmentType enrichmentType;

  public enum EnrichmentType {
    MSISDN
  }

  public Message(Map<String, String> content, EnrichmentType enrichmentType) {
    this.content = content;
    this.enrichmentType = enrichmentType;
  }

  public synchronized void rewriteContent(Map<String, String> content) {
    this.content = content;
  }

  public synchronized Map<String, String> getContent() {
    return this.content;
  }

  public EnrichmentType getEnrichmentType() {
    return this.enrichmentType;
  }
}
