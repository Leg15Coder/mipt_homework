package org.example;


import structures.CustomArrayList;

public class Main {
    public static void main(String[] args) {
        CustomArrayList<Integer> arr = new CustomArrayList<>();
        for (int i = 1; i <= 16; ++i) {
            arr.add(i);
        }
        System.out.println(arr.get(3));
        System.out.println(arr.get(7));
        System.out.println(arr.get(10));

        System.out.println(arr.remove(1));
        System.out.println(arr.remove(4));
        System.out.println(arr.remove(6));

        System.out.println(arr.get(3));
        System.out.println(arr.get(7));
        System.out.println(arr.get(10));
    }
}