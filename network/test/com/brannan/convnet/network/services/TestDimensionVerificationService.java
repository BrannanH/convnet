package com.brannan.convnet.network.services;

import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * 
 * @author Brannan R. Hancock
 *
 */
public class TestDimensionVerificationService {
	
	private int[] inputDimensions = {28,28,12};
	
	private DimensionVerificationService dimensionVerificationService = new DimensionVerificationService();
	
    /**
     * In this test setup the number of active dimensions is less than the
     * number of dimensions of the filter. Thus we expect an error.
     */
    @Test
    public void testTooManyFilterDimensions() {
        // Given
        int[] poolSizes = {4, 4, 4, 4};
        boolean caught = false;
        
        // When
        try {
            dimensionVerificationService.verifyLeftBiggerThanRight(inputDimensions, poolSizes);
        } catch ( IllegalArgumentException e ) {
            // Then
            caught = true;
        }
        assertTrue(caught);
    }
	
    
	/**
	 * In this test in dimension 2 the filter is bigger than dimension 2 of its operand, thus
	 * we expect an error.
	 */
	@Test
	public void testFeatureLargerThanImage() {
		// Given
		int[] poolSizes = {28, 28, 13};
		boolean caught = false;
		
		// When
		try {
		    dimensionVerificationService.verifyLeftBiggerThanRight(inputDimensions, poolSizes);
		} catch ( IllegalArgumentException e ) {
		    // Then
		    caught = true;
		}
		assertTrue(caught);
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
	
	
	/**
	 * Verifies that an error is thrown if dLossByDOut contains inconsistent dimensions.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testInvalidMappingForDLossByDOut() {
	    // Given
	    Map<List<Integer>, Map<List<Integer>, Double>> testMap = new HashMap<>();
	    testMap.put(Lists.newArrayList(0,0), Maps.newHashMap());
	    testMap.put(Lists.newArrayList(0,0,1), Maps.newHashMap());
	    
	    // When
	    dimensionVerificationService.verifyDerivativeMap(testMap);
	    
	    // Then expect exception
	}
	
	
	/**
	 * Verifies that an error is thrown if dOutByDIn contains inconsistent dimensions within pools.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testInvalidMappingForDOutByDInWithinGroup() {
	    // Given
	    Map<List<Integer>, Map<List<Integer>, Double>> testMap = new HashMap<>();
	    Map<List<Integer>, Double> subMap1 = Maps.newHashMap();
	    subMap1.put(Lists.newArrayList(0, 0), 0D);
	    subMap1.put(Lists.newArrayList(0, 0, 1), 0D);
	    testMap.put(Lists.newArrayList(0,0), subMap1);
	    
	    // When
        dimensionVerificationService.verifyDerivativeMap(testMap);
        
        // Then expect exception
	}
	
	
	/**
	 * Verifies than an error is thrown if dOutByDIn contains inconsistent dimensions across pools.
	 */
	@Test
	public void testInvalidMappingForDOutByDInBetweenGroups() {
	    // Given
        Map<List<Integer>, Map<List<Integer>, Double>> testMap = new HashMap<>();
        Map<List<Integer>, Double> subMap1 = Maps.newHashMap();
        subMap1.put(Lists.newArrayList(0, 0), 0D);
        subMap1.put(Lists.newArrayList(0, 1), 0D);
        testMap.put(Lists.newArrayList(0,0), subMap1);
        
        Map<List<Integer>, Double> subMap2 = Maps.newHashMap();
        subMap2.put(Lists.newArrayList(0, 0, 2), 0D);
        subMap2.put(Lists.newArrayList(0, 0, 3), 0D);
        testMap.put(Lists.newArrayList(0,1), subMap1);
        
        // When
        dimensionVerificationService.verifyDerivativeMap(testMap);
        
        // Then expect exception
	}
	
	/**
	 * Verifies that inconsistencies between dimensions in the inner and outer map throws an error.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testInvalidBetweenInnerAndOuterDimensions() {
	 // Given
        Map<List<Integer>, Map<List<Integer>, Double>> testMap = new HashMap<>();
        Map<List<Integer>, Double> subMap1 = Maps.newHashMap();
        subMap1.put(Lists.newArrayList(0, 0), 0D);
        subMap1.put(Lists.newArrayList(0, 1), 0D);
        testMap.put(Lists.newArrayList(0, 0, 0), subMap1);

        Map<List<Integer>, Double> subMap2 = Maps.newHashMap();
        subMap2.put(Lists.newArrayList(0, 2), 0D);
        subMap2.put(Lists.newArrayList(0, 3), 0D);
        testMap.put(Lists.newArrayList(0, 0, 1), subMap1);

        // When
        dimensionVerificationService.verifyDerivativeMap(testMap);

        // Then expect exception
	}
	
	
	/**
	 * Verifies that no errors are thrown if all validation criteria are met.
	 */
	@Test
	public void testValidReturnsTrue() {
	    // Given
	    Map<List<Integer>, Map<List<Integer>, Double>> testMap = new HashMap<>();
	    Map<List<Integer>, Double> subMap1 = Maps.newHashMap();
	    subMap1.put(Lists.newArrayList(0, 0), 0D);
	    subMap1.put(Lists.newArrayList(0, 1), 0D);
	    testMap.put(Lists.newArrayList(0,0), subMap1);

	    Map<List<Integer>, Double> subMap2 = Maps.newHashMap();
	    subMap2.put(Lists.newArrayList(0, 2), 0D);
	    subMap2.put(Lists.newArrayList(0, 3), 0D);
	    testMap.put(Lists.newArrayList(0,1), subMap1);

	    // When
	    boolean result = dimensionVerificationService.verifyDerivativeMap(testMap);

	    // Then
	    assertTrue(result);
	}
}
