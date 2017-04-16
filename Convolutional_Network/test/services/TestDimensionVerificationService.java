package services;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;

public class TestDimensionVerificationService {
	
	List<Integer> inputDimensions = Lists.newArrayList(28,28,12);
	int[] activeDimensions = {0,1};
	
	DimensionVerificationService dimensionVerificationService = new DimensionVerificationService();
	
	@Before
	public void setup() {
	}


    /**
     * In this test setup the number of active dimensions is less than the
     * number of dimensions of the filter. Thus we expect an error.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testTooManyFilterDimensions() {
        // Given
        List<Integer> poolSizes = Lists.newArrayList(4, 4, 4, 4);
        // When
        dimensionVerificationService.verify(inputDimensions, poolSizes);

        // Then expect exception
    }
	
	/**
	 * In this test in dimension 1 the filter is bigger than dimension 1 of its operand, thus
	 * we expect an error.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testFeatureLargerThanImage() {
		// Given
		List<Integer> poolSizes = Lists.newArrayList(3, 30);
		// When
		dimensionVerificationService.verify(inputDimensions, poolSizes);
		
		// Then expect exception
	}
	
}
