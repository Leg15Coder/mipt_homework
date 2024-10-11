package zoo.animals;

import zoo.food.FoodList;
import zoo.general.Predator;
import zoo.general.Walkable;

public class Tiger extends Predator implements Walkable {
  @Override
  public void eat(FoodList.Meat food) {
    if (food != FoodList.Meat.BEEF) {
      throw new IllegalArgumentException("Tiger must eat beef");
    }
    super.eat(food);
  }

  @Override
  public void walk() {
    System.out.println(this.getClass().getSimpleName() + " is walking");
  }
}
