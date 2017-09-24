package com.brannan.convnet.network.fundamentals;

public class MDABuilder {

    private int[] dimensions;
    private int[] increments;
    private double[] elements;


    /**
     * @return
     */
    public final MDA build() {
        final MDA mda = new MDA(dimensions, elements, increments);
        return mda;
    }


    /**
     * @param dimensions
     * @return
     */
    public MDABuilder withDimensions(final int... dimensions) {
        this.dimensions = dimensions;

        this.increments = new int[dimensions.length];
        increments[0] = 1;
        for (int i = 1; i < increments.length; i++) {
            increments[i] = increments[i - 1] * dimensions[i - 1];
        }
        this.elements = new double[dimensions[dimensions.length - 1] * increments[dimensions.length - 1]];
        return this;
    }


    public MDABuilder withDataPoint(final double element, final int... position) {
        MDAService.validatePosition(dimensions, position);
        elements[MDAService.getLocationForPosition(increments, position)] = element;
        return this;
    }

    public MDABuilder withAmountAddedToDataPoint(final double addition, final int[] position) {
        MDAService.validatePosition(dimensions, position);
        elements[MDAService.getLocationForPosition(increments, position)] += addition;
        return this;
    }
}
