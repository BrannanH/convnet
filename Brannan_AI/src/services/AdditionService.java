package services;

import fundamentals.MultiD;

/**
 * This class extends the ElementWiseOperationService, and allows it to be used for element wise addition
 * of two MDAs, and addition of a constant factor to every element in an MDA.
 * @author Brannan
 *
 */
public class AdditionService extends OperationService{

	@Override
	protected Double calculate(int[] position, MultiD operand1, MultiD operand2) {
		return operand1.get(position)+operand2.get(position);
	}

	@Override
	protected Double calculate(int[] position, MultiD operand1, Double operand2) {
		return operand1.get(position)+operand2;
	}

}
