package com.brannan.convnet.integration.extraction;

import java.util.Random;
import java.util.stream.IntStream;

import com.brannan.convnet.network.fundamentals.HelperLibrary.IntBiFunction;

public enum SampleType {
    CONSECUTIVE((numberOfImagesWanted, numberOfImagesInSet) -> IntStream.rangeClosed(0, numberOfImagesWanted)
            .filter(k -> k < numberOfImagesInSet).toArray()),

    RANDOM((numberOfImagesWanted, numberOfImagesInSet) -> IntStream
            .generate(() -> new Random().nextInt(numberOfImagesInSet)).limit(numberOfImagesWanted).toArray());

    private final IntBiFunction<int[]> generator;


    SampleType(final IntBiFunction<int[]> generator) {
        this.generator = generator;
    }


    /**
     * @return the generator
     */
    public IntBiFunction<int[]> getGenerator() {
        return generator;
    }
}