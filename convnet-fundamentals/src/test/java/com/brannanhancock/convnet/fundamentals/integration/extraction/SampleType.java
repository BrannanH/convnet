package com.brannanhancock.convnet.fundamentals.integration.extraction;

import java.util.Random;
import java.util.stream.IntStream;


public enum SampleType {
    CONSECUTIVE((numberOfImagesWanted, numberOfImagesInSet) -> IntStream.rangeClosed(0, numberOfImagesWanted)
            .limit(numberOfImagesInSet).toArray()),

    RANDOM((numberOfImagesWanted, numberOfImagesInSet) -> IntStream
            .generate(() -> new Random().nextInt(numberOfImagesInSet)).limit(numberOfImagesWanted).toArray());

    private final ToIntArrayBiFunction generator;


    SampleType(final ToIntArrayBiFunction generator) {
        this.generator = generator;
    }


    /**
     * @return the generator
     */
    public ToIntArrayBiFunction getGenerator() {
        return generator;
    }
}