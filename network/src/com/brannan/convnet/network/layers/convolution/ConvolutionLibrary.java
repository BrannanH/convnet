package com.brannan.convnet.network.layers.convolution;

import static java.util.stream.Collectors.groupingBy;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.stream.IntStream;

import com.google.common.collect.Lists;

public class ConvolutionLibrary {
    
    /**
     * This function calculates the output dimensions for a convolution which ignores edge effects.
     */
    private static final BiFunction<List<Integer>, List<Integer>, List<Integer>> IGNORE_PADDING_OPEARATION_DIMENSIONS = (List<Integer> a, List<Integer> b) -> {
        List<Integer> result = Lists.newArrayList();
        Map<Boolean, List<Integer>> groupByWhetherOrNotDimensionIsActive = IntStream.range(0, a.size())
                .boxed()
                .collect(groupingBy((Integer i) -> a.get(i) == b.get(i)));
        
        for (int i : groupByWhetherOrNotDimensionIsActive.get(true)) {
            result.add(i, a.get(i));
        }
        
        for (int i : groupByWhetherOrNotDimensionIsActive.get(false)) {
            result.add(i, Math.abs(a.get(i) - b.get(i)) + 1);
        }
        return result;
    };
    
    
    /**
     * This function returns the dimensions of the input as associated padding types allow for edge effects.
     */
    private static final BiFunction<List<Integer>, List<Integer>, List<Integer>> DONT_IGNORE_PADDING_OPEARATION_DIMENSIONS = (List<Integer> a, List<Integer> b) -> {
        return a;
    };

        
        
    /**
     * 
     * @author Brannan R. Hancock
     *
     */
    public enum PaddingType {
        
        IGNORE(IGNORE_PADDING_OPEARATION_DIMENSIONS),
        
        ZERO(DONT_IGNORE_PADDING_OPEARATION_DIMENSIONS), 
        
        REFLECTION(DONT_IGNORE_PADDING_OPEARATION_DIMENSIONS
                );
        
        private final BiFunction<List<Integer>, List<Integer>, List<Integer>> outputDimensionsFunction;
        
        PaddingType(final BiFunction<List<Integer>, List<Integer>, List<Integer>> outputDimensionsFunction) {
            this.outputDimensionsFunction = outputDimensionsFunction;
        }
        
        public BiFunction<List<Integer>, List<Integer>, List<Integer>> getOutputDimensionsFunction() {
            return this.outputDimensionsFunction;
        }
    }

}
