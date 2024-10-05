package com.brannanhancock.convnet.fundamentals.layers;


import com.brannanhancock.convnet.fundamentals.mda.MDA;

/**
 * This interface outlines the public methods which are necessary for a Layer in
 * a Convolutional Neural Network.
 *
 * @author Brannan
 *
 */
public interface Layer {


    public int[] getInputDimensions();
}
