package layers.pooling;

import static fundamentals.MDAHelper.get;
import static fundamentals.MDAHelper.put;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.google.common.collect.Lists;

import fundamentals.MDA;
import fundamentals.MDABuilder;
import layers.pooling.PoolingLibrary.PoolingType;
import services.DimensionVerificationService;

public class TestMeanPoolingLayer {

    private PoolingLayer layer;

    @Mock
    private DimensionVerificationService dimensionVerificationService;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        when(dimensionVerificationService.verify(anyListOf(Integer.class), anyListOf(Integer.class))).thenReturn(true);
        layer = new PoolingLayer(dimensionVerificationService);
    }


    @Test
    public void testSmallPool() {
        // Given;
        int[] inputDimensions = { 4, 4 };
        MDA operand = new MDABuilder().withDimensions(inputDimensions).build();
        double element = 1D;

        for (int j = 0; j < inputDimensions[1]; j++) {
            for (int i = 0; i < inputDimensions[1]; i++) {
                put(operand, element, j, i);
                element++;
            }
        }

        List<Integer> poolingSize = Lists.newArrayList(2, 1);

        MDA expectedOutput = new MDABuilder().withDimensions(2, 4).build();
        put(expectedOutput, 3, 0, 0);
        put(expectedOutput, 4, 0, 1);
        put(expectedOutput, 5, 0, 2);
        put(expectedOutput, 6, 0, 3);
        put(expectedOutput, 11, 1, 0);
        put(expectedOutput, 12, 1, 1);
        put(expectedOutput, 13, 1, 2);
        put(expectedOutput, 14, 1, 3);

        // When
        MDA output = layer.forward(operand, poolingSize, PoolingType.MEAN).getOutput();

        // Then
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 4; j++) {
                assertEquals("", get(expectedOutput, i, j), get(output, i, j), 0);
            }
        }
    }
}
