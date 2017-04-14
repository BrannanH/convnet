package layers.pooling;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.google.inject.Inject;

import services.DimensionVerificationService;

public class MeanPoolingLayer extends PoolingLayer {

	@Inject
	public MeanPoolingLayer(DimensionVerificationService dimensionsService) {
		super(dimensionsService);
	}

	@Override
	protected List<PoolTuple> selectFromPool(Set<PoolTuple> poolList) {
		List<PoolTuple> result = new ArrayList<>();
		PoolTuple output = new PoolTuple();
		output.element = 0D;
		result.add(output);
		for(PoolTuple contribution : poolList){
			result.get(0).element += (contribution.element/poolList.size());
			PoolTuple derivative = new PoolTuple();
			derivative.element = 1D/poolList.size();
			derivative.origin = contribution.origin;
			result.add(derivative);
		}
		return result;
	}

}
