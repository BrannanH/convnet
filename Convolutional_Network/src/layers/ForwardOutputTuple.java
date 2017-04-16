package layers;

import java.util.List;
import java.util.Map;

import fundamentals.MDA;

/**
 * This is the output of each of the layers which deal with an MDA.
 * 
 * @author Brannan
 *
 */
public class ForwardOutputTuple {

    // The output of the forward pass
    private MDA output;

    // The derivative of the output with respect to the input
    private Map<List<Integer>, Map<List<Integer>, Double>> dOutByDIn;

    // The derivative of the output with respect to the feature
    private MDA dOutByDFeature;


    public ForwardOutputTuple(MDA output, Map<List<Integer>, Map<List<Integer>, Double>> dOutByDIn,
            MDA dOoutByDFeature) {
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
     * @param output
     *            the output to set
     */
    public void setOutput(MDA output) {
        this.output = output;
    }


    /**
     * @return the dOutByDIn
     */
    public Map<List<Integer>, Map<List<Integer>, Double>> getdOutByDIn() {
        return dOutByDIn;
    }


    /**
     * @param dOutByDIn
     *            the dOutByDIn to set
     */
    public void setdOutByDIn(Map<List<Integer>, Map<List<Integer>, Double>> dOutByDIn) {
        this.dOutByDIn = dOutByDIn;
    }


    /**
     * @return the dOutByDFeature
     */
    public MDA getdOutByDFeature() {
        return dOutByDFeature;
    }


    /**
     * @param dOutByDFeature
     *            the dOutByDFeature to set
     */
    public void setdOutByDFeature(MDA dOutByDFeature) {
        this.dOutByDFeature = dOutByDFeature;
    }

}
