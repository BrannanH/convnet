package com.brannan.convnet.network.layers.convolution;

import static com.brannan.convnet.network.fundamentals.HelperLibrary.arrayEquality;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.brannan.convnet.network.layers.convolution.ConvolutionLibrary.PaddingType;
import com.brannan.convnet.network.services.DimensionVerificationService;

/**
 * 
 * @author Brannan R. Hancock
 *
 */
public class TestIgnorePadding {
    
    ConvolutionLayer layer;
    @Mock private DimensionVerificationService dimensionVerificationService;
    
    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        layer = new ConvolutionLayer(dimensionVerificationService);
    }
    

    /**
     * Verifies the output dimensions are correct.
     */
    @Test
    public void test() {
        // Given
        int[] operandDimensions = {1, 7};
        int[] filterDimensions = {1, 3};
        int[] expectedOutputDimensions = {1, 5};

        // When
        int[] outputDimensions = layer.outputDimensions(operandDimensions, filterDimensions,
                PaddingType.IGNORE);

        // Then
        assertTrue("Output dimensions should be correct", arrayEquality(expectedOutputDimensions, outputDimensions));
    }

}
