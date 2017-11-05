package com.brannanhancock.convnet.network.layers.pooling;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.brannanhancock.convnet.fundamentals.HelperLibrary;
import com.brannanhancock.convnet.fundamentals.MDA;
import com.brannanhancock.convnet.fundamentals.MDABuilder;
import com.brannanhancock.convnet.network.services.DimensionVerificationService;
import com.brannanhancock.convnet.network.testsupport.CompareMDAs;
import com.google.common.collect.Lists;

/**
 *
 * @author Brannan R. Hancock
 *
 */
public class TestPoolingLayer {

    private PoolingLayer layer;

    @Mock DimensionVerificationService dimensionVerificationService;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        layer = new PoolingLayer(dimensionVerificationService);
    }


    /**
     * Verifies the output of the reverse operation gets its dimensions from the third parameter.
     */
    @Test
    public void testReverseOutputSize() {
        // Given
        final int[] outputDimensions = {2,2};
        final int[] forwardInputDimensions = {4,4};
        final MDA dLossByDOut = new MDABuilder(outputDimensions).build();

        final Map<List<Integer>, Map<List<Integer>, Double>> dOutByDIn = new HashMap<>();

        // When
        final MDA output = layer.reverse(dLossByDOut, dOutByDIn, forwardInputDimensions);

        // Then
        verify(dimensionVerificationService).verifyDerivativeMap(dOutByDIn);
        assertTrue(HelperLibrary.arrayEquality(output.getDimensions(), forwardInputDimensions));
    }


    /**
     * Verifies every position in reverse's output is populated as expected.
     */
    @Test
    public void testReverseOutputForMaxPoolingLayer() {
        // Given
        final int[] forwardOutputDimensions = {2, 2};
        final int[] forwardInputDimensions = {4, 4};
        final MDABuilder dLossByDOutBuilder = new MDABuilder(forwardOutputDimensions);
        dLossByDOutBuilder.withDataPoint(1D, 0, 0);
        dLossByDOutBuilder.withDataPoint(1D, 0, 1);
        dLossByDOutBuilder.withDataPoint(1D, 1, 0);
        dLossByDOutBuilder.withDataPoint(1D, 1, 1);

        final Map<List<Integer>, Map<List<Integer>, Double>> dOutByDIn = new HashMap<>();
        Map<List<Integer>, Double> subMap = new HashMap<>();
        List<Integer> originalPosition = Lists.newArrayList(0, 0);
        subMap.put(originalPosition, 0D);
        originalPosition = Lists.newArrayList(0, 1);
        subMap.put(originalPosition, 0D);
        originalPosition = Lists.newArrayList(1, 0);
        subMap.put(originalPosition, 0D);
        originalPosition = Lists.newArrayList(1, 1);
        subMap.put(originalPosition, 1D);
        dOutByDIn.put(Lists.newArrayList(0, 0), subMap);
        subMap = new HashMap<>();
        originalPosition = Lists.newArrayList(0, 2);
        subMap.put(originalPosition, 0D);
        originalPosition = Lists.newArrayList(0, 3);
        subMap.put(originalPosition, 0D);
        originalPosition = Lists.newArrayList(1, 2);
        subMap.put(originalPosition, 0D);
        originalPosition = Lists.newArrayList(1, 3);
        subMap.put(originalPosition, 1D);
        dOutByDIn.put(Lists.newArrayList(0, 1), subMap);
        subMap = new HashMap<>();
        originalPosition = Lists.newArrayList(2, 0);
        subMap.put(originalPosition, 0D);
        originalPosition = Lists.newArrayList(2, 1);
        subMap.put(originalPosition, 0D);
        originalPosition = Lists.newArrayList(3, 0);
        subMap.put(originalPosition, 0D);
        originalPosition = Lists.newArrayList(3, 1);
        subMap.put(originalPosition, 1D);
        dOutByDIn.put(Lists.newArrayList(1, 0), subMap);
        subMap = new HashMap<>();
        originalPosition = Lists.newArrayList(2, 2);
        subMap.put(originalPosition, 0D);
        originalPosition = Lists.newArrayList(2, 3);
        subMap.put(originalPosition, 0D);
        originalPosition = Lists.newArrayList(3, 2);
        subMap.put(originalPosition, 0D);
        originalPosition = Lists.newArrayList(3, 3);
        subMap.put(originalPosition, 1D);
        dOutByDIn.put(Lists.newArrayList(1, 1), subMap);

        final MDABuilder expectedOutputBuilder = new MDABuilder(forwardInputDimensions);
        expectedOutputBuilder.withDataPoint(1D, 1, 1);
        expectedOutputBuilder.withDataPoint(1D, 3, 1);
        expectedOutputBuilder.withDataPoint(1D, 1, 3);
        expectedOutputBuilder.withDataPoint(1D, 3, 3);

        // When
        final MDA dLossByDIn = layer.reverse(dLossByDOutBuilder.build(), dOutByDIn, forwardInputDimensions);

        // Then
        verify(dimensionVerificationService).verifyDerivativeMap(dOutByDIn);
        CompareMDAs.checkExpectation(expectedOutputBuilder.build(), dLossByDIn);
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

        final Map<List<Integer>, Map<List<Integer>, Double>> dOutByDIn = new HashMap<>();
        final List<Integer> outputDimension = Lists.newArrayList(0,0);
        final Map<List<Integer>, Double> subEntry = new HashMap<>();
        List<Integer> inputDimension = Lists.newArrayList(0,0);
        subEntry.put(inputDimension, coefficient1);
        inputDimension = Lists.newArrayList(0,1);
        subEntry.put(inputDimension, coefficient2);
        dOutByDIn.put(outputDimension, subEntry);

        final MDABuilder expectedOutputBuilder = new MDABuilder(1,2);
        expectedOutputBuilder.withDataPoint(derivative*coefficient1, 0,0);
        expectedOutputBuilder.withDataPoint(derivative*coefficient2, 0,1);

        // When
        final MDA dLossByDIn = layer.reverse(dLossByDOutBuilder.build(), dOutByDIn, forwardInputDimensions);

        // Then
        verify(dimensionVerificationService).verifyDerivativeMap(dOutByDIn);
        CompareMDAs.checkExpectation(expectedOutputBuilder.build(), dLossByDIn);
    }
}
