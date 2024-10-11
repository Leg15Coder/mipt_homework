package zoo.animals;

import zoo.food.FoodList;
import zoo.general.Flying;
import zoo.general.Predator;

public class Eagle extends Predator implements Flying {
  @Override
  public void eat(FoodList.Meat food) {
    super.eat(food);
  }

  @Override
  public void fly() {
    System.out.println(this.getClass().getSimpleName() + " is walking");
  }
}
