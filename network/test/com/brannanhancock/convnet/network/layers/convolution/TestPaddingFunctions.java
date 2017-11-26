package com.brannanhancock.convnet.network.layers.convolution;

import static com.brannanhancock.convnet.fundamentals.HelperLibrary.isArrayEquality;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.brannanhancock.convnet.network.layers.convolution.ConvolutionLibrary.PaddingType;

public class TestPaddingFunctions {

    /**
     * Verifies the output dimensions are correct.
     */
    @Test
    public void testNoPaddingOutputSize() {
        // Given
        final PaddingType type = PaddingType.ZERO;
        final int[] operandDimensions = {1, 7};
        final int[] filterDimensions = {1, 3};

        // When
        final int[] outputDimensions = type.getOutputDimensionsFunction().apply(operandDimensions, filterDimensions);

        // Then
        assertTrue("Output dimensions should be correct", isArrayEquality(operandDimensions, outputDimensions));
    }


    /**
     * Verifies the output dimensions are correct.
     */
    @Test
    public void testPaddingOutputSize() {
        // Given
        final PaddingType type = PaddingType.NO_PADDING;
        final int[] operandDimensions = {1, 7};
        final int[] filterDimensions = {1, 3};
        final int[] expectedOutputDimensions = {1, 5};

        // When
        final int[] outputDimensions = type.getOutputDimensionsFunction().apply(operandDimensions, filterDimensions);

        // Then
        assertTrue("Output dimensions should be correct", isArrayEquality(expectedOutputDimensions, outputDimensions));
    }
}
