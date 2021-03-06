package com.brannanhancock.convnet.network.layers;

import java.util.List;
import java.util.Map;

import com.brannanhancock.convnet.fundamentals.MDA;

/**
 * This is the output of each of the layers which deal with an MDA.
 * 
 * @author Brannan R. Hancock
 *
 */
public class ForwardOutputTuple {

    // The output of the forward pass
    private final MDA output;

    /**
     * The derivative of the output with respect to the input
     * <a,b> { [<c,d> e], [<f,g> h] }
     * represents dOut_a,b/dIn_c,d = e, dOut_a,b/dIn_f,g = h
     */
    private final Map<List<Integer>, Map<List<Integer>, Double>> dOutByDIn;

    // The derivative of the output with respect to the feature
    private final Map<List<Integer>, Map<List<Integer>, Double>> dOutByDFeature;


    public ForwardOutputTuple(final MDA output, final Map<List<Integer>, Map<List<Integer>, Double>> dOutByDIn,
            final Map<List<Integer>, Map<List<Integer>, Double>> dOoutByDFeature) {
        this.output = output;
        this.dOutByDIn = dOutByDIn;
        this.dOutByDFeature = dOoutByDFeature;
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
    public Map<List<Integer>, Map<List<Integer>, Double>> getdOutByDIn() {
        return dOutByDIn;
    }


    /**
     * @return the dOutByDFeature
     */
    public Map<List<Integer>, Map<List<Integer>, Double>> getdOutByDFeature() {
        return dOutByDFeature;
    }
}
