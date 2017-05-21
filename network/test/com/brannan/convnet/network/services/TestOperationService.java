package com.brannan.convnet.network.services;

import static com.brannan.convnet.network.fundamentals.MDAHelper.get;
import static com.brannan.convnet.network.fundamentals.MDAHelper.put;
import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import com.brannan.convnet.network.fundamentals.MDA;
import com.brannan.convnet.network.fundamentals.MDABuilder;

/**
 * This class tests the ElementWiseOperationService by constructing an Addition
 * Service. Only the methods defined in the abstract ElementWiseOperationService
 * are tested here.
 * 
 * @author Brannan
 *
 */
public class TestOperationService {

    MDA multiD1;
    MDA multiD2;
    MDA result;
    OperationService service;


    @Before
    public void setUp() {
        service = new OperationService();
    }


    @Test(expected = IllegalArgumentException.class)
    public void testDimensionCheck() {
        // Given
        multiD1 = new MDABuilder().withDimensions(2, 2).build();
        multiD2 = new MDABuilder().withDimensions(1, 2).build();

        // When
        service.operate(multiD1, multiD2, (d1, d2) -> d1 + d2);

        // Then expect exception
    }


    @Test(expected = IllegalArgumentException.class)
    public void testDimensionLengthCheck() {
        // Given
        MDA multiD1 = new MDABuilder().withDimensions(2, 2).build();
        MDA multiD2 = new MDABuilder().withDimensions(2, 2, 1).build();

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
        multiD1 = new MDABuilder().withDimensions(1).build();
        multiD2 = new MDABuilder().withDimensions(1).build();

        put(multiD1, 1D, 0);
        put(multiD2, 2D, 0);

        // When
        MDA result = service.operate(multiD1, multiD2, (d1, d2) -> d1+d2);

        // Then
        assertEquals("Returned amount should be the sum", 3D, get(result, 0), 0);
    }


    /**
     * This tests the element wise operation on a 2x2 MDA
     */
    @Test
    public void biggerAddition() {
        // Given
        multiD1 = new MDABuilder().withDimensions(2, 2).build();
        multiD2 = new MDABuilder().withDimensions(2, 2).build();

        put(multiD1, 1D, 0, 0);
        put(multiD1, 2D, 0, 1);
        put(multiD1, 3D, 1, 0);
        put(multiD1, 4D, 1, 1);

        put(multiD2, 5D, 0, 0);
        put(multiD2, 6D, 0, 1);
        put(multiD2, 7D, 1, 0);
        put(multiD2, 8D, 1, 1);

        // When
        result = service.operate(multiD1, multiD2, (d1, d2) -> d1+d2);

        // Then
        assertEquals("Returned amount should be the sum", 6D, get(result, 0, 0), 0);
        assertEquals("Returned amount should be the sum", 8D, get(result, 0, 1), 0);
        assertEquals("Returned amount should be the sum", 10D, get(result, 1, 0), 0);
        assertEquals("Returned amount should be the sum", 12D, get(result, 1, 1), 0);
    }


    /**
     * This tests the element wise operation on a fully populated 28x28x3x10 MDA
     */
    @Test
    public void biggestAddition() {
        // Given
        int[] bigDimensions = { 28, 28, 3, 10 };
        int[] position = new int[bigDimensions.length];
        multiD1 = new MDABuilder().withDimensions(bigDimensions).build();
        multiD2 = new MDABuilder().withDimensions(bigDimensions).build();
        double element = 1;

        for (int row = 0; row < bigDimensions[0]; row++) {
            for (int column = 0; column < bigDimensions[1]; column++) {
                for (int channel = 0; channel < bigDimensions[2]; channel++) {
                    for (int image = 0; image < bigDimensions[3]; image++) {
                        position[0] = row;
                        position[1] = column;
                        position[2] = channel;
                        position[3] = image;
                        put(multiD1, element, position);
                        put(multiD2, 2 * element, position);
                        element++;
                    }
                }
            }
        }

        // When
        result = service.operate(multiD1, multiD2, (d1, d2) -> d1+d2);

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
                                get(result, position), 0);
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
        int[] bigDimensions = { 28, 28, 3, 10 };
        int[] position = new int[bigDimensions.length];
        multiD1 = new MDABuilder().withDimensions(bigDimensions).build();
        double element = 1;

