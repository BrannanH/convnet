package com.brannan.convnet.network.fundamentals;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Brannan
 *
 */
public class HelperLibrary {

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
        final List<T> r = new ArrayList<>();

        r.addAll(t);
        return r;
    }

    @FunctionalInterface
    public interface IntBiFunction<T> {
        T apply(int i, int j);
    }

    public static int[] listAsArray(final List<Integer> position) {
        return position.stream().mapToInt(Integer::intValue).toArray();
    }
}
