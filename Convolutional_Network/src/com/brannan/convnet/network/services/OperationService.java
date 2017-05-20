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
        
        if (!operand1.getDimensions().equals(operand2.getDimensions())) {
            throw new IllegalArgumentException("Dimensional Mis-match between arrays");
        }
        
        MDA result = new MDABuilder().withDimensions(operand1.getDimensions()).build();
        
        ToDoubleFunction<int[]> double1 = (int[] position) -> MDAHelper.get(operand1, position);
        
        ToDoubleFunction<int[]> double2 = (int[] position) -> MDAHelper.get(operand2, position);
        
        ToDoubleFunction<int[]> finalOperation = (int[] position) -> operation.applyAsDouble(double1.applyAsDouble(position), double2.applyAsDouble(position));

        recursiveOperate(result, new int[operand1.getDimensions().size()], finalOperation, 0);

        return result;
    }


    public MDA operate(final MDA operand1, final double operand2, final DoubleBinaryOperator operation) {
        MDA result = new MDABuilder().withDimensions(operand1.getDimensions()).build();
        
        ToDoubleFunction<int[]> double1 = (int[] position) -> MDAHelper.get(operand1, position);
        
        DoubleSupplier double2 = () -> operand2;

        ToDoubleFunction<int[]> finalFunction = (int[] position) -> operation.applyAsDouble(double1.applyAsDouble(position), double2.getAsDouble());
        recursiveOperate(result, new int[operand1.getDimensions().size()], finalFunction, 0);

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
        for (int i = 0; i < result.getDimensions().get(index); i++) {
            position[index] = i;
            if (index == result.getDimensions().size() - 1) {
                put(intermediateResult, operation.applyAsDouble(position), position);
                continue;
            }
            intermediateResult = recursiveOperate(intermediateResult, position, operation, index + 1);
        }
        return intermediateResult;
    }
}
