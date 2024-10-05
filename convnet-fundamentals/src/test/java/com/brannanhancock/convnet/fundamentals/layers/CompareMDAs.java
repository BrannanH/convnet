package com.brannanhancock.convnet.fundamentals.layers;

import com.brannanhancock.convnet.fundamentals.mda.MDA;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

public class CompareMDAs {

    public static void checkExpectation(final MDA expectedOutput, final MDA forward) {
        assertEquals("Number of dimensions is correct", expectedOutput.getDimensions().length,
                forward.getDimensions().length);
        for (int i = 0; i < expectedOutput.getDimensions().length; i++) {
            assertEquals("Each dimension should be correct. Asserting on: " + i, expectedOutput.getDimensions()[i],
                    forward.getDimensions()[i]);
        }
        checkAllElements(expectedOutput, forward, new int[forward.getDimensions().length],
                forward.getDimensions().length - 1);
    }

	private static void checkAllElements(final MDA expectedOutput, final MDA forward, final int[] position, final int place) {
		for(int i = 0; i < expectedOutput.getDimensions()[place]; i++) {
			position[place] = i;
			if(place != 0) {
				checkAllElements(expectedOutput, forward, position, place-1);
			} else {
				assertEquals("Each element should be correct. Asserting on: " + Arrays.asList(position).toString(), expectedOutput.get(position), forward.get(position), 0);
			}
		}

	}

}
