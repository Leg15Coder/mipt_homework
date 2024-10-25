package concurrency.messaging;

import concurrency.users.User;
import concurrency.users.UsersRepository;

import java.util.HashMap;
import java.util.Map;

public class EnrichmentByMSISDN implements Enrichment {
  private final UsersRepository data;

  public EnrichmentByMSISDN(UsersRepository usersRepository) {
    this.data = usersRepository;
  }

  public Map<String, String> enrich(Map<String, String> input) {
    if (input == null) {
      throw new IllegalArgumentException("Сообщение содержит пустой content");
    }
    Map<String, String> result = new HashMap<>(input);
    if (input.containsKey("msisdn")) {
      User current = data.findByMsisdn(input.get("msisdn"));
      if (current == null) {
        current = new User("unknown firstname", "unknown lastname");
      }
      result.put("firstName", current.getFirstName());
      result.put("lastName", current.getLastName());
    }
    return result;
  }
}
