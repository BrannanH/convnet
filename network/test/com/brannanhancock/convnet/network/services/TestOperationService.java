package com.brannanhancock.convnet.network.services;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import com.brannanhancock.convnet.fundamentals.MDA;
import com.brannanhancock.convnet.fundamentals.MDABuilder;
import com.brannanhancock.convnet.network.services.OperationService;

/**
 * This class tests the ElementWiseOperationService by constructing an Addition
 * Service. Only the methods defined in the abstract ElementWiseOperationService
 * are tested here.
 *
 * @author Brannan
 *
 */
public class TestOperationService {

    private MDABuilder multiD1;
    private MDABuilder multiD2;
    private MDA result;
    private OperationService service;


    @Before
    public void setUp() {
        service = new OperationService();
    }


    @Test(expected = IllegalArgumentException.class)
    public void testDimensionCheck() {
        // Given
        multiD1 = new MDABuilder(2, 2);
        multiD2 = new MDABuilder(1, 2);

        // When
        service.operate(multiD1.build(), multiD2.build(), (d1, d2) -> d1 + d2);

        // Then expect exception
    }


    @Test(expected = IllegalArgumentException.class)
    public void testDimensionLengthCheck() {
        // Given
        final MDA multiD1 = new MDABuilder(2, 2).build();
        final MDA multiD2 = new MDABuilder(2, 2, 1).build();

        // When
        service.operate(multiD1, multiD2, (d1, d2) -> d1+d2);

        // Then expect exception
    }

    /**
     * This tests a simple 1 element MDA operation
     */
    @Test
    public void simpleAddition() {
        // Given
        multiD1 = new MDABuilder(1);
        multiD2 = new MDABuilder(1);

        multiD1.withDataPoint(1D, 0);
        multiD2.withDataPoint(2D, 0);

        // When
        final MDA result = service.operate(multiD1.build(), multiD2.build(), (d1, d2) -> d1+d2);

        // Then
        assertEquals("Returned amount should be the sum", 3D, result.get(0), 0);
    }


    /**
     * This tests the element wise operation on a 2x2 MDA
     */
    @Test
    public void biggerAddition() {
        // Given
        multiD1 = new MDABuilder(2, 2);
        multiD2 = new MDABuilder(2, 2);

        multiD1.withDataPoint(1D, 0, 0);
        multiD1.withDataPoint(2D, 0, 1);
        multiD1.withDataPoint(3D, 1, 0);
        multiD1.withDataPoint(4D, 1, 1);
        multiD2.withDataPoint(5D, 0, 0);
        multiD2.withDataPoint(6D, 0, 1);
        multiD2.withDataPoint(7D, 1, 0);
        multiD2.withDataPoint(8D, 1, 1);

        // When
        result = service.operate(multiD1.build(), multiD2.build(), (d1, d2) -> d1+d2);

        // Then
        assertEquals("Returned amount should be the sum", 6D,  result.get(0, 0), 0);
        assertEquals("Returned amount should be the sum", 8D,  result.get(0, 1), 0);
        assertEquals("Returned amount should be the sum", 10D, result.get(1, 0), 0);
        assertEquals("Returned amount should be the sum", 12D, result.get(1, 1), 0);
    }


    /**
     * This tests the element wise operation on a fully populated 28x28x3x10 MDA
     */
    @Test
    public void biggestAddition() {
        // Given
        final int[] bigDimensions = { 28, 28, 3, 10};
        final int[] position = new int[bigDimensions.length];
        multiD1 = new MDABuilder(bigDimensions);
        multiD2 = new MDABuilder(bigDimensions);
        double element = 1;

        for (int row = 0; row < bigDimensions[0]; row++) {
            for (int column = 0; column < bigDimensions[1]; column++) {
                for (int channel = 0; channel < bigDimensions[2]; channel++) {
                    for (int image = 0; image < bigDimensions[3]; image++) {
                        position[0] = row;
                        position[1] = column;
                        position[2] = channel;
                        position[3] = image;
                        multiD1.withDataPoint(element, position);
                        multiD2.withDataPoint(2 * element, position);
                        element++;
                    }
                }
            }
        }

        // When
        result = service.operate(multiD1.build(), multiD2.build(), (d1, d2) -> d1+d2);

        // Then
        element = 1D;
        for (int row = 0; row < bigDimensions[0]; row++) {
            for (int column = 0; column < bigDimensions[1]; column++) {
                for (int channel = 0; channel < bigDimensions[2]; channel++) {
                    for (int image = 0; image < bigDimensions[3]; image++) {
                        position[0] = row;
                        position[1] = column;
                        position[2] = channel;
                        position[3] = image;
                        assertEquals("Value not correctly returned for " + row + column + channel + image, element * 3,
                                result.get(position), 0);
                        element++;
                    }
                }
            }
        }
    }


    /**
     * This tests the element wise operation on a fully populated 28x28x3x10
     * array having a constant added to it.
     */
    @Test
    public void biggestAdditionConstant() {
        // Given
        final int[] bigDimensions = { 28, 28, 3, 10 };
        final int[] position = new int[bigDimensions.length];
        multiD1 = new MDABuilder(bigDimensions);
        double element = 1;

        for (int row = 0; row < bigDimensions[0]; row++) {
            for (int column = 0; column < bigDimensions[1]; column++) {
                for (int channel = 0; channel < bigDimensions[2]; channel++) {
                    for (int image = 0; image < bigDimensions[3]; image++) {
                        position[0] = row;
                        position[1] = column;
                        position[2] = channel;
                        position[3] = image;
                        multiD1.withDataPoint(element, position);
                        element++;
                    }
                }
            }
        }

        // When
        result = service.operate(multiD1.build(), 10D, (d1, d2) -> d1+d2);

        // Then
        element = 1D;
        for (int row = 0; row < bigDimensions[0]; row++) {
            for (int column = 0; column < bigDimensions[1]; column++) {
                for (int channel = 0; channel < bigDimensions[2]; channel++) {
                    for (int image = 0; image < bigDimensions[3]; image++) {
                        position[0] = row;
                        position[1] = column;
                        position[2] = channel;
                        position[3] = image;
                        assertEquals("Value not correctly returned for " + row + column + channel + image, element + 10,
                                result.get(position), 0);
                        element++;
                    }
                }
            }
        }
    }

