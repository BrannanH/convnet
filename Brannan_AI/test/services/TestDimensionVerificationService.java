package services;

import org.junit.Before;
import org.junit.Test;

import fundamentals.MultiD;
import layers.Feature;

public class TestDimensionVerificationService {
	
	int[] inputDimensions = {28,28,12};
	int[] activeDimensions = {0,1};
	Feature feature = new Feature();
	
	DimensionVerificationService dimensionVerificationService = new DimensionVerificationService();
	
	@Before
	public void setup() {
		feature.setActiveDimensions(activeDimensions);
	}

	/**
	 * In this test setup the number of active dimensions is less than the number of dimensions
	 * of the filter. Thus we expect an error.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testTooManyFilterDimensions() {
		// Given
		int[] filterDimensions = {3,3,3};
		MultiD filter = new MultiD(filterDimensions);
		
		feature.setActiveDimensions(activeDimensions);
		feature.setFeatureMap(filter);
		
		// When
		dimensionVerificationService.verify(inputDimensions,feature);
		
		// Then expect exception
	}
	
	/**
	 * In this test in dimension 1 the filter is bigger than dimension 1 of its operand, thus
	 * we expect an error.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testFeatureLargerThanImage() {
		// Given
		MultiD filter = new MultiD(3,30);
		feature.setFeatureMap(filter);
		
		// When
		dimensionVerificationService.verify(inputDimensions,feature);
		
		// Then expect exception
	}
	
}
