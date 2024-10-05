package com.brannanhancock.convnet.fundamentals.layers.convolution;

import com.brannanhancock.convnet.fundamentals.layers.DimensionVerificationService;
import com.brannanhancock.convnet.fundamentals.mda.MDA;
import com.brannanhancock.convnet.fundamentals.mda.MDABuilder;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertArrayEquals;
import static org.mockito.Mockito.when;

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
        layer = new ConvolutionLayer(feature, inputDimensions, connections, ConvolutionLibrary.PaddingType.NO_PADDING);
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
        final MDA output = convolutionService.forwardNoTrain(layer, operand);

        // Then
        assertArrayEquals("Output Dimensions should be as expected", expectedOutputDimensions, output.getDimensions());
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
        final MDA output = convolutionService.forwardNoTrain(layer, operand);

        // Then - expect error
    }
}
