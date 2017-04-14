package layers;

import static fundamentals.HelperLibrary.arrayAsList;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import fundamentals.MDA;
import fundamentals.MDABuilder;
import services.DimensionVerificationService;

public class TestConvolutionLayer {
	
	@Mock
	private static DimensionVerificationService dimensionsService;
	
	private ConvolutionLayer convolutionLayer;
	
	private int[] inputDimensionsArray = {28,28,10};
	
	private List<Integer> inputDimensions = new ArrayList<>();
	
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		convolutionLayer = new ConvolutionLayer(dimensionsService);
		inputDimensions = arrayAsList(inputDimensionsArray);
	}
	
	@Test
	public void testOddDimensions() {
		// Given
		int[] filterDimensions = {5,5};
		int[] filteringDimensions = {0,1};
		int[] inputDimensions = {28,28,12};
		
		MDA featureMap = new MDABuilder().withDimensions(filterDimensions).build();
		Feature feature = new Feature();
		feature.setActiveDimensions(filteringDimensions);
		feature.setFeatureMap(featureMap);
		
		when(dimensionsService.verify(arrayAsList(inputDimensions), feature)).thenReturn(true);
		
		// When
		List<Integer> results = convolutionLayer.outputDimensions(arrayAsList(inputDimensions), feature);
		
		// Then
		assertEquals("Dimension 0 should reduce by 4", (Integer) (inputDimensions[0] - filterDimensions[0] + 1), results.get(0));
		assertEquals("Dimension 1 should reduce by 4", (Integer) (inputDimensions[1] - filterDimensions[1] + 1), results.get(1));
		assertEquals("Dimension 2 should be unaffected", (Integer) inputDimensions[2], results.get(2));
	}
	
	@Test
	public void testEvenDimensions() {
		// Given
		int[] filterDimensions = {4,4};
		int[] filteringDimensions = {0,1};
		
		MDA featureMap = new MDABuilder().withDimensions(filterDimensions).build();
		Feature feature = new Feature();
		feature.setActiveDimensions(filteringDimensions);
		feature.setFeatureMap(featureMap);
		
		when(dimensionsService.verify(inputDimensions, feature)).thenReturn(true);
		
		// When
		List<Integer> results = convolutionLayer.outputDimensions(inputDimensions, feature);
				
		// Then
		assertEquals("Dimensions should reduce by 4", (Integer) (inputDimensions.get(0) - filterDimensions[0]), results.get(0));
		assertEquals("Dimension 1 should reduce by 4", (Integer) (inputDimensions.get(1) - filterDimensions[1]), results.get(1));
		assertEquals("Dimension 2 should be unaffected", inputDimensions.get(2), results.get(2));
	}


}
