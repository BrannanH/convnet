package com.brannan.convnet.network.layers.pooling;

import static com.brannan.convnet.network.fundamentals.MDAHelper.get;
import static com.brannan.convnet.network.fundamentals.MDAHelper.put;
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
        int[] inputDimensions = { 4, 4 };
        MDA operand = new MDABuilder().withDimensions(inputDimensions).build();
        double element = 1D;

        for (int j = 0; j < inputDimensions[1]; j++) {
            for (int i = 0; i < inputDimensions[1]; i++) {
                put(operand, element, j, i);
                element++;
            }
        }

        int[] poolingSize = {2, 1};

        MDA expectedOutput = new MDABuilder().withDimensions(2, 4).build();
        put(expectedOutput, 3, 0, 0);
        put(expectedOutput, 4, 0, 1);
        put(expectedOutput, 5, 0, 2);
        put(expectedOutput, 6, 0, 3);
        put(expectedOutput, 11, 1, 0);
        put(expectedOutput, 12, 1, 1);
        put(expectedOutput, 13, 1, 2);
        put(expectedOutput, 14, 1, 3);

        // When
        MDA output = layer.forward(operand, poolingSize, PoolingType.MEAN).getOutput();

        // Then
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 4; j++) {
                assertEquals("", get(expectedOutput, i, j), get(output, i, j), 0);
            }
        }
    }
}
