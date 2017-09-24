package com.brannan.convnet.network.fundamentals;

public class MDABuilder {

    private final int[] increments;
    private final int[] dimensions;
    private final double[] elements;
    private final MDAService mdaService = new MDAService();

    /**
     * @return
     */
    public final MDA build() {
        return new MDA(dimensions, increments, elements, mdaService);
    }


    /**
     * @param dimensions
     * @return
     */
    public MDABuilder(final int... dimensions) {
        this.dimensions = dimensions;

        this.increments = new int[dimensions.length];
        increments[0] = 1;
        for (int i = 1; i < increments.length; i++) {
            increments[i] = increments[i - 1] * dimensions[i - 1];
        }
        this.elements = new double[dimensions[dimensions.length - 1] * increments[dimensions.length - 1]];
    }


    public MDABuilder withDataPoint(final double element, final int... position) {
        mdaService.validatePosition(dimensions, position);
        elements[mdaService.getLocationForPosition(increments, position)] = element;
        return this;
    }

    public MDABuilder withAmountAddedToDataPoint(final double addition, final int... position) {
        mdaService.validatePosition(dimensions, position);
        elements[mdaService.getLocationForPosition(increments, position)] += addition;
        return this;
    }
}
