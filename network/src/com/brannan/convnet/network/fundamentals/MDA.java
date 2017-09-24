package com.brannan.convnet.network.fundamentals;

/**
 * @author Brannan
 *
 */
public class MDA {

    private final int[] dimensions;
    private final double[] elements;
    private final int[] increments;

    MDA(final int[] dimensions, final double[] elements, final int[] increments) {
        this.dimensions = dimensions;
        this.elements = elements;
        this.increments = increments;
    }

    /**
     * @return the dimensions
     */
    public int[] getDimensions() {
        return dimensions.clone();
    }


    /**
     * To be used only by {@link MDAService}
     * @return the elements
     */
    double[] getElements() {
        return elements;
    }


    /**
     * To be used only by {@link MDAService}
     * @return the increments
     */
    int[] getIncrements() {
        return increments;
    }
}
