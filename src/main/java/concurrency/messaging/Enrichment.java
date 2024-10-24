package concurrency.messaging;

import java.util.HashMap;
import java.util.Map;

public class Enrichment {
  public synchronized Map<String, String> enrich(Map<String, String> input) {
    Map<String, String> result = new HashMap<>(input);
    return result;
  }
}