    /**
     * This tests a simple 1 element array operation
     */
    @Test
    public void simpleMultiplication() {
        // Given
        multiD1 = new MDABuilder(1);
        multiD2 = new MDABuilder(1);

        multiD1.withDataPoint(1D, 0);
        multiD2.withDataPoint(2D, 0);

        // When
        final MDA result = service.operate(multiD1.build(), multiD2.build(), (d1, d2) -> d1*d2);

        // Then
        assertEquals("Returned amount should be the product", 2D, result.get(0), 0);
    }


    /**
     * This tests the element wise opration on a 2x2 array
     */
    @Test
    public void biggerMultiplication() {
        // Given
        multiD1 = new MDABuilder(2, 2);
        multiD2 = new MDABuilder(2, 2);

        multiD1.withDataPoint(1D, 0, 0);
        multiD1.withDataPoint(2D, 0, 1);
        multiD1.withDataPoint(3D, 1, 0);
        multiD1.withDataPoint(4D, 1, 1);
        multiD2.withDataPoint(5D, 0, 0);
        multiD2.withDataPoint(6D, 0, 1);
        multiD2.withDataPoint(7D, 1, 0);
        multiD2.withDataPoint(8D, 1, 1);

        // When
        result = service.operate(multiD1.build(), multiD2.build(), (d1, d2) -> d1*d2);

        // Then
        assertEquals("Returned amount should be the sum", 5D,  result.get(0, 0), 0);
        assertEquals("Returned amount should be the sum", 12D, result.get(0, 1), 0);
        assertEquals("Returned amount should be the sum", 21D, result.get(1, 0), 0);
        assertEquals("Returned amount should be the sum", 32D, result.get(1, 1), 0);
    }


    /**
     * This tests the element wise operation on a fully populated 28x28x3x10
     * array
     */
    @Test
    public void biggestMultiplication() {
        // Given
        final int[] bigDimensions = { 28, 28, 3, 10 };
        final int[] position = new int[bigDimensions.length];
        multiD1 = new MDABuilder(bigDimensions);
        multiD2 = new MDABuilder(bigDimensions);
        double element = 1;

        for (int row = 0; row < bigDimensions[0]; row++) {
            for (int column = 0; column < bigDimensions[1]; column++) {
                for (int channel = 0; channel < bigDimensions[2]; channel++) {
                    for (int image = 0; image < bigDimensions[3]; image++) {
                        position[0] = row;
                        position[1] = column;
                        position[2] = channel;
                        position[3] = image;
                        multiD1.withDataPoint(element, position);
                        multiD2.withDataPoint(2 * element, position);
                        element++;
                    }
                }
            }
        }

        // When

        result = service.operate(multiD1.build(), multiD2.build(), (d1, d2) -> d1*d2);
        element = 1D;

        // Then
        for (int row = 0; row < bigDimensions[0]; row++) {
            for (int column = 0; column < bigDimensions[1]; column++) {
                for (int channel = 0; channel < bigDimensions[2]; channel++) {
                    for (int image = 0; image < bigDimensions[3]; image++) {
                        position[0] = row;
                        position[1] = column;
                        position[2] = channel;
                        position[3] = image;
                        assertEquals("Value not correctly returned for " + row + column + channel + image,
                                2 * Math.pow(element, 2), result.get(position), 0);
                        element++;
                    }
                }
            }
        }
    }


    /**
     * This tests the element wise operation on a fully populated 28x28x3x10
     * array being multiplied by a constant factor.
     */
    @Test
    public void biggestMultiplicationConstant() {
        // Given
        final int[] bigDimensions = { 28, 28, 3, 10 };
        final int[] position = new int[bigDimensions.length];
        multiD1 = new MDABuilder(bigDimensions);
        double element = 1;

        for (int row = 0; row < bigDimensions[0]; row++) {
            for (int column = 0; column < bigDimensions[1]; column++) {
                for (int channel = 0; channel < bigDimensions[2]; channel++) {
                    for (int image = 0; image < bigDimensions[3]; image++) {
                        position[0] = row;
                        position[1] = column;
                        position[2] = channel;
                        position[3] = image;
                        multiD1.withDataPoint(element, position);
                        element++;
                    }
                }
            }
        }

        // When
        result = service.operate(multiD1.build(), 10D, (d1, d2) -> d1*d2);

        // Then
        element = 1D;
        for (int row = 0; row < bigDimensions[0]; row++) {
            for (int column = 0; column < bigDimensions[1]; column++) {
                for (int channel = 0; channel < bigDimensions[2]; channel++) {
                    for (int image = 0; image < bigDimensions[3]; image++) {
                        position[0] = row;
                        position[1] = column;
                        position[2] = channel;
                        position[3] = image;
                        assertEquals("Value not correctly returned for " + row + column + channel + image, element * 10,
                                result.get(position), 0);
                        element++;
                    }
                }
            }
        }
    }
}
