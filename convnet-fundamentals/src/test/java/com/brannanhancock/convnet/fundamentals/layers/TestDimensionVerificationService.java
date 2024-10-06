package com.brannanhancock.convnet.fundamentals.layers;

import org.junit.Test;

import static org.junit.Assert.assertFalse;


/**
 * 
 * @author Brannan R. Hancock
 *
 */
public class TestDimensionVerificationService {
	
	private final int[] inputDimensions = {28,28,12};
	
	private final DimensionVerificationService dimensionVerificationService = new DimensionVerificationService();
	
    /**
     * In this test setup the number of active dimensions is less than the
     * number of dimensions of the filter. Thus we expect an error.
     */
    @Test
    public void testTooManyFilterDimensions() {
        // Given
        int[] poolSizes = {4, 4, 4, 4};

        // When
		boolean result = dimensionVerificationService.verifyLeftBiggerThanRight(inputDimensions, poolSizes);

		assertFalse(result);
    }
	
    
	/**
	 * In this test in dimension 2 the filter is bigger than dimension 2 of its operand, thus
	 * we expect an error.
	 */
	@Test
	public void testFeatureLargerThanImage() {
		// Given
		int[] poolSizes = {28, 28, 13};

		// When
		boolean result = dimensionVerificationService.verifyLeftBiggerThanRight(inputDimensions, poolSizes);

		// Then
		assertFalse(result);
	}
	
	
	/**
	 * Verifies that an allowable corner case of filter size = input size does not throw
	 * an error.
	 */
	@Test
	public void testSuccess() {
	    // Given
	    int[] poolSizes = {28, 28, 12};
	    
	    // When
	    dimensionVerificationService.verifyLeftBiggerThanRight(inputDimensions, poolSizes);
	    
	    // Then pass as no exceptions are thrown.
	}
}
