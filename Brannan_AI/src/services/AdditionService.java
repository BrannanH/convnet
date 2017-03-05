package services;

import fundamentals.MultiD;

/**
 * This class extends the ElementWiseOperationService, and allows it to be used for element wise addition
 * of two Multi Dimensional Arrays, and addition of a constant factor to every element in a Multi Dimensional
 * Array.
 * @author Brannan
 *
 */
public class AdditionService extends OperationService{

	@Override
	protected Double calculate(int[] position, MultiD<Double> operand1, MultiD<Double> operand2) {
		return operand1.get(position)+operand2.get(position);
	}

	@Override
	protected Double calculate(int[] position, MultiD<Double> operand1, Double operand2) {
		return operand1.get(position)+operand2;
	}

}
