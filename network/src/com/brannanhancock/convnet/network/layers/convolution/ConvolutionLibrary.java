package com.brannanhancock.convnet.network.layers.convolution;

import java.util.function.BiFunction;

public class ConvolutionLibrary {

    /**
     *
     * @author Brannan R. Hancock
     *
     */
    public enum PaddingType {

        NO_PADDING(ConvolutionLibrary::dontPad),

        ZERO(ConvolutionLibrary::pad),

        REFLECTION(ConvolutionLibrary::pad);

        private final BiFunction<int[], int[], int[]> outputDimensionsFunction;

        PaddingType(final BiFunction<int[], int[], int[]> outputDimensionsFunction) {
            this.outputDimensionsFunction = outputDimensionsFunction;
        }

        public BiFunction<int[], int[], int[]> getOutputDimensionsFunction() {
            return this.outputDimensionsFunction;
        }
    }


    /**
     * This function calculates the output dimensions for a convolution which
     * ignores edge effects.
     */
    private static final int[] dontPad(final int[] operandDimensions, final int[] filterDimensions) {
        final int[] result = new int[operandDimensions.length];
        for (int i = 0; i < operandDimensions.length; i++) {
            if (operandDimensions[i] == filterDimensions[i]) {
                result[i] = operandDimensions[i];
            } else {
                result[i] = operandDimensions[i] - filterDimensions[i] + 1;
            }
        }
        return result;
    }


    /**
     * This function returns the dimensions of the input as associated padding types allow for edge effects.
     */
    private static final int[] pad(final int[] operandDimensions, @SuppressWarnings("unused") final int[] filterDimensions) {
        return operandDimensions;
    }
}
