package com.brannanhancock.convnet.fundamentals.layers;


import com.brannanhancock.convnet.fundamentals.mda.MDA;

/**
 * This interface outlines the public methods which are necessary for a Layer in
 * a Convolutional Neural Network.
 *
 * @author Brannan
 *
 */
public abstract class Layer {

    /**
     * This method is used in the forward pass through the Neural Network, where
     * the derivative of the output with the feature map is calculated, as well
     * as the operation on the input being computed.
     *
     * @param operand - the MDA at the input to this layer.
     * @return - the MDA which is the result of this layer.
     */
    public abstract MDA forward(MDA operand);


    /**
     * This method is used in the forward pass through the Neural Network for
     * testing, and thus no training should happen, no derivatives are
     * calculated.
     *
     * @param operand - the MDA at the input to this layer.
     * @return - the MDA which is the result of this layer.
     */
    public abstract MDA forwardNoTrain(MDA operand);


    /**
     * This method is used in the reverse pass through the Neural Network
     *
     * @param dLossByDOut
     *            - the calculated contribution towards the loss function for
     *            each element output from this layer on the forward pass.
     * @return
     */
    public abstract MDA reverse(MDA dLossByDOut);


    /**
     * Returns the dimensions of the output array. Used to initialise various
     * layer at the correct size.
     *
     * @return - an array representing the size of each dimension in the MDA
     *         output by a forward pass through this layer.
     */
    public abstract int[] outputDimensions();
}
