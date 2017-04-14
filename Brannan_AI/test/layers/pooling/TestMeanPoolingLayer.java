package layers.pooling;

import static fundamentals.MDAHelper.get;
import static fundamentals.MDAHelper.put;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import fundamentals.MDA;
import fundamentals.MDABuilder;
import layers.Feature;
import services.DimensionVerificationService;

public class TestMeanPoolingLayer {
	
	private MeanPoolingLayer layer;
	
	@Mock
	private DimensionVerificationService dimensionVerificationService;
	
	@Before
	public void setup(){
		MockitoAnnotations.initMocks(this);
		when(dimensionVerificationService.verify(anyListOf(Integer.class), any(Feature.class))).thenReturn(true);
		layer = new MeanPoolingLayer(dimensionVerificationService);
	}
	
	@Test
	public void testSmallPool(){
		// Given;
		int[] inputDimensions = {4,4};
		int[] poolingDimensions = {0, 1}; 
		MDA operand = new MDABuilder().withDimensions(inputDimensions).build();
		double element = 1D;
		
		for(int j = 0; j < inputDimensions[1]; j++){
			for(int i = 0; i < inputDimensions[1]; i++){
				put(operand, element, j, i);
				element++;
			}
		}
		
		int poolingSize = 2;
		MDA featureMap = new MDABuilder().withDimensions(poolingSize, 1).build();
		Feature feature = new Feature();
		feature.setActiveDimensions(poolingDimensions);
		feature.setFeatureMap(featureMap);
		
		MDA expectedOutput = new MDABuilder().withDimensions(2,4).build();
		put(expectedOutput, 3, 0,0);
		put(expectedOutput, 4, 0,1);
		put(expectedOutput, 5, 0,2);
		put(expectedOutput, 6, 0,3);
		put(expectedOutput, 11, 1,0);
		put(expectedOutput, 12, 1,1);
		put(expectedOutput, 13, 1,2);
		put(expectedOutput, 14, 1,3);
		
		// When
		MDA output = layer.forward(operand, feature).getOutput();
		
		// Then
		for(int i = 0; i < 2; i++){
			for(int j = 0; j < 4; j++){
				assertEquals("", get(expectedOutput, i,j), get(output, i,j), 0);
			}
		}
	}
	

}
