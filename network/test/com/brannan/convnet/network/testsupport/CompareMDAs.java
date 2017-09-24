package com.brannan.convnet.network.testsupport;

import static com.brannan.convnet.network.fundamentals.HelperLibrary.arrayAsList;
import static com.brannan.convnet.network.fundamentals.MDAHelper.get;
import static org.junit.Assert.assertEquals;

import com.brannan.convnet.network.fundamentals.MDA;

public class CompareMDAs {
	
    public static void checkExpectation(MDA expectedOutput, MDA forward) {
        assertEquals("Number of dimensions is correct", expectedOutput.getDimensions().length,
                forward.getDimensions().length);
        for (int i = 0; i < expectedOutput.getDimensions().length; i++) {
            assertEquals("Each dimension should be correct. Asserting on: " + i, expectedOutput.getDimensions()[i],
                    forward.getDimensions()[i]);
        }
        checkAllElements(expectedOutput, forward, new int[forward.getDimensions().length],
                forward.getDimensions().length - 1);
    }

	private static void checkAllElements(MDA expectedOutput, MDA forward, int[] position, int place) {
		for(int i = 0; i < expectedOutput.getDimensions()[place]; i++) {
			position[place] = i;
			if(place != 0) {
				checkAllElements(expectedOutput, forward, position, place-1);
			} else {
				assertEquals("Each element should be correct. Asserting on: " + arrayAsList(position).toString(), get(expectedOutput, position), get(forward, position), 0);
			}
		}
		
	}

}
