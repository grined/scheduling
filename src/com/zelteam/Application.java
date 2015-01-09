package com.zelteam;

public class Application {
    public static void main(String[] args) {
        Inputable inputable = new InputBuilder();

        new RepScheduler(inputable).run();
        //new NoRepScheduler(inputBuilder).run();

    }
}
