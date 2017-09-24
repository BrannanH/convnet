package com.brannan.convnet.network.layers.pooling;

import static com.brannan.convnet.network.testsupport.CompareMDAs.checkExpectation;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;

import org.junit.Before;
import org.junit.Test;

import com.brannan.convnet.network.fundamentals.MDA;
import com.brannan.convnet.network.fundamentals.MDABuilder;
import com.brannan.convnet.network.layers.ForwardOutputTuple;
import com.brannan.convnet.network.layers.pooling.PoolingLibrary.PoolingType;
import com.brannan.convnet.network.services.DimensionVerificationService;
import com.brannan.convnet.network.testsupport.CompareMDAs;

/**
 * Test class for the Median Test Layer
 *
 * @author Brannan
 *
 */
public class TestMedianLayer {

    private double count = 1;

    private final DimensionVerificationService dimensionsVerificationService = mock(DimensionVerificationService.class);

    private PoolingLayer layer;


    @Before
    public void setup() {
        layer = new PoolingLayer(dimensionsVerificationService);
    }


    @Test
    public void testInstantiation() {
        assertNotNull(layer);
    }


    @Test
    public void testSimplePool() {
        // Given
        final int[] inputDimensions = { 1, 6 };
        final MDABuilder inputBuilder = new MDABuilder().withDimensions(inputDimensions);
        inputBuilder.withDataPoint(1, 0, 0);
        inputBuilder.withDataPoint(2, 0, 1);
        inputBuilder.withDataPoint(3, 0, 2);
        inputBuilder.withDataPoint(4, 0, 3);
        inputBuilder.withDataPoint(5, 0, 4);
        inputBuilder.withDataPoint(6, 0, 5);

        final int[] poolSizes = {1, 3};

        final MDABuilder expectedOutputBuilder = new MDABuilder().withDimensions(1, 2);
        expectedOutputBuilder.withDataPoint(2, 0, 0);
        expectedOutputBuilder.withDataPoint(5, 0, 1);

        // When
        final ForwardOutputTuple output = layer.forward(inputBuilder.build(), poolSizes, PoolingType.MEDIAN);
        final MDA forward = output.getOutput();

        // Then
        CompareMDAs.checkExpectation(expectedOutputBuilder.build(), forward);
    }


    @Test
    public void testThreeByThreePool() {
        // Given
        final int[] inputDimensions = { 6, 9 };
        final MDA input = populateMultiD(inputDimensions);

        final int[] poolSizes = {3, 3};

        final MDABuilder expectedOutputBuilder = new MDABuilder().withDimensions(2, 3);
        expectedOutputBuilder.withDataPoint(8, 0, 0);
        expectedOutputBuilder.withDataPoint(26, 0, 1);
        expectedOutputBuilder.withDataPoint(44, 0, 2);
        expectedOutputBuilder.withDataPoint(11, 1, 0);
        expectedOutputBuilder.withDataPoint(29, 1, 1);
        expectedOutputBuilder.withDataPoint(47, 1, 2);

        // When
        final ForwardOutputTuple output = layer.forward(input, poolSizes, PoolingType.MEDIAN);
        final MDA forward = output.getOutput();

        // Then
        checkExpectation(expectedOutputBuilder.build(), forward);
    }


    @Test
    public void testThreeByThreeBy2Pool() {
        // Given
        final int[] inputDimensions = { 6, 6, 2 };
        final MDA input = populateMultiD(inputDimensions);

        final int[] poolSizes = {3, 3, 2};

        final MDABuilder expectedOutputBuilder = new MDABuilder().withDimensions(2, 2, 1);
        expectedOutputBuilder.withDataPoint(15D, 0, 0, 0);
        expectedOutputBuilder.withDataPoint(18D, 1, 0, 0);
        expectedOutputBuilder.withDataPoint(33D, 0, 1, 0);
        expectedOutputBuilder.withDataPoint(36D, 1, 1, 0);

        // When
        final ForwardOutputTuple output = layer.forward(input, poolSizes, PoolingType.MEDIAN);
        final MDA forward = output.getOutput();

        // Then
        checkExpectation(expectedOutputBuilder.build(), forward);
    }


    private MDA populateMultiD(final int[] inputDimensions) {
        final int[] position = new int[inputDimensions.length];
        return populate(new MDABuilder().withDimensions(inputDimensions), position, inputDimensions.length - 1, inputDimensions).build();
    }


    private MDABuilder populate(MDABuilder mdaBuilder, final int[] position, final int place, final int[] dimensions) {

        for (int i = 0; i < dimensions[place]; i++) {
            position[place] = i;
            if (place == 0) {
                mdaBuilder.withDataPoint(count, position);
                count++;
            } else {
                mdaBuilder = populate(mdaBuilder, position, place - 1, dimensions);
            }
        }
        return mdaBuilder;
    }

}
