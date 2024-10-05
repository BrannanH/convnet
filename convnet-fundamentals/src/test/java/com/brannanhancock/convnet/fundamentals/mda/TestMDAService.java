package com.brannanhancock.convnet.fundamentals.mda;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

public class TestMDAService {

    int[] dimensions = { 1, 2 };

    MDA mda;


    @Before
    public void setUp() {
        mda = new MDABuilder(dimensions).build();
    }


    /**
     * This test verifies that if the wrong number of dimensions are specified
     * for a get on the MDA, an illegal argument exception is thrown.
     */
    @Test
    public void testGetMisMatchNumberOfDimensions() {
        // Given
        final int[] badDimensions = { 1, 2, 3 };

        // When
        try {
            mda.get(badDimensions);
            // Then
            fail();
        } catch (final IllegalArgumentException e) {
            assertEquals("Error message should be as expected", "The given Multi Dimensional Array has [2] dimensions, however [3] were specified, these must match.", e.getMessage());
        }

    }


    /**
     * This test verifies that if too large of an index is specified on a get on
     * a MDA, then an illegal argument exception is thrown.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetOutOfBoundsIndexInsertion() {
        // Given
        final int[] badDimensions = { 1, 1 };

        // When
        mda.get(badDimensions);

        // Then
    }


    /**
     * This test verifies that if a negative position is given for a get in a
     * MDA, then an index out of bounds exception is thrown.
     */
    @Test
    public void testGetNegativePositionRejected() {
        // Given
        final int[] badDimensions = { -1, 1 };

        // When
        try {
            mda.get(badDimensions);
            // Then
            fail();
        } catch (final IllegalArgumentException e) {
            assertEquals("Error message should be as expected", "Position[-1] was [-1], however it must be positive.", e.getMessage());
        }
    }
}
