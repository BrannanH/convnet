package fundamentals;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class TestMultiD {
	
	int[] dimensions = {1,2};
	
	
	MultiD<Double> multiD;
	
	@Before
	public void setUp(){
		multiD = new MultiD<Double>(dimensions);
	}
	
	
	/**
	 * This test verifies that the dimensions specified upon constructon of a Multi Dimensional Array are
	 * maintained.
	 */
	@Test
	public void testConstructMultiD(){
		// Given
		
		// When
		
		// Then
		assertEquals("Dimensions should match those entered", dimensions, multiD.getDimensions());
	}
	
	
	/**
	 * This test verifies that if the wrong number of dimensions are specified for a put in a Multi Dimensional 
	 * Array, an illegal argument exception is thrown.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testPutMisMatchNumberOfDimensions(){
		// Given
		int[] badDimensions = {1,2,3};
		
		// When
		multiD.put(3D, badDimensions);
		
		// Then
	}
	
	
	/**
	 * This test verifies that if too large of an index is specified on a put in a Multi Dimensional Array, 
	 * then an illegal argument exception is thrown. 
	 */
	@Test(expected = IndexOutOfBoundsException.class)
	public void testPutOutOfBoundsIndexInsertion(){
		// Given
		int[] badDimensions = {1,1};
		
		// When
		multiD.put(3D, badDimensions);
		
		// Then
	}
	
	
	/**
	 * This test verifies that if a negative position is given for a put in a Multi Dimensional Array, 
	 * then an index out of bounds exception is thrown.
	 */
	@Test(expected = IndexOutOfBoundsException.class)
	public void testPutNegativePositionRejected(){
		// Given
		int[] badDimensions = {-1,1};
		
		// When
		multiD.put(3D, badDimensions);
		
		// Then
	}
	
	/**
	 * This test verifies that if the wrong number of dimensions are specified for the multi dimensional array
	 * an illegal argument exception is thrown.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testGetMisMatchNumberOfDimensions(){
		// Given
		int[] badDimensions = {1,2,3};
		
		// When
		multiD.get(badDimensions);
		
		// Then
	}
	
	
	/**
	 * This test verifies that if too large of an index is specified on a mutli dimensional array, then an
	 * illegal argument exception is thrown. 
	 */
	@Test(expected = IndexOutOfBoundsException.class)
	public void testGetOutOfBoundsIndexInsertion(){
		// Given
		int[] badDimensions = {1,1};
		
		// When
		multiD.get(badDimensions);
		
		// Then
	}
	
	
	/**
	 * This test verifies that if a negative position is given for a get in a Multi Dimensional Array, 
	 * then an index out of bounds exception is thrown.
	 */
	@Test(expected = IndexOutOfBoundsException.class)
	public void testGetNegativePositionRejected(){
		// Given
		int[] badDimensions = {-1,1};
		
		// When
		multiD.get(badDimensions);
		
		// Then
	}
	
	
	/**
	 * This test verifies that if an element in the multi dimensional array is to be replaced, the new element
	 * persists.
	 */
	@Test
	public void replace(){
		// Given
		int[] position = {0,0};
		double wrongElement = 1D;
		double correctElement = 2D;
		
		// When
		multiD.put(wrongElement, position);
		multiD.put(correctElement, position);
		
		assertEquals("The second specified element should be returned", correctElement, multiD.get(position), 0);
	}
	
	
	/**
	 * This test verifies that a small 2x2 array can be fully populated, and have the data retrieved from it.
	 */
	@Test
	public void smallArrayPopulated(){
		// Given
		multiD = new MultiD<Double>(2,2);
		
		// When
		multiD.put(0D, 0,0);
		multiD.put(1D, 0,1);
		multiD.put(2D, 1,0);
		multiD.put(3D, 1,1);
		
		// Then
		assertEquals("Position 1", 0D, multiD.get(0,0), 0);
		assertEquals("Position 2", 1D, multiD.get(0,1), 0);
		assertEquals("Position 3", 2D, multiD.get(1,0), 0);
		assertEquals("Position 4", 3D, multiD.get(1,1), 0);
	}
	
	
	/**
	 * This test verifies that a large multi dimensional array can be fully populated, and still have each unique
	 * element retrieved.
	 */
	@Test
	public void testBigArrayFullyPopulated(){
		// Given
		int[] bigDimensions = {28,28,3,10};
		int[] position2 = new int[bigDimensions.length];
		multiD = new MultiD<Double>(bigDimensions);
		double element = 0D;
		
		// When
		for(int row = 0; row < bigDimensions[0]; row++){
			for(int column = 0; column < bigDimensions[1]; column++){
				for(int channel = 0; channel < bigDimensions[2]; channel++){
					for(int image = 0; image < bigDimensions[3]; image++){
						position2[0] = row;
						position2[1] = column;
						position2[2] = channel;
						position2[3] = image;
						multiD.put(element, position2);
						element++;
					}
				}
			}
		}
		element = 0D;
		
		// Then
		for(int row = 0; row < bigDimensions[0]; row++){
			for(int column = 0; column < bigDimensions[1]; column++){
				for(int channel = 0; channel < bigDimensions[2]; channel++){
					for(int image = 0; image < bigDimensions[3]; image++){
						position2[0] = row;
						position2[1] = column;
						position2[2] = channel;
						position2[3] = image;
						assertEquals("Value not correctly returned for " + row + column + channel + image, element, multiD.get(position2), 0);
						element++;
					}
				}
			}
		}
	}
	
}
