package layers.pooling;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import services.DimensionVerificationService;

public class MedianPoolingLayer extends PoolingLayer {

	public MedianPoolingLayer(DimensionVerificationService dimensionsService) {
		super(dimensionsService);
	}

	@Override
	protected List<PoolTuple> selectFromPool(Set<PoolTuple> pool) {
		
		List<PoolTuple> poolList = new ArrayList<>();
		poolList.addAll(pool);
		Collections.sort(poolList, new Comparator<PoolTuple>() {

			@Override
			public int compare(PoolTuple arg0, PoolTuple arg1) {
				if(arg0.element < arg1.element) {
					return -1;
				} else {
					return 1;
				}
			}
		});
			
		int median = Math.floorDiv(pool.size(), 2);
		if(Math.floorMod(pool.size(), 2) == 0) {
			median--;
		}
		List<PoolTuple> output = new ArrayList<>();
		output.add(poolList.get(median));
		return output;
	}


}
