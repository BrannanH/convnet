package services;

import fundamentals.MultiD;

/**
 * This class provides the methods necessary for element wise operations. It implements a recursive call to
 * allow every element in a MDA to be affected, and the operate method is abstract such that
 * this framework can have many different applications. There are two public entry points for this class, one
 * allows you to specify two MDAs which will result in an elementwise operation between the two.
 * The other allows the specification of a single MDA, and a constant factor, which can be applied
 * to each element of the MDA with whichever operation is defined.
 * @author Brannan
 *
 */
abstract class OperationService {
	
	/**
	 * This public entry point validates the two MDAs which are passed to it, and 
	 * sets up the calls to the recursive function which will carry out the element wise operations.
	 * @param operand1
	 * @param operand2
	 * @return
	 */
	public MultiD operate(MultiD operand1, MultiD operand2){
		if(operand1.getDimensions().length != operand2.getDimensions().length){
			throw(new IllegalArgumentException("Dimensional Mis-match between summed arrays, they have different lengths"));
		}
		for(int i = 0; i < operand1.getDimensions().length; i++){
			if(operand1.getDimensions()[i] != operand2.getDimensions()[i]){
				throw(new IllegalArgumentException("Dimensional Mis-match between summed arrays at dimension " + i));
			}
		}
		MultiD result = new MultiD(operand1.getDimensions());
		
		recursiveOperate(result, operand1, operand2, new int[operand1.getDimensions().length], 0); 
		
	return result;
	}
	
	public MultiD operate(MultiD operand1, Double operand2){
		MultiD result = new MultiD(operand1.getDimensions());
		
		recursiveOperate(result, operand1, operand2, new int[operand1.getDimensions().length], 0); 
		
	return result;
	}
	

	/**
	 * This recursive function ensures that each element in the result is calculated using the same location
	 * from the two operand MDAs.
	 * @param result
	 * @param operand1
	 * @param operand2
	 * @param position
	 * @param index
	 * @return
	 */
	private MultiD recursiveOperate(MultiD result, MultiD operand1, MultiD operand2, int[] position, int index) {
		for(int i = 0; i < result.getDimensions()[index]; i++){
			position[index] = i;
			Double element = calculate(position, operand1, operand2);
			result.put(element, position);
			if(index == result.getDimensions().length-1){
				continue;
			}
			result = recursiveOperate(result, operand1, operand2, position, index+1);
		}
		return result;
	}
	
	
	/**
	 * This recursive function ensures that each element in the result is calculated using the same location
	 * from the operand MDAs, and the constant factor.
	 * @param result
	 * @param operand1
	 * @param operand2
	 * @param position
	 * @param index
	 * @return
	 */
	private MultiD recursiveOperate(MultiD result, MultiD operand1, double operand2, int[] position, int index) {
		for(int i = 0; i < result.getDimensions()[index]; i++){
			position[index] = i;
			Double element = calculate(position, operand1, operand2);
			result.put(element, position);
			if(index == result.getDimensions().length-1){
				continue;
			}
			result = recursiveOperate(result, operand1, operand2, position, index+1);
		}
		return result;
	}
	
	/**
	 * This abstract method allows for numerous implementations of element wise operation.
	 * @param operand1
	 * @param operand2
	 * @return
	 */
	protected abstract Double calculate(int[] position, MultiD operand1, MultiD operand2);
	
	/**
	 * This abstract method allows for numerous implementations of the constant operation.
	 * @param operand1
	 * @param operand2
	 * @return
	 */
	protected abstract Double calculate(int[] position, MultiD operand1, Double operand2);
}
