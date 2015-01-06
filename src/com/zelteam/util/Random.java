package com.zelteam.util;

import org.apache.commons.math3.distribution.*;

public enum Random {
    INSTANCE;

    private final PoissonDistribution poissonDistribution;
    private java.util.Random random;

    Random(){
        random = new java.util.Random();
        poissonDistribution = new PoissonDistribution(5);
    }

    public static java.util.Random simple(){
        return INSTANCE.random;
    }

    public static PoissonDistribution poisson(double mean){
        return new PoissonDistribution(mean);
    }

    public static IntegerDistribution uniformInt(int lower, int upper){
        return new UniformIntegerDistribution(lower, upper);
    }

    public static RealDistribution uniformReal(double lower, double upper){
        return new UniformRealDistribution(lower, upper);
    }
}
