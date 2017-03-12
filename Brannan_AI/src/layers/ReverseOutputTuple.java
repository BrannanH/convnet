package layers;

import fundamentals.MultiD;

/**
 * This is the output type for each Layers reverse operation
 * @author Brannan
 *
 */
public class ReverseOutputTuple {
	
	// The derivative of the Cost Function with respect to the input
	private MultiD dCostByDIn;
	
	// The derivative of the Cost Function with respec to the feature
	private MultiD dCostByDFeature;
	
	
	/**
	 * @return the dCostByDIn
	 */
	public MultiD getdCostByDIn() {
		return dCostByDIn;
	}
	
	
	/**
	 * @param dLossByDIn the dLossByDIn to set
	 */
	public void setdCostByDIn(MultiD dCostByDIn) {
		this.dCostByDIn = dCostByDIn;
	}
	
	
	/**
	 * @return the dLossByDFeature
	 */
	public MultiD getdCostByDFeature() {
		return dCostByDFeature;
	}
	
	/**
	 * @param dCostByDFeature the dCostByDFeature to set
	 */
	public void setdCostByDFeature(MultiD dCostByDFeature) {
		this.dCostByDFeature = dCostByDFeature;
	}

}
