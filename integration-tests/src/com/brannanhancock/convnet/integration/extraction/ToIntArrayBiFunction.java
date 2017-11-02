package com.brannanhancock.convnet.integration.extraction;

@FunctionalInterface
public interface ToIntArrayBiFunction {

    int[] applyAsArray(int t, int r);

}
