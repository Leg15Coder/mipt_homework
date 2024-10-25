package concurrency.messaging;

import java.util.*;

public class MessageParser {
  private final static ArrayList<String> choices = new ArrayList<>(List.of(new String[]{
      "action", "button_click", "page", "book_card", "city", "Moscow", "count", "none", "readble", "true"
  }));

  public Message parseMessage(boolean MSISDN) {
    Map<String, String> content = new HashMap<>();
    Random random = new Random();
    int amount = random.nextInt(1, 9);
    for (int q = 0; q < amount; ++q) {
      int i = random.nextInt(0, 9);
      int j = random.nextInt(0, 9);
      content.put(choices.get(i), choices.get(j));
    }
    if (MSISDN) {
      content.put("msisdn", "88005553535");
    }
    return new Message(content, Message.EnrichmentType.MSISDN);
  }
}
