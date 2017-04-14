package services;

import static fundamentals.MDAHelper.get;

import fundamentals.MDA;

/**
 * This class extends the ElementWiseOperationService, and allows it to be used for element wise addition
 * of two MDAs, and addition of a constant factor to every element in an MDA.
 * @author Brannan
 *
 */
public class AdditionService extends OperationService{

	@Override
	protected Double calculate(int[] position, MDA operand1, MDA operand2) {
		return get(operand1, position)+ get(operand2, position);
	}

	@Override
	protected Double calculate(int[] position, MDA operand1, Double operand2) {
		return get(operand1, position)+operand2;
	}

}
