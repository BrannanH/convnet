package com.brannanhancock.convnet.fundamentals.integration.extraction;

@FunctionalInterface
public interface ToIntArrayBiFunction {

    int[] applyAsArray(int t, int r);

}
