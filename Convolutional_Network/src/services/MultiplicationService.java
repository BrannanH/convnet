package services;

import static fundamentals.MDAHelper.get;

import fundamentals.MDA;

/**
 * This class extends the ElementWiseOperationService, and allows it to be used
 * for element wise multiplication of two MDAs, and multiplication of every
 * element in an MDA by a constant factor.
 * 
 * @author Brannan
 *
 */
public class MultiplicationService extends OperationService {

    /**
     * 
     */
    @Override
    protected double calculate(int[] position, MDA operand1, MDA operand2) {
        return get(operand1, position) * get(operand2, position);
    }


    /**
     * 
     */
    @Override
    protected double calculate(int[] position, MDA operand1, double operand2) {
        return get(operand1, position) * operand2;
    }

}
