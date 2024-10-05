package com.brannanhancock.convnet.fundamentals.mda;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertFalse;

public class TestMDAEquality {

    @Test
    public void testEqualDimensionsAndElements() {
        // Given
        int[] dimensions = {3,4,5};
        double[] elements = {1.1,2.2,3.3};
        MDA mda = new MDA(dimensions, new int[10], elements, new MDAService());
        MDA comparison = new MDA(dimensions.clone(), new int[10], elements.clone(), new MDAService());

        // When
        boolean result = mda.equals(comparison);

        // Then
        Assert.assertTrue("Result should be true", result);
    }


    @Test
    public void testUnEqualDimensions() {
        // Given
        int[] dimensions = {3,4,5};
        int[] dimensionsToCompare = {3,4};
        double[] elements = {1.1,2.2,3.3};
        MDA mda = new MDA(dimensions, new int[10], elements, new MDAService());
        MDA comparison = new MDA(dimensionsToCompare, new int[10], elements, new MDAService());

        // When
        boolean result = mda.equals(comparison);

        // Then
        assertFalse("Result should be false: dimensions are different", result);
    }


    @Test
    public void testUnEqualElements() {
        // Given
        int[] dimensions = {3,4,5};
        double[] elements = {1.1,2.2,3.3};
        double[] elementsToCompare = {1.1, 2.2};
        MDA mda = new MDA(dimensions, new int[10], elements, new MDAService());
        MDA comparison = new MDA(dimensions.clone(), new int[10], elementsToCompare, new MDAService());

        // When
        boolean result = mda.equals(comparison);

        // Then
        assertFalse("Result should be false: elements are different", result);
    }

    @Test
    public void testWrongObject() {
        // Given
        Object other = new Object();
        int[] dimensions = {3,4,5};
        double[] elements = {1.1,2.2,3.3};
        MDA mda = new MDA(dimensions, new int[10], elements, new MDAService());

        // When
        boolean result = mda.equals(other);

        // Then
        assertFalse(result);
    }
}
