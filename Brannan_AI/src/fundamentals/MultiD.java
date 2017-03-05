package fundamentals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * This class is the fundamental class used to hold data in a multi dimensional array. It is implemented as a map
 * with a list of integers for the key, and a generic type for the value. 
 * @author Brannan
 *
 * @param <T>
 */
public class MultiD<T> {
	
	private int[] dimensions;
	private HashMap<List<Integer> , T> elements = new HashMap<>();

	public MultiD(int... dimensions){
		this.dimensions = dimensions;
	}
	
	/**
	 * Used to populate the HashMap
	 * @param element
	 * @param position
	 */
	public void put(T element, int... position){
		validatePosition(position);
		this.elements.put(retrieveForPosition(position), element);
	}
	
	/**
	 * getter for the dimensons field
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
	public T get(int... position) {
		validatePosition(position);
		return elements.get(retrieveForPosition(position));
	}
	
	
	/**
	 * This method casts the position into a list which the HashMap can use to put and get.
	 * @param position
	 * @return
	 */
	private List<Integer> retrieveForPosition(int[] position){
		List<Integer> retrieve = new ArrayList<>();
		for(int i = 0; i < position.length; i++){
			retrieve.add(position[i]);
		}
		return retrieve;
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
