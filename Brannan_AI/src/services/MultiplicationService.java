package services;

import fundamentals.MultiD;

/**
 * This class extends the ElementWiseOperationService, and allows it to be used for element wise multiplication
 * of two Multi Dimensional Arrays, and multiplication of every element in a Multi Dimensional Array by a
 * constant factor.
 * @author Brannan
 *
 */
public class MultiplicationService extends OperationService {

	@Override
	protected Double calculate(int[] position, MultiD<Double> operand1, MultiD<Double> operand2) {
		return operand1.get(position)*operand2.get(position);
	}

	@Override
	protected Double calculate(int[] position, MultiD<Double> operand1, Double operand2) {
		return operand1.get(position)*operand2;
	}

}
