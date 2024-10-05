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
public class ConvolutionLayer extends Layer {

    private final ConvolutionService convolutionService;
    private final MDA feature;
    private final int[] inputDimensions;
    private final int[][] connections;
    private final PaddingType paddingType;

    private Optional<int[]> outputDimensions = Optional.empty();

    ConvolutionLayer(final MDA feature,
                     final int[] inputDimensions,
                     final int[][] connections,
                     final PaddingType paddingType,
                     final ConvolutionService convolutionService) {
        this.convolutionService = convolutionService;
        this.feature = feature;
        this.inputDimensions = inputDimensions;
        this.connections = connections;
        this.paddingType = paddingType;
    }

    @Override
    public MDA forward(final MDA operand) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public MDA forwardNoTrain(final MDA operand) {
        if(!Arrays.equals(operand.getDimensions(), inputDimensions)) {
            throw new IllegalArgumentException("Operand passed to Convolution Layer did not have legal dimensions");
        }

        if(!convolutionService.verifyInputBiggerThanFeature(operand.getDimensions(), feature.getDimensions())) {
            throw new IllegalArgumentException("Operand passed to Convolution Layer is smaller than this Layer's feature");
        }

        final MDABuilder resultBuilder = new MDABuilder(outputDimensions());
        // TODO Auto-generated method stub
        return resultBuilder.build();
    }

    @Override
    public MDA reverse(final MDA dLossByDOut) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int[] outputDimensions() {
        if(outputDimensions.isPresent()) {
            return outputDimensions.get();
        }
        outputDimensions = Optional.of(paddingType.getOutputDimensionsFunction().apply(inputDimensions, feature.getDimensions()));
        return outputDimensions.get();
    }

}
