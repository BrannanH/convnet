package com.brannan.convnet.network.fundamentals;

import static com.brannan.convnet.network.fundamentals.HelperLibrary.arrayAsList;
import static com.brannan.convnet.network.fundamentals.HelperLibrary.arrayEquality;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TestMDABuilder {

    @Test
    public void testBuild() {
        // Given
        int[] inputDimensions = { 2, 3, 4 };
        int[] expectedIncrements = { 1, 2, 6 };

        MDABuilder builder = new MDABuilder();

        // When
        MDA mDA = builder.withDimensions(inputDimensions).build();

        // Then
        assertTrue("Dimensions should be as expected", arrayEquality(inputDimensions, mDA.getDimensions()));
        assertEquals("Increments should be as expected", arrayAsList(expectedIncrements),
                arrayAsList(mDA.getIncrements()));
    }

}
