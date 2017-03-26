package layers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.inject.Inject;

import fundamentals.MultiD;
import services.DimensionVerificationService;

/**
 * This class implements the majority of the logic for pooling classes. They need to fill in
 * the abstract methods for selecting a value based on a pool.
 * @author Brannan
 *
 */
public abstract class SeparablePoolingLayer implements Layer {
	
	private DimensionVerificationService dimensionsService;

	@Inject
	public SeparablePoolingLayer(DimensionVerificationService dimensionsService) {
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
	abstract protected List<PoolTuple> selectFromPool(List<PoolTuple> poolList);
	
	
	/**
	 * @see{Layer#forward}
	 */
	@Override
	public ForwardOutputTuple forward(MultiD operand, Feature feature) {
		MultiD dOutByDIn = new MultiD(operand.getDimensions());
		Map<List<Integer>, List<PoolTuple>> origins = new HashMap<>();
		
		int[] dimension = new int[1];
		dimension[0] = feature.getActiveDimensions()[0];
		
		MultiD intermediateResult = operand;
		
		RecursiveGroup recursiveArguments = new RecursiveGroup();
		recursiveArguments.origins = origins;
		
		for(int poolingDimension = 0; poolingDimension < feature.getActiveDimensions().length; poolingDimension++) {
			
			recursiveArguments = resetArguments(feature, poolingDimension, intermediateResult, recursiveArguments);
			
			intermediateResult = recursivePool(recursiveArguments, operand.getDimensions().length-1);
		}
		
		for(List<PoolTuple> list : origins.values()){
			for(PoolTuple element: list){
				dOutByDIn.put(element.element, element.origin);
			}
		}
		
		ForwardOutputTuple output = new ForwardOutputTuple();
		output.setOutput(intermediateResult);
		
		output.setdOutByDIn(dOutByDIn);
		
		return output;
	}

	
	/**
	 * This method works by breaking down the necessary pooling into the independent dimensions.
	 * Each of these simpler pooling operation uses a recursive method to select every necessary pool
	 * in the MDA, and this in turn calls the abstract method selectFromPool, such that different methods
	 * of pooling can be implemented by different child classes.
	 * It sets up the intermediary MDAs which will be passed between successive poolings, then calls the
	 * recursive function several times.
	 * 
	 */
	@Override
	public MultiD forwardNoTrain(MultiD operand, Feature feature) {
		
		int[] dimension = new int[1];
		dimension[0] = feature.getActiveDimensions()[0];
		
		MultiD intermediateResult = operand;
		
		RecursiveGroup recursiveArguments = new RecursiveGroup();
		
		for(int poolingDimension = 0; poolingDimension < feature.getActiveDimensions().length; poolingDimension++) {
			
			recursiveArguments = resetArguments(feature, poolingDimension, intermediateResult, recursiveArguments);
			
			intermediateResult = recursivePool(recursiveArguments, operand.getDimensions().length-1);
		}
		
		return intermediateResult;
	}
	
	
	private RecursiveGroup resetArguments(Feature feature, int poolingDimension, MultiD intermediateResult, RecursiveGroup recursiveArguments) {
		int[] dimension = new int[1];
		dimension[0] = feature.getActiveDimensions()[poolingDimension];
		
		MultiD intermediateFilter = new MultiD(feature.getFeatureMap().getDimensions()[poolingDimension]);
		Feature intermediateFeature = new Feature();
		intermediateFeature.setActiveDimensions( dimension );
		intermediateFeature.setFeatureMap(intermediateFilter);
		
		recursiveArguments.operand = intermediateResult;
		recursiveArguments.intermediary = new MultiD(outputDimensions(intermediateResult.getDimensions(), intermediateFeature));
		recursiveArguments.poolingDimension = feature.getActiveDimensions()[poolingDimension];
		recursiveArguments.poolSize = feature.getFeatureMap().getDimensions()[poolingDimension];
		recursiveArguments.set = new boolean[intermediateResult.getDimensions().length];
		recursiveArguments.position = new int[intermediateResult.getDimensions().length];
		
		return recursiveArguments;
	}

	
	/**
	 * @see{Layer#reverse}
	 */
	public ReverseOutputTuple reverse(MultiD dLossByDOut, MultiD dOutByDIn, MultiD dOutByDFeature){
		return null;
	}

	
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

	
	/**
	 * This method calls itself recursively, and it allows for the separation of each pooling dimension.
	 * This method must behave in three different ways, depending on the place variable. If place is
	 * not equal to the poolingDimension, it iterates through every legal value of position[place] and 
	 * calls itself with place decremented, to allow for finding all possible pools.
	 * If place is equal to the poolingDimension but not all of the elements in position have been set,
	 * then this simply calls itself with place decremented so the un-set positions can be set.
	 * If place = 0, we have reached the end of the position array, with every value set on the way
	 * except for that of the poolingDimension. So it then calls itself again, with place = poolingDimension. 
	 * Now that every position has been set, the method builds up all the possible pools it can with the
	 * one degree of freedom it has (the position in the poolingDimension), and calls the selectFromPool
	 * method on each. The selected value is then placed in the return MDA in the element specified by 
	 * the position array, and pool number.
	 * @param operand
	 * @param intermediary
	 * @param poolingDimension
	 * @param poolSize
	 * @param position
	 * @param set
	 * @param place
	 * @return
	 */
	private MultiD recursivePool(RecursiveGroup recursiveArguments, int place) {
		// Need to iterate through all positions
		if(place != recursiveArguments.poolingDimension) {
			
			nonPoolingDimension(recursiveArguments, place);
			
			// all possibilities of this position have been explored, so now this will return, free up this position to be set again.
			recursiveArguments.set[place] = false;
		}
		
		// may need to do the pooling if everything has been set
		if(place == recursiveArguments.poolingDimension) {
			
			// ready iff every other position has been set. Computes the pooling here
			if(isReady(place, recursiveArguments.set)) {
				
				int count = 0;
				
				for(int poolNumber = 0; poolNumber < recursiveArguments.intermediary.getDimensions()[recursiveArguments.poolingDimension]; poolNumber++) {
					
					int startOfPool = count;
					
					List<PoolTuple> poolList = buildPool(recursiveArguments, count);
					count+= recursiveArguments.poolSize;
										
					List<PoolTuple> result = selectFromPool(poolList);
					PoolTuple element = result.get(0);
						
					List<Integer> positionInIntermediary = arrayAsList(recursiveArguments.position);
					positionInIntermediary.set(recursiveArguments.poolingDimension, poolNumber);
					
					int[] positionInIntermediaryAsArray = listAsArray(positionInIntermediary);
					recursiveArguments.intermediary.put(element.element, positionInIntermediaryAsArray);
					
					if(recursiveArguments.origins != null){
							
						recursiveArguments.origins = updateDerivatives(recursiveArguments, result, poolNumber, startOfPool, positionInIntermediary);
					}
					
				}
				
				return recursiveArguments.intermediary;
			} else {
				
				place--;
				recursivePool(recursiveArguments, place);
			}
		}
		return recursiveArguments.intermediary;
	}
	
	
	/**
	 * A helper function to cast a list of Integers to an array of ints.
	 * @param list
	 * @return
	 */
	private int[] listAsArray(List<Integer> list) {
		int[] result = new int[list.size()];
		for(int j = 0; j < list.size(); j++){
			result[j] = list.get(j);
		}
		return result;
	}
	
	
	/**
	 * A helper function to cast an int array to a list of Integers.
	 * @param array
	 * @return
	 */
	private List<Integer> arrayAsList(int[] array) {
		List<Integer> result = new ArrayList<>();
		
		for(int j = 0; j < array.length; j++){
			
			result.add(array[j]);
		}
		return result;
	}
	
	
	/**
	 * The instructions to execute if this recursion is handling a dimension which is not the
	 * present pooling dimension.
	 * @param recursiveArguments
	 * @param place
	 */
	private void nonPoolingDimension(RecursiveGroup recursiveArguments, int place) {
		recursiveArguments.set[place] = true;
		
		for(int i = 0; i < recursiveArguments.operand.getDimensions()[place]; i++) {
			
			recursiveArguments.position[place] = i;
			
			// if place is 0, all entries in position must have been set, so hand back to do the pooling
			if(place == 0) {
				recursivePool(recursiveArguments, recursiveArguments.poolingDimension);
				
			} else {
				recursivePool(recursiveArguments, place - 1);
			}
		}
	}
	
	
	/**
	 * checks to see if each of the dimensions has been set.
	 * @param position
	 * @param place
	 * @param set
	 * @return
	 */
	private boolean isReady(int place, boolean[] set){
		
		for(int i = 0; i < set.length; i++) {
			
			if(i != place && set[i] != true) {
				
				return false;
			}
		}
		
		return true;
	}
	
	
	/**
	 * A method to retrieve the elements for a pool and return them in a list of PoolTuples.
	 * @param recursiveArguments
	 * @param count
	 * @return
	 */
	private List<PoolTuple> buildPool(RecursiveGroup recursiveArguments, int count) {
		
		List<PoolTuple> poolList = new ArrayList<>();
		for(int j = 0; j < recursiveArguments.poolSize; j++) {
			
			int[] retrieve = recursiveArguments.position.clone();
			retrieve[recursiveArguments.poolingDimension] = count;
			
			PoolTuple poolElement = new PoolTuple();
			poolElement.element = recursiveArguments.operand.get(retrieve);
			poolElement.origin = retrieve.clone();
			poolList.add(poolElement);
			
			count++;
		}
		
		return poolList;
	}
	
	
	/**
	 * This method updates the values stored in the derivatives of the final output MDA. These are updated during
	 * each recursion, and separable operation because when pooling in any dimension which isn't the first 
	 * previous derivatives will become redundant. E.g. a value brought forward during pooling in dimension 1, may
	 * not be brought forward when pooling in dimension 2, so does not contribute in the derivative of the output
	 * with respect to the input.
	 * during each recursion as some 
	 * @param recursiveArguments
	 * @param result
	 * @param poolNumber
	 * @param startOfPool
	 * @param positionInIntermediary
	 * @return
	 */
	private Map<List<Integer>, List<PoolTuple>> updateDerivatives(RecursiveGroup recursiveArguments, List<PoolTuple> result, int poolNumber, int startOfPool, List<Integer> positionInIntermediary) {
	
		List<Integer> legacyPosition = arrayAsList(recursiveArguments.position);
		result.remove(0);
		
		for(int positionInPool = 0; positionInPool < recursiveArguments.poolSize; positionInPool++) {
				
			legacyPosition.set(recursiveArguments.poolingDimension, startOfPool+positionInPool);
				
			if(recursiveArguments.origins.containsKey(legacyPosition)){
						
				recursiveArguments.origins.put(positionInIntermediary, recursiveArguments.origins.get(legacyPosition));
				recursiveArguments.origins.remove(legacyPosition);
			} else {
						
				recursiveArguments.origins.put(positionInIntermediary, result);
			}
				
		}
		return recursiveArguments.origins;
	}
	
	
	/**
	 * An internal class to pass around the arguments operated on during recursion.
	 * @author Brannan
	 */
	private class RecursiveGroup {
		MultiD operand; 
		MultiD intermediary; 
		int poolingDimension; 
		int poolSize;
		int[] position; 
		boolean[] set; 
		Map<List<Integer>, List<PoolTuple>> origins;
	}
	
	
	/**
	 * A class to associate a value with its origin in the un-pooled MDA.
	 * @author Brannan
	 *
	 */
	protected class PoolTuple {
		double element;
		int[] origin;
	}

}
