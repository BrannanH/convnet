package layers.pooling;

import static fundamentals.MDAHelper.put;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static testsupport.CompareMDAs.checkExpectation;

import org.junit.Before;
import org.junit.Test;

import fundamentals.MDA;
import fundamentals.MDABuilder;
import layers.Feature;
import layers.ForwardOutputTuple;
import services.DimensionVerificationService;
import testsupport.CompareMDAs;

/**
 * Test class for the Median Test Layer
 * @author Brannan
 *
 */
public class TestMedianLayer {

	private double count = 1;
	
	private DimensionVerificationService dimensionsVerificationService = mock(DimensionVerificationService.class);
	
	private MedianPoolingLayer layer;
	
	@Before
	public void setup() {
		when(dimensionsVerificationService.verify(anyListOf(Integer.class), any(Feature.class))).thenReturn(true);
		layer = new MedianPoolingLayer(dimensionsVerificationService);
	}
	
	@Test
	public void testInstantiation() {
		assertNotNull(layer);
	}
	
	@Test
	public void testSimplePool() {
		// Given
		int[] inputDimensions = {1,6};
		MDA input = new MDABuilder().withDimensions(inputDimensions).build();
		put(input, 1, 0,0);
		put(input, 2, 0,1);
		put(input, 3, 0,2);
		put(input, 4, 0,3);
		put(input, 5, 0,4);
		put(input, 6, 0,5);
		
		int[] activeDimensions = {0, 1};
		int[] poolSizes = {1, 3};
		MDA featureMap = new MDABuilder().withDimensions(poolSizes).build();
		Feature inputFeature = new Feature(featureMap, activeDimensions);
		
		MDA expectedOutput = new MDABuilder().withDimensions(1,2).build();
		put(expectedOutput, 2, 0,0);
		put(expectedOutput, 5, 0,1);
		
		// When
		ForwardOutputTuple output = layer.forward(input, inputFeature);
		MDA forward = output.getOutput();
		
		// Then
		CompareMDAs.checkExpectation(expectedOutput, forward);
	}

	@Test
	public void testThreeByThreePool() {
		// Given
		int[] inputDimensions = {6,9};
		MDA input = populateMultiD(inputDimensions);
		
		int[] activeDimensions = {0,1};
		int[] poolSizes = {3,3};
		MDA featureMap = new MDABuilder().withDimensions(poolSizes).build();
		Feature inputFeature = new Feature(featureMap, activeDimensions);
		
		MDA expectedOutput = new MDABuilder().withDimensions(2,3).build();
		put(expectedOutput, 8, 0,0);
		put(expectedOutput, 26, 0,1);
		put(expectedOutput, 44, 0,2);
		put(expectedOutput, 11, 1,0);
		put(expectedOutput, 29, 1,1);
		put(expectedOutput, 47, 1,2);
		
		// When
		ForwardOutputTuple output = layer.forward(input, inputFeature);
		MDA forward = output.getOutput();
		
		// Then
		checkExpectation(expectedOutput, forward);
	}
	
	
	@Test
	public void testThreeByThreeBy2Pool() {
		// Given
		int[] inputDimensions = {6,6,2};
		MDA input = populateMultiD(inputDimensions);
		
		int[] activeDimensions = {0,1,2};
		int[] poolSizes = {3,3,2};
		MDA featureMap = new MDABuilder().withDimensions(poolSizes).build();
		Feature inputFeature = new Feature(featureMap, activeDimensions);
		
		MDA expectedOutput = new MDABuilder().withDimensions(2,2,1).build();
		put(expectedOutput, 15D, 0,0,0);
		put(expectedOutput, 18D, 1,0,0);
		put(expectedOutput, 33D, 0,1,0);
		put(expectedOutput, 36D, 1,1,0);
		
		// When
		ForwardOutputTuple output = layer.forward(input, inputFeature);
		MDA forward = output.getOutput();
		
		// Then
		checkExpectation(expectedOutput, forward);
	}
	
	
	private MDA populateMultiD(int[] inputDimensions) {
		int[] position = new int[inputDimensions.length];
		return populate(new MDABuilder().withDimensions(inputDimensions).build(), position, inputDimensions.length-1);
	}

	private MDA populate(MDA mda, int[] position, int place) {
		
		for(int i = 0; i < mda.getDimensions().get(place); i++) {
			position[place] = i;
			if(place == 0) {
				put(mda, count, position);
				count++;
			} else {
				mda = populate(mda, position, place-1);
			}
		}
		return mda;
	}
	
}
