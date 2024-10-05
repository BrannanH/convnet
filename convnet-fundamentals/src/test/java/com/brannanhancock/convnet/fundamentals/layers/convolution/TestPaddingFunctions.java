package com.brannanhancock.convnet.fundamentals.layers.convolution;

import static org.junit.Assert.assertArrayEquals;

import org.junit.Test;

import java.util.Arrays;

public class TestPaddingFunctions {

    /**
     * Verifies the output dimensions are correct.
     */
    @Test
    public void testNoPaddingOutputSize() {
        // Given
        final ConvolutionLibrary.PaddingType type = ConvolutionLibrary.PaddingType.ZERO;
        final int[] operandDimensions = {1, 7};
        final int[] filterDimensions = {1, 3};

        // When
        final int[] outputDimensions = type.getOutputDimensionsFunction().apply(operandDimensions, filterDimensions);

        // Then
        assertArrayEquals("Output dimensions should be correct", operandDimensions, outputDimensions);
    }


    /**
     * Verifies the output dimensions are correct.
     */
    @Test
    public void testPaddingOutputSize() {
        // Given
        final ConvolutionLibrary.PaddingType type = ConvolutionLibrary.PaddingType.NO_PADDING;
        final int[] operandDimensions = {1, 7};
        final int[] filterDimensions = {1, 3};
        final int[] expectedOutputDimensions = {1, 5};

        // When
        final int[] outputDimensions = type.getOutputDimensionsFunction().apply(operandDimensions, filterDimensions);

        // Then
        assertArrayEquals("Output dimensions should be correct", expectedOutputDimensions, outputDimensions);
    }
}
