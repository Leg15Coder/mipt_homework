package concurrency.messaging;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;

public class MessageTest {

  @Test
  public void rewriteContent() {
    Map<String, String> content = new HashMap<>();
    content.put("action", "button_click");
    content.put("page", "book_card");
    content.put("msisdn", "88005553535");
    Message message = new Message(content, Message.EnrichmentType.MSISDN);
    Map<String, String> newContent = new HashMap<>();
    newContent.put("firstName", "Vasya");
    newContent.put("lastName", "Ivanov");
    newContent.put("msisdn", "88005553535");
    message.rewriteContent(newContent);
    assertEquals(message.getContent(), newContent);
    assertNotEquals(message.getContent(), content);
  }

  @Test
  public void getContent() {
    Map<String, String> content = new HashMap<>();
    content.put("action", "button_click");
    content.put("page", "book_card");
    content.put("msisdn", "88005553535");
    Message message = new Message(content, Message.EnrichmentType.MSISDN);
    assertEquals(message.getContent(), content);
  }

  @Test
  public void getEnrichmentType() {
    Map<String, String> content = new HashMap<>();
    content.put("action", "button_click");
    content.put("page", "book_card");
    content.put("msisdn", "88005553535");
    Message message = new Message(content, Message.EnrichmentType.MSISDN);
    assertEquals(message.getEnrichmentType(), Message.EnrichmentType.MSISDN);
  }
}