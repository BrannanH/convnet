package layers;


import static java.util.Collections.max;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.google.inject.Inject;

import services.DimensionVerificationService;

/**
 * This Layer pools by selecting the max from each pool.
 * @author Brannan
 *
 */
public class MaxPoolingLayer extends SeparablePoolingLayer {

	@Inject
	public MaxPoolingLayer(DimensionVerificationService dimensionsService) {
		super(dimensionsService);
	}
	

	/**
	 * 
	 */
	@Override
	protected List<PoolTuple> selectFromPool(List<PoolTuple> poolList) {
		
		PoolTuple max = max(poolList, new Comparator<PoolTuple>() {

			@Override
			public int compare(PoolTuple arg0, PoolTuple arg1) {
				if(arg0.element < arg1.element) {
					return -1;
				} else {
					return 1;
					}
			}
			
		});
		
		List<PoolTuple> result = new ArrayList<>();
		result.add(max);
		
		PoolTuple derivative = new PoolTuple();
		derivative.element = 1D;
		derivative.origin = max.origin;
		result.add(derivative);
		
			return result;
		}

}
