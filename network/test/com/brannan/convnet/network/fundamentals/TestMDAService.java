package com.brannan.convnet.network.fundamentals;

import static com.brannan.convnet.network.fundamentals.MDAService.get;

import org.junit.Before;
import org.junit.Test;

public class TestMDAService {

    int[] dimensions = { 1, 2 };

    MDA mda;


    @Before
    public void setUp() {
        mda = new MDABuilder().withDimensions(dimensions).build();
    }


    /**
     * This test verifies that if the wrong number of dimensions are specified
     * for a get on the MDA, an illegal argument exception is thrown.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetMisMatchNumberOfDimensions() {
        // Given
        final int[] badDimensions = { 1, 2, 3 };

        // When
        get(mda, badDimensions);

        // Then
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
        get(mda, badDimensions);

        // Then
    }


    /**
     * This test verifies that if a negative position is given for a get in a
     * MDA, then an index out of bounds exception is thrown.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetNegativePositionRejected() {
        // Given
        final int[] badDimensions = { -1, 1 };

        // When
        get(mda, badDimensions);

        // Then
    }



}
