package org.example;

import spark.Request;
import spark.Response;
import spark.Spark;

public class Main {

    public static void main(String[] args) {
        Spark.get("/hello", (Request request, Response response) -> "Hello, World!");
    }
}