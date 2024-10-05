package com.brannanhancock.convnet.fundamentals.layers;

import com.brannanhancock.convnet.fundamentals.mda.MDA;

public class ReversePassOutput {

    private final Layer layer;
    private final MDA dLossByDFeature;
    private final MDA dLossByDIn;

    public ReversePassOutput(Layer layer, MDA dLossByDFeature, MDA dLossByDIn) {

        this.layer = layer;
        this.dLossByDFeature = dLossByDFeature;
        this.dLossByDIn = dLossByDIn;
    }

    public Layer getLayer() {
        return layer;
    }

    public MDA getDLossByDFeature() {
        return dLossByDFeature;
    }

    public MDA getDLossByDIn() {
        return dLossByDIn;
    }
}
