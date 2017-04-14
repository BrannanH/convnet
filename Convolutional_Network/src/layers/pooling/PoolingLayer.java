package layers.pooling;

import static fundamentals.HelperLibrary.arrayAsList;
import static fundamentals.HelperLibrary.cloneList;
import static fundamentals.MDAHelper.get;
import static fundamentals.MDAHelper.put;
import static java.lang.Math.floorDiv;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntConsumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.google.inject.Inject;

import fundamentals.HelperLibrary;
import fundamentals.MDA;
import fundamentals.MDABuilder;
import layers.Feature;
import layers.ForwardOutputTuple;
import layers.Layer;
import layers.ReverseOutputTuple;
import services.DimensionVerificationService;

public abstract class PoolingLayer implements Layer {
	
	protected DimensionVerificationService dimensionsService;
	
	@Inject
	public PoolingLayer(DimensionVerificationService dimensionsService) {
		this.dimensionsService = dimensionsService;
	}

	
	/**
	 * This method needs to be overriden to specify how the specific pooling service is going to 
	 * populate its position in the pooled MDA. The first item in the list should be the element to 
	 * pass forward to the pooled MDA, and every subsequent item in the returned list is the value
	 * to put in the MDA representing the derivative of the output with respect to the input.
	 * @param poolList
	 * @return
	 */
	abstract protected List<PoolTuple> selectFromPool(Set<PoolTuple> poolList);
	
	
	@Override
	public ForwardOutputTuple forward(MDA operand, Feature feature) {
		Map<List<Integer>, Set<PoolTuple>> pools = createInitialPooling(operand);
		List<Integer> poolingSizes = new ArrayList<>();
		
		pools = createPools(operand, feature, pools, poolingSizes);
		
		MDA forward = createOutput(operand.getDimensions(), feature, pools);
		ForwardOutputTuple result = new ForwardOutputTuple(forward, null, null);
		return result;
	}

	
	@Override
	public MDA forwardNoTrain(MDA operand, Feature feature) {
		
		Map<List<Integer>, Set<PoolTuple>> pools = createInitialPooling(operand);
		List<Integer> poolingSizes = new ArrayList<>();
		
		pools = createPools(operand, feature, pools, poolingSizes);
		
		return createOutput(operand.getDimensions(), feature, pools);
	}

	private MDA createOutput(List<Integer> inputDimensions, Feature feature, Map<List<Integer>, Set<PoolTuple>> pools) {
		MDA output = new MDABuilder().withDimensions(outputDimensions(inputDimensions, feature)).build();
		for(List<Integer> position : pools.keySet()) {
			
			List<PoolTuple> forward = selectFromPool(pools.get(position));
			put(output, forward.get(0).element, position);
		}
		return output;
	}

	private Map<List<Integer>, Set<PoolTuple>> createPools(MDA operand, Feature feature,
			Map<List<Integer>, Set<PoolTuple>> pools, List<Integer> poolingSizes) {
		
		
		for(int i = 0; i < operand.getDimensions().size(); i++) {
			poolingSizes.add(1);
			for(int j = 0; j < feature.getActiveDimensions().length; j++) {
				if(i == feature.getActiveDimensions()[j]) {
					poolingSizes.set(feature.getActiveDimensions()[i], feature.getFeatureMap().getDimensions().get(i));
				}
			}
		}
		
		pools = recursivePool(pools, poolingSizes);
		return pools;
	}
	
	

	private Map<List<Integer>, Set<PoolTuple>> recursivePool(Map<List<Integer>, Set<PoolTuple>> pools, List<Integer> poolSizes) {

		Map<List<Integer>, List<List<Integer>>> intermediateMapping = new HashMap<>();
		Map<List<Integer>, List<List<Integer>>> intermediateMapping2 = new HashMap<>();

		intermediateMapping = pools.keySet().stream().collect(Collectors.groupingBy( w -> {
			List<Integer> clone = cloneList(w);
			clone.set(0, floorDiv(w.get(0), poolSizes.get(0)));
			return clone;
		}));
		
		IntStream.range(1, poolSizes.size())
		.filter(i -> poolSizes.get(i)!=1)
		.forEach(computeIntermediateMapping(poolSizes, intermediateMapping, intermediateMapping2));
		
		Map<List<Integer>, Set<PoolTuple>> poolsToReturn = new HashMap<>();
		for(Entry<List<Integer>, List<List<Integer>>> entry : intermediateMapping.entrySet()) {
			poolsToReturn.put(entry.getKey(), new HashSet<PoolTuple>());
			for(List<Integer> location : entry.getValue()) {
				poolsToReturn.get(entry.getKey()).addAll(pools.get(location));
			}
		}
		return poolsToReturn;
	}


