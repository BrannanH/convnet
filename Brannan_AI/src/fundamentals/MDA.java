package fundamentals;

import java.util.List;

public class MDA {

	private List<Integer> dimensions;
	private double[] elements; 
	private int[] increments;
	
	
	/**
	 * @return the dimensions
	 */
	public List<Integer> getDimensions() {
		return dimensions;
	}
	
	
	/**
	 * @param dimensions the dimensions to set
	 */
	void setDimensions(List<Integer> dimensions) {
		this.dimensions = dimensions;
	}
	
	
	/**
	 * @return the elements
	 */
	double[] getElements() {
		return elements;
	}
	
	
	/**
	 * @param elements the elements to set
	 */
	void setElements(double[] elements) {
		this.elements = elements;
	}
	
	
	/**
	 * @return the increments
	 */
	int[] getIncrements() {
		return increments;
	}
	
	
	/**
	 * @param increments the increments to set
	 */
	void setIncrements(int[] increments) {
		this.increments = increments;
	}
	
	
}
