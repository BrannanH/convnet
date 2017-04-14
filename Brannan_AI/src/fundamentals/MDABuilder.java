package fundamentals;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MDABuilder {
	
	private List<Integer> dimensions;
	private int[] increments;
	private double[] elements;
	
	public MDA build() {
		MDA output = new MDA();
		output.setDimensions(dimensions);
		output.setElements(elements);
		output.setIncrements(increments);
		return output;
	}
	
	public MDABuilder withDimensions(int... dimensions) {
		return withDimensions(Arrays.stream(dimensions).boxed().collect(Collectors.toList()));
	}
	
	public MDABuilder withDimensions(List<Integer> dimensions) {
		this.dimensions = dimensions;
		
		this.increments = new int[dimensions.size()];
		increments[0] = 1;
		for(int i = 1; i < increments.length; i++) {
			increments[i] = increments[i-1]*dimensions.get(i-1);
		}
		this.elements = new double[dimensions.get(dimensions.size()-1)*increments[dimensions.size()-1]];
		return this;
	}

}
