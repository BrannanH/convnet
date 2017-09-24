package com.brannan.convnet.network.layers.pooling;

import static com.brannan.convnet.network.fundamentals.MDAHelper.put;
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

    private DimensionVerificationService dimensionsVerificationService = mock(DimensionVerificationService.class);

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
        int[] inputDimensions = { 1, 6 };
        MDA input = new MDABuilder().withDimensions(inputDimensions).build();
        put(input, 1, 0, 0);
        put(input, 2, 0, 1);
        put(input, 3, 0, 2);
        put(input, 4, 0, 3);
        put(input, 5, 0, 4);
        put(input, 6, 0, 5);

        int[] poolSizes = {1, 3};

        MDA expectedOutput = new MDABuilder().withDimensions(1, 2).build();
        put(expectedOutput, 2, 0, 0);
        put(expectedOutput, 5, 0, 1);

        // When
        ForwardOutputTuple output = layer.forward(input, poolSizes, PoolingType.MEDIAN);
        MDA forward = output.getOutput();

        // Then
        CompareMDAs.checkExpectation(expectedOutput, forward);
    }


    @Test
    public void testThreeByThreePool() {
        // Given
        int[] inputDimensions = { 6, 9 };
        MDA input = populateMultiD(inputDimensions);

        int[] poolSizes = {3, 3};

        MDA expectedOutput = new MDABuilder().withDimensions(2, 3).build();
        put(expectedOutput, 8, 0, 0);
        put(expectedOutput, 26, 0, 1);
        put(expectedOutput, 44, 0, 2);
        put(expectedOutput, 11, 1, 0);
        put(expectedOutput, 29, 1, 1);
        put(expectedOutput, 47, 1, 2);

        // When
        ForwardOutputTuple output = layer.forward(input, poolSizes, PoolingType.MEDIAN);
        MDA forward = output.getOutput();

        // Then
        checkExpectation(expectedOutput, forward);
    }


    @Test
    public void testThreeByThreeBy2Pool() {
        // Given
        int[] inputDimensions = { 6, 6, 2 };
        MDA input = populateMultiD(inputDimensions);

        int[] poolSizes = {3, 3, 2};

        MDA expectedOutput = new MDABuilder().withDimensions(2, 2, 1).build();
        put(expectedOutput, 15D, 0, 0, 0);
        put(expectedOutput, 18D, 1, 0, 0);
        put(expectedOutput, 33D, 0, 1, 0);
        put(expectedOutput, 36D, 1, 1, 0);

        // When
        ForwardOutputTuple output = layer.forward(input, poolSizes, PoolingType.MEDIAN);
        MDA forward = output.getOutput();

        // Then
        checkExpectation(expectedOutput, forward);
    }


    private MDA populateMultiD(int[] inputDimensions) {
        int[] position = new int[inputDimensions.length];
        return populate(new MDABuilder().withDimensions(inputDimensions).build(), position, inputDimensions.length - 1);
    }


    private MDA populate(MDA mda, int[] position, int place) {

        for (int i = 0; i < mda.getDimensions()[place]; i++) {
            position[place] = i;
            if (place == 0) {
                put(mda, count, position);
                count++;
            } else {
                mda = populate(mda, position, place - 1);
            }
        }
        return mda;
    }

}
