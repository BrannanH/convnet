package layers;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import fundamentals.MultiD;
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
		MultiD input = new MultiD(inputDimensions);
		for(int i = 0; i < inputDimensions[0]; i++) {
			for(int j = 0; j < inputDimensions[1]; j++) {
				input.put(element, i, j);
				element++;
			}
		}
		
		Feature feature = new Feature();
		MultiD poolMap = new MultiD(poolingSize);
		feature.setActiveDimensions(poolingDimensions);
		feature.setFeatureMap(poolMap);
		
		when(dimensionVerificationService.verify(any(int[].class), eq(feature))).thenReturn(true);
		
		MultiD expectedResult = new MultiD(2,4);
		expectedResult.put(4D, 0, 0);
		expectedResult.put(5D, 0, 1);
		expectedResult.put(6D, 0, 2);
		expectedResult.put(7D, 0, 3);
		expectedResult.put(12D, 1, 0);
		expectedResult.put(13D, 1, 1);
		expectedResult.put(14D, 1, 2);
		expectedResult.put(15D, 1, 3);
		
		MultiD expectedDerivative = new MultiD(inputDimensions);
		expectedDerivative.put(1D, 1, 0);
		expectedDerivative.put(1D, 1, 1);
		expectedDerivative.put(1D, 1, 2);
		expectedDerivative.put(1D, 1, 3);
		expectedDerivative.put(1D, 3, 0);
		expectedDerivative.put(1D, 3, 1);
		expectedDerivative.put(1D, 3, 2);
		expectedDerivative.put(1D, 3, 3);
		
		// When
		ForwardOutputTuple output = maxPoolingLayer.forward(input, feature);
		MultiD result = output.getOutput();
		MultiD derivative = output.getdOutByDIn();
		
		// Then
		assertEquals("The dimensions of the output should be correct", expectedResult.getDimensions()[0], result.getDimensions()[0]);
		assertEquals("The dimensions of the output should be correct", expectedResult.getDimensions()[1], result.getDimensions()[1]);
		assertEquals("Each element should be correct", expectedResult.get(0,0), result.get(0,0), 0);
		assertEquals("Each element should be correct", expectedResult.get(0,1), result.get(0,1), 0);
		assertEquals("Each element should be correct", expectedResult.get(0,2), result.get(0,2), 0);
		assertEquals("Each element should be correct", expectedResult.get(0,3), result.get(0,3), 0);
		assertEquals("Each element should be correct", expectedResult.get(1,0), result.get(1,0), 0);
		assertEquals("Each element should be correct", expectedResult.get(1,1), result.get(1,1), 0);
		assertEquals("Each element should be correct", expectedResult.get(1,2), result.get(1,2), 0);
		assertEquals("Each element should be correct", expectedResult.get(1,3), result.get(1,3), 0);
		
		assertEquals("The dimensions of the output should be correct", expectedDerivative.getDimensions()[0], derivative.getDimensions()[0]);
		assertEquals("The dimensions of the output should be correct", expectedDerivative.getDimensions()[1], derivative.getDimensions()[1]);
		assertEquals("Each element should be correct", expectedDerivative.get(0,0), derivative.get(0,0), 0);
		assertEquals("Each element should be correct", expectedDerivative.get(0,1), derivative.get(0,1), 0);
		assertEquals("Each element should be correct", expectedDerivative.get(0,2), derivative.get(0,2), 0);
		assertEquals("Each element should be correct", expectedDerivative.get(0,3), derivative.get(0,3), 0);
		assertEquals("Each element should be correct", expectedDerivative.get(1,0), derivative.get(1,0), 0);
		assertEquals("Each element should be correct", expectedDerivative.get(1,1), derivative.get(1,1), 0);
		assertEquals("Each element should be correct", expectedDerivative.get(1,2), derivative.get(1,2), 0);
		assertEquals("Each element should be correct", expectedDerivative.get(1,3), derivative.get(1,3), 0);
		assertEquals("Each element should be correct", expectedDerivative.get(2,0), derivative.get(2,0), 0);
		assertEquals("Each element should be correct", expectedDerivative.get(2,1), derivative.get(2,1), 0);
		assertEquals("Each element should be correct", expectedDerivative.get(2,2), derivative.get(2,2), 0);
		assertEquals("Each element should be correct", expectedDerivative.get(2,3), derivative.get(2,3), 0);
		assertEquals("Each element should be correct", expectedDerivative.get(3,0), derivative.get(3,0), 0);
		assertEquals("Each element should be correct", expectedDerivative.get(3,1), derivative.get(3,1), 0);
		assertEquals("Each element should be correct", expectedDerivative.get(3,2), derivative.get(3,2), 0);
		assertEquals("Each element should be correct", expectedDerivative.get(3,3), derivative.get(3,3), 0);
	}
	
	@Test
	public void TestBigPool() {
		// Given
		int[] inputDimensions = {4,4};
		int[] poolingDimensions = {0,1};
		int[] poolingSize = {2,2};
		double element = 0;
		MultiD input = new MultiD(inputDimensions);
		for(int i = 0; i < inputDimensions[0]; i++) {
			for(int j = 0; j < inputDimensions[1]; j++) {
				input.put(element, i, j);
				element++;
			}
		}
		
		Feature feature = new Feature();
		MultiD poolMap = new MultiD(poolingSize);
		feature.setActiveDimensions(poolingDimensions);
		feature.setFeatureMap(poolMap);
		
		when(dimensionVerificationService.verify(any(int[].class), eq(feature))).thenReturn(true);
		
		MultiD expectedResult = new MultiD(2,2);
		expectedResult.put(5D, 0, 0);
		expectedResult.put(7D, 0, 1);
		expectedResult.put(13D, 1, 0);
		expectedResult.put(15D, 1, 1);
		
		MultiD expectedDerivative = new MultiD(inputDimensions);
		expectedDerivative.put(1D, 1, 1);
		expectedDerivative.put(1D, 1, 3);
		expectedDerivative.put(1D, 3, 1);
		expectedDerivative.put(1D, 3, 3);
		
		// When
		ForwardOutputTuple output = maxPoolingLayer.forward(input, feature);
		MultiD result = output.getOutput();
		MultiD derivative = output.getdOutByDIn();
		
		// Then
		assertEquals("The dimensions of the output should be correct", expectedResult.getDimensions()[0], result.getDimensions()[0]);
		assertEquals("The dimensions of the output should be correct", expectedResult.getDimensions()[1], result.getDimensions()[1]);
		assertEquals("Each element should be correct", expectedResult.get(0,0), result.get(0,0), 0);
		assertEquals("Each element should be correct", expectedResult.get(0,1), result.get(0,1), 0);
		assertEquals("Each element should be correct", expectedResult.get(1,0), result.get(1,0), 0);
		assertEquals("Each element should be correct", expectedResult.get(1,1), result.get(1,1), 0);
		
		assertEquals("The dimensions of the output should be correct", expectedDerivative.getDimensions()[0], derivative.getDimensions()[0]);
		assertEquals("The dimensions of the output should be correct", expectedDerivative.getDimensions()[1], derivative.getDimensions()[1]);
		assertEquals("Each element should be correct", expectedDerivative.get(0,0), derivative.get(0,0), 0);
		assertEquals("Each element should be correct", expectedDerivative.get(0,1), derivative.get(0,1), 0);
		assertEquals("Each element should be correct", expectedDerivative.get(0,2), derivative.get(0,2), 0);
		assertEquals("Each element should be correct", expectedDerivative.get(0,3), derivative.get(0,3), 0);
		assertEquals("Each element should be correct", expectedDerivative.get(1,0), derivative.get(1,0), 0);
		assertEquals("Each element should be correct", expectedDerivative.get(1,1), derivative.get(1,1), 0);
		assertEquals("Each element should be correct", expectedDerivative.get(1,2), derivative.get(1,2), 0);
		assertEquals("Each element should be correct", expectedDerivative.get(1,3), derivative.get(1,3), 0);
		assertEquals("Each element should be correct", expectedDerivative.get(2,0), derivative.get(2,0), 0);
		assertEquals("Each element should be correct", expectedDerivative.get(2,1), derivative.get(2,1), 0);
		assertEquals("Each element should be correct", expectedDerivative.get(2,2), derivative.get(2,2), 0);
		assertEquals("Each element should be correct", expectedDerivative.get(2,3), derivative.get(2,3), 0);
		assertEquals("Each element should be correct", expectedDerivative.get(3,0), derivative.get(3,0), 0);
		assertEquals("Each element should be correct", expectedDerivative.get(3,1), derivative.get(3,1), 0);
		assertEquals("Each element should be correct", expectedDerivative.get(3,2), derivative.get(3,2), 0);
		assertEquals("Each element should be correct", expectedDerivative.get(3,3), derivative.get(3,3), 0);
	}
	
	@Test
	public void TestBiggestPool() {
		// Given
		int[] inputDimensions = {4,4,2};
		int[] poolingDimensions = {0,1,2};
		int[] poolingSize = {2,2,2};
		double element = 0;
		MultiD input = new MultiD(inputDimensions);
		for(int k = 0; k < inputDimensions[2]; k++) {
			for(int i = 0; i < inputDimensions[0]; i++) {
				for(int j = 0; j < inputDimensions[1]; j++) {
					input.put(element, i, j, k);
					element++;
				}
			}
		}
		
		Feature feature = new Feature();
		MultiD poolMap = new MultiD(poolingSize);
		feature.setActiveDimensions(poolingDimensions);
		feature.setFeatureMap(poolMap);
		
		when(dimensionVerificationService.verify(any(int[].class), eq(feature))).thenReturn(true);
		
		MultiD expectedResult = new MultiD(2,2,1);
		expectedResult.put(21D, 0, 0, 0);
		expectedResult.put(23D, 0, 1, 0);
		expectedResult.put(29D, 1, 0, 0);
		expectedResult.put(31D, 1, 1, 0);
		
		MultiD expectedDerivative = new MultiD(inputDimensions);
		expectedDerivative.put(1D, 1, 1, 1);
		expectedDerivative.put(1D, 1, 3, 1);
		expectedDerivative.put(1D, 3, 1, 1);
		expectedDerivative.put(1D, 3, 3, 1);
		
		// When
		ForwardOutputTuple result = maxPoolingLayer.forward(input, feature);
		MultiD output = result.getOutput();
		MultiD derivative = result.getdOutByDIn();
		
		// Then
		assertEquals("The dimensions of the output should be correct", expectedResult.getDimensions()[0], output.getDimensions()[0]);
		assertEquals("The dimensions of the output should be correct", expectedResult.getDimensions()[1], output.getDimensions()[1]);
		assertEquals("The dimensions of the output should be correct", expectedResult.getDimensions()[2], output.getDimensions()[2]);
		assertEquals("Each element should be correct", expectedResult.get(0,0,0), output.get(0,0,0), 0);
		assertEquals("Each element should be correct", expectedResult.get(0,1,0), output.get(0,1,0), 0);
		assertEquals("Each element should be correct", expectedResult.get(1,0,0), output.get(1,0,0), 0);
		assertEquals("Each element should be correct", expectedResult.get(1,1,0), output.get(1,1,0), 0);
		
		
		assertEquals("The dimenions of the derivative should be correct", expectedDerivative.getDimensions().length, derivative.getDimensions().length);
		assertEquals("The dimenions of the derivative should be correct", expectedDerivative.getDimensions()[0], derivative.getDimensions()[0]);
		assertEquals("The dimenions of the derivative should be correct", expectedDerivative.getDimensions()[1], derivative.getDimensions()[1]);
		assertEquals("The dimenions of the derivative should be correct", expectedDerivative.getDimensions()[2], derivative.getDimensions()[2]);

		for(int k = 0; k < expectedDerivative.getDimensions()[2]; k++){
			for(int j = 0; j < expectedDerivative.getDimensions()[2]; j++){
				for(int i = 0; i < expectedDerivative.getDimensions()[2]; i++){
					assertEquals("Each element should be correct " + i + " " + j + " " + k, expectedDerivative.get(i,j,k), derivative.get(i,j,k), 0);
				}
			}
		}
	}
	
	@Test
	public void TestNonFullPool() {
		// Given
		int[] inputDimensions = {4,4,2};
		int[] poolingDimensions = {0,2};
		int[] poolingSize = {2,2};
		double element = 0;
		MultiD input = new MultiD(inputDimensions);
		for(int k = 0; k < inputDimensions[2]; k++) {
			for(int i = 0; i < inputDimensions[0]; i++) {
				for(int j = 0; j < inputDimensions[1]; j++) {
					input.put(element, i, j, k);
					element++;
				}
			}
		}
		
		Feature feature = new Feature();
		MultiD poolMap = new MultiD(poolingSize);
		feature.setActiveDimensions(poolingDimensions);
		feature.setFeatureMap(poolMap);
		
		when(dimensionVerificationService.verify(any(int[].class), eq(feature))).thenReturn(true);
		
		MultiD expectedResult = new MultiD(2,4,1);
		expectedResult.put(20D, 0, 0, 0);
		expectedResult.put(21D, 0, 1, 0);
		expectedResult.put(22D, 0, 2, 0);
		expectedResult.put(23D, 0, 3, 0);
		expectedResult.put(28D, 1, 0, 0);
		expectedResult.put(29D, 1, 1, 0);
		expectedResult.put(30D, 1, 2, 0);
		expectedResult.put(31D, 1, 3, 0);
		
		MultiD expectedDerivative = new MultiD(inputDimensions);
		expectedDerivative.put(1D, 1, 0, 1);
		expectedDerivative.put(1D, 1, 1, 1);
		expectedDerivative.put(1D, 1, 2, 1);
		expectedDerivative.put(1D, 1, 3, 1);
		expectedDerivative.put(1D, 3, 0, 1);
		expectedDerivative.put(1D, 3, 1, 1);
		expectedDerivative.put(1D, 3, 2, 1);
		expectedDerivative.put(1D, 3, 3, 1);
		
		
		// When
		ForwardOutputTuple result = maxPoolingLayer.forward(input, feature);
		MultiD output = result.getOutput();
		MultiD derivative = result.getdOutByDIn();
		
		// Then
		assertEquals("The dimensions of the output should be correct", expectedResult.getDimensions()[0], output.getDimensions()[0]);
		assertEquals("The dimensions of the output should be correct", expectedResult.getDimensions()[1], output.getDimensions()[1]);
		assertEquals("The dimensions of the output should be correct", expectedResult.getDimensions()[2], output.getDimensions()[2]);
		assertEquals("Each element should be correct", expectedResult.get(0,0,0), output.get(0,0,0), 0);
		assertEquals("Each element should be correct", expectedResult.get(0,1,0), output.get(0,1,0), 0);
		assertEquals("Each element should be correct", expectedResult.get(0,2,0), output.get(0,2,0), 0);
		assertEquals("Each element should be correct", expectedResult.get(0,3,0), output.get(0,3,0), 0);
		assertEquals("Each element should be correct", expectedResult.get(1,0,0), output.get(1,0,0), 0);
		assertEquals("Each element should be correct", expectedResult.get(1,1,0), output.get(1,1,0), 0);
		assertEquals("Each element should be correct", expectedResult.get(1,2,0), output.get(1,2,0), 0);
		assertEquals("Each element should be correct", expectedResult.get(1,3,0), output.get(1,3,0), 0);
		
		assertEquals("The dimenions of the derivative should be correct", expectedDerivative.getDimensions().length, derivative.getDimensions().length);
		assertEquals("The dimenions of the derivative should be correct", expectedDerivative.getDimensions()[0], derivative.getDimensions()[0]);
		assertEquals("The dimenions of the derivative should be correct", expectedDerivative.getDimensions()[1], derivative.getDimensions()[1]);
		assertEquals("The dimenions of the derivative should be correct", expectedDerivative.getDimensions()[2], derivative.getDimensions()[2]);
		
		for(int k = 0; k < expectedDerivative.getDimensions()[2]; k++){
			for(int j = 0; j < expectedDerivative.getDimensions()[2]; j++){
				for(int i = 0; i < expectedDerivative.getDimensions()[2]; i++){
					assertEquals("Each element should be correct " + i + " " + j + " " + k, expectedDerivative.get(i,j,k), derivative.get(i,j,k), 0);
				}
			}
		}
	}
}
