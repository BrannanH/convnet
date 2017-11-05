package com.brannanhancock.convnet.network.services;

import static com.brannanhancock.convnet.network.services.ExtractDimensionService.subsetOf;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.brannanhancock.convnet.fundamentals.MDA;
import com.brannanhancock.convnet.fundamentals.MDABuilder;
import com.brannanhancock.convnet.network.services.ExtractDimensionService;

public class TestExtractDimensionService {

	int[] dimensions = {2,3,4, 5, 9};
	MDABuilder multiD1Builder = new MDABuilder(dimensions);
	final MDA multiD1 = multiD1Builder.build();

	@Test(expected = IllegalArgumentException.class)
	public void testDimensionTooLarge() {
		// Given
		final int dimension = 5;
		final int[] instance = {1};
		final MDA multiD1 = multiD1Builder.build();

		// When
		ExtractDimensionService.subsetOf(multiD1, dimension, instance);

		// Then expect exception
	}


    @Test(expected = IllegalArgumentException.class)
    public void testDimensionNegative() {
        // Given
        final int dimension = -1;
        final int[] instance = { 1 };


        // When
        subsetOf(multiD1, dimension, instance);

        // Then
    }

	@Test(expected = IllegalArgumentException.class)
	public void testInstanceTooLarge() {
		// Given
		final int dimension = 2;
		final int[] instance = {4};

		// When
		subsetOf(multiD1, dimension, instance);

		// Then
	}

	@Test(expected = IllegalArgumentException.class)
	public void testInstanceNegative() {
		// Given
		final int dimension = 2;
		final int[] instance = {-1};

		// When
		subsetOf(multiD1, dimension, instance);

		// Then
	}

	@Test
	public void checkRightDimensionalitySingleInstance() {
		// Given
		final int dimension = 2;
		final int instance = 0;

		// When
		final MDA result = subsetOf(multiD1, dimension, instance);

		// Then
		assertEquals("There should be one fewer dimensions in the result than the original operand", multiD1.getDimensions().length - 1, result.getDimensions().length);
		assertEquals("Dimension 0's size should be the same", multiD1.getDimensions()[0], result.getDimensions()[0]);
		assertEquals("Dimension 1's size should be the same", multiD1.getDimensions()[0], result.getDimensions()[0]);
	}

	@Test
	public void checkDataExtractsSingleInstance() {
		// Given
		final int dimension = 2;
		final int instance = 0;
		double element = 0D;

		for(int i = 0; i < dimensions[0]; i++) {
			for(int j = 0; j < dimensions[1]; j++) {
				for(int k = 0; k < dimensions[2]; k++) {
					for(int l = 0; l < dimensions[3]; l++) {
						for(int m = 0; m < dimensions[3]; m++) {
							multiD1Builder.withDataPoint(element, i, j, k, 0 ,0);
							element++;
						}
					}
				}
			}
		}

		// When
		final MDA result = subsetOf(multiD1, dimension, instance);

		// Then
		for(int i = 0; i < dimensions[0]; i++) {
			for(int j = 0; j < dimensions[1]; j++) {
				for(int l = 0; l < dimensions[3]; l++) {
					for(int m = 0; m < dimensions[3]; m++) {
						assertEquals("Data should be transferred " + i + " " + j, multiD1.get(i, j, instance, l, m), result.get(i, j, l, m), 0);
					}
				}
			}
		}
	}

	@Test
	public void checkRightDimensionalityMultipleInstance() {
		// Given
		final int dimension = 2;
		final int[] instances = {0, 1};

		// When
		final MDA result = subsetOf(multiD1, dimension, instances);

		// Then
		assertEquals("Dimensions should be the same", multiD1.getDimensions().length, result.getDimensions().length);
		assertEquals("Dimension 0's size should be the same", multiD1.getDimensions()[0], result.getDimensions()[0]);
		assertEquals("Dimension 1's size should be the same", multiD1.getDimensions()[1], result.getDimensions()[1]);
		assertEquals("Dimension 2's size should be the length of the instances provided", instances.length, result.getDimensions()[dimension]);
		assertEquals("Dimension 3's size should be the same", multiD1.getDimensions()[3], result.getDimensions()[3]);
		assertEquals("Dimension 4's size should be the same", multiD1.getDimensions()[4], result.getDimensions()[4]);
	}

	@Test
	public void checkDataExtractsMultipleInstance() {
		// Given
		final int dimension = 2;
		final int[] instances = {0,1};
		double element = 0D;

		for(int i = 0; i < dimensions[0]; i++) {
			for(int j = 0; j < dimensions[1]; j++) {
				for(int k = 0; k < dimensions[2]; k++) {
					for(int l = 0; l < dimensions[3]; l++) {
						for(int m = 0; m < dimensions[3]; m++) {
							multiD1Builder.withDataPoint(element, i, j, k, 0 ,0);
							element++;
						}
					}
				}
			}
		}

		// When
		final MDA result = subsetOf(multiD1Builder.build(), dimension, instances);

		// Then
		for(final int instance:instances) {
			for(int i = 0; i < dimensions[0]; i++) {
				for(int j = 0; j < dimensions[1]; j++) {
					for(int l = 0; l < dimensions[3]; l++) {
						for(int m = 0; m < dimensions[3]; m++) {
							assertEquals("Data should be transferred " + i + " " + j, multiD1.get(i, j, instance, l, m), result.get(i, j, instance, l, m), 0);
						}
					}
				}
			}
		}
	}


}
