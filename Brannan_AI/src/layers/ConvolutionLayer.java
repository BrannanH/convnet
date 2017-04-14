package layers;

import java.util.List;

import com.google.inject.Inject;

import fundamentals.HelperLibrary;
import fundamentals.MDA;
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
	public ForwardOutputTuple forward(MDA operand, Feature feature) {
		// TODO Auto-generated method stub
		return null;
	}

	
	/**
	 * @see{Layer#forwardNoTrain}
	 */
	@Override
	public MDA forwardNoTrain(MDA operand, Feature feature) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @see{Layer#reverse}
	 */
	@Override
	public ReverseOutputTuple reverse(MDA dLossByDOut, MDA dOutByDIn, MDA dOutByDFeature) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @see{Layer#outputDimensions}
	 */
	@Override
	public List<Integer> outputDimensions(List<Integer> inputDimensions, Feature feature) {
		
		dimensionsService.verify(inputDimensions, feature);
		
		int[] results = new int[inputDimensions.size()];
		
		for(int i = 0; i < inputDimensions.size(); i++) {
			
			results[i] = inputDimensions.get(i);
			
			for(int dimension : feature.getActiveDimensions()) {
				
				if(dimension == i) {
					
					if(Math.floorMod(feature.getFeatureMap().getDimensions().get(i), 2) == 1) {
						
						results[i] -= (feature.getFeatureMap().getDimensions().get(i)-1);
						
					} else {
						
						results[i] -= (feature.getFeatureMap().getDimensions().get(i));
					}
				}
			}
		}
		
		return HelperLibrary.arrayAsList(results);
	}

}
