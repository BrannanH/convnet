package com.brannan.convnet.network.fundamentals;

import java.util.List;
import java.util.OptionalInt;
import java.util.stream.IntStream;

/**
 * 
 * @author Brannan R. Hancock
 *
 */
public class MDAHelper {

    /**
     * @param mda
     * @param position
     * @return the element in the Multi Dimensional Array at the specified
     *         position
     */
    public static double get(final MDA mda, final int... position) {
        return get(mda, HelperLibrary.arrayAsList(position));
    }


    /**
     * @param mda
     * @param position
     * @return the element in the Multi Dimensional Array at the specified
     *         position
     */
    public static double get(final MDA mda, final List<Integer> position) {
        try {
            validatePosition(mda, position);
        } catch (Exception e) {
            throw e;
        }
        return mda.getElements()[getLocationForPosition(mda.getIncrements(), position)];
    }


    /**
     * Used to populate the Multi Dimensional Array
     * @param element
     * @param position
     */
    public static void put(final MDA mda, final double element, final int... position) {
        put(mda, element, HelperLibrary.arrayAsList(position));
    }


    /**
     * Used to populate the Multi Dimensional Array
     * @param element
     * @param position
     */
    public static void put(final MDA mda, final double element, final List<Integer> position) {
        validatePosition(mda, position);
        mda.getElements()[getLocationForPosition(mda.getIncrements(), position)] = element;
    }
    
    /**
     * Defers to the addTo method using a list representation of the supplied position.
     * @param mda
     * @param addition
     * @param position
     */
    public static void addTo(final MDA mda, final double addition, final int...position) {
        addTo(mda, addition, HelperLibrary.arrayAsList(position));
    }


    /**
     * Works as an accumulator, adding the amount specified in addition to the given position in the specified MDA.
     * @param mda
     * @param addition
     * @param position
     */
    public static void addTo(final MDA mda, final double addition, final List<Integer> position) {
        put(mda, get(mda, position) + addition, position);
    }


    /**
     * This method casts the position to the location in the array
     * @param position
     * @return
     */
    private static int getLocationForPosition(final int[] increments, final List<Integer> position) {
        int location = 0;
        for (int i = 0; i < position.size(); i++) {
            location += increments[i] * position.get(i);
        }
        return location;
    }


    /**
     * Validates the positions passed to the helper against the dimensions of
     * the Multi Dimensional Array
     * @param mda
     * @param position
     */
    private static void validatePosition(final MDA mda, final List<Integer> position) {

        if (position.size() != mda.getDimensions().length) {
            throw new IllegalArgumentException("The given Multi Dimensional Array has [" + mda.getDimensions().length
                    + "] dimensions, however [" + position.size() + "] were specified, these must match.");
        }

        OptionalInt firstError = position.stream().mapToInt(Integer::intValue).filter(w -> w < 0).findFirst();
        if (firstError.isPresent()) {
            throw new IllegalArgumentException("Position[" + firstError.getAsInt() + "] was ["
                    + position.get(firstError.getAsInt()) + "], however it must be positive.");
        }

        firstError = IntStream.range(0, position.size()).filter(w -> position.get(w) >= mda.getDimensions()[w])
                .findFirst();
        if (firstError.isPresent()) {
            throw new IndexOutOfBoundsException("Dimension[" + firstError.getAsInt() + "] has size ["
                    + mda.getDimensions()[firstError.getAsInt()] + "], [" + position.get(firstError.getAsInt())
                    + "] was specified.");
        }
    }
}
