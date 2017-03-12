package layers;

import com.google.inject.Inject;

import fundamentals.MultiD;
import services.DimensionVerificationService;

/**
 * This class implements the Injected Constructor, and the output dimension calculation, but allows
 * the forward and reverse path methods to be specified by a child class. This allows for various methods
 * of pooling, e.g. max pooling and average pooling.
 * @author Brannan
 *
 */
public abstract class PoolingLayer implements Layer {
	
	private DimensionVerificationService dimensionsService;

	@Inject
	public PoolingLayer(DimensionVerificationService dimensionsService) {
		this.dimensionsService = dimensionsService;
	}

	
	/**
	 * @see{Layer#forward}
	 */
	@Override
	abstract public ForwardOutputTuple forward(MultiD operand, Feature feature);

	
	/**
	 * @see{Layer#forwardNoTrain}
	 */
	@Override
	abstract public MultiD forwardNoTrain(MultiD operand, Feature feature);

	
	/**
	 * @see{Layer#reverse}
	 */
	@Override
	abstract public ReverseOutputTuple reverse(MultiD dLossByDOut, MultiD dOutByDIn, MultiD dOutByDFeature);

	/**
	 * @see{Layer#outputDimensions}
	 */
	@Override
	public int[] outputDimensions(int[] inputDimensions, Feature feature) {
		
		dimensionsService.verify(inputDimensions, feature);
		
		int[] results = new int[inputDimensions.length];
		results = inputDimensions.clone();
		
		for(int i = 0; i < feature.getActiveDimensions().length; i++) {
			
			results[feature.getActiveDimensions()[i]] = Math.floorDiv( results[feature.getActiveDimensions()[i]], feature.getFeatureMap().getDimensions()[i] );
		}
		
		return results;
	}

}
