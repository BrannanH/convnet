package testsupport;

import static fundamentals.HelperLibrary.arrayAsList;
import static fundamentals.MDAHelper.get;
import static org.junit.Assert.assertEquals;

import fundamentals.MDA;

public class CompareMDAs {
	
	public static void checkExpectation(MDA expectedOutput, MDA forward) {
		assertEquals("Number of dimensions is correct", expectedOutput.getDimensions().size(), forward.getDimensions().size());
		for(int i = 0; i < expectedOutput.getDimensions().size(); i++) {
			assertEquals("Each dimension should be correct. Asserting on: " + i, expectedOutput.getDimensions().get(i), forward.getDimensions().get(i));
		}
		checkAllElements(expectedOutput, forward, new int[forward.getDimensions().size()], forward.getDimensions().size()-1);
	}

	private static void checkAllElements(MDA expectedOutput, MDA forward, int[] position, int place) {
		for(int i = 0; i < expectedOutput.getDimensions().get(place); i++) {
			position[place] = i;
			if(place != 0) {
				checkAllElements(expectedOutput, forward, position, place-1);
			} else {
				assertEquals("Each element should be correct. Asserting on: " + arrayAsList(position).toString(), get(expectedOutput, position), get(forward, position), 0);
			}
		}
		
	}

}
