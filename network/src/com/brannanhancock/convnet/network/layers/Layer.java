package com.brannanhancock.convnet.network.layers;

import java.util.List;
import java.util.Map;

import com.brannanhancock.convnet.fundamentals.MDA;

/**
 * This interface outlines the public methods which are necessary for a Layer in
 * a Convolutional Neural Network.
 * 
 * @author Brannan
 *
 */
public interface Layer {

    /**
     * This method is used in the forward pass through the Neural Network, where
     * the derivative of the output with the feature map is calculated, as well
     * as the operation on the input being computed. This should always call
     * forwardNoTrain before calculating derivatives.
     * 
     * @param operand
     * @param featureMap
     * @param activeDimensions
     * @return
     */
    ForwardOutputTuple forward(MDA operand, MDA feature);


    /**
     * This method is used in the forward pass through the Neural Network for
     * testing, and thus no training should happen, no derivatives are
     * calculated.
     * 
     * @param operand
     * @param featureMap
     * @param activeDimensions
     * @return
     */
    MDA forwardNoTrain(MDA operand, MDA feature);


    /**
     * This method is used in the reverse pass through the Neural Network
     * 
     * @param operand
     * @param featureMap
     * @param activeDimensions
     * @return
     */
    ReverseOutputTuple reverse(MDA dLossByDOut, Map<List<Integer>, Map<List<Integer>, Double>> dOutByDIn,
            Map<List<Integer>, Map<List<Integer>, Double>> dOutByDFeature);


    /**
     * This method is used when setting up the Neural Network, and will compute
     * the dimensions of the output of the Layer for a given set of Input
     * Dimensions and Feature.
     * 
     * @param inputDimensions
     * @param feature
     * @return
     */
    List<Integer> outputDimensions(List<Integer> inputDimensions, MDA feature);
}
