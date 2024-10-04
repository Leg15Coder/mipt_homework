package zoo.general;

import zoo.food.FoodList;

public abstract class Herbivore extends Animal {
  public void eat(FoodList.Grass food) {
    System.out.println(this.getClass().getSimpleName() + " eat " + food.getClass().getSimpleName());
  }
}
