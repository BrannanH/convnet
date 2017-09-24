package com.brannan.convnet.network.layers.pooling;

import static com.brannan.convnet.network.fundamentals.MDAHelper.put;
import static com.brannan.convnet.network.testsupport.CompareMDAs.checkExpectation;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.brannan.convnet.network.fundamentals.MDA;
import com.brannan.convnet.network.fundamentals.MDABuilder;
import com.brannan.convnet.network.layers.ForwardOutputTuple;
import com.brannan.convnet.network.layers.pooling.PoolingLibrary.PoolingType;
import com.brannan.convnet.network.services.DimensionVerificationService;
import com.google.common.collect.Lists;

/**
 * This class tests the MaxPoolingLayer
 * @author Brannan R. Hancock
 *
 */
public class TestMaxPoolingLayer {
	
	@Mock
	DimensionVerificationService dimensionVerificationService;
	
	private PoolingLayer maxPoolingLayer;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		maxPoolingLayer = new PoolingLayer(dimensionVerificationService);
	}
	
	@Test
	public void TestSmallPool() {
		// Given
		int[] inputDimensions = {4,4};
		int[] poolingSize = {2,1};
		
		double element = 0;
		MDA input = new MDABuilder().withDimensions(inputDimensions).build();
		for(int i = 0; i < inputDimensions[0]; i++) {
			for(int j = 0; j < inputDimensions[1]; j++) {
				put(input, element, i, j);
				element++;
			}
		}
		
		MDA expectedResult = new MDABuilder().withDimensions(2,4).build();
		put(expectedResult, 4D, 0, 0);
		put(expectedResult, 5D, 0, 1);
		put(expectedResult, 6D, 0, 2);
		put(expectedResult, 7D, 0, 3);
		put(expectedResult, 12D, 1, 0);
		put(expectedResult, 13D, 1, 1);
		put(expectedResult, 14D, 1, 2);
		put(expectedResult, 15D, 1, 3);
		
		Map<List<Integer>, Map<List<Integer>, Double>> expectedDerivative = new HashMap<List<Integer>, Map<List<Integer>, Double>>();
		Map<List<Integer>, Double> temp = new HashMap<>();
		temp.put(Lists.newArrayList(0, 0), 0D);
		temp.put(Lists.newArrayList(1, 0), 1D);
		expectedDerivative.put(Lists.newArrayList(0,0), temp);
		temp = new HashMap<>();
		temp.put(Lists.newArrayList(0, 1), 0D);
		temp.put(Lists.newArrayList(1, 1), 1D);
		expectedDerivative.put(Lists.newArrayList(0,1), temp);
		temp = new HashMap<>();
		temp.put(Lists.newArrayList(0, 2), 0D);
		temp.put(Lists.newArrayList(1, 2), 1D);
		expectedDerivative.put(Lists.newArrayList(0,2), temp);
		temp = new HashMap<>();
		temp.put(Lists.newArrayList(0, 3), 0D);
		temp.put(Lists.newArrayList(1, 3), 1D);
		expectedDerivative.put(Lists.newArrayList(0,3), temp);
		temp = new HashMap<>();
		temp.put(Lists.newArrayList(2, 0), 0D);
		temp.put(Lists.newArrayList(3, 0), 1D);
		expectedDerivative.put(Lists.newArrayList(1,0), temp);
		temp = new HashMap<>();
		temp.put(Lists.newArrayList(2, 1), 0D);
		temp.put(Lists.newArrayList(3, 1), 1D);
		expectedDerivative.put(Lists.newArrayList(1,1), temp);
		temp = new HashMap<>();
		temp.put(Lists.newArrayList(2, 2), 0D);
		temp.put(Lists.newArrayList(3, 2), 1D);
		expectedDerivative.put(Lists.newArrayList(1,2), temp);
		temp = new HashMap<>();
		temp.put(Lists.newArrayList(2, 3), 0D);
		temp.put(Lists.newArrayList(3, 3), 1D);
		expectedDerivative.put(Lists.newArrayList(1,3), temp);
		temp = new HashMap<>();
		
		// When
		ForwardOutputTuple output = maxPoolingLayer.forward(input, poolingSize, PoolingType.MAX);
		MDA result = output.getOutput();
		Map<List<Integer>, Map<List<Integer>, Double>> derivative = output.getdOutByDIn();
		
		// Then
		checkExpectation(expectedResult, result);
		checkDerivatives(expectedDerivative, derivative);
	}
	
	private void checkDerivatives(Map<List<Integer>, Map<List<Integer>, Double>> expectedDerivative, Map<List<Integer>, Map<List<Integer>, Double>> derivative) {
		for(Entry<List<Integer>, Map<List<Integer>, Double>> entry: expectedDerivative.entrySet()) {
			assertTrue(derivative.containsKey(entry.getKey()));
			assertEquals(entry.getValue(), derivative.get(entry.getKey()));
		}
		
	}

	@Test
	public void TestBigPool() {
		// Given
		int[] inputDimensions = {4,4};
		int[] poolingSize = {2,2};
		double element = 0;
		MDA input = new MDABuilder().withDimensions(inputDimensions).build();
		for(int i = 0; i < inputDimensions[0]; i++) {
			for(int j = 0; j < inputDimensions[1]; j++) {
				put(input, element, i, j);
				element++;
			}
		}
		
		MDA expectedResult = new MDABuilder().withDimensions(2,2).build();
		put(expectedResult, 5D, 0, 0);
		put(expectedResult, 7D, 0, 1);
		put(expectedResult, 13D, 1, 0);
		put(expectedResult, 15D, 1, 1);
		
		Map<List<Integer>, Map<List<Integer>, Double>> expectedDerivative = new HashMap<List<Integer>, Map<List<Integer>, Double>>();
		Map<List<Integer>, Double> temp = new HashMap<>();
		temp.put(Lists.newArrayList(0, 0), 0D);
		temp.put(Lists.newArrayList(0, 1), 0D);
		temp.put(Lists.newArrayList(1, 0), 0D);
		temp.put(Lists.newArrayList(1, 1), 1D);
		expectedDerivative.put(Lists.newArrayList(0,0), temp);
		temp = new HashMap<>();
		temp.put(Lists.newArrayList(0, 2), 0D);
		temp.put(Lists.newArrayList(0, 3), 0D);
		temp.put(Lists.newArrayList(1, 2), 0D);
		temp.put(Lists.newArrayList(1, 3), 1D);
		expectedDerivative.put(Lists.newArrayList(0,1), temp);
		temp = new HashMap<>();
		temp.put(Lists.newArrayList(2, 0), 0D);
		temp.put(Lists.newArrayList(2, 1), 0D);
		temp.put(Lists.newArrayList(3, 0), 0D);
		temp.put(Lists.newArrayList(3, 1), 1D);
		expectedDerivative.put(Lists.newArrayList(1,0), temp);
		temp = new HashMap<>();
		temp.put(Lists.newArrayList(2, 2), 0D);
		temp.put(Lists.newArrayList(2, 3), 0D);
		temp.put(Lists.newArrayList(3, 2), 0D);
		temp.put(Lists.newArrayList(3, 3), 1D);
		expectedDerivative.put(Lists.newArrayList(1,1), temp);
		
		// When
		ForwardOutputTuple output = maxPoolingLayer.forward(input, poolingSize, PoolingType.MAX);
		MDA result = output.getOutput();
		Map<List<Integer>, Map<List<Integer>, Double>> derivative = output.getdOutByDIn();
		
		// Then
		checkExpectation(expectedResult, result);
		checkDerivatives(expectedDerivative, derivative);
	}
	
	@Test
	public void TestBiggestPool() {
		// Given
		int[] inputDimensions = {4,4,2};
		int[] poolingSize = {2,2,2};
		double element = 0;
		MDA input = new MDABuilder().withDimensions(inputDimensions).build();
		for(int k = 0; k < inputDimensions[2]; k++) {
			for(int i = 0; i < inputDimensions[0]; i++) {
				for(int j = 0; j < inputDimensions[1]; j++) {
					put(input, element, i, j, k);
					element++;
				}
			}
		}
		
		MDA expectedResult = new MDABuilder().withDimensions(2,2,1).build();
		put(expectedResult, 21D, 0, 0, 0);
		put(expectedResult, 23D, 0, 1, 0);
		put(expectedResult, 29D, 1, 0, 0);
		put(expectedResult, 31D, 1, 1, 0);
		
		Map<List<Integer>, Map<List<Integer>, Double>> expectedDerivative = new HashMap<List<Integer>, Map<List<Integer>, Double>>();
		Map<List<Integer>, Double> temp = new HashMap<>();
		temp.put(Lists.newArrayList(0, 0, 0), 0D);
		temp.put(Lists.newArrayList(0, 1, 0), 0D);
		temp.put(Lists.newArrayList(1, 0, 0), 0D);
		temp.put(Lists.newArrayList(1, 1, 0), 0D);
		temp.put(Lists.newArrayList(0, 0, 1), 0D);
		temp.put(Lists.newArrayList(0, 1, 1), 0D);
		temp.put(Lists.newArrayList(1, 0, 1), 0D);
		temp.put(Lists.newArrayList(1, 1, 1), 1D);
		expectedDerivative.put(Lists.newArrayList(0,0,0), temp);
		temp = new HashMap<>();
		temp.put(Lists.newArrayList(0, 2, 0), 0D);
		temp.put(Lists.newArrayList(0, 3, 0), 0D);
		temp.put(Lists.newArrayList(1, 2, 0), 0D);
		temp.put(Lists.newArrayList(1, 3, 0), 0D);
		temp.put(Lists.newArrayList(0, 2, 1), 0D);
		temp.put(Lists.newArrayList(0, 3, 1), 0D);
		temp.put(Lists.newArrayList(1, 2, 1), 0D);
		temp.put(Lists.newArrayList(1, 3, 1), 1D);
		expectedDerivative.put(Lists.newArrayList(0,1,0), temp);
		temp = new HashMap<>();
		temp.put(Lists.newArrayList(2, 0, 0), 0D);
		temp.put(Lists.newArrayList(2, 1, 0), 0D);
		temp.put(Lists.newArrayList(3, 0, 0), 0D);
		temp.put(Lists.newArrayList(3, 1, 0), 0D);
		temp.put(Lists.newArrayList(2, 0, 1), 0D);
		temp.put(Lists.newArrayList(2, 1, 1), 0D);
		temp.put(Lists.newArrayList(3, 0, 1), 0D);
		temp.put(Lists.newArrayList(3, 1, 1), 1D);
		expectedDerivative.put(Lists.newArrayList(1,0,0), temp);
		temp = new HashMap<>();
		temp.put(Lists.newArrayList(2, 2, 0), 0D);
		temp.put(Lists.newArrayList(2, 3, 0), 0D);
		temp.put(Lists.newArrayList(3, 2, 0), 0D);
		temp.put(Lists.newArrayList(3, 3, 0), 0D);
		temp.put(Lists.newArrayList(2, 2, 1), 0D);
		temp.put(Lists.newArrayList(2, 3, 1), 0D);
		temp.put(Lists.newArrayList(3, 2, 1), 0D);
		temp.put(Lists.newArrayList(3, 3, 1), 1D);
		expectedDerivative.put(Lists.newArrayList(1,1,0), temp);
		
		// When
		ForwardOutputTuple result = maxPoolingLayer.forward(input, poolingSize, PoolingType.MAX);
		MDA output = result.getOutput();
		Map<List<Integer>, Map<List<Integer>, Double>> derivative = result.getdOutByDIn();
		
		// Then
		checkExpectation(expectedResult, output);
		checkDerivatives(expectedDerivative, derivative);
	}
	

    @Test
    public void TestNonFullPool() {
        // Given
        int[] inputDimensions = { 4, 4, 2 };
        int[] poolingSize = { 2, 1, 2 };
        double element = 0;
        MDA input = new MDABuilder().withDimensions(inputDimensions).build();
        for (int k = 0; k < inputDimensions[2]; k++) {
            for (int i = 0; i < inputDimensions[0]; i++) {
                for (int j = 0; j < inputDimensions[1]; j++) {
                    put(input, element, i, j, k);
                    element++;
                }
            }
        }

        MDA expectedResult = new MDABuilder().withDimensions(2, 4, 1).build();
        put(expectedResult, 20D, 0, 0, 0);
        put(expectedResult, 21D, 0, 1, 0);
        put(expectedResult, 22D, 0, 2, 0);
        put(expectedResult, 23D, 0, 3, 0);
        put(expectedResult, 28D, 1, 0, 0);
        put(expectedResult, 29D, 1, 1, 0);
        put(expectedResult, 30D, 1, 2, 0);
        put(expectedResult, 31D, 1, 3, 0);

        Map<List<Integer>, Map<List<Integer>, Double>> expectedDerivative = new HashMap<List<Integer>, Map<List<Integer>, Double>>();
        Map<List<Integer>, Double> temp = new HashMap<>();
        temp.put(Lists.newArrayList(0, 0, 0), 0D);
        temp.put(Lists.newArrayList(0, 0, 1), 0D);
        temp.put(Lists.newArrayList(1, 0, 0), 0D);
        temp.put(Lists.newArrayList(1, 0, 1), 1D);
        expectedDerivative.put(Lists.newArrayList(0, 0, 0), temp);
        temp = new HashMap<>();
        temp.put(Lists.newArrayList(0, 1, 0), 0D);
        temp.put(Lists.newArrayList(0, 1, 1), 0D);
        temp.put(Lists.newArrayList(1, 1, 0), 0D);
        temp.put(Lists.newArrayList(1, 1, 1), 1D);
        expectedDerivative.put(Lists.newArrayList(0, 1, 0), temp);
        temp = new HashMap<>();
        temp.put(Lists.newArrayList(0, 2, 0), 0D);
        temp.put(Lists.newArrayList(0, 2, 1), 0D);
        temp.put(Lists.newArrayList(1, 2, 0), 0D);
        temp.put(Lists.newArrayList(1, 2, 1), 1D);
        expectedDerivative.put(Lists.newArrayList(0, 2, 0), temp);
        temp = new HashMap<>();
        temp.put(Lists.newArrayList(0, 3, 0), 0D);
        temp.put(Lists.newArrayList(0, 3, 1), 0D);
        temp.put(Lists.newArrayList(1, 3, 0), 0D);
        temp.put(Lists.newArrayList(1, 3, 1), 1D);
        expectedDerivative.put(Lists.newArrayList(0, 3, 0), temp);
        temp = new HashMap<>();
        temp.put(Lists.newArrayList(2, 0, 0), 0D);
        temp.put(Lists.newArrayList(2, 0, 1), 0D);
        temp.put(Lists.newArrayList(3, 0, 0), 0D);
        temp.put(Lists.newArrayList(3, 0, 1), 1D);
        expectedDerivative.put(Lists.newArrayList(1, 0, 0), temp);
        temp = new HashMap<>();
        temp.put(Lists.newArrayList(2, 1, 0), 0D);
        temp.put(Lists.newArrayList(2, 1, 1), 0D);
        temp.put(Lists.newArrayList(3, 1, 0), 0D);
        temp.put(Lists.newArrayList(3, 1, 1), 1D);
        expectedDerivative.put(Lists.newArrayList(1, 1, 0), temp);
        temp = new HashMap<>();
        temp.put(Lists.newArrayList(2, 2, 0), 0D);
        temp.put(Lists.newArrayList(2, 2, 1), 0D);
        temp.put(Lists.newArrayList(3, 2, 0), 0D);
        temp.put(Lists.newArrayList(3, 2, 1), 1D);
        expectedDerivative.put(Lists.newArrayList(1, 2, 0), temp);
        temp = new HashMap<>();
        temp.put(Lists.newArrayList(2, 3, 0), 0D);
        temp.put(Lists.newArrayList(2, 3, 1), 0D);
        temp.put(Lists.newArrayList(3, 3, 0), 0D);
        temp.put(Lists.newArrayList(3, 3, 1), 1D);
        expectedDerivative.put(Lists.newArrayList(1, 3, 0), temp);

        // When
        ForwardOutputTuple result = maxPoolingLayer.forward(input, poolingSize, PoolingType.MAX);
        MDA output = result.getOutput();
        Map<List<Integer>, Map<List<Integer>, Double>> derivative = result.getdOutByDIn();

        // Then
        checkExpectation(expectedResult, output);
        checkDerivatives(expectedDerivative, derivative);
    }
}
