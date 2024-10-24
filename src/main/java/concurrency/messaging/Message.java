package concurrency.messaging;

import java.util.Map;

public class Message {
  private Map<String, String> content;
  private EnrichmentType enrichmentType;

  public enum EnrichmentType {
    MSISDN;
  }
}
