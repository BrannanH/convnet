package com.brannan.convnet.network.layers.pooling;

import static com.brannan.convnet.network.fundamentals.MDAService.get;
import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.brannan.convnet.network.fundamentals.MDA;
import com.brannan.convnet.network.fundamentals.MDABuilder;
import com.brannan.convnet.network.layers.pooling.PoolingLibrary.PoolingType;
import com.brannan.convnet.network.services.DimensionVerificationService;

public class TestMeanPoolingLayer {

    private PoolingLayer layer;

    @Mock
    private DimensionVerificationService dimensionVerificationService;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        layer = new PoolingLayer(dimensionVerificationService);
    }


    @Test
    public void testSmallPool() {
        // Given;
        final int[] inputDimensions = { 4, 4 };
        final MDABuilder operandBuilder = new MDABuilder().withDimensions(inputDimensions);
        double element = 1D;

        for (int j = 0; j < inputDimensions[1]; j++) {
            for (int i = 0; i < inputDimensions[1]; i++) {
                operandBuilder.withDataPoint(element, j, i);
                element++;
            }
        }

        final int[] poolingSize = {2, 1};

        final MDABuilder expectedOutputBuilder = new MDABuilder().withDimensions(2, 4);
        expectedOutputBuilder.withDataPoint(3, 0, 0);
        expectedOutputBuilder.withDataPoint(4, 0, 1);
        expectedOutputBuilder.withDataPoint(5, 0, 2);
        expectedOutputBuilder.withDataPoint(6, 0, 3);
        expectedOutputBuilder.withDataPoint(11, 1, 0);
        expectedOutputBuilder.withDataPoint(12, 1, 1);
        expectedOutputBuilder.withDataPoint(13, 1, 2);
        expectedOutputBuilder.withDataPoint(14, 1, 3);

        // When
        final MDA output = layer.forward(operandBuilder.build(), poolingSize, PoolingType.MEAN).getOutput();

        // Then
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 4; j++) {
                assertEquals("", get(expectedOutputBuilder.build(), i, j), get(output, i, j), 0);
            }
        }
    }
}
