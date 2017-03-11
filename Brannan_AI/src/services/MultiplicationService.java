package services;

import fundamentals.MultiD;

/**
 * This class extends the ElementWiseOperationService, and allows it to be used for element wise multiplication
 * of two MDAs, and multiplication of every element in an MDA by a constant factor.
 * @author Brannan
 *
 */
public class MultiplicationService extends OperationService {

	@Override
	protected Double calculate(int[] position, MultiD operand1, MultiD operand2) {
		return operand1.get(position)*operand2.get(position);
	}

	@Override
	protected Double calculate(int[] position, MultiD operand1, Double operand2) {
		return operand1.get(position)*operand2;
	}

}
