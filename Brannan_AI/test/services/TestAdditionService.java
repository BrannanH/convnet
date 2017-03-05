package services;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import fundamentals.MultiD;

public class TestAdditionService {
	
	MultiD<Double> multiD1;
	MultiD<Double> multiD2;
	MultiD<Double> result;
	AdditionService additionService;
	
	@Before
	public void setUp(){
		additionService = new AdditionService();
	}
	
	
	/**
	 * This tests a simple 1 element array operation
	 */
	@Test
	public void simpleAddition(){
		// Given
		multiD1 = new MultiD<>(1);
		multiD2 = new MultiD<>(1);
		
		multiD1.put(1D,0);
		multiD2.put(2D,0);
		
		// When
		MultiD<Double> result = additionService.operate(multiD1, multiD2);
		
		// Then
		assertEquals("Returned amount should be the sum", 3D, result.get(0), 0);
	}
	
	
	/**
	 * This tests the element wise opration on a 2x2 array
	 */
	@Test
	public void biggerAddition(){
		// Given
		multiD1 = new MultiD<Double>(2,2);
		multiD2 = new MultiD<Double>(2,2);
		
		multiD1.put(1D, 0,0);
		multiD1.put(2D, 0,1);
		multiD1.put(3D, 1,0);
		multiD1.put(4D, 1,1);
		
		multiD2.put(5D, 0,0);
		multiD2.put(6D, 0,1);
		multiD2.put(7D, 1,0);
		multiD2.put(8D, 1,1);
		
		// When
		result = additionService.operate(multiD1, multiD2);
		
		// Then
		assertEquals("Returned amount should be the sum", 6D, result.get(0,0), 0);
		assertEquals("Returned amount should be the sum", 8D, result.get(0,1), 0);
		assertEquals("Returned amount should be the sum", 10D, result.get(1,0), 0);
		assertEquals("Returned amount should be the sum", 12D, result.get(1,1), 0);
	}
	
	
	/**
	 * This tests the element wise operation on a fully populated 28x28x3x10 array 
	 */
	@Test
	public void biggestAddition(){
		// Given
		int[] bigDimensions = {28,28,3,10};
		int[] position = new int[bigDimensions.length];
		multiD1 = new MultiD<Double>(bigDimensions);
		multiD2 = new MultiD<Double>(bigDimensions);
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
		result = additionService.operate(multiD1, multiD2);

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
						assertEquals("Value not correctly returned for " + row + column + channel + image, element*3, result.get(position), 0);
						element++;
					}
				}
			}
		}
	}
	
	/**
	 * This tests the element wise operation on a fully populated 28x28x3x10 array having a constant added
	 * to it. 
	 */
	@Test
	public void biggestAdditionConstant(){
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
	  result = additionService.operate(multiD1, 10D);
	  
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
	  				assertEquals("Value not correctly returned for " + row + column + channel + image, element+10, result.get(position), 0);
	  				element++;
	  			}
	  		}
	  	}
	  }
	}

}
