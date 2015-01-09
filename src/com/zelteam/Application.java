package com.zelteam;

import com.zelteam.input.Inputable;
import com.zelteam.input.LLNLInput;
import com.zelteam.schedule.RepScheduler;

import java.io.IOException;

public class Application {
    public static void main(String[] args) throws IOException {
//        Inputable inputable = new RandomBuilder();   // Random
//        Inputable inputable = new SDSC0Input(); // SDSC0Input
        Inputable inputable = new LLNLInput(); // LLNLInput

        new RepScheduler(inputable).run();      // RepScheduler
        //new NoRepScheduler(inputable).run(); //  NoRepScheduler
    }
}
