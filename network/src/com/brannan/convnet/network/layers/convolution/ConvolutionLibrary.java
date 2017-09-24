package com.brannan.convnet.network.layers.convolution;

import java.util.function.BiFunction;

public class ConvolutionLibrary {

    /**
     * This function calculates the output dimensions for a convolution which ignores edge effects.
     */
    private static final BiFunction<int[], int[], int[]> IGNORE_PADDING_OPEARATION_DIMENSIONS = (final int[] operandDimensions, final int[] filterDimensions) -> {
        final int[] result = new int[operandDimensions.length];
        for (int i = 0; i < operandDimensions.length; i++) {
            if (operandDimensions[i] == filterDimensions[i]) {
                result[i] = operandDimensions[i];
            } else {
                result[i] = operandDimensions[i] - filterDimensions[i] + 1;
            }
        }
        return result;
    };


    /**
     * This function returns the dimensions of the input as associated padding types allow for edge effects.
     */
    private static final BiFunction<int[], int[], int[]> DONT_IGNORE_PADDING_OPEARATION_DIMENSIONS = (final int[] a, final int[] b) -> a;



    /**
     *
     * @author Brannan R. Hancock
     *
     */
    public enum PaddingType {

        IGNORE(IGNORE_PADDING_OPEARATION_DIMENSIONS),

        ZERO(DONT_IGNORE_PADDING_OPEARATION_DIMENSIONS),

        REFLECTION(DONT_IGNORE_PADDING_OPEARATION_DIMENSIONS
                );

        private final BiFunction<int[], int[], int[]> outputDimensionsFunction;

        PaddingType(final BiFunction<int[], int[], int[]> outputDimensionsFunction) {
            this.outputDimensionsFunction = outputDimensionsFunction;
        }

        public BiFunction<int[], int[], int[]> getOutputDimensionsFunction() {
            return this.outputDimensionsFunction;
        }
    }

}
