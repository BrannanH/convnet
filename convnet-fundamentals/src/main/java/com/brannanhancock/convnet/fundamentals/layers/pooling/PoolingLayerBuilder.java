package com.brannanhancock.convnet.fundamentals.layers.pooling;

import java.util.Optional;

import com.brannanhancock.convnet.fundamentals.layers.LayerBuilder;
import com.brannanhancock.convnet.fundamentals.layers.pooling.PoolingLibrary.PoolingType;
import jakarta.inject.Inject;

/**
 * This class explicitly injects the dependencies of the Pooling Layer it
 * builds, such that the Pooling Layer itself doesn't need to be injected. This
 * results in this builder being highly coupled to the Pooling Layer, but that
 * is inherent in its nature.
 *
 * @author Brannan R. Hancock
 *
 */
final class PoolingLayerBuilder implements LayerBuilder<PoolingLayer> {

    private final PoolingService poolingLayerService;

    private Optional<int[]> poolSizes = Optional.empty();

    private Optional<PoolingType> poolingType = Optional.empty();

    private Optional<int[]> inputDimensions = Optional.empty();

    @Inject
    PoolingLayerBuilder(final PoolingService poolingLayerService) {
        this.poolingLayerService = poolingLayerService;
    }

    PoolingLayerBuilder withPoolSizes(final int... poolSizes) {
        this.poolSizes = Optional.of(poolSizes);
        return this;
    }

    PoolingLayerBuilder withPoolingType(final PoolingType poolingType) {
        this.poolingType = Optional.of(poolingType);
        return this;
    }

    PoolingLayerBuilder withInputDimensions(final int... inputDimensions) {
        this.inputDimensions = Optional.of(inputDimensions);
        return this;
    }

    /**
     * @see {@link LayerBuilder#build}
     */
    @Override
    public PoolingLayer build() {
        if (inputDimensions.isPresent()) {
            return new PoolingLayer(poolSizes.orElse(inputDimensions.get()), // Default to the input dimensions resulting in no pooling
                    poolingType.orElse(PoolingType.MAX), // default to max pooling
                    inputDimensions.get(), // must be present to get into this if
                    poolingLayerService); // the service dependency of the Pooling Layer
        } else {
            throw new IllegalStateException("The pooling factors must be set before a pooling layer can be built.");
        }
    }
}
