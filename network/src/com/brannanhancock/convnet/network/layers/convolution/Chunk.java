package com.brannanhancock.convnet.network.layers.convolution;

import com.brannanhancock.convnet.fundamentals.MDA;

class Chunk {

    private final MDA data;
    private final int[] destination;
    private final int sourceEntity;

    Chunk(MDA data, int[] destination, int sourceEntity) {
        this.data = data;
        this.destination = destination;
        this.sourceEntity = sourceEntity;
    }

    MDA getData() {
        return data;
    }


    int[] getDestination() {
        return destination;
    }


    int getSourceEntity() {
        return sourceEntity;
    }
}
