package zoo.general;

import zoo.food.FoodList;

public abstract class Predator extends Animal {
  public void eat(FoodList.Meat food) {
    System.out.println(this.getClass().getSimpleName() + " eat " + food.getClass().getSimpleName());
  }
}
