package services;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import fundamentals.MultiD;

public class TestMultiplicationService {
	MultiD<Double> multiD1;
	MultiD<Double> multiD2;
	MultiD<Double> result;
	MultiplicationService multiplicationService;
	
	@Before
	public void setUp(){
		multiplicationService = new MultiplicationService();
	}
	
	
	/**
	 * This tests a simple 1 element array operation
	 */
	@Test
	public void simpleMultiplication(){
		// Given
		multiD1 = new MultiD<>(1);
		multiD2 = new MultiD<>(1);
		
		multiD1.put(1D,0);
		multiD2.put(2D,0);
		
		// When
		MultiD<Double> result = multiplicationService.operate(multiD1, multiD2);
		
		// Then
		assertEquals("Returned amount should be the product", 2D, result.get(0), 0);
	}
	
	
	/**
	 * This tests the element wise opration on a 2x2 array
	 */
	@Test
	public void biggerMultiplication(){
		// Given
		multiD1 = new MultiD<>(2,2);
		multiD2 = new MultiD<>(2,2);
		
		multiD1.put(1D, 0,0);
		multiD1.put(2D, 0,1);
		multiD1.put(3D, 1,0);
		multiD1.put(4D, 1,1);
		
		multiD2.put(5D, 0,0);
		multiD2.put(6D, 0,1);
		multiD2.put(7D, 1,0);
		multiD2.put(8D, 1,1);
		
		// When
		result = multiplicationService.operate(multiD1, multiD2);
		
		// Then
		assertEquals("Returned amount should be the sum", 5D, result.get(0,0), 0);
		assertEquals("Returned amount should be the sum", 12D, result.get(0,1), 0);
		assertEquals("Returned amount should be the sum", 21D, result.get(1,0), 0);
		assertEquals("Returned amount should be the sum", 32D, result.get(1,1), 0);
	}
	
	
	/**
	 * This tests the element wise operation on a fully populated 28x28x3x10 array 
	 */
	@Test
	public void biggestAddition(){
		// Given
		int[] bigDimensions = {28,28,3,10};
		int[] position = new int[bigDimensions.length];
		multiD1 = new MultiD<>(bigDimensions);
		multiD2 = new MultiD<>(bigDimensions);
		double element = 1;
		
		for(int row = 0; row < bigDimensions[0]; row++){
			for(int column = 0; column < bigDimensions[1]; column++){
				for(int channel = 0; channel < bigDimensions[2]; channel++){
					for(int image = 0; image < bigDimensions[3]; image++){
						position[0] = row;
						position[1] = column;
						position[2] = channel;
						position[3] = image;
						multiD1.put(element, position);
						multiD2.put(2*element, position);
						element++;
					}
				}
			}
		}
		
		// When
		
		result = multiplicationService.operate(multiD1, multiD2);
		element = 1D;

		// Then
		for(int row = 0; row < bigDimensions[0]; row++){
			for(int column = 0; column < bigDimensions[1]; column++){
				for(int channel = 0; channel < bigDimensions[2]; channel++){
					for(int image = 0; image < bigDimensions[3]; image++){
						position[0] = row;
						position[1] = column;
						position[2] = channel;
						position[3] = image;
						assertEquals("Value not correctly returned for " + row + column + channel + image, 2*Math.pow(element, 2), result.get(position), 0);
						element++;
					}
				}
			}
		}
	}
	
	/**
	 * This tests the element wise operation on a fully populated 28x28x3x10 array being multiplied by a constant
	 * factor.
	 */
	@Test
	public void biggestMultiplicationConstant(){
		// Given
		int[] bigDimensions = {28,28,3,10};
		int[] position = new int[bigDimensions.length];
		multiD1 = new MultiD<Double>(bigDimensions);
		double element = 1;
		
		for(int row = 0; row < bigDimensions[0]; row++){
			for(int column = 0; column < bigDimensions[1]; column++){
				for(int channel = 0; channel < bigDimensions[2]; channel++){
					for(int image = 0; image < bigDimensions[3]; image++){
						position[0] = row;
						position[1] = column;
						position[2] = channel;
						position[3] = image;
						multiD1.put(element, position);
						element++;
					}
				}
			}
		}
		
	  // When
	  result = multiplicationService.operate(multiD1, 10D);
	  
	  // Then
	  element = 1D;
	  for(int row = 0; row < bigDimensions[0]; row++){
	  	for(int column = 0; column < bigDimensions[1]; column++){
	  		for(int channel = 0; channel < bigDimensions[2]; channel++){
	  			for(int image = 0; image < bigDimensions[3]; image++){
	  				position[0] = row;
	  				position[1] = column;
	  				position[2] = channel;
	  				position[3] = image;
	  				assertEquals("Value not correctly returned for " + row + column + channel + image, element*10, result.get(position), 0);
	  				element++;
	  			}
	  		}
	  	}
	  }
	}

}
