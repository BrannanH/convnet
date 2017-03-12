package services;

import layers.Feature;

/**
 * This service carries out comparisons between dimensions specified at the input to a layer, the feature map
 * and the feature map's active dimensions. These are needed at construction of the neural net, and every layer
 * will need to verify its operations are dimensionally succinct.
 * @author Brannan
 *
 */
public class DimensionVerificationService {

	/**
	 * This method returns true if neither of its validations throw errors. This checks
	 * <li> The number of active dimensions equal the number of dimensions specified on the feature map </li>
	 * <li> The feature map is smaller than the input to the layer which it is to be compared with </li>
	 * @param inputDimensions
	 * @param feature
	 * @return
	 */
	public boolean verify(int[] inputDimensions, Feature feature) {
		
		if(feature.getActiveDimensions().length != feature.getFeatureMap().getDimensions().length) {
			
			throw(new IllegalArgumentException("Mismatch between number of specified dimensions to filter in, and number of dimensions on the filter"));
		}
		
		for(int i = 0; i < feature.getActiveDimensions().length; i++) {
			
			if(inputDimensions[i] < feature.getFeatureMap().getDimensions()[i]) {
				
				throw(new IllegalArgumentException("Each filter dimension must be smaller than or equal to the equivalent dimension on the MDA"));			
			}
		}
		return true;
	}
	
}
