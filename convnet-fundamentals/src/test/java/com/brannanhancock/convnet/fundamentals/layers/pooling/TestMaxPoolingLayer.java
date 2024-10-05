package com.brannanhancock.convnet.fundamentals.layers.pooling;

import com.brannanhancock.convnet.fundamentals.layers.CompareMDAs;
import com.brannanhancock.convnet.fundamentals.layers.DimensionVerificationService;
import com.brannanhancock.convnet.fundamentals.layers.ForwardOutputTuple;
import com.brannanhancock.convnet.fundamentals.mda.MDA;
import com.brannanhancock.convnet.fundamentals.mda.MDABuilder;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

/**
 * This class tests the MaxPoolingLayer
 * @author Brannan R. Hancock
 *
 */
public class TestMaxPoolingLayer {

	@Mock
	private DimensionVerificationService dimensionVerificationService;

	private PoolingService maxPoolingLayer;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		when(dimensionVerificationService.verifyLeftBiggerThanRight(Mockito.any(), Mockito.any())).thenReturn(true);
		maxPoolingLayer = new PoolingService(dimensionVerificationService);
	}

	@Test
	public void TestSmallPool() {
		// Given
		final int[] inputDimensions = {4,4};
		final int[] poolingSize = {2,1};

		double element = 0;
		final MDABuilder inputBuilder = new MDABuilder(inputDimensions);
		for(int i = 0; i < inputDimensions[0]; i++) {
			for(int j = 0; j < inputDimensions[1]; j++) {
			    inputBuilder.withDataPoint(element, i, j);
				element++;
			}
		}

		final MDABuilder expectedResult = new MDABuilder(2,4);
		expectedResult.withDataPoint(4D, 0, 0);
		expectedResult.withDataPoint(5D, 0, 1);
		expectedResult.withDataPoint(6D, 0, 2);
		expectedResult.withDataPoint(7D, 0, 3);
		expectedResult.withDataPoint(12D, 1, 0);
		expectedResult.withDataPoint(13D, 1, 1);
		expectedResult.withDataPoint(14D, 1, 2);
		expectedResult.withDataPoint(15D, 1, 3);

		final Map<List<Integer>, Map<List<Integer>, Double>> expectedDerivative = new HashMap<>();
		Map<List<Integer>, Double> temp = new HashMap<>();
		temp.put(List.of(0, 0), 0D);
		temp.put(List.of(1, 0), 1D);
		expectedDerivative.put(List.of(0,0), temp);
		temp = new HashMap<>();
		temp.put(List.of(0, 1), 0D);
		temp.put(List.of(1, 1), 1D);
		expectedDerivative.put(List.of(0,1), temp);
		temp = new HashMap<>();
		temp.put(List.of(0, 2), 0D);
		temp.put(List.of(1, 2), 1D);
		expectedDerivative.put(List.of(0,2), temp);
		temp = new HashMap<>();
		temp.put(List.of(0, 3), 0D);
		temp.put(List.of(1, 3), 1D);
		expectedDerivative.put(List.of(0,3), temp);
		temp = new HashMap<>();
		temp.put(List.of(2, 0), 0D);
		temp.put(List.of(3, 0), 1D);
		expectedDerivative.put(List.of(1,0), temp);
		temp = new HashMap<>();
		temp.put(List.of(2, 1), 0D);
		temp.put(List.of(3, 1), 1D);
		expectedDerivative.put(List.of(1,1), temp);
		temp = new HashMap<>();
		temp.put(List.of(2, 2), 0D);
		temp.put(List.of(3, 2), 1D);
		expectedDerivative.put(List.of(1,2), temp);
		temp = new HashMap<>();
		temp.put(List.of(2, 3), 0D);
		temp.put(List.of(3, 3), 1D);
		expectedDerivative.put(List.of(1,3), temp);
		temp = new HashMap<>();

		// When
		final ForwardOutputTuple output = maxPoolingLayer.forward(new PoolingLayer(poolingSize, PoolingLibrary.PoolingType.MAX, inputDimensions), inputBuilder.build());
		final MDA result = output.getOutput();
		final Map<List<Integer>, Map<List<Integer>, Double>> derivative = output.getdOutByDIn();

		// Then
		CompareMDAs.checkExpectation(expectedResult.build(), result);
		checkDerivatives(expectedDerivative, derivative);
	}

	private void checkDerivatives(final Map<List<Integer>, Map<List<Integer>, Double>> expectedDerivative, final Map<List<Integer>, Map<List<Integer>, Double>> derivative) {
		for(final Entry<List<Integer>, Map<List<Integer>, Double>> entry: expectedDerivative.entrySet()) {
			assertTrue(derivative.containsKey(entry.getKey()));
			assertEquals(entry.getValue(), derivative.get(entry.getKey()));
		}

	}

	@Test
	public void TestBigPool() {
		// Given
		final int[] inputDimensions = {4,4};
		final int[] poolingSize = {2,2};
		double element = 0;
		final MDABuilder inputBuilder = new MDABuilder(inputDimensions);
		for(int i = 0; i < inputDimensions[0]; i++) {
			for(int j = 0; j < inputDimensions[1]; j++) {
				inputBuilder.withDataPoint(element, i, j);
				element++;
			}
		}

		final MDABuilder expectedResult = new MDABuilder(2,2);
		expectedResult.withDataPoint(5D, 0, 0);
		expectedResult.withDataPoint(7D, 0, 1);
		expectedResult.withDataPoint(13D, 1, 0);
		expectedResult.withDataPoint(15D, 1, 1);

		final Map<List<Integer>, Map<List<Integer>, Double>> expectedDerivative = new HashMap<>();
		Map<List<Integer>, Double> temp = new HashMap<>();
		temp.put(List.of(0, 0), 0D);
		temp.put(List.of(0, 1), 0D);
		temp.put(List.of(1, 0), 0D);
		temp.put(List.of(1, 1), 1D);
		expectedDerivative.put(List.of(0,0), temp);
		temp = new HashMap<>();
		temp.put(List.of(0, 2), 0D);
		temp.put(List.of(0, 3), 0D);
		temp.put(List.of(1, 2), 0D);
		temp.put(List.of(1, 3), 1D);
		expectedDerivative.put(List.of(0,1), temp);
		temp = new HashMap<>();
		temp.put(List.of(2, 0), 0D);
		temp.put(List.of(2, 1), 0D);
		temp.put(List.of(3, 0), 0D);
		temp.put(List.of(3, 1), 1D);
		expectedDerivative.put(List.of(1,0), temp);
		temp = new HashMap<>();
		temp.put(List.of(2, 2), 0D);
		temp.put(List.of(2, 3), 0D);
		temp.put(List.of(3, 2), 0D);
		temp.put(List.of(3, 3), 1D);
		expectedDerivative.put(List.of(1,1), temp);

		// When
		final ForwardOutputTuple output = maxPoolingLayer.forward(new PoolingLayer(poolingSize, PoolingLibrary.PoolingType.MAX, inputDimensions), inputBuilder.build());
		final MDA result = output.getOutput();
		final Map<List<Integer>, Map<List<Integer>, Double>> derivative = output.getdOutByDIn();

		// Then
		CompareMDAs.checkExpectation(expectedResult.build(), result);
		checkDerivatives(expectedDerivative, derivative);
	}

	@Test
	public void TestBiggestPool() {
		// Given
		final int[] inputDimensions = {4,4,2};
		final int[] poolingSize = {2,2,2};
		double element = 0;
		final MDABuilder inputBuilder = new MDABuilder(inputDimensions);
		for(int k = 0; k < inputDimensions[2]; k++) {
			for(int i = 0; i < inputDimensions[0]; i++) {
				for(int j = 0; j < inputDimensions[1]; j++) {
					inputBuilder.withDataPoint(element, i, j, k);
					element++;
				}
			}
		}

		final MDABuilder expectedResultBuilder = new MDABuilder(2,2,1);
		expectedResultBuilder.withDataPoint(21D, 0, 0, 0);
		expectedResultBuilder.withDataPoint(23D, 0, 1, 0);
		expectedResultBuilder.withDataPoint(29D, 1, 0, 0);
		expectedResultBuilder.withDataPoint(31D, 1, 1, 0);

		final Map<List<Integer>, Map<List<Integer>, Double>> expectedDerivative = new HashMap<>();
		Map<List<Integer>, Double> temp = new HashMap<>();
		temp.put(List.of(0, 0, 0), 0D);
		temp.put(List.of(0, 1, 0), 0D);
		temp.put(List.of(1, 0, 0), 0D);
		temp.put(List.of(1, 1, 0), 0D);
		temp.put(List.of(0, 0, 1), 0D);
		temp.put(List.of(0, 1, 1), 0D);
		temp.put(List.of(1, 0, 1), 0D);
		temp.put(List.of(1, 1, 1), 1D);
		expectedDerivative.put(List.of(0,0,0), temp);
		temp = new HashMap<>();
		temp.put(List.of(0, 2, 0), 0D);
		temp.put(List.of(0, 3, 0), 0D);
		temp.put(List.of(1, 2, 0), 0D);
		temp.put(List.of(1, 3, 0), 0D);
		temp.put(List.of(0, 2, 1), 0D);
		temp.put(List.of(0, 3, 1), 0D);
		temp.put(List.of(1, 2, 1), 0D);
		temp.put(List.of(1, 3, 1), 1D);
		expectedDerivative.put(List.of(0,1,0), temp);
		temp = new HashMap<>();
		temp.put(List.of(2, 0, 0), 0D);
		temp.put(List.of(2, 1, 0), 0D);
		temp.put(List.of(3, 0, 0), 0D);
		temp.put(List.of(3, 1, 0), 0D);
		temp.put(List.of(2, 0, 1), 0D);
		temp.put(List.of(2, 1, 1), 0D);
		temp.put(List.of(3, 0, 1), 0D);
		temp.put(List.of(3, 1, 1), 1D);
		expectedDerivative.put(List.of(1,0,0), temp);
		temp = new HashMap<>();
		temp.put(List.of(2, 2, 0), 0D);
		temp.put(List.of(2, 3, 0), 0D);
		temp.put(List.of(3, 2, 0), 0D);
		temp.put(List.of(3, 3, 0), 0D);
		temp.put(List.of(2, 2, 1), 0D);
		temp.put(List.of(2, 3, 1), 0D);
		temp.put(List.of(3, 2, 1), 0D);
		temp.put(List.of(3, 3, 1), 1D);
		expectedDerivative.put(List.of(1,1,0), temp);

		// When
		final ForwardOutputTuple result = maxPoolingLayer.forward(new PoolingLayer(poolingSize, PoolingLibrary.PoolingType.MAX, inputDimensions), inputBuilder.build());
		final MDA output = result.getOutput();
		final Map<List<Integer>, Map<List<Integer>, Double>> derivative = result.getdOutByDIn();

		// Then
		CompareMDAs.checkExpectation(expectedResultBuilder.build(), output);
		checkDerivatives(expectedDerivative, derivative);
	}


    @Test
    public void TestNonFullPool() {
        // Given
        final int[] inputDimensions = { 4, 4, 2 };
        final int[] poolingSize = { 2, 1, 2 };
        double element = 0;
        final MDABuilder inputBuilder = new MDABuilder(inputDimensions);
        for (int k = 0; k < inputDimensions[2]; k++) {
            for (int i = 0; i < inputDimensions[0]; i++) {
                for (int j = 0; j < inputDimensions[1]; j++) {
                    inputBuilder.withDataPoint(element, i, j, k);
                    element++;
                }
            }
        }

        final MDABuilder expectedResultBuilder = new MDABuilder(2, 4, 1);
        expectedResultBuilder.withDataPoint(20D, 0, 0, 0);
        expectedResultBuilder.withDataPoint(21D, 0, 1, 0);
        expectedResultBuilder.withDataPoint(22D, 0, 2, 0);
        expectedResultBuilder.withDataPoint(23D, 0, 3, 0);
        expectedResultBuilder.withDataPoint(28D, 1, 0, 0);
        expectedResultBuilder.withDataPoint(29D, 1, 1, 0);
        expectedResultBuilder.withDataPoint(30D, 1, 2, 0);
        expectedResultBuilder.withDataPoint(31D, 1, 3, 0);

        final Map<List<Integer>, Map<List<Integer>, Double>> expectedDerivative = new HashMap<>();
        Map<List<Integer>, Double> temp = new HashMap<>();
        temp.put(List.of(0, 0, 0), 0D);
        temp.put(List.of(0, 0, 1), 0D);
        temp.put(List.of(1, 0, 0), 0D);
        temp.put(List.of(1, 0, 1), 1D);
        expectedDerivative.put(List.of(0, 0, 0), temp);
        temp = new HashMap<>();
        temp.put(List.of(0, 1, 0), 0D);
        temp.put(List.of(0, 1, 1), 0D);
        temp.put(List.of(1, 1, 0), 0D);
        temp.put(List.of(1, 1, 1), 1D);
        expectedDerivative.put(List.of(0, 1, 0), temp);
        temp = new HashMap<>();
        temp.put(List.of(0, 2, 0), 0D);
        temp.put(List.of(0, 2, 1), 0D);
        temp.put(List.of(1, 2, 0), 0D);
        temp.put(List.of(1, 2, 1), 1D);
        expectedDerivative.put(List.of(0, 2, 0), temp);
        temp = new HashMap<>();
        temp.put(List.of(0, 3, 0), 0D);
        temp.put(List.of(0, 3, 1), 0D);
        temp.put(List.of(1, 3, 0), 0D);
        temp.put(List.of(1, 3, 1), 1D);
        expectedDerivative.put(List.of(0, 3, 0), temp);
        temp = new HashMap<>();
        temp.put(List.of(2, 0, 0), 0D);
        temp.put(List.of(2, 0, 1), 0D);
        temp.put(List.of(3, 0, 0), 0D);
        temp.put(List.of(3, 0, 1), 1D);
        expectedDerivative.put(List.of(1, 0, 0), temp);
        temp = new HashMap<>();
        temp.put(List.of(2, 1, 0), 0D);
        temp.put(List.of(2, 1, 1), 0D);
        temp.put(List.of(3, 1, 0), 0D);
        temp.put(List.of(3, 1, 1), 1D);
        expectedDerivative.put(List.of(1, 1, 0), temp);
        temp = new HashMap<>();
        temp.put(List.of(2, 2, 0), 0D);
        temp.put(List.of(2, 2, 1), 0D);
        temp.put(List.of(3, 2, 0), 0D);
        temp.put(List.of(3, 2, 1), 1D);
        expectedDerivative.put(List.of(1, 2, 0), temp);
        temp = new HashMap<>();
        temp.put(List.of(2, 3, 0), 0D);
        temp.put(List.of(2, 3, 1), 0D);
        temp.put(List.of(3, 3, 0), 0D);
        temp.put(List.of(3, 3, 1), 1D);
        expectedDerivative.put(List.of(1, 3, 0), temp);

        // When
        final ForwardOutputTuple result = maxPoolingLayer.forward(new PoolingLayer(poolingSize, PoolingLibrary.PoolingType.MAX, inputDimensions), inputBuilder.build());
        final MDA output = result.getOutput();
        final Map<List<Integer>, Map<List<Integer>, Double>> derivative = result.getdOutByDIn();

        // Then
        CompareMDAs.checkExpectation(expectedResultBuilder.build(), output);
        checkDerivatives(expectedDerivative, derivative);
    }
}
