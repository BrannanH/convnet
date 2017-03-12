package layers;

import fundamentals.MultiD;

/**
 * This class is used to associate a feature map with an array specifying the dimensions
 * of its input on which it will act.
 * @author Brannan
 *
 */
public class Feature {
	
	// This is the feature map which this Feature will use to operate on its input
	private MultiD featureMap;
	
	
	// This is the array specifying which dimensions the feature map will act upon.
	private int[] activeDimensions;
	
	
	/**
	 * @return the featureMap
	 */
	public MultiD getFeatureMap() {
		return featureMap;
	}
	
	
	/**
	 * @param featureMap the featureMap to set
	 */
	public void setFeatureMap(MultiD featureMap) {
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
