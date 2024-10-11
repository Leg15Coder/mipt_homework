package zoo.animals;

import zoo.food.FoodList;
import zoo.general.Predator;
import zoo.general.Swimmable;

public class Dolphin extends Predator implements Swimmable {
  @Override
  public void eat(FoodList.Meat food) {
    if (food != FoodList.Meat.FISHMEAT) {
      throw new IllegalArgumentException("Dolphin must eat fish");
    }
    super.eat(food);
  }

  @Override
  public void swim() {
    System.out.println(this.getClass().getSimpleName() + " is walking");
  }
}
