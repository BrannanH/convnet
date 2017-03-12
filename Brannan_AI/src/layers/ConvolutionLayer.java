package layers;

import com.google.inject.Inject;

import fundamentals.MultiD;
import services.DimensionVerificationService;

/**
 * This layer allows for Convolution, or feature detection, the key operation within Convolutional Neural
 * Networks.
 * @author Brannan
 *
 */
public class ConvolutionLayer implements Layer {

	private DimensionVerificationService dimensionsService;
	
	@Inject
	public ConvolutionLayer(DimensionVerificationService dimensionsService) {
		this.dimensionsService = dimensionsService;
	}

	/**
	 * @see{Layer#forward}
	 */
	@Override
	public ForwardOutputTuple forward(MultiD operand, Feature feature) {
		// TODO Auto-generated method stub
		return null;
	}

	
	/**
	 * @see{Layer#forwardNoTrain}
	 */
	@Override
	public MultiD forwardNoTrain(MultiD operand, Feature feature) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @see{Layer#reverse}
	 */
	@Override
	public ReverseOutputTuple reverse(MultiD dLossByDOut, MultiD dOutByDIn, MultiD dOutByDFeature) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @see{Layer#outputDimensions}
	 */
	@Override
	public int[] outputDimensions(int[] inputDimensions, Feature feature) {
		
		dimensionsService.verify(inputDimensions, feature);
		
		int[] results = new int[inputDimensions.length];
		
		for(int i = 0; i < inputDimensions.length; i++) {
			
			results[i] = inputDimensions[i];
			
			for(int dimension : feature.getActiveDimensions()) {
				
				if(dimension == i) {
					
					if(Math.floorMod(feature.getFeatureMap().getDimensions()[i], 2) == 1) {
						
						results[i] -= (feature.getFeatureMap().getDimensions()[i]-1);
						
					} else {
						
						results[i] -= (feature.getFeatureMap().getDimensions()[i]);
					}
				}
			}
		}
		
		return results;
	}

}
