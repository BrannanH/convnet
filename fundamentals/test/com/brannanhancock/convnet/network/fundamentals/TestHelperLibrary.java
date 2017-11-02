package com.brannanhancock.convnet.network.fundamentals;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

import com.brannanhancock.convnet.network.fundamentals.HelperLibrary;
import com.google.common.collect.Lists;

public class TestHelperLibrary {

    @Test
    public void test() {
        // Given
        final int[] array1 = {0, 1, 2};
        final int[] array2 = {1, 1, 2};

        // When
        final boolean result = HelperLibrary.arrayEquality(array1, array2);

        // Then
        assertFalse("Arrays are not equal", result);
    }


    @Test
    public void test2() {
        // Given
        final int[] array = {1,2};

        // When
        final List<Integer> list = HelperLibrary.arrayAsList(array);

        // Then
        assertTrue(list.contains(1));
        assertTrue(list.contains(2));
    }


    @Test
    public void test3() {
        // Given
        final List<Integer> position = Lists.newArrayList(5,4);

        // When
        final int[] result = HelperLibrary.listAsArray(position);

        // Then
        assertTrue(result[0] == 5);
        assertTrue(result[1] == 4);
    }

}
