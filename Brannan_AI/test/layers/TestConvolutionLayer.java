package layers;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import fundamentals.MultiD;
import services.DimensionVerificationService;

public class TestConvolutionLayer {
	
	@Mock
	private static DimensionVerificationService dimensionsService;
	
	private ConvolutionLayer convolutionLayer;
	
	private int[] inputDimensions = {28,28,10};
	
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		convolutionLayer = new ConvolutionLayer(dimensionsService);
	}
	
	@Test
	public void test() {
		// Given
		int[] filterDimensions = {5,5};
		int[] filteringDimensions = {0,1};
		int[] inputDimensions = {28,28,12};
		
		MultiD featureMap = new MultiD(filterDimensions);
		Feature feature = new Feature();
		feature.setActiveDimensions(filteringDimensions);
		feature.setFeatureMap(featureMap);
		
		when(dimensionsService.verify(inputDimensions, feature)).thenReturn(true);
		
		// When
		int[] results = convolutionLayer.outputDimensions(inputDimensions, feature);
		
		// Then
		assertEquals("Dimension 0 should reduce by 4", inputDimensions[0] - filterDimensions[0] + 1, results[0]);
		assertEquals("Dimension 1 should reduce by 4", inputDimensions[1] - filterDimensions[1] + 1, results[1]);
		assertEquals("Dimension 2 should be unaffected", inputDimensions[2], results[2]);
	}
	
	@Test
	public void test2() {
		// Given
		int[] filterDimensions = {4,4};
		int[] filteringDimensions = {0,1};
		
		MultiD featureMap = new MultiD(filterDimensions);
		Feature feature = new Feature();
		feature.setActiveDimensions(filteringDimensions);
		feature.setFeatureMap(featureMap);
		
		when(dimensionsService.verify(inputDimensions, feature)).thenReturn(true);
		
		// When
		int[] results = convolutionLayer.outputDimensions(inputDimensions, feature);
				
		// Then
		assertEquals("Dimensions should reduce by 4", inputDimensions[0] - filterDimensions[0], results[0]);
		assertEquals("Dimension 1 should reduce by 4", inputDimensions[1] - filterDimensions[1], results[1]);
		assertEquals("Dimension 2 should be unaffected", inputDimensions[2], results[2]);
	}


}
