package com.brannanhancock.convnet.network.fundamentals;

import java.util.List;

/**
 * A begrudgingly smart model for storing mutlidimensional data in an array.
 * @author Brannan
 *
 */
public class MDA {

    private final int[] dimensions;
    private final int[] increments;
    private final double[] elements;
    private final MDAService mdaService;


    MDA(final int[] dimensions, final int[] increments, final double[] elements, final MDAService mdaService) {
        this.dimensions = dimensions;
        this.increments = increments;
        this.elements = elements;
        this.mdaService = mdaService;
    }


    /**
     * @return the dimensions
     */
    public int[] getDimensions() {
        return dimensions.clone();
    }


    public double get(final int... position) {
        mdaService.validatePosition(dimensions, position);
        return elements[mdaService.getLocationForPosition(increments, position)];
    }


    public double get(final List<Integer> position) {
        return get(position.stream().mapToInt(Integer::intValue).toArray());
    }
}
