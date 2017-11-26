package com.brannanhancock.convnet.fundamentals;

import com.google.common.collect.Lists;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TestHelperLibrary {

    @Test
    public void test() {
        // Given
        final int[] array1 = {0, 1, 2};
        final int[] array2 = {1, 1, 2};

        // When
        final boolean result = HelperLibrary.isArrayEquality(array1, array2);

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


    @Test(expected = UnsupportedOperationException.class)
    public void testConstructorThrowsError() {
        // Given - nothing

        // When
        new HelperLibrary();

        // Then - expect exception
    }

    @Test
    public void testMatchingInputsReturnTrue() {
        // Given
        final int[] array1 = {28, 28};
        final int[] array2 = {28, 28};

        // When
        final boolean result = HelperLibrary.isArrayEquality(array2, array1);

        // Then
        assertTrue("These two inputs are equal, so should return true", result);
    }

    @Test
    public void testInputsOfDifferentLengthsReturnFalse() {
        // Given
        final int[] array1 = {28, 28, 1};
        final int[] array2 = {28, 28};

        // When
        final boolean result = HelperLibrary.isArrayEquality(array2, array1);

        // Then
        assertFalse("These two inputs are equal, so should return true", result);
    }

    @Test
    public void testDifferentInputsWithSameLengthsReturnFalse() {
        // Given
        final int[] array1 = {28, 27};
        final int[] array2 = {28, 28};

        // When
        final boolean result = HelperLibrary.isArrayEquality(array2, array1);

        // Then
        assertFalse("These two inputs are equal, so should return true", result);
    }

}
