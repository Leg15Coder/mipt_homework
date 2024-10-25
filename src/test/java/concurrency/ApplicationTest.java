package concurrency;

import concurrency.messaging.EnrichmentService;
import concurrency.messaging.Message;
import concurrency.messaging.MessageParser;
import concurrency.users.User;
import concurrency.users.UsersRepository;
import org.junit.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ApplicationTest {
  UsersRepository usersRepository = new UsersRepository();

  private void updateRepository() {
    usersRepository.updateUserByMsisdn("89201054185", new User("dima", "ryaz"));
    usersRepository.updateUserByMsisdn("88005553535", new User("Anton", "sosed"));
    usersRepository.updateUserByMsisdn("87776662660", new User("Matvey", "the best"));
}

  @Test
  public void shouldReturnEnrichedMessage() {
    updateRepository();
    Application app = new Application(
        new MessageParser(),
        new EnrichmentService(usersRepository)
                  );

    Message enrichedMessage = app.enrich(app.parseMessage(true));
    Message notEnrichedMessage = app.enrich(app.parseMessage(false));

    assertTrue(enrichedMessage.getContent().containsKey("firstName"));
    assertTrue(enrichedMessage.getContent().containsKey("lastName"));
    assertFalse(notEnrichedMessage.getContent().containsKey("firstName"));
    assertFalse(notEnrichedMessage.getContent().containsKey("lastName"));
  }

  @Test
  public void shouldSucceedEnrichmentInConcurrentEnvironmentSuccessfully() throws InterruptedException {
    updateRepository();
    Application app = new Application(
        new MessageParser(),
        new EnrichmentService(usersRepository)
                  );
    List<Message> enrichmentResults = new CopyOnWriteArrayList<>();
    ExecutorService executorService = Executors.newFixedThreadPool(5);
    CountDownLatch latch = new CountDownLatch(5);

    for (int i = 0; i < 3; i++) {
      executorService.submit(() -> {
        enrichmentResults.add(
            app.enrich(app.parseMessage(true))
        );
        latch.countDown();
      });
    }

    Message rawMessage = app.parseMessage(true);
    for (int i = 0; i < 2; i++) {
      executorService.submit(() -> {
        enrichmentResults.add(
            app.enrich(rawMessage)
        );
        latch.countDown();
      });
    }

    latch.await();

    for (int i = 0; i < 5; i++) {
      assertTrue(enrichmentResults.get(i).getContent().containsKey("firstName"));
      assertTrue(enrichmentResults.get(i).getContent().containsKey("lastName"));
      assertEquals(enrichmentResults.get(i).getContent().get("firstName"), "Anton");
      assertEquals(enrichmentResults.get(i).getContent().get("lastName"), "sosed");
    }
  }
}
