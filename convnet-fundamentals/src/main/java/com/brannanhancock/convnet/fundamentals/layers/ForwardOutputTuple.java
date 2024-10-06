package com.brannanhancock.convnet.fundamentals.layers;

import com.brannanhancock.convnet.fundamentals.mda.MDA;

import java.util.List;
import java.util.Map;

/**
 * This is the output of each of the layers which deal with an MDA.
 * 
 * @author Brannan R. Hancock
 *
 */
public class ForwardOutputTuple {

    private final Layer layer;

    // The output of the forward pass
    private final MDA output;

    /**
     * The derivative of the output with respect to the input
     * Location a,b,c,d holding value e represents
     * represents dOut_a,b/dIn_c,d = e
     */
    private final MDA dOutByDIn;

    // The derivative of the output with respect to the feature
    private final Map<List<Integer>, Map<List<Integer>, Double>> dOutByDFeature;


    public ForwardOutputTuple(final Layer layer, final MDA output, final MDA dOutByDIn,
                              final Map<List<Integer>, Map<List<Integer>, Double>> dOutByDFeature) {
        this.layer = layer;
        this.output = output;
        this.dOutByDIn = dOutByDIn;
        this.dOutByDFeature = dOutByDFeature;
    }

    public Layer getLayer() {
        return layer;
    }

    /**
     * @return the output
     */
    public MDA getOutput() {
        return output;
    }


    /**
     * @return the dOutByDIn
     */
    public MDA getdOutByDIn() {
        return dOutByDIn;
    }


    /**
     * @return the dOutByDFeature
     */
    public Map<List<Integer>, Map<List<Integer>, Double>> getdOutByDFeature() {
        return dOutByDFeature;
    }
}
