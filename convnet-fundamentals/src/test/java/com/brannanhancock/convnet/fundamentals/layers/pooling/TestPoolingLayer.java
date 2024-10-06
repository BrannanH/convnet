package com.brannanhancock.convnet.fundamentals.layers.pooling;

import com.brannanhancock.convnet.fundamentals.layers.DimensionVerificationService;
import com.brannanhancock.convnet.fundamentals.mda.MDA;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class TestPoolingLayer {

    private PoolingService poolingService = new PoolingService(new DimensionVerificationService());
    private @Mock MDA mda;
    private PoolingLayer.Builder builder;


    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        builder = new PoolingLayer.Builder();
    }


    /**
     * This test verifies that if no pooling type is specified to the pooling
     * layer builder, it is defaulted to MAX. It also verifies that if no pool
     * sizes are are specified, the input dimensions are used, resulting in no
     * pooling.
     */
    @Test
    public void testPoolingLayerDefaultsToMaxPoolingTypeAndNoPooling() {
        // Given
        final int[] inputDimensions = {4,5};
        final PoolingLibrary.PoolingType poolingType = PoolingLibrary.PoolingType.MAX;

        // When
        final PoolingLayer poolingLayer = builder.withInputDimensions(inputDimensions).build();

        // Then
        assertEquals("Pooling Type Default should be max", PoolingLibrary.PoolingType.MAX, poolingLayer.getPoolingType());

        // When
        final int[] result = poolingService.outputDimensionsFor(poolingLayer);

        // Then
        assertArrayEquals("Result of default pooling should have same output dimensions as input", inputDimensions, result);
    }


    @Test(expected = IllegalStateException.class)
    public void testInputDimensionsMustBeSpecified() {
        // Given - nothing

        // When
        builder.withPoolingType(PoolingLibrary.PoolingType.MEAN).withPoolSizes(1,2,3).build();

        // Then - expect exception
    }
}
