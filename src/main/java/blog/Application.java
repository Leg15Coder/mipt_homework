package blog;

import java.util.List;

public class Application {
  private final List<Controller> controllers;

  public Application(List<Controller> controllers) {
    this.controllers = controllers;
  }

  public void start() {
    for (Controller controller : controllers) {
      controller.initializeEndpoints();
    }
  }
}
