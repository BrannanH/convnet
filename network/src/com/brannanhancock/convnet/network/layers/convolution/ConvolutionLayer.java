package com.brannanhancock.convnet.network.layers.convolution;

import java.util.List;
import java.util.Map;

import com.brannanhancock.convnet.fundamentals.MDA;
import com.brannanhancock.convnet.fundamentals.MDABuilder;
import com.brannanhancock.convnet.network.layers.ForwardOutputTuple;
import com.brannanhancock.convnet.network.layers.convolution.ConvolutionLibrary.PaddingType;
import com.brannanhancock.convnet.network.services.DimensionVerificationService;

/**
 *
 * @author Brannan R. Hancock
 *
 */
public class ConvolutionLayer {

    private final DimensionVerificationService dimensionVerificationService;

    public ConvolutionLayer(final DimensionVerificationService dimensionVerificationService) {
        this.dimensionVerificationService = dimensionVerificationService;
    }

    public ForwardOutputTuple forward(final MDA operand, final MDA feature, final PaddingType paddingType) {
        // TODO Auto-generated method stub
        return null;
    }

    public MDA forwardNoTrain(final MDA operand, final MDA feature, final PaddingType paddingType) {
        dimensionVerificationService.verifyLeftBiggerThanRight(operand.getDimensions(), feature.getDimensions());
        return new MDABuilder(outputDimensions(operand.getDimensions(), feature.getDimensions(), paddingType)).build();
    }

    public MDA reverse(final MDA dLossByDOut, final Map<List<Integer>, Map<List<Integer>, Double>> dOutByDIn,
            final Map<List<Integer>, Map<List<Integer>, Double>> dOutByDFeature) {
        // TODO Auto-generated method stub
        return null;
    }

    public int[] outputDimensions(final int[] inputDimensions, final int[] filterDimensions, final PaddingType paddingType) {
        return paddingType.getOutputDimensionsFunction().apply(inputDimensions, filterDimensions);
    }

}
