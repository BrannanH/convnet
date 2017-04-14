package layers;

import fundamentals.MDA;

/**
 * This is the output type for each Layers reverse operation
 * @author Brannan
 *
 */
public class ReverseOutputTuple {
	
	// The derivative of the Cost Function with respect to the input
	private MDA dCostByDIn;
	
	// The derivative of the Cost Function with respec to the feature
	private MDA dCostByDFeature;
	
	
	/**
	 * @return the dCostByDIn
	 */
	public MDA getdCostByDIn() {
		return dCostByDIn;
	}
	
	
	/**
	 * @param dLossByDIn the dLossByDIn to set
	 */
	public void setdCostByDIn(MDA dCostByDIn) {
		this.dCostByDIn = dCostByDIn;
	}
	
	
	/**
	 * @return the dLossByDFeature
	 */
	public MDA getdCostByDFeature() {
		return dCostByDFeature;
	}
	
	/**
	 * @param dCostByDFeature the dCostByDFeature to set
	 */
	public void setdCostByDFeature(MDA dCostByDFeature) {
		this.dCostByDFeature = dCostByDFeature;
	}

}
