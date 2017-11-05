package com.brannanhancock.convnet.network.layers;

import com.brannanhancock.convnet.fundamentals.MDA;

/**
 * This is the output type for each Layers reverse operation
 * 
 * @author Brannan R. Hancock
 *
 */
public class ReverseOutputTuple {

    // The derivative of the Cost Function with respect to the input
    private final MDA dLossByDIn;

    // The derivative of the Cost Function with respect to the feature
//    private final Optional<MDA> dLossByDFeature;

    
    /**
     * 
     * @param dLossByDIn
     * @param dLossByDFeature
     */
    public ReverseOutputTuple(final MDA dLossByDIn) {
//        public ReverseOutputTuple(final MDA dLossByDIn, final Optional<MDA> dLossByDFeature) {
//        this.dLossByDFeature = dLossByDFeature;
        this.dLossByDIn = dLossByDIn;
    }

    /**
     * @return the dLossByDIn
     */
    public MDA getdLossByDIn() {
        return dLossByDIn;
    }


//    /**
//     * @return the dLossByDFeature
//     */
//    public Optional<MDA> getDLossByDFeature() {
//        return dLossByDFeature;
//    }
}
