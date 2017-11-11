package com.brannanhancock.convnet.fundamentals;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Brannan
 *
 */
public final class HelperLibrary {

    HelperLibrary() {
        throw new UnsupportedOperationException("Utility Class should not be instantiated.");
    }

    /**
     * @param t
     * @return
     */
    public static List<Integer> arrayAsList(final int[] t) {
        return stream(t).boxed().collect(toList());
    }

    public static boolean arrayEquality(final int[] array1, final int[] array2) {
        for (int i = 0; i < array1.length; i++) {
            if (array1[i] != array2[i]) {
                return false;
            }
        }
        return true;
    }


    /**
     * @param t
     * @return
     */
    public static <T> List<T> cloneList(final List<T> t) {
        return t.stream().collect(Collectors.toList());
    }


    public static int[] listAsArray(final List<Integer> position) {
        return position.stream().mapToInt(Integer::intValue).toArray();
    }
}
