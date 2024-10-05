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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertArrayEquals;
import static org.mockito.Mockito.verify;

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
        PoolingLayer layer = new PoolingLayer(poolingSizes, PoolingLibrary.PoolingType.MAX, forwardInputDimensions);

        // When
        final ReversePassOutput output = poolingService.reverse(new ForwardOutputTuple(layer, dLossByDOut, dOutByDIn, null), dLossByDOut);

        // Then
        verify(dimensionVerificationService).verifyDerivativeMap(dOutByDIn);
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

        final Map<List<Integer>, Map<List<Integer>, Double>> dOutByDIn = new HashMap<>();
        Map<List<Integer>, Double> subMap = new HashMap<>();
        List<Integer> originalPosition = List.of(0, 0);
        subMap.put(originalPosition, 0D);
        originalPosition = List.of(0, 1);
        subMap.put(originalPosition, 0D);
        originalPosition = List.of(1, 0);
        subMap.put(originalPosition, 0D);
        originalPosition = List.of(1, 1);
        subMap.put(originalPosition, 1D);
        dOutByDIn.put(List.of(0, 0), subMap);
        subMap = new HashMap<>();
        originalPosition = List.of(0, 2);
        subMap.put(originalPosition, 0D);
        originalPosition = List.of(0, 3);
        subMap.put(originalPosition, 0D);
        originalPosition = List.of(1, 2);
        subMap.put(originalPosition, 0D);
        originalPosition = List.of(1, 3);
        subMap.put(originalPosition, 1D);
        dOutByDIn.put(List.of(0, 1), subMap);
        subMap = new HashMap<>();
        originalPosition = List.of(2, 0);
        subMap.put(originalPosition, 0D);
        originalPosition = List.of(2, 1);
        subMap.put(originalPosition, 0D);
        originalPosition = List.of(3, 0);
        subMap.put(originalPosition, 0D);
        originalPosition = List.of(3, 1);
        subMap.put(originalPosition, 1D);
        dOutByDIn.put(List.of(1, 0), subMap);
        subMap = new HashMap<>();
        originalPosition = List.of(2, 2);
        subMap.put(originalPosition, 0D);
        originalPosition = List.of(2, 3);
        subMap.put(originalPosition, 0D);
        originalPosition = List.of(3, 2);
        subMap.put(originalPosition, 0D);
        originalPosition = List.of(3, 3);
        subMap.put(originalPosition, 1D);
        dOutByDIn.put(List.of(1, 1), subMap);

        final MDABuilder expectedOutputBuilder = new MDABuilder(forwardInputDimensions);
        expectedOutputBuilder.withDataPoint(1D, 1, 1);
        expectedOutputBuilder.withDataPoint(1D, 3, 1);
        expectedOutputBuilder.withDataPoint(1D, 1, 3);
        expectedOutputBuilder.withDataPoint(1D, 3, 3);

        PoolingLayer layer = new PoolingLayer(poolingSizes, PoolingLibrary.PoolingType.MAX, forwardInputDimensions);

        // When
        final ReversePassOutput dLossByDIn = poolingService.reverse(new ForwardOutputTuple(layer, new MDABuilder(forwardOutputDimensions).build(), dOutByDIn, null), dLossByDOutBuilder.build());

        // Then
        verify(dimensionVerificationService).verifyDerivativeMap(dOutByDIn);
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

        final Map<List<Integer>, Map<List<Integer>, Double>> dOutByDIn = new HashMap<>();
        final List<Integer> outputDimension = List.of(0,0);
        final Map<List<Integer>, Double> subEntry = new HashMap<>();
        List<Integer> inputDimension = List.of(0,0);
        subEntry.put(inputDimension, coefficient1);
        inputDimension = List.of(0,1);
        subEntry.put(inputDimension, coefficient2);
        dOutByDIn.put(outputDimension, subEntry);

        final MDABuilder expectedOutputBuilder = new MDABuilder(1,2);
        expectedOutputBuilder.withDataPoint(derivative*coefficient1, 0,0);
        expectedOutputBuilder.withDataPoint(derivative*coefficient2, 0,1);

        PoolingLayer layer = new PoolingLayer(poolingSizes, PoolingLibrary.PoolingType.MAX, forwardInputDimensions);
        // When
        final ReversePassOutput dLossByDIn = poolingService.reverse(new ForwardOutputTuple(layer, new MDABuilder(1,1).build(),dOutByDIn, null), dLossByDOutBuilder.build());

        // Then
        verify(dimensionVerificationService).verifyDerivativeMap(dOutByDIn);
        CompareMDAs.checkExpectation(expectedOutputBuilder.build(), dLossByDIn.getDLossByDIn());
    }
}
