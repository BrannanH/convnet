package com.brannanhancock.convnet.fundamentals.layers.pooling;

import com.brannanhancock.convnet.fundamentals.layers.Layer;
import com.brannanhancock.convnet.fundamentals.layers.LayerBuilder;
import com.brannanhancock.convnet.fundamentals.layers.pooling.PoolingLibrary.PoolingType;

import java.util.Objects;
import java.util.stream.IntStream;

/**
 *
 */
public final class PoolingLayer implements Layer {

    private final int[] poolSizes;
    private final PoolingType poolingType;
    private final int[] inputDimensions;

    private PoolingLayer(final int[] poolSizes, final PoolingType poolingType, final int[] inputDimensions) {
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

    public static final class Builder implements LayerBuilder<PoolingLayer> {

        private int[] poolSizes;

        private PoolingType poolingType = PoolingType.MAX;

        private int[] inputDimensions;

        Builder withPoolSizes(final int... poolSizes) {
            this.poolSizes = poolSizes;
            return this;
        }

        Builder withPoolingType(final PoolingType poolingType) {
            this.poolingType = poolingType;
            return this;
        }

        Builder withInputDimensions(final int... inputDimensions) {
            this.inputDimensions = inputDimensions;
            return this;
        }

        /**
         * @see {@link LayerBuilder#build}
         */
        @Override
        public PoolingLayer build() {
            if (Objects.isNull(inputDimensions)) {
                throw new IllegalStateException("The pooling factors must be set before a pooling layer can be built.");
            }
            if (Objects.isNull(poolSizes)) {
                poolSizes = IntStream.generate(() -> 1).limit(inputDimensions.length).toArray();
            }
            return new PoolingLayer(poolSizes,
                    poolingType,
                    inputDimensions);
        }
    }
}
