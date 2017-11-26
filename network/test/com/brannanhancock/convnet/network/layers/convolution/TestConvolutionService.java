package com.brannanhancock.convnet.network.layers.convolution;

import com.brannanhancock.convnet.fundamentals.MDA;
import com.brannanhancock.convnet.fundamentals.MDABuilder;
import com.brannanhancock.convnet.network.services.DimensionVerificationService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TestConvolutionService {

    private ConvolutionService service;
    @Mock private DimensionVerificationService dimensionVerificationService;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        service = new ConvolutionService(dimensionVerificationService);
    }

    @Test
    public void testDeferral() {
        // Given
        int[] operandDimensions = {2,2};
        int[] featureDimensions = {3,3};

        // When
        service.verifyInputBiggerThanFeature(operandDimensions, featureDimensions);

        // Then
        Mockito.verify(dimensionVerificationService).verifyLeftBiggerThanRight(operandDimensions, featureDimensions);
    }


    @Test
    public void testSmallChunks() {
        // Given
        int[] operandDimensions = {5,5,3};
        int[] featureDimensions = {3,3,1};
        MDA operand = new MDABuilder(operandDimensions).build();

        // When
        Set<Chunk> result = service.chunk(operand, featureDimensions);

        // Then
        assertEquals("result should have 9 chunks", 9*3, result.size());
    }

    @Test
    public void testBigChunks() {
        // Given
        int[] operandDimensions = {32,32,3,5};
        int[] featureDimensions = {5,5,3,12};
        MDA operand = new MDABuilder(operandDimensions).build();

        // When
        Set<Chunk> result = service.chunk(operand, featureDimensions);

        // Then
        assertEquals("result should have (32-5+1)*(32-5+1)*(3-3+1)*5 chunks", 3920, result.size());
    }

    @Test
    public void testPerfectFit() {
        // Given
        int[] operandDimensions = {5,5,3};
        int[] featureDimensions = {5,5,12};
        MDA operand = twoByTwoMDA(operandDimensions[0], operandDimensions[1]);

        // When
        Set<Chunk> result = service.chunk(operand, featureDimensions);

        // Then
        assertEquals("There should only three chunks", 3, result.size(), 0);
        int[] sourceEntities = result.stream().mapToInt(Chunk::getSourceEntity).sorted().toArray();
        assertEquals("First source should be 0", 0, sourceEntities[0], 0);
        assertEquals("Second source should be 1", 1, sourceEntities[1], 0);
        assertEquals("Third source should be 2", 2, sourceEntities[2], 0);

        Optional<Chunk> zerothEntity = result.stream().filter(chunk -> chunk.getSourceEntity() == 0).findAny();
        assertTrue(zerothEntity.isPresent());
        assertEquals(zerothEntity.get().getData(), twoByTwoWithOffset(5,5, 1));

        Optional<Chunk> firstEntity = result.stream().filter(chunk -> chunk.getSourceEntity() == 1).findAny();
        assertTrue(firstEntity.isPresent());
        assertEquals(firstEntity.get().getData(), twoByTwoWithOffset(5,5, 26));

        Optional<Chunk> secondEntity = result.stream().filter(chunk -> chunk.getSourceEntity() == 2).findAny();
        assertTrue(secondEntity.isPresent());
        assertEquals(secondEntity.get().getData(), twoByTwoWithOffset(5,5, 51));
    }

    private MDA twoByTwoMDA(int size1, int size2) {
        MDABuilder operandBuilder = new MDABuilder(size1, size2, 3);
        double count = 1D;
        for(int k = 0; k < 3; k++) {
            for (int i = 0; i < size1; i++) {
                for (int j = 0; j < size2; j++) {
                    operandBuilder.withDataPoint(count, i, j, k);
                    count++;
                }
            }
        }
        return operandBuilder.build();
    }

    private MDA twoByTwoWithOffset(int size1, int size2, double offset) {
        MDABuilder operandBuilder = new MDABuilder(size1, size2);
        double count = offset;
            for (int i = 0; i < size1; i++) {
                for (int j = 0; j < size2; j++) {
                    operandBuilder.withDataPoint(count, i, j);
                    count++;
                }
            }
        return operandBuilder.build();
    }
}
