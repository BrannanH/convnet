package com.brannanhancock.convnet.fundamentals;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;

import java.util.List;

/**
 * @author Brannan
 *
 */
public final class HelperLibrary {

    HelperLibrary() {
        throw new UnsupportedOperationException("Utility Class should not be instantiated.");
    }

    /**
     * @param t - an array to create a list from the elements.
     * @return a list of Integers with the same values as the elements in the array preserving order
     */
    public static List<Integer> arrayAsList(final int[] t) {
        return stream(t).boxed().collect(toList());
    }


    public static int[] listAsArray(final List<Integer> position) {
        return position.stream().mapToInt(Integer::intValue).toArray();
    }


    /**
     * Compares the length and contents of arrays in element order for equality.
     *
     * @param array1 - the first array to compare
     * @param array2 - the array to compare the first array to
     * @return true if lengths are equal, and the contents of each array at each index is equal.
     */
    public static boolean isArrayEquality(final int[] array1, final int[] array2) {
        if (array1.length != array2.length) {
            return false;
        }
        for (int i = 0; i < array1.length; i++) {
            if (array1[i] != array2[i]) {
                return false;
            }
        }
        return true;
    }

    /**
     * Compares the length and contents of arrays in element order for equality.
     *
     * @param array1 - the first array to compare
     * @param array2 - the array to compare the first array to
     * @return true if lengths are equal, and the contents of each array at each index is equal.
     */
    public static boolean isArrayEquality(final double[] array1, final double[] array2) {
        if (array1.length != array2.length) {
            return false;
        }
        for (int i = 0; i < array1.length; i++) {
            if (Math.abs(array1[i] - array2[i]) > Double.MIN_NORMAL) {
                return false;
            }
        }
        return true;
    }
}
