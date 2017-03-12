package layers;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import fundamentals.MultiD;
import services.DimensionVerificationService;

public class TestPoolingLayer {
	
	@Mock
	private DimensionVerificationService dimensionsService;
	
	private PoolingLayer poolingLayer;
	
	private int[] inputDimensions = {28,28,10};
	
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		poolingLayer = new MaxPoolingLayer(dimensionsService);
	}
	
	@Test
	public void test() {
		// Given
		int[] poolDimensions = {2,7};
		int[] poolDirections = {0,1};
		
		MultiD featureMap = new MultiD(poolDimensions);
		Feature feature = new Feature();
		feature.setActiveDimensions(poolDirections);
		feature.setFeatureMap(featureMap);
		
		when(dimensionsService.verify(inputDimensions, feature)).thenReturn(true);
		
		// When
		int[] results = poolingLayer.outputDimensions(inputDimensions, feature);
		
		// Then
		assertEquals("First results should be correct", inputDimensions[0]/poolDimensions[0], results[0]);
		assertEquals("Second results should be correct", inputDimensions[1]/poolDimensions[1], results[1]);
		assertEquals("Third results should be unaffected", inputDimensions[2], results[2]);
	}
	
	@Test
	public void test2() {
		// Given
		int[] poolDimensions = {3,5};
		int[] poolDirections = {0,2};
		
		MultiD featureMap = new MultiD(poolDimensions);
		Feature feature = new Feature();
		feature.setActiveDimensions(poolDirections);
		feature.setFeatureMap(featureMap);
				
		// When
		int[] results = poolingLayer.outputDimensions(inputDimensions, feature);
						
		// Then
		assertEquals("First results should be correct", Math.floorDiv(inputDimensions[0],poolDimensions[0]), results[0]);
		assertEquals("Second results should be unaffected", inputDimensions[1], results[1]);
		assertEquals("Third results should be unaffected", Math.floorDiv(inputDimensions[2],poolDimensions[1]), results[2]);
	}

}
