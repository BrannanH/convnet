package com.brannanhancock.convnet.fundamentals.layers.convolution;



import com.brannanhancock.convnet.fundamentals.layers.DimensionVerificationService;
import com.brannanhancock.convnet.fundamentals.mda.MDA;
import com.brannanhancock.convnet.fundamentals.mda.MDABuilder;
import jakarta.inject.Inject;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

class ConvolutionService {

    private final DimensionVerificationService dimensionVerificationService;

    @Inject
    ConvolutionService(final DimensionVerificationService dimensionVerificationService) {
        this.dimensionVerificationService = dimensionVerificationService;
    }

    public boolean verifyInputBiggerThanFeature(int[] operandDimensions, int[] featureDimensions) {
        return dimensionVerificationService.verifyLeftBiggerThanRight(operandDimensions, featureDimensions);
    }

    public Set<Chunk> chunk(MDA operand, int[] featureDimensions) {
        int[] numberOfCentres = computeNumberOfCentres(operand.getDimensions(), featureDimensions);
        Set<int[]> destinations = computeDestintations(numberOfCentres);
        int[] alpha = computeAlpha(operand, featureDimensions);
        Set<Chunk> chunks = new HashSet<>();
        for(int i = 0; i < operand.getDimensions()[operand.getDimensions().length - 1]; i++) {
          for(int[] destination : destinations) {
            chunks.add(singleChunk(operand, destination, featureDimensions, alpha, i));
          }
        }
        return chunks;
    }

    private Chunk singleChunk(MDA operand, int[] destination, int[] featureDimensions, int[] alpha, int entry) {
        int[] chunkDimensions = new int[featureDimensions.length - 1];
        for(int i = 0; i < featureDimensions.length - 1; i++) {
            chunkDimensions[i] = featureDimensions[i];
        }
        MDABuilder builder = new MDABuilder(chunkDimensions);
        recursiveChunk(destination, operand, alpha, 0, new int[destination.length], builder, entry);
        return new Chunk(builder.build(), destination, entry);
    }

    private int[] computeAlpha(MDA operand, int[] featureDimensions) {
        int[] alpha = new int[featureDimensions.length - 1];
        for(int i = 0; i < featureDimensions.length - 1; i++) {
            alpha[i] = (featureDimensions[i] - 1) / 2;
        }
        return alpha;
    }

    private Set<int[]> computeDestintations(int[] numberOfCentres) {
        int totalNumberOfCentres = Arrays.stream(numberOfCentres).reduce(1, (a, b) -> a*b);
        Set<int[]> destinations = new HashSet<>();
        populateDestinations(destinations, numberOfCentres, 0, new int[numberOfCentres.length]);
        return destinations;
    }

    private void populateDestinations(Set<int[]> destinations, int[] numberOfCentres, int positionIndex, int[] destination) {
        for(int i = 0; i < numberOfCentres[positionIndex]; i++) {
            destination[positionIndex] = i;
            if(positionIndex == numberOfCentres.length - 1) {
                destinations.add(destination.clone());
            } else {
                populateDestinations(destinations, numberOfCentres, positionIndex + 1, destination);
            }
        }
    }

    private int[] computeNumberOfCentres(int[] operandDimensions, int[] featureDimensions) {
        int[] numberOfCentres = new int[operandDimensions.length - 1];
        for(int i = 0; i < operandDimensions.length - 1; i++) {
                numberOfCentres[i] = operandDimensions[i] - featureDimensions[i] + 1;
        }
        return numberOfCentres;
    }


    private void recursiveChunk(final int[] destination, MDA operand, int[] alpha, int dimension, int[] source, MDABuilder builder, int entry) {
        for (int i = 0; i < (2 * alpha[dimension]) + 1; i++) {
            source[dimension] = destination[dimension] + i;
            if (dimension == destination.length - 1) {
                int[] locationInOperand = new int[destination.length + 1];
                int[] locationInBuilder = source.clone();
                for(int j = 0; j < destination.length; j++) {
                    locationInOperand[j] = source[j];
                    locationInBuilder[j] = source[j] - destination[j];
                }
                locationInOperand[locationInOperand.length - 1] = entry;
                builder.withDataPoint(operand.get(locationInOperand), locationInBuilder);
            } else {
                recursiveChunk(destination, operand, alpha, dimension + 1, source, builder, entry);
            }
        }
    }
}
