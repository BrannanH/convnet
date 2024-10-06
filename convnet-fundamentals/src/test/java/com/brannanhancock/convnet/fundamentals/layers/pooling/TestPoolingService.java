package com.brannanhancock.convnet.fundamentals.layers.pooling;

import com.brannanhancock.convnet.fundamentals.layers.CompareMDAs;
import com.brannanhancock.convnet.fundamentals.layers.DimensionVerificationService;
import com.brannanhancock.convnet.fundamentals.layers.ForwardOutputTuple;
import com.brannanhancock.convnet.fundamentals.layers.ReversePassOutput;
import com.brannanhancock.convnet.fundamentals.mda.MDA;
import com.brannanhancock.convnet.fundamentals.mda.MDABuilder;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import static org.junit.Assert.assertArrayEquals;

/**
 *
 * @author Brannan R. Hancock
 *
 */
public class TestPoolingService {

    private PoolingService poolingService;

    private @Mock DimensionVerificationService dimensionVerificationService;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        poolingService = new PoolingService(dimensionVerificationService);
    }


    /**
     * Verifies the output of the reverse operation gets its dimensions from the third parameter.
     */
    @Test
    public void testReverseOutputSize() {
        // Given
        final int[] outputDimensions = {2,2};
        final int[] poolingSizes = {2,2};
        final int[] forwardInputDimensions = {4,4};
        final MDA dLossByDOut = new MDABuilder(outputDimensions).build();

        final Map<List<Integer>, Map<List<Integer>, Double>> dOutByDIn = new HashMap<>();
        PoolingLayer layer = new PoolingLayer.Builder().withPoolSizes(poolingSizes).withPoolingType(PoolingLibrary.PoolingType.MAX).withInputDimensions(forwardInputDimensions).build();

        // When
        final ReversePassOutput output = poolingService.reverse(new ForwardOutputTuple(layer, dLossByDOut, new MDABuilder(IntStream.concat(Arrays.stream(outputDimensions), Arrays.stream(forwardInputDimensions)).toArray()).build(), null), dLossByDOut);

        // Then
        assertArrayEquals(forwardInputDimensions, output.getDLossByDIn().getDimensions());
    }


    /**
     * Verifies every position in reverse's output is populated as expected.
     */
    @Test
    public void testReverseOutputForMaxPoolingLayer() {
        // Given
        final int[] poolingSizes = {2, 2};
        final int[] forwardOutputDimensions = {2, 2};
        final int[] forwardInputDimensions = {4, 4};
        final MDABuilder dLossByDOutBuilder = new MDABuilder(forwardOutputDimensions);
        dLossByDOutBuilder.withDataPoint(1D, 0, 0);
        dLossByDOutBuilder.withDataPoint(1D, 0, 1);
        dLossByDOutBuilder.withDataPoint(1D, 1, 0);
        dLossByDOutBuilder.withDataPoint(1D, 1, 1);

        MDABuilder dOutByDIn = new MDABuilder(IntStream.concat(Arrays.stream(forwardOutputDimensions), Arrays.stream(forwardInputDimensions)).toArray());
        final Map<List<Integer>, Map<List<Integer>, Double>> dOutByDIn2 = new HashMap<>();
        dOutByDIn.withDataPoint(1, 0,0,1,1);
        dOutByDIn.withDataPoint(1, 0,1,1,3);
        dOutByDIn.withDataPoint(1, 1,0,3,1);
        dOutByDIn.withDataPoint(1, 1,1,3,3);

        final MDABuilder expectedOutputBuilder = new MDABuilder(forwardInputDimensions);
        expectedOutputBuilder.withDataPoint(1D, 1, 1);
        expectedOutputBuilder.withDataPoint(1D, 3, 1);
        expectedOutputBuilder.withDataPoint(1D, 1, 3);
        expectedOutputBuilder.withDataPoint(1D, 3, 3);

        PoolingLayer layer = new PoolingLayer.Builder().withPoolSizes(poolingSizes).withPoolingType(PoolingLibrary.PoolingType.MAX).withInputDimensions(forwardInputDimensions).build();

        // When
        final ReversePassOutput dLossByDIn = poolingService.reverse(new ForwardOutputTuple(layer, new MDABuilder(forwardOutputDimensions).build(), dOutByDIn.build(), null), dLossByDOutBuilder.build());

        // Then
        CompareMDAs.checkExpectation(expectedOutputBuilder.build(), dLossByDIn.getDLossByDIn());
    }


    /**
     * Verifies the elements in reverse's outputs are calculated as expected.
     */
    @Test
    public void testMultiplication() {
        // Given
        final double derivative = 50D;
        final double coefficient1 = 0.3D;
        final double coefficient2 = 0.7D;
        final MDABuilder dLossByDOutBuilder = new MDABuilder(1,1);
        dLossByDOutBuilder.withDataPoint(derivative, 0, 0);
        final int[] forwardInputDimensions = {1,2};
        final int[] poolingSizes = {1,2};

        MDABuilder dOutByDInBuilder = new MDABuilder(1,1,1,2);
        dOutByDInBuilder.withDataPoint(coefficient1, 0,0,0,0);
        dOutByDInBuilder.withDataPoint(coefficient2, 0,0,0,1);


        final MDABuilder expectedOutputBuilder = new MDABuilder(1,2);
        expectedOutputBuilder.withDataPoint(derivative*coefficient1, 0,0);
        expectedOutputBuilder.withDataPoint(derivative*coefficient2, 0,1);

        PoolingLayer layer = new PoolingLayer.Builder().withPoolSizes(poolingSizes).withPoolingType(PoolingLibrary.PoolingType.MAX).withInputDimensions(forwardInputDimensions).build();
        // When
        final ReversePassOutput dLossByDIn = poolingService.reverse(new ForwardOutputTuple(layer, new MDABuilder(1,1).build(), dOutByDInBuilder.build(), null), dLossByDOutBuilder.build());

        // Then
        CompareMDAs.checkExpectation(expectedOutputBuilder.build(), dLossByDIn.getDLossByDIn());
    }
}
