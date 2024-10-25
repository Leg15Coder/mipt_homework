package concurrency.messaging;

import java.util.Map;

public interface Enrichment {
  Map<String, String> enrich(Map<String, String> input);
}
