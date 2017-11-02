package com.brannanhancock.convnet.network.layers.convolution;

import static com.brannanhancock.convnet.network.fundamentals.HelperLibrary.arrayEquality;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.brannanhancock.convnet.network.fundamentals.MDA;
import com.brannanhancock.convnet.network.fundamentals.MDABuilder;
import com.brannanhancock.convnet.network.layers.convolution.ConvolutionLayer;
import com.brannanhancock.convnet.network.layers.convolution.ConvolutionLibrary.PaddingType;
import com.brannanhancock.convnet.network.services.DimensionVerificationService;

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
        final int[] operandDimensions = {1, 5};
        final int[] filterDimensions = {1, 3};
        final int[] expectedOutputDimensions = {1, 3};

        final MDA operand = new MDABuilder(operandDimensions).build();
        final MDA feature = new MDABuilder(filterDimensions).build();
        final PaddingType paddingType = PaddingType.IGNORE;

        // When
        final MDA output = layer.forwardNoTrain(operand, feature, paddingType);

        // Then
        Mockito.verify(dimensionVerificationService).verifyLeftBiggerThanRight(operandDimensions, filterDimensions);
        assertTrue("Output Dimensions should be as expected", arrayEquality(expectedOutputDimensions, output.getDimensions()));
    }
}
