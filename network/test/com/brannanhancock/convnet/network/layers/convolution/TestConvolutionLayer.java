package com.brannanhancock.convnet.network.layers.convolution;

import static com.brannanhancock.convnet.fundamentals.HelperLibrary.isArrayEquality;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import com.brannanhancock.convnet.network.services.DimensionVerificationService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.brannanhancock.convnet.fundamentals.MDA;
import com.brannanhancock.convnet.fundamentals.MDABuilder;
import com.brannanhancock.convnet.network.layers.convolution.ConvolutionLayer;
import com.brannanhancock.convnet.network.layers.convolution.ConvolutionLibrary.PaddingType;

/**
 *
 * @author Brannan R. Hancock
 *
 */
public class TestConvolutionLayer {

    private ConvolutionLayer layer;
    @Mock private DimensionVerificationService dimensionVerificationService;
    private ConvolutionService convolutionService;
    private final int[] featureDimensions = {5,5};
    private MDA feature;
    private final int[] inputDimensions = {32,32};
    private final int[][] connections = new int[1][6];


    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        convolutionService = new ConvolutionService(dimensionVerificationService);
        feature = new MDABuilder(featureDimensions).build();
        layer = new ConvolutionLayer(feature, inputDimensions, connections, PaddingType.NO_PADDING, convolutionService);
    }

    /**
     * Verifies the dimension verification service is called when computing a forward pass in non training mode
     */
    @Test
    public void test() {
        // Given
        final int[] expectedOutputDimensions = {28, 28};

        final MDA operand = new MDABuilder(inputDimensions).build();
        when(dimensionVerificationService.verifyLeftBiggerThanRight(inputDimensions, featureDimensions)).thenReturn(true);

        // When
        final MDA output = layer.forwardNoTrain(operand);

        // Then
        assertTrue("Output Dimensions should be as expected", isArrayEquality(expectedOutputDimensions, output.getDimensions()));
    }


    /**
     * Verifies the dimension verification service is called when computing a forward pass in non training mode
     */
    @Test(expected = IllegalArgumentException.class)
    public void testWrongNumberOfDimensions() {
        // Given
        final int[] expectedOutputDimensions = {28, 28, 1};

        final MDA operand = new MDABuilder(inputDimensions).build();
        when(dimensionVerificationService.verifyLeftBiggerThanRight(inputDimensions, featureDimensions)).thenReturn(false);

        // When
        final MDA output = layer.forwardNoTrain(operand);

        // Then - expect error
    }
}
