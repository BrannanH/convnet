package com.brannan.convnet.network.fundamentals;

import static com.brannan.convnet.network.fundamentals.HelperLibrary.arrayAsList;
import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

import com.brannan.convnet.network.fundamentals.MDA;
import com.brannan.convnet.network.fundamentals.MDABuilder;

public class TestMDABuilder {

    @Test
    public void testBuild() {
        // Given
        int[] inputDimensions = { 2, 3, 4 };
        int[] expectedIncrements = { 1, 2, 6 };
        List<Integer> expectedDimensions = arrayAsList(inputDimensions);

        MDABuilder builder = new MDABuilder();

        // When
        MDA mDA = builder.withDimensions(inputDimensions).build();

        // Then
        assertEquals("Dimensions should be as expected", expectedDimensions, mDA.getDimensions());
        assertEquals("Increments should be as expected", arrayAsList(expectedIncrements),
                arrayAsList(mDA.getIncrements()));
    }

}
