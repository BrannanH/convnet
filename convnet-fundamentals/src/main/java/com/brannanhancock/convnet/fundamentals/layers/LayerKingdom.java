package com.brannanhancock.convnet.fundamentals.layers;

import com.brannanhancock.convnet.fundamentals.layers.convolution.ConvolutionLayer;
import com.brannanhancock.convnet.fundamentals.layers.convolution.ConvolutionService;
import com.brannanhancock.convnet.fundamentals.layers.pooling.PoolingLayer;
import com.brannanhancock.convnet.fundamentals.layers.pooling.PoolingService;

import java.util.Map;

public class LayerKingdom {

    private static final LayerKingdom INSTANCE = new LayerKingdom();
    private final DimensionVerificationService dimensionVerificationService = new DimensionVerificationService();
    private final Map<Class<? extends Layer>, LayerService<? extends Layer>> layerServices = Map.of(
            ConvolutionLayer.class, new ConvolutionService(dimensionVerificationService),
            PoolingLayer.class, new PoolingService(dimensionVerificationService)
    );

    static LayerKingdom instance() {
        return INSTANCE;
    }

    @SuppressWarnings("unchecked")
    <T extends Layer> LayerService<T> getLayerServiceFor(Class<T> layer) {
        return (LayerService<T>) layerServices.getOrDefault(layer, layerServices.get(ConvolutionLayer.class));
    }

    static class LayerServiceTuple<T extends Layer> {
        Class<T> layerClass;
        LayerService<T> layerServiceInstance;
    }
}
