package com.brannanhancock.convnet.network.layers;


public interface LayerBuilder <T extends Layer> {

    /**
     * This method should return an instance of the Layer this builder is
     * responsible for building. The result of this should be based on other
     * parameters passed into the builder via a fluent API.
     *
     * @return
     */
    T build();
}
