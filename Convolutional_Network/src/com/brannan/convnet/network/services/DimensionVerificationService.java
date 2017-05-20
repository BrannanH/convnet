package com.brannan.convnet.network.services;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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
     * @param feature
     * @return
     */
    public void verifyLeftBiggerThanRight(final List<Integer> inputDimensions, final List<Integer> featureDimensions) {

        if (inputDimensions.size() != featureDimensions.size()) {
            throw new IllegalArgumentException("The correct number of dimensions she be specified: Input has "
                    + inputDimensions.size() + " whereas the filter has " + featureDimensions.size());
        }

        for (int i = 0; i < featureDimensions.size(); i++) {

            if (inputDimensions.get(i) < featureDimensions.get(i)) {

                throw new IllegalArgumentException(
                        "Each filter dimension must be smaller than or equal to the equivalent dimension on the MDA");
            }
        }
    }


    /**
     * Verifies that all references in the build up derivatives mapping are dimensionally consistent. 
     * @param testMap
     * @return
     */
    public boolean verifyDerivativeMap(final Map<List<Integer>, Map<List<Integer>, Double>> testMap) {
        int outerMaxLength = testMap.keySet().stream().collect(Collectors.maxBy(Comparator.comparingInt(List::size))).get()
                .size();
        Optional<List<Integer>> invalid = testMap.keySet().stream().filter(l -> l.size() != outerMaxLength).findAny();
        if (invalid.isPresent()) {
            throw new IllegalArgumentException("The dimensions specified for dOutByDIn should have consistent length.");
        }

        int innerMaxLength = testMap.values().stream().flatMap((Map<List<Integer>, Double> k) -> k.keySet().stream())
                .collect(Collectors.maxBy(Comparator.comparingInt(List::size))).get().size();
        invalid = testMap.values().stream().flatMap((Map<List<Integer>, Double> k) -> k.keySet().stream())
                .filter(l -> l.size() != innerMaxLength).findAny();
        if (invalid.isPresent()) {
            throw new IllegalArgumentException(
                    "The dimensions specified for dLossByDOut should have consistent length.");
        }
        
        if (outerMaxLength != innerMaxLength) {
            throw new IllegalArgumentException(
                    "The number of dimensions in the derivative mappings should be the same.");
        }
        return true;
    }
}
