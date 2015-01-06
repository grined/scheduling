package com.zelteam;

public class Application {
    public static void main(String[] args) {
        InputBuilder inputBuilder = new InputBuilder();

        new RepScheduler(inputBuilder).run();
        new NoRepScheduler(inputBuilder).run();

    }
}
