package com.brannanhancock.convnet.fundamentals.layers.pooling;

import com.brannanhancock.convnet.fundamentals.layers.DimensionVerificationService;
import com.brannanhancock.convnet.fundamentals.mda.MDA;
import com.brannanhancock.convnet.fundamentals.mda.MDABuilder;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

public class TestMeanPoolingLayer {

    private PoolingService layer;

    @Mock
    private DimensionVerificationService dimensionVerificationService;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        layer = new PoolingService(dimensionVerificationService);
    }


    @Test
    public void testSmallPool() {
        // Given;
        final int[] inputDimensions = { 4, 4 };
        final MDABuilder operandBuilder = new MDABuilder(inputDimensions);
        double element = 1D;

        for (int j = 0; j < inputDimensions[1]; j++) {
            for (int i = 0; i < inputDimensions[1]; i++) {
                operandBuilder.withDataPoint(element, j, i);
                element++;
            }
        }
        final MDA operand = operandBuilder.build();

        final int[] poolingSize = {2, 1};

        final MDABuilder expectedOutputBuilder = new MDABuilder(2, 4);
        expectedOutputBuilder.withDataPoint(3, 0, 0);
        expectedOutputBuilder.withDataPoint(4, 0, 1);
        expectedOutputBuilder.withDataPoint(5, 0, 2);
        expectedOutputBuilder.withDataPoint(6, 0, 3);
        expectedOutputBuilder.withDataPoint(11, 1, 0);
        expectedOutputBuilder.withDataPoint(12, 1, 1);
        expectedOutputBuilder.withDataPoint(13, 1, 2);
        expectedOutputBuilder.withDataPoint(14, 1, 3);
        final MDA expectedOutput = expectedOutputBuilder.build();

        when(dimensionVerificationService.verifyLeftBiggerThanRight(inputDimensions, poolingSize)).thenReturn(true);

        // When
        final MDA output = layer.forward(new PoolingLayer.Builder().withPoolSizes(poolingSize).withPoolingType(PoolingLibrary.PoolingType.MEAN).withInputDimensions(inputDimensions).build(), operand).getOutput();

        // Then
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 4; j++) {
                assertEquals("", expectedOutput.get(i, j), output.get(i, j), 0);
            }
        }
    }
}
