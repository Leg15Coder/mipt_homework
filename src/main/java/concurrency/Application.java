package concurrency;

import concurrency.messaging.EnrichmentService;
import concurrency.messaging.Message;
import concurrency.messaging.MessageParser;

public class Application {
  MessageParser parser;
  EnrichmentService enrichmentService;

  public Application(MessageParser parser, EnrichmentService enrichmentService) {
    this.parser = parser;
    this.enrichmentService = enrichmentService;
  }

  public Message enrich(Message message) {
    return enrichmentService.enrich(message);
  }

  public Message parseMessage(boolean MSISDN) {
    return parser.parseMessage(MSISDN);
  }
}
