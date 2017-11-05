package com.brannanhancock.convnet.network.services;

import java.util.function.DoubleBinaryOperator;
import java.util.function.DoubleSupplier;
import java.util.function.ToDoubleFunction;

import com.brannanhancock.convnet.fundamentals.MDA;
import com.brannanhancock.convnet.fundamentals.MDABuilder;

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

        final MDABuilder resultBuilder = new MDABuilder(operand1.getDimensions());

        final ToDoubleFunction<int[]> double1 = operand1::get;

        final ToDoubleFunction<int[]> double2 = operand2::get;

        final ToDoubleFunction<int[]> finalOperation = (final int[] position) -> operation.applyAsDouble(double1.applyAsDouble(position), double2.applyAsDouble(position));

        recursiveOperate(resultBuilder, new int[operand1.getDimensions().length], finalOperation, 0, operand1.getDimensions());

        return resultBuilder.build();
    }


    public MDA operate(final MDA operand1, final double operand2, final DoubleBinaryOperator operation) {
        final MDABuilder resultBuilder = new MDABuilder(operand1.getDimensions());

        final ToDoubleFunction<int[]> double1 = operand1::get;

        final DoubleSupplier double2 = () -> operand2;

        final ToDoubleFunction<int[]> finalFunction = (final int[] position) -> operation.applyAsDouble(double1.applyAsDouble(position), double2.getAsDouble());
        recursiveOperate(resultBuilder, new int[operand1.getDimensions().length], finalFunction, 0, operand1.getDimensions());

        return resultBuilder.build();
    }


    /**
     * This recursive function ensures that each element in the result is
     * calculated using the same location from the operand MDAs, and the
     * constant factor.
     *
     * @param resultBuilder
     * @param operand1
     * @param operand2
     * @param position
     * @param index
     * @return
     */
    private MDABuilder recursiveOperate(final MDABuilder resultBuilder, final int[] position, final ToDoubleFunction<int[]> operation, final int index, final int[] dimensions) {
        for (int i = 0; i < dimensions[index]; i++) {
            position[index] = i;
            if (index == dimensions.length - 1) {
                resultBuilder.withDataPoint(operation.applyAsDouble(position), position);
                continue;
            }
            recursiveOperate(resultBuilder, position, operation, index + 1, dimensions);
        }
        return resultBuilder;
    }
}
