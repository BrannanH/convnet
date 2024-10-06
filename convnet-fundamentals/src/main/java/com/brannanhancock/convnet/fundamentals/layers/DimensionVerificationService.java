package com.brannanhancock.convnet.fundamentals.layers;

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
}
