package layers;

import fundamentals.MDA;

/**
 * This class is used to associate a feature map with an array specifying the dimensions
 * of its input on which it will act.
 * @author Brannan
 *
 */
public class Feature {
	
	// This is the feature map which this Feature will use to operate on its input
	private MDA featureMap;
	
	
	// This is the array specifying which dimensions the feature map will act upon.
	private int[] activeDimensions;
	
	public Feature() {
		//no-op
	}
	
	public Feature(MDA featureMap, int... activeDimensions) {
		this.featureMap = featureMap;
		this.activeDimensions = activeDimensions;
	}
	/**
	 * @return the featureMap
	 */
	public MDA getFeatureMap() {
		return featureMap;
	}
	
	
	/**
	 * @param featureMap the featureMap to set
	 */
	public void setFeatureMap(MDA featureMap) {
		this.featureMap = featureMap;
	}
	
	
	/**
	 * @return the activeDimensions
	 */
	public int[] getActiveDimensions() {
		return activeDimensions;
	}
	
	
	/**
	 * @param activeDimensions the activeDimensions to set
	 */
	public void setActiveDimensions(int[] activeDimensions) {
		this.activeDimensions = activeDimensions;
	}

}
