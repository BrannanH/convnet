package com.brannan.convnet.network.fundamentals;

import java.util.List;

/**
 * @author Brannan
 *
 */
public class MDA {

    private List<Integer> dimensions;
    private double[] elements;
    private int[] increments;


    /**
     * @return the dimensions
     */
    public final List<Integer> getDimensions() {
        return dimensions;
    }


    /**
     * @param dimensions
     *            the dimensions to set
     */
    final void setDimensions(final List<Integer> dimensions) {
        this.dimensions = dimensions;
    }


    /**
     * @return the elements
     */
    final double[] getElements() {
        return elements;
    }


    /**
     * @param elements
     *            the elements to set
     */
    final void setElements(final double[] elements) {
        this.elements = elements;
    }


    /**
     * @return the increments
     */
    final int[] getIncrements() {
        return increments;
    }


    /**
     * @param increments
     *            the increments to set
     */
    final void setIncrements(final int[] increments) {
        this.increments = increments;
    }
}