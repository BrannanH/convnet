package com.brannan.convnet.network.services;

import static com.brannan.convnet.network.fundamentals.MDAHelper.put;

import java.util.function.DoubleBinaryOperator;
import java.util.function.DoubleSupplier;
import java.util.function.ToDoubleFunction;

import com.brannan.convnet.network.fundamentals.MDA;
import com.brannan.convnet.network.fundamentals.MDABuilder;
import com.brannan.convnet.network.fundamentals.MDAHelper;

/**
 * This class provides the methods necessary for element wise operations. It
 * implements a recursive call to allow every element in a MDA to be affected,
 * and the operate method is abstract such that this framework can have many
 * different applications. There are two public entry points for this class, one
 * allows you to specify two MDAs which will result in an elementwise operation
 * between the two. The other allows the specification of a single MDA, and a
 * constant factor, which can be applied to each element of the MDA with
 * whichever operation is defined.
 * 
 * @author Brannan
 *
 */
public class OperationService {

    /**
     * This public entry point validates the two MDAs which are passed to it,
     * and sets up the calls to the recursive function which will carry out the
     * element wise operations.
     * 
     * @param operand1
     * @param operand2
     * @return
     */
    public MDA operate(final MDA operand1, final MDA operand2, final DoubleBinaryOperator operation) {
        
        for (int i = 0; i < operand1.getDimensions().length; i++) {
            if (operand1.getDimensions()[i] != operand2.getDimensions()[i]) {
                throw new IllegalArgumentException("Dimensional Mis-match between arrays");
            }
        } 
        
        final MDA result = new MDABuilder().withDimensions(operand1.getDimensions()).build();
        
        final ToDoubleFunction<int[]> double1 = (final int[] position) -> MDAHelper.get(operand1, position);
        
        final ToDoubleFunction<int[]> double2 = (final int[] position) -> MDAHelper.get(operand2, position);
        
        final ToDoubleFunction<int[]> finalOperation = (final int[] position) -> operation.applyAsDouble(double1.applyAsDouble(position), double2.applyAsDouble(position));

        recursiveOperate(result, new int[operand1.getDimensions().length], finalOperation, 0);

        return result;
    }


    public MDA operate(final MDA operand1, final double operand2, final DoubleBinaryOperator operation) {
        final MDA result = new MDABuilder().withDimensions(operand1.getDimensions()).build();
        
        final ToDoubleFunction<int[]> double1 = (final int[] position) -> MDAHelper.get(operand1, position);
        
        final DoubleSupplier double2 = () -> operand2;

        final ToDoubleFunction<int[]> finalFunction = (final int[] position) -> operation.applyAsDouble(double1.applyAsDouble(position), double2.getAsDouble());
        recursiveOperate(result, new int[operand1.getDimensions().length], finalFunction, 0);

        return result;
    }


    /**
     * This recursive function ensures that each element in the result is
     * calculated using the same location from the operand MDAs, and the
     * constant factor.
     * 
     * @param result
     * @param operand1
     * @param operand2
     * @param position
     * @param index
     * @return
     */
    private MDA recursiveOperate(final MDA result, final int[] position, final ToDoubleFunction<int[]> operation, final int index) {
        MDA intermediateResult = result;
        for (int i = 0; i < result.getDimensions()[index]; i++) {
            position[index] = i;
            if (index == result.getDimensions().length - 1) {
                put(intermediateResult, operation.applyAsDouble(position), position);
                continue;
            }
            intermediateResult = recursiveOperate(intermediateResult, position, operation, index + 1);
        }
        return intermediateResult;
    }
}
