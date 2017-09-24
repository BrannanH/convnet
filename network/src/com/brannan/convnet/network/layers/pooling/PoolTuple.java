package com.brannan.convnet.network.layers.pooling;

import static com.brannan.convnet.network.fundamentals.HelperLibrary.arrayAsList;

/**
 * A class to associate a value with its origin in the un-pooled MDA.
 *
 * @author Brannan R. Hancock
 *
 */
class PoolTuple {

    private final double element;
    private final int[] origin;

    PoolTuple(final double element, final int[] origin) {
        this.element = element;
        this.origin = origin.clone();
    }

    /**
     * @return element
     */
    public double getElement() {
        return element;
    }


    /**
     * @return origin
     */
    public int[] getOrigin() {
        return origin.clone();
    }


    /**
     * For debugging purposes
     */
    @Override
    public String toString() {
        return arrayAsList(origin).toString() + element;
    }
}
