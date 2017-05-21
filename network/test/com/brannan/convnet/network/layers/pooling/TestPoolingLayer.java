package com.brannan.convnet.network.layers.pooling;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.brannan.convnet.network.fundamentals.MDA;
import com.brannan.convnet.network.fundamentals.MDABuilder;
import com.brannan.convnet.network.fundamentals.MDAHelper;
import com.brannan.convnet.network.layers.pooling.PoolingLayer;
import com.brannan.convnet.network.services.DimensionVerificationService;
import com.brannan.convnet.network.testsupport.CompareMDAs;
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
        List<Integer> outputDimensions = Lists.newArrayList(2,2);
        List<Integer> forwardInputDimensions = Lists.newArrayList(4,4);
        MDA dLossByDOut = new MDABuilder().withDimensions(outputDimensions).build();

        Map<List<Integer>, Map<List<Integer>, Double>> dOutByDIn = new HashMap<>();

        // When
        MDA output = layer.reverse(dLossByDOut, dOutByDIn, forwardInputDimensions);

        // Then
        verify(dimensionVerificationService).verifyDerivativeMap(dOutByDIn);
        assertEquals(output.getDimensions(), forwardInputDimensions);
    }


    /**
     * Verifies every position in reverse's output is populated as expected.
     */
    @Test
    public void testReverseOutputForMaxPoolingLayer() {
        // Given
        List<Integer> forwardOutputDimensions = Lists.newArrayList(2, 2);
        List<Integer> forwardInputDimensions = Lists.newArrayList(4, 4);
        MDA dLossByDOut = new MDABuilder().withDimensions(forwardOutputDimensions).build();
        MDAHelper.put(dLossByDOut, 1D, 0, 0);
        MDAHelper.put(dLossByDOut, 1D, 0, 1);
        MDAHelper.put(dLossByDOut, 1D, 1, 0);
        MDAHelper.put(dLossByDOut, 1D, 1, 1);

        Map<List<Integer>, Map<List<Integer>, Double>> dOutByDIn = new HashMap<>();
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

        MDA expectedOutput = new MDABuilder().withDimensions(forwardInputDimensions).build();
        MDAHelper.put(expectedOutput, 1D, 1, 1);
        MDAHelper.put(expectedOutput, 1D, 3, 1);
        MDAHelper.put(expectedOutput, 1D, 1, 3);
        MDAHelper.put(expectedOutput, 1D, 3, 3);

        // When
        MDA dLossByDIn = layer.reverse(dLossByDOut, dOutByDIn, forwardInputDimensions);

        // Then
        verify(dimensionVerificationService).verifyDerivativeMap(dOutByDIn);
        CompareMDAs.checkExpectation(expectedOutput, dLossByDIn);
    }

    
    /**
     * Verifies the elements in reverse's outputs are calculated as expected.
     */
    @Test
    public void testMultiplication() {
        // Given
        double derivative = 50D;
        double coefficient1 = 0.3D;
        double coefficient2 = 0.7D;
        MDA dLossByDOut = new MDABuilder().withDimensions(1,1).build();
        MDAHelper.put(dLossByDOut, derivative, 0, 0);
        List<Integer> forwardInputDimensions = Lists.newArrayList(1,2);
        
        Map<List<Integer>, Map<List<Integer>, Double>> dOutByDIn = new HashMap<>();
        List<Integer> outputDimension = Lists.newArrayList(0,0);
        Map<List<Integer>, Double> subEntry = new HashMap<>();
        List<Integer> inputDimension = Lists.newArrayList(0,0);
        subEntry.put(inputDimension, coefficient1);
        inputDimension = Lists.newArrayList(0,1);
        subEntry.put(inputDimension, coefficient2);
        dOutByDIn.put(outputDimension, subEntry);
        
        MDA expectedOutput = new MDABuilder().withDimensions(1,2).build();
        MDAHelper.put(expectedOutput, derivative*coefficient1, 0,0);
        MDAHelper.put(expectedOutput, derivative*coefficient2, 0,1);
        
        // When
        MDA dLossByDIn = layer.reverse(dLossByDOut, dOutByDIn, forwardInputDimensions);
        
        // Then
        verify(dimensionVerificationService).verifyDerivativeMap(dOutByDIn);
        CompareMDAs.checkExpectation(expectedOutput, dLossByDIn);
    }
}
