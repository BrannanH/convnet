package layers;

import fundamentals.MultiD;
import services.DimensionVerificationService;

/**
 * This Layer pools by selecting the max from each pool.
 * @author Brannan
 *
 */
public class MaxPoolingLayer extends PoolingLayer {

	public MaxPoolingLayer(DimensionVerificationService dimensionsService) {
		super(dimensionsService);
	}

	@Override
	public ForwardOutputTuple forward(MultiD operand, Feature feature) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MultiD forwardNoTrain(MultiD operand, Feature feature) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ReverseOutputTuple reverse(MultiD dLossByDOut, MultiD dOutByDIn, MultiD dOutByDFeature) {
		// TODO Auto-generated method stub
		return null;
	}

}