        for (int row = 0; row < bigDimensions[0]; row++) {
            for (int column = 0; column < bigDimensions[1]; column++) {
                for (int channel = 0; channel < bigDimensions[2]; channel++) {
                    for (int image = 0; image < bigDimensions[3]; image++) {
                        position[0] = row;
                        position[1] = column;
                        position[2] = channel;
                        position[3] = image;
                        put(multiD1, element, position);
                        element++;
                    }
                }
            }
        }

        // When
        result = service.operate(multiD1, 10D, (d1, d2) -> d1+d2);

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
                                get(result, position), 0);
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
        multiD1 = new MDABuilder().withDimensions(1).build();
        multiD2 = new MDABuilder().withDimensions(1).build();

        put(multiD1, 1D, 0);
        put(multiD2, 2D, 0);

        // When
        MDA result = service.operate(multiD1, multiD2, (d1, d2) -> d1*d2);

        // Then
        assertEquals("Returned amount should be the product", 2D, get(result, 0), 0);
    }


    /**
     * This tests the element wise opration on a 2x2 array
     */
    @Test
    public void biggerMultiplication() {
        // Given
        multiD1 = new MDABuilder().withDimensions(2, 2).build();
        multiD2 = new MDABuilder().withDimensions(2, 2).build();

        put(multiD1, 1D, 0, 0);
        put(multiD1, 2D, 0, 1);
        put(multiD1, 3D, 1, 0);
        put(multiD1, 4D, 1, 1);

        put(multiD2, 5D, 0, 0);
        put(multiD2, 6D, 0, 1);
        put(multiD2, 7D, 1, 0);
        put(multiD2, 8D, 1, 1);

        // When
        result = service.operate(multiD1, multiD2, (d1, d2) -> d1*d2);

        // Then
        assertEquals("Returned amount should be the sum", 5D, get(result, 0, 0), 0);
        assertEquals("Returned amount should be the sum", 12D, get(result, 0, 1), 0);
        assertEquals("Returned amount should be the sum", 21D, get(result, 1, 0), 0);
        assertEquals("Returned amount should be the sum", 32D, get(result, 1, 1), 0);
    }


    /**
     * This tests the element wise operation on a fully populated 28x28x3x10
     * array
     */
    @Test
    public void biggestMultiplication() {
        // Given
        int[] bigDimensions = { 28, 28, 3, 10 };
        int[] position = new int[bigDimensions.length];
        multiD1 = new MDABuilder().withDimensions(bigDimensions).build();
        multiD2 = new MDABuilder().withDimensions(bigDimensions).build();
        double element = 1;

        for (int row = 0; row < bigDimensions[0]; row++) {
            for (int column = 0; column < bigDimensions[1]; column++) {
                for (int channel = 0; channel < bigDimensions[2]; channel++) {
                    for (int image = 0; image < bigDimensions[3]; image++) {
                        position[0] = row;
                        position[1] = column;
                        position[2] = channel;
                        position[3] = image;
                        put(multiD1, element, position);
                        put(multiD2, 2 * element, position);
                        element++;
                    }
                }
            }
        }

        // When

        result = service.operate(multiD1, multiD2, (d1, d2) -> d1*d2);
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
                                2 * Math.pow(element, 2), get(result, position), 0);
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
        int[] bigDimensions = { 28, 28, 3, 10 };
        int[] position = new int[bigDimensions.length];
        multiD1 = new MDABuilder().withDimensions(bigDimensions).build();
        double element = 1;

        for (int row = 0; row < bigDimensions[0]; row++) {
            for (int column = 0; column < bigDimensions[1]; column++) {
                for (int channel = 0; channel < bigDimensions[2]; channel++) {
                    for (int image = 0; image < bigDimensions[3]; image++) {
                        position[0] = row;
                        position[1] = column;
                        position[2] = channel;
                        position[3] = image;
                        put(multiD1, element, position);
                        element++;
                    }
                }
            }
        }

        // When
        result = service.operate(multiD1, 10D, (d1, d2) -> d1*d2);

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
                                get(result, position), 0);
                        element++;
                    }
                }
            }
        }
    }
}
