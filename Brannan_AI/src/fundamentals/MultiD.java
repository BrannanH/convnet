package fundamentals;

/**
 * This class is the fundamental class used to hold data in a Multi Dimensional Array (MDA). It is implemented as an array of doubles
 * accessed by specifying a position as an integer array.
 * @author Brannan
 *
 * @param <T>
 */
public class MultiD {
	
	private int[] dimensions;
	private double[] elements; 
	
	/**
	 * This holds the number of positions in the array you need to move along, by an increment in each dimension.
	 */
	private int[] increments;

	public MultiD(int... dimensions){
		this.dimensions = dimensions;
		
		this.increments = new int[dimensions.length];
		increments[0] = 1;
		for(int i = 1; i < increments.length; i++) {
			increments[i] = increments[i-1]*dimensions[i-1];
		}
		this.elements = new double[dimensions[dimensions.length-1]*increments[dimensions.length-1]];
	}
	
	/**
	 * Used to populate the array
	 * @param element
	 * @param position
	 */
	public void put(double element, int... position){
		validatePosition(position);
		this.elements[getLocationForPosition(position)] = element;
	}
	
	/**
	 * getter for dimensions field
	 * @return
	 */
	public int[] getDimensions(){
		return this.dimensions;
	}

	
	/**
	 * Returns the element at the specified position in the Multi Dimensional Array
	 * @param position
	 * @return
	 */
	public double get(int... position) {
		validatePosition(position);
		return elements[(getLocationForPosition(position))];
	}
	
	
	/**
	 * This method casts the position to the location in the array
	 * @param position
	 * @return
	 */
	private int getLocationForPosition(int[] position){
		int location = 0;
		for(int i = 0; i < position.length; i++) {
			location += increments[i]*position[i];
		}
		return location;
	}

	/**
	 * This method puts barriers on the kind of key which can be put and retrieved from the Multi Dimensional Array.
	 * They must be of the correct dimensionality, and each dimension of the specified position must be
	 * in array bounds.
	 * @param position
	 */
	private void validatePosition(int[] position){
		if(position.length == dimensions.length){
			for(int i = 0; i < position.length; i++){
				if(position[i] >= dimensions[i] || position[i] < 0){
					throw(new IndexOutOfBoundsException("Index " + i + " is out of bounds, " + position[i] + " was specified, for a dimensions size " + dimensions[i]));
				}
			}
		} else {
			throw(new IllegalArgumentException("Dimensional Mismatch between specified insertion and Multi Dimensional Array"));
		}
	}

}
