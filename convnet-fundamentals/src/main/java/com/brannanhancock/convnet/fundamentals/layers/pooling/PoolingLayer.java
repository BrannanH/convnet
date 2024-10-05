package com.brannanhancock.convnet.fundamentals.layers.pooling;

import com.brannanhancock.convnet.fundamentals.layers.Layer;
import com.brannanhancock.convnet.fundamentals.layers.pooling.PoolingLibrary.PoolingType;

/**
 *
 */
public final class PoolingLayer implements Layer {

    private final int[] poolSizes;
    private final PoolingType poolingType;
    private final int[] inputDimensions;

    PoolingLayer(final int[] poolSizes, final PoolingType poolingType, final int[] inputDimensions) {
        this.poolSizes = poolSizes;
        this.poolingType = poolingType;
        this.inputDimensions = inputDimensions;
    }

    public int[] getPoolSizes() {
        return poolSizes;
    }

    public PoolingType getPoolingType() {
        return poolingType;
    }

    public int[] getInputDimensions() {
        return inputDimensions;
    }
}
