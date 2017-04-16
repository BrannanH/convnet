package layers.pooling;

import static java.util.Collections.max;
import static java.util.stream.Collectors.averagingDouble;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.mockito.internal.util.collections.Sets;

import layers.pooling.PoolingLayer.PoolTuple;

public class PoolingLibrary {
    
    private static final double EPSILON = 0.0000001;

    private static final Function<Set<PoolTuple>, Double> MAX_POOL = p -> max(p,
            (a, b) -> Double.compare(a.getElement(), b.getElement())).getElement();

    private static final Function<Set<PoolTuple>, Double> MEAN_POOL = p -> p.stream()
            .collect(averagingDouble((a) -> a.getElement()));

    private static final Function<Set<PoolTuple>, Double> MEDIAN_POOL = p -> {
        List<PoolTuple> sorted = p.stream().collect(Collectors.toList());
        Collections.sort(sorted, (a, b) -> Double.compare(a.getElement(), b.getElement()));
        if (sorted.size() % 2 == 0) {
            return sorted.get((sorted.size()) / 2 - 1).getElement();
        } else {
            return sorted.get(Math.floorDiv(sorted.size(), 2)).getElement();
        }
    };

    
    /**
     * 
     */
    private static final Function<Set<PoolTuple>, Set<PoolTuple>> MAX_DERIVATIVE = p -> {
        double max = MAX_POOL.apply(p);
        PoolTuple blah = p.stream().filter((PoolTuple a) -> Math.abs(a.getElement() - max) < EPSILON).findAny().get();
        blah.setElement(1D);
        return Sets.newSet(blah);
    };

    
    /**
     * 
     */
    private static final Function<Set<PoolTuple>, Set<PoolTuple>> MEAN_DERIVATIVE = p -> {
        double derivative = p.size();
        Set<PoolTuple> result = new HashSet<>();
        p.stream().forEach(a -> {
            PoolTuple forward = new PoolTuple(1D / derivative, a.getOrigin());
            result.add(forward);
        });
        return result;
    };

    
    /**
     * @author Brannan
     *
     */
    public enum PoolingType {

        MAX(MAX_POOL, MAX_DERIVATIVE), MEAN(MEAN_POOL, MEAN_DERIVATIVE), MEDIAN(MEDIAN_POOL, MAX_DERIVATIVE);

        private Function<Set<PoolTuple>, Set<PoolTuple>> derivativeMethod;


        /**
         * @param poolingMethod
         * @param derivativeMethod
         */
        PoolingType(Function<Set<PoolTuple>, Double> poolingMethod,
                Function<Set<PoolTuple>, Set<PoolTuple>> derivativeMethod) {
            this.poolingMethod = poolingMethod;
            this.derivativeMethod = derivativeMethod;
        }

        
        /**
         * 
         */
        private Function<Set<PoolTuple>, Double> poolingMethod;


        /**
         * @return the poolingMethod
         */
        public Function<Set<PoolTuple>, Double> getPoolingMethod() {
            return poolingMethod;
        }


        /**
         * @return the derivativeMethod
         */
        public Function<Set<PoolTuple>, Set<PoolTuple>> getDerivativeMethod() {
            return derivativeMethod;
        }
    }
}
