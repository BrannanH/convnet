package com.brannan.convnet.network.layers.convolution;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.brannan.convnet.network.layers.convolution.ConvolutionLayer;
import com.brannan.convnet.network.layers.convolution.ConvolutionLibrary.PaddingType;
import com.brannan.convnet.network.services.DimensionVerificationService;
import com.google.common.collect.Lists;

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
        List<Integer> operandDimensions = Lists.newArrayList(1, 7);
        List<Integer> filterDimensions = Lists.newArrayList(1, 3);
        List<Integer> expectedOutputDimensions = Lists.newArrayList(1, 5);

        // When
        List<Integer> outputDimensions = layer.outputDimensions(operandDimensions, filterDimensions,
                PaddingType.IGNORE);

        // Then
        assertEquals("Output dimensions should be correct", expectedOutputDimensions, outputDimensions);
    }

}
