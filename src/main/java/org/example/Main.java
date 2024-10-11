package org.example;

import zoo.animals.*;
import zoo.food.FoodList;

public class Main {
    public static void main(String[] args) {
        Horse horse = new Horse();
        Tiger tiger = new Tiger();
        Dolphin dolphin = new Dolphin();
        Eagle eagle = new Eagle();
        Camel camel = new Camel();

        horse.eat(FoodList.Grass.GRASS);
        tiger.eat(FoodList.Meat.BEEF);
        dolphin.eat(FoodList.Meat.FISHMEAT);
        eagle.eat(FoodList.Meat.OTHERMEAT);

        try {
            tiger.eat(FoodList.Meat.FISHMEAT);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        try {
            dolphin.eat(FoodList.Meat.BEEF);
        } catch (Exception e) {
            System.out.println(e.toString());
        }

        dolphin.swim();
        eagle.fly();
        camel.walk();
    }
}