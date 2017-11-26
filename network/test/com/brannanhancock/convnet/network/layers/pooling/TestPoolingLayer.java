package com.brannanhancock.convnet.network.layers.pooling;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.brannanhancock.convnet.fundamentals.MDA;
import com.brannanhancock.convnet.network.layers.ForwardOutputTuple;
import com.brannanhancock.convnet.network.layers.pooling.PoolingLibrary.PoolingType;

public class TestPoolingLayer {

    private @Mock PoolingService poolingService;
    private @Mock MDA mda;
    private PoolingLayerBuilder builder;


    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        builder = new PoolingLayerBuilder(poolingService);
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
        final PoolingType poolingType = PoolingType.MAX;
        final PoolingLayer poolingLayer = builder.withInputDimensions(inputDimensions).build();

        final MDA outputMDA = mock(MDA.class);
        final Map<List<Integer>, Map<List<Integer>, Double>> map = new HashMap<>();
        final ForwardOutputTuple expectedOutput = new ForwardOutputTuple(outputMDA, map , null);
        when(poolingService.forward(mda, inputDimensions, poolingType)).thenReturn(expectedOutput);

        // When
        final MDA result = poolingLayer.forward(mda);

        // Then
        assertEquals("The resultant MDA should be the one returned by the pooling Service", outputMDA, result);
    }


    @Test(expected = IllegalStateException.class)
    public void testInputDimensionsMustBeSpecified() {
        // Given - nothing

        // When
        builder.withPoolingType(PoolingType.MEAN).withPoolSizes(1,2,3).build();

        // Then - expect exception
    }


    @Test
    public void testForwardNoTrainCorrectlyDelegates() {
     // Given
        final int[] inputDimensions = {4,5};
        final PoolingType poolingType = PoolingType.MAX;
        final PoolingLayer poolingLayer = builder.withInputDimensions(inputDimensions).build();

        final MDA outputMDA = mock(MDA.class);
        when(poolingService.forwardNoTrain(mda, inputDimensions, poolingType)).thenReturn(outputMDA);

        // When
        final MDA result = poolingLayer.forwardNoTrain(mda);

        // Then
        assertEquals("The resultant MDA should be the one returned by the pooling Service", outputMDA, result);
    }


    @Test
    public void testReverseCorrectlyDelegates() {
        // Given
        final int[] inputDimensions = {4,5};
        final PoolingType poolingType = PoolingType.MAX;
        final PoolingLayer poolingLayer = builder.withInputDimensions(inputDimensions).build();

        final MDA outputMDA = mock(MDA.class);
        final Map<List<Integer>, Map<List<Integer>, Double>> dOutByDIn = new HashMap<>();
        final ForwardOutputTuple expectedOutput = new ForwardOutputTuple(outputMDA, dOutByDIn , null);
        when(poolingService.forward(mda, inputDimensions, poolingType)).thenReturn(expectedOutput);

        final MDA dLossByDOut = mock(MDA.class);

        // When
        poolingLayer.forward(mda);
        poolingLayer.reverse(dLossByDOut);

        // Then - Reverse should be passed dOutByDIn which came from a forward pass
        verify(poolingService).reverse(dLossByDOut, dOutByDIn, inputDimensions);
    }


    @Test
    public void testReverseWithoutForwardPassReturnsZeros() {
        // Given
        final int[] inputDimensions = {4,5};
        final int[] poolSizes = {2,1};
        final PoolingLayer poolingLayer = builder.withInputDimensions(inputDimensions)
                .withPoolSizes(poolSizes).build();

        final MDA dLossByDOut = mock(MDA.class);

        // When
        final MDA result = poolingLayer.reverse(dLossByDOut);

        // Then
        assertEquals("Resulting MDA should be the correct size", inputDimensions.length, result.getDimensions().length);
        for(int i = 0; i < inputDimensions.length; i++) {
            assertEquals("Dimension " + i + " should be the same", inputDimensions[i], result.getDimensions()[i]);
        }
        for(int i = 0; i < inputDimensions[0]; i++) {
            for(int j = 0; j < inputDimensions[1]; j++) {
                        assertEquals("All entries in the MDA should be zero", 0, result.get(i, j), 0);
            }
        }
    }
}
