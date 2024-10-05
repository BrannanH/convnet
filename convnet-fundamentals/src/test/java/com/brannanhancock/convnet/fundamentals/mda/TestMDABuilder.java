package com.brannanhancock.convnet.fundamentals.mda;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TestMDABuilder {

    private int[] dimensions = {1, 2};

    @Before
    public void setUp() {

    }

    @Test
    public void testBuild() {
        // Given
        final int[] inputDimensions = { 2, 3, 4 };

        final MDABuilder builder = new MDABuilder(inputDimensions);

        // When
        final MDA mDA = builder.build();

        // Then
        assertArrayEquals("Dimensions should be as expected", inputDimensions, mDA.getDimensions());
    }


    /**
     * This test verifies that if the wrong number of dimensions are specified
     * for a put in an MDA, an illegal argument exception is thrown.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testPutMisMatchNumberOfDimensions() {
        // Given
        final int[] badDimensions = { 1, 2, 3 };
        final MDABuilder mda = new MDABuilder(dimensions);

        // When
        mda.withDataPoint(3D, badDimensions);

        // Then
    }


    /**
     * This test verifies that if too large of an index is specified on a put in
     * a MDA, then an illegal argument exception is thrown.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testPutOutOfBoundsIndexInsertion() {
        // Given
        final int[] badDimensions = { 1, 1 };
        final MDABuilder mda = new MDABuilder(dimensions);

        // When
        mda.withDataPoint(3D, badDimensions);

        // Then
    }


    /**
     * This test verifies that if a negative position is given for a put in a
     * MDA, then an index out of bounds exception is thrown.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testPutNegativePositionRejected() {
        // Given
        final int[] badDimensions = { -1, 1 };
        final MDABuilder mda = new MDABuilder(dimensions);

        // When
        mda.withDataPoint(3D, badDimensions);

        // Then
    }


    /**
     * This test verifies that if an element in the MDA is to be replaced, the
     * new element persists.
     */
    @Test
    public void replace() {
        // Given
        final int[] position = { 0, 0 };
        final double wrongElement = 1D;
        final double correctElement = 2D;
        final MDABuilder mdaBuilder = new MDABuilder(dimensions);

        // When
        mdaBuilder.withDataPoint(wrongElement, position);
        mdaBuilder.withDataPoint(correctElement, position);
        final MDA mda = mdaBuilder.build();

        assertEquals("The second specified element should be returned", correctElement, mda.get(position), 0);
    }


    /**
     * This test verifies that a small 2x2 MDA can be fully populated, and have
     * the data retrieved from it.
     */
    @Test
    public void smallArrayPopulated() {
        // Given
        final MDABuilder mdaBuilder = new MDABuilder(2, 2);

        // When
        mdaBuilder.withDataPoint(0D, 0, 0);
        mdaBuilder.withDataPoint(1D, 0, 1);
        mdaBuilder.withDataPoint(2D, 1, 0);
        mdaBuilder.withDataPoint(3D, 1, 1);
        final MDA mda = mdaBuilder.build();

        // Then
        assertEquals("Position 1", 0D, mda.get(0, 0), 0);
        assertEquals("Position 2", 1D, mda.get(0, 1), 0);
        assertEquals("Position 3", 2D, mda.get(1, 0), 0);
        assertEquals("Position 4", 3D, mda.get(1, 1), 0);
    }


    /**
     * This test verifies that a large MDA can be fully populated, and still
     * have each unique element retrieved.
     */
    @Test
    public void testBigArrayFullyPopulated() {
        // Given
        final int[] bigDimensions = { 28, 28, 3, 10 };
        final int[] position = new int[bigDimensions.length];
        final MDABuilder mdaBuilder = new MDABuilder(bigDimensions);
        double element = 0D;

        // When
        for (int row = 0; row < bigDimensions[0]; row++) {
            for (int column = 0; column < bigDimensions[1]; column++) {
                for (int channel = 0; channel < bigDimensions[2]; channel++) {
                    for (int image = 0; image < bigDimensions[3]; image++) {
                        position[0] = row;
                        position[1] = column;
                        position[2] = channel;
                        position[3] = image;
                        mdaBuilder.withDataPoint(element, position);
                        element++;
                    }
                }
            }
        }
        element = 0D;

        final MDA mda = mdaBuilder.build();

        // Then
        for (int row = 0; row < bigDimensions[0]; row++) {
            for (int column = 0; column < bigDimensions[1]; column++) {
                for (int channel = 0; channel < bigDimensions[2]; channel++) {
                    for (int image = 0; image < bigDimensions[3]; image++) {
                        position[0] = row;
                        position[1] = column;
                        position[2] = channel;
                        position[3] = image;
                        assertEquals("Value not correctly returned for " + row + column + channel + image, element,
                                mda.get(position), 0);
                        element++;
                    }
                }
            }
        }
    }


    @Test
    public void testBuildPerservesData() {
        // Given
        final double input = 5;
        final MDABuilder builder = new MDABuilder(2);

        builder.withDataPoint(input, 0);
        final MDA mda = builder.build();

        // When
        final double result = mda.get(0);
        assertEquals("result should be equal to input", input, result, 0);
    }

    @Test
    public void testAddToAccumulates() {
        // Given
        final List<Integer> position = List.of(0);
        final double input = 5;
        final double addition = 6;
        final MDABuilder builder = new MDABuilder(2);

        builder.withDataPoint(input, 0);
        builder.withAmountAddedToDataPoint(addition, 0);
        final MDA mda = builder.build();

        // When
        final double result = mda.get(position);
        assertEquals("result should be equal to input plus addition", input+addition, result, 0);
    }
}
