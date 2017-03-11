package services;

import static org.junit.Assert.assertEquals;
import static services.ExtractDimensionService.subsetOf;

import org.junit.Test;

import fundamentals.MultiD;

public class TestExtractDimensionService {
	
	int[] dimensions = {2,3,4, 5, 9};
	MultiD multiD1 = new MultiD(dimensions);

	@Test(expected = IllegalArgumentException.class)
	public void testDimensionTooLarge() {
		// Given
		int dimension = 5;
		int[] instance = {1};
		
		// When
		subsetOf(multiD1, dimension, instance);
		
		// Then expect exception
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testDimensionNegative() {
		// Given
		int dimension = -1;
		int[] instance = {1};
		
		// When
		subsetOf(multiD1, dimension, instance);
		
		// Then
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testInstanceTooLarge() {
		// Given
		int dimension = 2;
		int[] instance = {4};
		
		// When
		subsetOf(multiD1, dimension, instance);
		
		// Then
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testInstanceNegative() {
		// Given
		int dimension = 2;
		int[] instance = {-1};
		
		// When
		subsetOf(multiD1, dimension, instance);
		
		// Then
	}
	
	@Test
	public void checkRightDimensionalitySingleInstance() {
		// Given
		int dimension = 2;
		int instance = 0;
		
		// When
		MultiD result = subsetOf(multiD1, dimension, instance);
		
		// Then
		assertEquals("There should be one fewer dimensions in the result than the original operand", multiD1.getDimensions().length - 1, result.getDimensions().length);
		assertEquals("Dimension 0's size should be the same", multiD1.getDimensions()[0], result.getDimensions()[0]);
		assertEquals("Dimension 1's size should be the same", multiD1.getDimensions()[1], result.getDimensions()[1]);
	}
	
	@Test
	public void checkDataExtractsSingleInstance() {
		// Given
		int dimension = 2;
		int instance = 0;
		double element = 0D;
		
		for(int i = 0; i < dimensions[0]; i++) {
			for(int j = 0; j < dimensions[1]; j++) {
				for(int k = 0; k < dimensions[2]; k++) {
					for(int l = 0; l < dimensions[3]; l++) {
						for(int m = 0; m < dimensions[3]; m++) {
							multiD1.put(element, i, j, k, 0 ,0);
							element++;
						}
					}
				}
			}
		}
				
		// When
		MultiD result = subsetOf(multiD1, dimension, instance);
		
		// Then
		for(int i = 0; i < dimensions[0]; i++) {
			for(int j = 0; j < dimensions[1]; j++) {
				for(int l = 0; l < dimensions[3]; l++) {
					for(int m = 0; m < dimensions[3]; m++) {
						assertEquals("Data should be transferred " + i + " " + j, multiD1.get(i, j, instance, l, m), result.get(i, j, l, m), 0);
					}
				}
			}
		}
	}
	
	@Test
	public void checkRightDimensionalityMultipleInstance() {
		// Given
		int dimension = 2;
		int[] instances = {0, 1};
				
		// When
		MultiD result = subsetOf(multiD1, dimension, instances);
				
		// Then
		assertEquals("Dimensions should be the same", multiD1.getDimensions().length, result.getDimensions().length);
		assertEquals("Dimension 0's size should be the same", multiD1.getDimensions()[0], result.getDimensions()[0]);
		assertEquals("Dimension 1's size should be the same", multiD1.getDimensions()[1], result.getDimensions()[1]);
		assertEquals("Dimension 2's size should be the length of the instances provided", instances.length, result.getDimensions()[dimension]);
		assertEquals("Dimension 3's size should be the same", multiD1.getDimensions()[3], result.getDimensions()[3]);
		assertEquals("Dimension 4's size should be the same", multiD1.getDimensions()[4], result.getDimensions()[4]);
	}
	
	@Test
	public void checkDataExtractsMultipleInstance() {
		// Given
		int dimension = 2;
		int[] instances = {0,1};
		double element = 0D;
		
		for(int i = 0; i < dimensions[0]; i++) {
			for(int j = 0; j < dimensions[1]; j++) {
				for(int k = 0; k < dimensions[2]; k++) {
					for(int l = 0; l < dimensions[3]; l++) {
						for(int m = 0; m < dimensions[3]; m++) {
							multiD1.put(element, i, j, k, 0 ,0);
							element++;
						}
					}
				}
			}
		}
				
		// When
		MultiD result = subsetOf(multiD1, dimension, instances);
		
		// Then
		for(int instance:instances) {
			for(int i = 0; i < dimensions[0]; i++) {
				for(int j = 0; j < dimensions[1]; j++) {
					for(int l = 0; l < dimensions[3]; l++) {
						for(int m = 0; m < dimensions[3]; m++) {
							assertEquals("Data should be transferred " + i + " " + j, multiD1.get(i, j, instance, l, m), result.get(i, j, instance, l, m), 0);
						}
					}
				}
			}
		}
	}


}
