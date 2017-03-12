package layers;

import fundamentals.MultiD;

/**
 * This is the output of each of the layers which deal with an MDA.
 * @author Brannan
 *
 */
public class ForwardOutputTuple {
	
	// The output of the forward pass
	private MultiD output;
	
	// The derivative of the output with respect to the input
	private MultiD dOutByDIn;
	
	// The derivative of the output with respect to the feature
	private MultiD dOutByDFeature;
	
	
	/**
	 * @return the output
	 */
	public MultiD getOutput() {
		return output;
	}
	
	
	/**
	 * @param output the output to set
	 */
	public void setOutput(MultiD output) {
		this.output = output;
	}
	
	
	/**
	 * @return the dOutByDIn
	 */
	public MultiD getdOutByDIn() {
		return dOutByDIn;
	}
	
	
	/**
	 * @param dOutByDIn the dOutByDIn to set
	 */
	public void setdOutByDIn(MultiD dOutByDIn) {
		this.dOutByDIn = dOutByDIn;
	}
	
	
	/**
	 * @return the dOutByDFeature
	 */
	public MultiD getdOutByDFeature() {
		return dOutByDFeature;
	}
	
	
	/**
	 * @param dOutByDFeature the dOutByDFeature to set
	 */
	public void setdOutByDFeature(MultiD dOutByDFeature) {
		this.dOutByDFeature = dOutByDFeature;
	}

}
