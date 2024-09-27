package org.example;

public class Main {
    public interface Herbivore {
        default void eat(Grass food) {
            System.out.println(this.getClass().getSimpleName() + " eat " + food.getClass().getSimpleName());
        }
    }

    public interface Predator {
        default void eat(Meat food) {
            System.out.println(this.getClass().getSimpleName() + " eat " + food.getClass().getSimpleName());
        }
    }

    public interface Overland {
      default void walk() {
          System.out.println(this.getClass().getSimpleName() + " is walking");
      }
    }

    public interface Waterfowl {
      default void swim() {
          System.out.println(this.getClass().getSimpleName() + " is swimming");
      }
    }

    public interface Flying {
        default void fly() {
            System.out.println(this.getClass().getSimpleName() + " is flying");
        }
    }

    public static class Horse implements Herbivore, Overland {}

    public static class Tiger implements Predator, Overland {
        public void eat(Meat food) {
            if (food != Meat.BEEF) {
                throw new IllegalArgumentException("Tiger must eat beef");
            }
            Predator.super.eat(food);
        }
    }

    public static class Dolphin implements Predator, Waterfowl {
        public void eat(Meat food) {
            if (food != Meat.FISHMEAT) {
                throw new IllegalArgumentException("Dolphin must eat fish");
            }
            Predator.super.eat(food);
        }
    }

    public static class Eagle implements Predator, Flying {}

    public static class Camel implements Herbivore, Overland {}

    public static class Grass {}

    public enum Meat {
        FISHMEAT,
        BEEF,
        OTHERMEAT
    }

    public static void main(String[] args) {
        Grass leaves = new Grass();

        Horse horse = new Horse();
        Tiger tiger = new Tiger();
        Dolphin dolphin = new Dolphin();
        Eagle eagle = new Eagle();
        Camel camel = new Camel();

        horse.eat(leaves);
        tiger.eat(Meat.BEEF);
        dolphin.eat(Meat.FISHMEAT);
        eagle.eat(Meat.OTHERMEAT);

        try {
            tiger.eat(Meat.FISHMEAT);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        try {
            dolphin.eat(Meat.BEEF);
        } catch (Exception e) {
            System.out.println(e.toString());
        }

        dolphin.swim();
        eagle.fly();
        camel.walk();
    }
}