package layers.pooling;

import static fundamentals.MDAHelper.put;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;
import static testsupport.CompareMDAs.checkExpectation;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import fundamentals.MDA;
import fundamentals.MDABuilder;
import layers.Feature;
import layers.ForwardOutputTuple;
import services.DimensionVerificationService;

/**
 * This class tests the MaxPoolingLayer
 * @author Brannan
 *
 */
public class TestMaxPoolingLayer {
	
	@Mock
	DimensionVerificationService dimensionVerificationService;
	
	private MaxPoolingLayer maxPoolingLayer;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		maxPoolingLayer = new MaxPoolingLayer(dimensionVerificationService);
	}
	
	@Test
	public void TestSmallPool() {
		// Given
		int[] inputDimensions = {4,4};
		int[] poolingDimensions = {0};
		int poolingSize = 2;
		double element = 0;
		MDA input = new MDABuilder().withDimensions(inputDimensions).build();
		for(int i = 0; i < inputDimensions[0]; i++) {
			for(int j = 0; j < inputDimensions[1]; j++) {
				put(input, element, i, j);
				element++;
			}
		}
		
		Feature feature = new Feature();
		MDA poolMap = new MDABuilder().withDimensions(poolingSize).build();
		feature.setActiveDimensions(poolingDimensions);
		feature.setFeatureMap(poolMap);
		
		when(dimensionVerificationService.verify((Matchers.anyListOf(Integer.class)), eq(feature))).thenReturn(true);
		
		MDA expectedResult = new MDABuilder().withDimensions(2,4).build();
		put(expectedResult, 4D, 0, 0);
		put(expectedResult, 5D, 0, 1);
		put(expectedResult, 6D, 0, 2);
		put(expectedResult, 7D, 0, 3);
		put(expectedResult, 12D, 1, 0);
		put(expectedResult, 13D, 1, 1);
		put(expectedResult, 14D, 1, 2);
		put(expectedResult, 15D, 1, 3);
		
		MDA expectedDerivative = new MDABuilder().withDimensions(inputDimensions).build();
		put(expectedDerivative, 1D, 1, 0);
		put(expectedDerivative, 1D, 1, 1);
		put(expectedDerivative, 1D, 1, 2);
		put(expectedDerivative, 1D, 1, 3);
		put(expectedDerivative, 1D, 3, 0);
		put(expectedDerivative, 1D, 3, 1);
		put(expectedDerivative, 1D, 3, 2);
		put(expectedDerivative, 1D, 3, 3);
		
		// When
		ForwardOutputTuple output = maxPoolingLayer.forward(input, feature);
		MDA result = output.getOutput();
		MDA derivative = output.getdOutByDIn();
		
		// Then
		checkExpectation(expectedResult, result);
//		checkExpectation(expectedDerivative, derivative);
	}
	
	@Test
	public void TestBigPool() {
		// Given
		int[] inputDimensions = {4,4};
		int[] poolingDimensions = {0,1};
		int[] poolingSize = {2,2};
		double element = 0;
		MDA input = new MDABuilder().withDimensions(inputDimensions).build();
		for(int i = 0; i < inputDimensions[0]; i++) {
			for(int j = 0; j < inputDimensions[1]; j++) {
				put(input, element, i, j);
				element++;
			}
		}
		
		Feature feature = new Feature();
		MDA poolMap = new MDABuilder().withDimensions(poolingSize).build();
		feature.setActiveDimensions(poolingDimensions);
		feature.setFeatureMap(poolMap);
		
		when(dimensionVerificationService.verify(anyListOf(Integer.class), eq(feature))).thenReturn(true);
		
		MDA expectedResult = new MDABuilder().withDimensions(2,2).build();
		put(expectedResult, 5D, 0, 0);
		put(expectedResult, 7D, 0, 1);
		put(expectedResult, 13D, 1, 0);
		put(expectedResult, 15D, 1, 1);
		
		MDA expectedDerivative = new MDABuilder().withDimensions(inputDimensions).build();
		put(expectedDerivative, 1D, 1, 1);
		put(expectedDerivative, 1D, 1, 3);
		put(expectedDerivative, 1D, 3, 1);
		put(expectedDerivative, 1D, 3, 3);
		
		// When
		ForwardOutputTuple output = maxPoolingLayer.forward(input, feature);
		MDA result = output.getOutput();
		MDA derivative = output.getdOutByDIn();
		
		// Then
		checkExpectation(expectedResult, result);
//		checkExpectation(expectedDerivative, derivative);
	}
	
	@Test
	public void TestBiggestPool() {
		// Given
		int[] inputDimensions = {4,4,2};
		int[] poolingDimensions = {0,1,2};
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
		
		Feature feature = new Feature();
		MDA poolMap = new MDABuilder().withDimensions(poolingSize).build();
		feature.setActiveDimensions(poolingDimensions);
		feature.setFeatureMap(poolMap);
		
		when(dimensionVerificationService.verify(anyListOf(Integer.class), eq(feature))).thenReturn(true);
		
		MDA expectedResult = new MDABuilder().withDimensions(2,2,1).build();
		put(expectedResult, 21D, 0, 0, 0);
		put(expectedResult, 23D, 0, 1, 0);
		put(expectedResult, 29D, 1, 0, 0);
		put(expectedResult, 31D, 1, 1, 0);
		
		MDA expectedDerivative = new MDABuilder().withDimensions(inputDimensions).build();
		put(expectedDerivative, 1D, 1, 1, 1);
		put(expectedDerivative, 1D, 1, 3, 1);
		put(expectedDerivative, 1D, 3, 1, 1);
		put(expectedDerivative, 1D, 3, 3, 1);
		
		// When
		ForwardOutputTuple result = maxPoolingLayer.forward(input, feature);
		MDA output = result.getOutput();
		MDA derivative = result.getdOutByDIn();
		
		// Then
		checkExpectation(expectedResult, output);
//		checkExpectation(expectedDerivative, derivative);
	}
	
	@Test
	public void TestNonFullPool() {
		// Given
		int[] inputDimensions = {4,4,2};
		int[] poolingDimensions = {0,1,2};
		int[] poolingSize = {2,1,2};
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
		
		Feature feature = new Feature();
		MDA poolMap = new MDABuilder().withDimensions(poolingSize).build();
		feature.setActiveDimensions(poolingDimensions);
		feature.setFeatureMap(poolMap);
		
		when(dimensionVerificationService.verify(anyListOf(Integer.class), eq(feature))).thenReturn(true);
		
		MDA expectedResult = new MDABuilder().withDimensions(2,4,1).build();
		put(expectedResult, 20D, 0, 0, 0);
		put(expectedResult, 21D, 0, 1, 0);
		put(expectedResult, 22D, 0, 2, 0);
		put(expectedResult, 23D, 0, 3, 0);
		put(expectedResult, 28D, 1, 0, 0);
		put(expectedResult, 29D, 1, 1, 0);
		put(expectedResult, 30D, 1, 2, 0);
		put(expectedResult, 31D, 1, 3, 0);
		
		MDA expectedDerivative = new MDABuilder().withDimensions(inputDimensions).build();
		put(expectedDerivative, 1D, 1, 0, 1);
		put(expectedDerivative, 1D, 1, 1, 1);
		put(expectedDerivative, 1D, 1, 2, 1);
		put(expectedDerivative, 1D, 1, 3, 1);
		put(expectedDerivative, 1D, 3, 0, 1);
		put(expectedDerivative, 1D, 3, 1, 1);
		put(expectedDerivative, 1D, 3, 2, 1);
		put(expectedDerivative, 1D, 3, 3, 1);
		
		
		// When
		ForwardOutputTuple result = maxPoolingLayer.forward(input, feature);
		MDA output = result.getOutput();
		MDA derivative = result.getdOutByDIn();
		
		// Then
		checkExpectation(expectedResult, output);
//		checkExpectation(expectedDerivative, derivative);
	}
}
