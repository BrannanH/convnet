package com.brannan.convnet.network.fundamentals;

import static com.brannan.convnet.network.fundamentals.HelperLibrary.arrayAsList;
import static com.brannan.convnet.network.fundamentals.MDAHelper.get;
import static com.brannan.convnet.network.fundamentals.MDAHelper.put;
import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class TestMDAHelper {

    int[] dimensions = { 1, 2 };

    MDA mda;


    @Before
    public void setUp() {
        mda = new MDABuilder().withDimensions(dimensions).build();
    }


    /**
     * This test verifies that the dimensions specified upon constructon of a
     * MDA are maintained.
     */
    @Test
    public void testConstructMultiD() {
        // Given

        // When

        // Then
        assertEquals("Dimensions should match those entered", arrayAsList(dimensions), mda.getDimensions());
    }


    /**
     * This test verifies that if the wrong number of dimensions are specified
     * for a put in an MDA, an illegal argument exception is thrown.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testPutMisMatchNumberOfDimensions() {
        // Given
        int[] badDimensions = { 1, 2, 3 };

        // When
        put(mda, 3D, badDimensions);

        // Then
    }


    /**
     * This test verifies that if too large of an index is specified on a put in
     * a MDA, then an illegal argument exception is thrown.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testPutOutOfBoundsIndexInsertion() {
        // Given
        int[] badDimensions = { 1, 1 };

        // When
        put(mda, 3D, badDimensions);

        // Then
    }


    /**
     * This test verifies that if a negative position is given for a put in a
     * MDA, then an index out of bounds exception is thrown.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testPutNegativePositionRejected() {
        // Given
        int[] badDimensions = { -1, 1 };

        // When
        put(mda, 3D, badDimensions);

        // Then
    }


    /**
     * This test verifies that if the wrong number of dimensions are specified
     * for a get on the MDA, an illegal argument exception is thrown.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetMisMatchNumberOfDimensions() {
        // Given
        int[] badDimensions = { 1, 2, 3 };

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
        int[] badDimensions = { 1, 1 };

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
        int[] badDimensions = { -1, 1 };

        // When
        get(mda, badDimensions);

        // Then
    }


    /**
     * This test verifies that if an element in the MDA is to be replaced, the
     * new element persists.
     */
    @Test
    public void replace() {
        // Given
        int[] position = { 0, 0 };
        double wrongElement = 1D;
        double correctElement = 2D;

        // When
        put(mda, wrongElement, position);
        put(mda, correctElement, position);

        assertEquals("The second specified element should be returned", correctElement, get(mda, position), 0);
    }


    /**
     * This test verifies that a small 2x2 MDA can be fully populated, and have
     * the data retrieved from it.
     */
    @Test
    public void smallArrayPopulated() {
        // Given
        mda = new MDABuilder().withDimensions(2, 2).build();

        // When
        put(mda, 0D, 0, 0);
        put(mda, 1D, 0, 1);
        put(mda, 2D, 1, 0);
        put(mda, 3D, 1, 1);

        // Then
        assertEquals("Position 1", 0D, get(mda, 0, 0), 0);
        assertEquals("Position 2", 1D, get(mda, 0, 1), 0);
        assertEquals("Position 3", 2D, get(mda, 1, 0), 0);
        assertEquals("Position 4", 3D, get(mda, 1, 1), 0);
    }


    /**
     * This test verifies that a large MDA can be fully populated, and still
     * have each unique element retrieved.
     */
    @Test
    public void testBigArrayFullyPopulated() {
        // Given
        int[] bigDimensions = { 28, 28, 3, 10 };
        int[] position = new int[bigDimensions.length];
        mda = new MDABuilder().withDimensions(bigDimensions).build();
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
                        put(mda, element, position);
                        element++;
                    }
                }
            }
        }
        element = 0D;

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
                                get(mda, position), 0);
                        element++;
                    }
                }
            }
        }
    }
}
