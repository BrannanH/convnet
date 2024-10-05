package com.brannanhancock.convnet.fundamentals.layers.convolution;

import com.brannanhancock.convnet.fundamentals.layers.Layer;
import com.brannanhancock.convnet.fundamentals.layers.convolution.ConvolutionLibrary.PaddingType;
import com.brannanhancock.convnet.fundamentals.mda.MDA;
import com.brannanhancock.convnet.fundamentals.mda.MDABuilder;

import java.util.Arrays;
import java.util.Optional;

/**
 *
 * @author Brannan R. Hancock
 *
 */
public class ConvolutionLayer implements Layer {

    private final MDA feature;
    private final int[] inputDimensions;
    private final int[][] connections;
    private final PaddingType paddingType;

    ConvolutionLayer(final MDA feature,
                     final int[] inputDimensions,
                     final int[][] connections,
                     final PaddingType paddingType) {
        this.feature = feature;
        this.inputDimensions = inputDimensions;
        this.connections = connections;
        this.paddingType = paddingType;
    }

    @Override
    public int[] getInputDimensions() {
        return inputDimensions;
    }

    public MDA getFeature() {
        return feature;
    }

    public PaddingType getPaddingType() {
        return paddingType;
    }
}
