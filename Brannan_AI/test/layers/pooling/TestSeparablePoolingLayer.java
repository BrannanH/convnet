//package layers.pooling;
//
//import static fundamentals.HelperLibrary.arrayAsList;
//import static org.junit.Assert.assertEquals;
//import static org.mockito.Mockito.when;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import org.junit.Before;
//import org.junit.Test;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//
//import fundamentals.HelperLibrary;
//import fundamentals.MDA;
//import fundamentals.MDABuilder;
//import layers.Feature;
//import services.DimensionVerificationService;
//
//public class TestSeparablePoolingLayer {
//	
//	@Mock
//	private DimensionVerificationService dimensionsService;
//	
//	private MaxPooling poolingLayer;
//	
//	private int[] inputDimensionArray = {28,28,10};
//	
//	private List<Integer> inputDimensions = new ArrayList<>();
//	
//	@Before
//	public void setup() {
//		MockitoAnnotations.initMocks(this);
//		poolingLayer = new MaxPoolingLayer(dimensionsService);
//		inputDimensions = HelperLibrary.arrayAsList(inputDimensionArray);
//	}
//	
//	@Test
//	public void testDivisibleDimensions() {
//		// Given
//		int[] poolDimensions = {2,7};
//		int[] poolDirections = {0,1};
//		
//		MDA featureMap = new MDABuilder().withDimensions(poolDimensions).build();
//		Feature feature = new Feature();
//		feature.setActiveDimensions(poolDirections);
//		feature.setFeatureMap(featureMap);
//		
//		when(dimensionsService.verify(inputDimensions, feature)).thenReturn(true);
//		
//		// When
//		List<Integer> results = poolingLayer.outputDimensions(inputDimensions, feature);
//		
//		// Then
//		assertEquals("First results should be correct", (Integer) (inputDimensions.get(0)/arrayAsList(poolDimensions).get(0)), results.get(0));
//		assertEquals("Second results should be correct", (Integer) (inputDimensions.get(1)/arrayAsList(poolDimensions).get(1)), results.get(1));
//		assertEquals("Third results should be unaffected", inputDimensions.get(2), results.get(2));
//	}
//	
//	@Test
//	public void testNonDivisibleDimensions() {
//		// Given
//		int[] poolDimensions = {3,5};
//		int[] poolDirections = {0,2};
//		
//		MDA featureMap = new MDABuilder().withDimensions(poolDimensions).build();
//		Feature feature = new Feature();
//		feature.setActiveDimensions(poolDirections);
//		feature.setFeatureMap(featureMap);
//				
//		// When
//		List<Integer> results = poolingLayer.outputDimensions(inputDimensions, feature);
//						
//		// Then
//		assertEquals("First results should be correct", (Integer) (Math.floorDiv(inputDimensions.get(0), poolDimensions[0])), results.get(0));
//		assertEquals("Second results should be unaffected", inputDimensions.get(1), results.get(1));
//		assertEquals("Third results should be unaffected", (Integer) Math.floorDiv(inputDimensions.get(2),poolDimensions[1]), results.get(2));
//	}
//
//}
