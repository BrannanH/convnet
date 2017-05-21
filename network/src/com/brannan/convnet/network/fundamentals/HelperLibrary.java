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


    /**
     * @param t
     * @return
     */
    public static <T> List<T> cloneList(final List<T> t) {
        List<T> r = new ArrayList<>();
        
        r.addAll(t);
        return r;
    }
    
    @FunctionalInterface
    public interface IntBiFunction<T> {
        T apply(int i, int j);
    }
}
