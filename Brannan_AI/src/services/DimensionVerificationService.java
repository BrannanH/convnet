package services;

import layers.Feature;

public class DimensionVerificationService {

	/**
	 * This method returns true if neither of its validations throw errors.
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
