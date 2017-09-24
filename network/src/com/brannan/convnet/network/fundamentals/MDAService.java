package com.brannan.convnet.network.fundamentals;

import java.util.Arrays;
import java.util.List;
import java.util.OptionalInt;
import java.util.stream.IntStream;

/**
 *
 * @author Brannan R. Hancock
 *
 */
public class MDAService {

    /**
     * @param mda
     * @param position
     * @return the element in the Multi Dimensional Array at the specified
     *         position
     */
    public static double get(final MDA mda, final int... position) {
            validatePosition(mda.getDimensions(), position);
        return mda.getElements()[getLocationForPosition(mda.getIncrements(), position)];
    }

    public static double get(final MDA mda, final List<Integer> position) {
        return get(mda, HelperLibrary.listAsArray(position));
    }


    /**
     * This method casts the position to the location in the array
     * @param position
     * @return
     */
    static int getLocationForPosition(final int[] increments, final int[] position) {
        int location = 0;
        for (int i = 0; i < position.length; i++) {
            location += increments[i] * position[i];
        }
        return location;
    }


    /**
     * Validates the positions passed to the helper against the dimensions of
     * the Multi Dimensional Array
     * @param mda
     * @param position
     */
    static void validatePosition(final int[] dimensions, final int[] position) {

        if (position.length != dimensions.length) {
            throw new IllegalArgumentException("The given Multi Dimensional Array has [" + dimensions.length
                    + "] dimensions, however [" + position.length + "] were specified, these must match.");
        }

        OptionalInt firstError = Arrays.stream(position).filter(w -> w < 0).findAny();
        if (firstError.isPresent()) {
            throw new IllegalArgumentException("Position[" + firstError.getAsInt() + "] was ["
                    + position[firstError.getAsInt()] + "], however it must be positive.");
        }

        firstError = IntStream.range(0, position.length).filter(w -> position[w] >= dimensions[w])
                .findFirst();
        if (firstError.isPresent()) {
            throw new IndexOutOfBoundsException("Dimension[" + firstError.getAsInt() + "] has size ["
                    + dimensions[firstError.getAsInt()] + "], [" + position[firstError.getAsInt()]
                    + "] was specified.");
        }
    }



}
