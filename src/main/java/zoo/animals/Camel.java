package zoo.animals;

import zoo.food.FoodList;
import zoo.general.Herbivore;
import zoo.general.Walkable;

public class Camel extends Herbivore implements Walkable {
  @Override
  public void eat(FoodList.Grass food) {
    super.eat(food);
  }

  @Override
  public void walk() {
    System.out.println(this.getClass().getSimpleName() + " is walking");
  }
}