	private IntConsumer computeIntermediateMapping(List<Integer> poolSizes, Map<List<Integer>, List<List<Integer>>> intermediateMapping,
			Map<List<Integer>, List<List<Integer>>> intermediateMapping2) {
		
		return i -> {
			
			Map<List<Integer>, List<List<Integer>>> result = intermediateMapping.keySet().stream().collect(Collectors.groupingBy( collectByMappingToQuotient(poolSizes, i)));
			
			result.entrySet().stream().forEach( updateMappings(intermediateMapping, intermediateMapping2));
			
			intermediateMapping.clear();
			
			for(Entry<List<Integer>, List<List<Integer>>> entry : intermediateMapping2.entrySet()) {
				
				intermediateMapping.put(entry.getKey(), entry.getValue());
			}
			intermediateMapping2.clear();
		};
	}


	private Consumer<? super Entry<List<Integer>, List<List<Integer>>>> updateMappings(Map<List<Integer>, List<List<Integer>>> intermediateMapping,
			Map<List<Integer>, List<List<Integer>>> intermediateMapping2) {
		
		return entry -> {
			
			if(!intermediateMapping2.containsKey(entry.getKey())) {
				
				intermediateMapping2.put(entry.getKey(), new ArrayList<List<Integer>>());
			}
			
			for(List<Integer> location: entry.getValue()) {
				
				intermediateMapping2.get(entry.getKey()).addAll(intermediateMapping.get(location));
			}
		};
	}


	private Function<? super List<Integer>, ? extends List<Integer>> collectByMappingToQuotient(List<Integer> poolSizes, int i) {
		return w -> {
			
			List<Integer> clone = cloneList(w);
			clone.set(i, floorDiv(w.get(i), poolSizes.get(i)));
			return clone;
		};
	}

	
	private Map<List<Integer>, Set<PoolTuple>> createInitialPooling(MDA operand) {
		Map<List<Integer>, Set<PoolTuple>> pools = new HashMap<>();
		int[] position = new int[operand.getDimensions().size()];
		
		pools = build(pools, operand, position, operand.getDimensions().size()-1);
		
		return pools;
	}

	
	private Map<List<Integer>, Set<PoolTuple>> build(Map<List<Integer>, Set<PoolTuple>> pools, MDA operand,
			int[] position, int place) {
		for(int i = 0; i < operand.getDimensions().get(place); i++) {
			position[place] = i;
			if(place != 0) {
				pools = build(pools, operand, position, place-1);
			} else {
				Set<PoolTuple> pool = new HashSet<>();
				PoolTuple element = new PoolTuple();
				element.element = get(operand, position);
				element.origin = position.clone();
				pool.add(element);
				pools.put(arrayAsList(position), pool);
			}
		}
		return pools;
	}

	
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
		
		List<Integer> results = new ArrayList<>();
		results = HelperLibrary.cloneList(inputDimensions);
		
		for(int i = 0; i < feature.getActiveDimensions().length; i++) {
			
			results.set(feature.getActiveDimensions()[i], Math.floorDiv(results.get(feature.getActiveDimensions()[i]), feature.getFeatureMap().getDimensions().get(i)));
		}
		
		return results;
	}
	
	
	/**
	 * A class to associate a value with its origin in the un-pooled MDA.
	 * @author Brannan
	 *
	 */
	protected class PoolTuple {
		double element;
		int[] origin;
		
		@Override
		public String toString() {
			return arrayAsList(origin).toString() + element; 
		}
	}
	

}
