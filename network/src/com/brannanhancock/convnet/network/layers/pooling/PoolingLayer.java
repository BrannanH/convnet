package com.brannanhancock.convnet.network.layers.pooling;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.brannanhancock.convnet.fundamentals.MDA;
import com.brannanhancock.convnet.fundamentals.MDABuilder;
import com.brannanhancock.convnet.network.layers.ForwardOutputTuple;
import com.brannanhancock.convnet.network.layers.Layer;
import com.brannanhancock.convnet.network.layers.pooling.PoolingLibrary.PoolingType;

public final class PoolingLayer implements Layer {

    private final int[] poolSizes;
    private final PoolingType poolingType;
    private final PoolingService poolingLayerService;
    private final int[] inputDimensions;
    private Optional<Map<List<Integer>, Map<List<Integer>, Double>>> dOutByDIn = Optional.empty();

    PoolingLayer(final int[] poolSizes, final PoolingType poolingType, final int[] inputDimensions, final PoolingService poolingLayerService) {
        this.poolSizes = poolSizes;
        this.poolingType = poolingType;
        this.poolingLayerService = poolingLayerService;
        this.inputDimensions = inputDimensions;
    }

    /**
     * @see {@link Layer#forward}
     */
    @Override
    public MDA forward(final MDA operand) {
        final ForwardOutputTuple forwardOutputTuple = poolingLayerService.forward(operand, poolSizes, poolingType);
        this.dOutByDIn = Optional.of(forwardOutputTuple.getdOutByDIn());
        return forwardOutputTuple.getOutput();
    }

    /**
     * @see {@link Layer#forwardNoTrain}
     */
    @Override
    public MDA forwardNoTrain(final MDA operand) {
        return poolingLayerService.forwardNoTrain(operand, poolSizes, poolingType);
    }


    /**
     * @see {@link Layer#reverse}
     */
    @Override
    public MDA reverse(final MDA dLossByDOut) {
        if (dOutByDIn.isPresent()) {
            return poolingLayerService.reverse(dLossByDOut, dOutByDIn.get(), inputDimensions);
        } else {
            return new MDABuilder(inputDimensions).build();
        }
    }


    /**
     * @see {@link Layer#outputDimensions}
     */
    @Override
    public int[] outputDimensions() {
        return poolingLayerService.outputDimensions(inputDimensions, poolSizes);
    }
}
