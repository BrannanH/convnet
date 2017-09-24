package com.brannan.convnet.network.layers.convolution;

import static com.brannan.convnet.network.fundamentals.HelperLibrary.arrayEquality;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.brannan.convnet.network.fundamentals.MDA;
import com.brannan.convnet.network.fundamentals.MDABuilder;
import com.brannan.convnet.network.layers.convolution.ConvolutionLibrary.PaddingType;
import com.brannan.convnet.network.services.DimensionVerificationService;

/**
 * 
 * @author Brannan R. Hancock
 *
 */
public class TestConvolutionLayer {
    
    ConvolutionLayer layer;
    @Mock private DimensionVerificationService dimensionVerificationService;
    
    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        layer = new ConvolutionLayer(dimensionVerificationService);
    }
    
    /**
     * Verifies the dimension verification service is called when computing a forward pass in test mode
     */
    @Test
    public void test() {
        // Given
        int[] operandDimensions = {1, 5};
        int[] filterDimensions = {1, 3};
        int[] expectedOutputDimensions = {1, 3};
        
        MDA operand = new MDABuilder().withDimensions(operandDimensions).build();
        MDA feature = new MDABuilder().withDimensions(filterDimensions).build();
        PaddingType paddingType = PaddingType.IGNORE;
        
        // When
        MDA output = layer.forwardNoTrain(operand, feature, paddingType);
        
        // Then
        Mockito.verify(dimensionVerificationService).verifyLeftBiggerThanRight(operandDimensions, filterDimensions);
        assertTrue("Output Dimensions should be as expected", arrayEquality(expectedOutputDimensions, output.getDimensions()));
    }
}
