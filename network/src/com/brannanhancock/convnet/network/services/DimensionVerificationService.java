package com.brannanhancock.convnet.network.services;

import java.util.*;
import java.util.stream.Collectors;

/**
 * This service carries out comparisons between dimensions specified at the input to a layer, the feature map
 * and the feature map's active dimensions. These are needed at construction of the neural net, and every layer
 * will need to verify its operations are dimensionally succinct.
 * @author Brannan R. Hancock
 *
 */
public class DimensionVerificationService {

    /**
     * This method returns true if neither of its validations throw errors. This
     * checks
     * <li>The number of active dimensions equal the number of dimensions
     * specified on the feature map</li>
     * <li>The feature map is smaller than the input to the layer which it is to
     * be compared with</li>
     *
     * @param inputDimensions
     * @param featureDimensions
     * @return
     */
    public boolean verifyLeftBiggerThanRight(final int[] inputDimensions, final int[] featureDimensions) {

        if (inputDimensions.length != featureDimensions.length) {
            return false;
        }

        for (int i = 0; i < featureDimensions.length; i++) {
            if (inputDimensions[i] < featureDimensions[i]) {
                return false;
            }
        }
        return true;
    }


    /**
     * Verifies that all references in the build up derivatives mapping are dimensionally consistent.
     * @param testMap
     * @return
     */
    public boolean verifyDerivativeMap(final Map<List<Integer>, Map<List<Integer>, Double>> testMap) {
        final OptionalInt outerMaxLength = testMap.keySet().stream().mapToInt(List::size).max();
        if(!outerMaxLength.isPresent()) {
            throw new IllegalArgumentException("The dimensions for dOutByDIn can not be empty.");
        }

        Optional<List<Integer>> invalid = testMap.keySet().stream()
                .filter(l -> l.size() != outerMaxLength.getAsInt())
                .findAny();

        if (invalid.isPresent()) {
            throw new IllegalArgumentException("The dimensions specified for dOutByDIn should have consistent length.");
        }

        final int innerMaxLength = testMap.values().stream().flatMap((final Map<List<Integer>, Double> k) -> k.keySet().stream())
                .collect(Collectors.maxBy(Comparator.comparingInt(List::size))).get().size();

        invalid = testMap.values().stream().flatMap((final Map<List<Integer>, Double> k) -> k.keySet().stream())
                .filter(l -> l.size() != innerMaxLength)
                .findAny();

        if (invalid.isPresent()) {
            throw new IllegalArgumentException(
                    "The dimensions specified for dLossByDOut should have consistent length.");
        }

        if (outerMaxLength.getAsInt() != innerMaxLength) {
            throw new IllegalArgumentException(
                    "The number of dimensions in the derivative mappings should be the same.");
        }
        return true;
    }
}
