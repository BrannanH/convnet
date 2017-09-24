package com.brannan.convnet.network.fundamentals;

import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class TestHelperLibrary {
    
    @Test
    public void test() {
        // Given
        int[] array1 = {0, 1, 2};
        int[] array2 = {1, 1, 2};
        
        // When
        boolean result = HelperLibrary.arrayEquality(array1, array2);
        
        // Then
        assertFalse("Arrays are not equal", result);
    }

}
