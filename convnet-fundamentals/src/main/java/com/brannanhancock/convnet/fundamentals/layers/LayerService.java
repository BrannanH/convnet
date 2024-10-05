package com.brannanhancock.convnet.fundamentals.layers;

import com.brannanhancock.convnet.fundamentals.mda.MDA;

public interface LayerService<T extends Layer> {

    /**
     * Computes the forward pass of the network in training phase, thus also computing all necessary
     * derivatives to update all relevant values in the network on the reverse pass.
     * @param layer
     * @param operand
     * @return
     */
    ForwardOutputTuple forward(T layer, MDA operand);

    /**
     * Used when the Convolutional Neural Network is in real operation, rather than training.
     * @param layer
     * @param operand
     * @return
     */
    MDA forwardNoTrain(T layer, MDA operand);


    /**
     * Used on the reverse pass when training the network. Uses the backpropagated derivatives to update the layer as
     * necessary and continue back propogating the derivatives for the layer(s) which fed this one.
     * @param resultFromForwardPass
     * @param dLossByDOut
     * @return
     */
    ReversePassOutput reverse(ForwardOutputTuple resultFromForwardPass, MDA dLossByDOut);

    int[] outputDimensionsFor(T layer);
}
