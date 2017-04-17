package layers.pooling;

import static java.util.Collections.max;
import static java.util.stream.Collectors.averagingDouble;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 
 * @author Brannan R. Hancock
 *
 */
public class PoolingLibrary {

    private static final double EPSILON = 0.0000001;

    
    /**
     * Function to compute Max Pooling.
     */
    private static final Function<Set<PoolTuple>, Double> MAX_POOL = p -> max(p,
            (a, b) -> Double.compare(a.getElement(), b.getElement())).getElement();

    
    /**
     * Function to compute Mean Pooling.
     */
    private static final Function<Set<PoolTuple>, Double> MEAN_POOL = p -> p.stream()
            .collect(averagingDouble((a) -> a.getElement()));

    
    /**
     * Function to compute Median Pooling.
     */
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
     * Function to compute the derivatives from Max Pooling.
     */
    private static final Function<Set<PoolTuple>, Set<PoolTuple>> MAX_DERIVATIVE = p -> {
        Set<PoolTuple> results = new HashSet<>();
        double max = MAX_POOL.apply(p);
        Map<Boolean, List<PoolTuple>> maxAndNotMax = p.stream()
                .collect(Collectors.groupingBy(a -> Math.abs(a.getElement() - max) < EPSILON));
        PoolTuple maxy = new PoolTuple(1D, maxAndNotMax.get(true).get(0).getOrigin());
        results.add(maxy);
        for (int i = 1; i < maxAndNotMax.get(true).size(); i++) {
            PoolTuple nonMax = new PoolTuple(0D, maxAndNotMax.get(true).get(i).getOrigin());
            results.add(nonMax);
        }
        for (int i = 0; i < maxAndNotMax.get(false).size(); i++) {
            PoolTuple nonMax = new PoolTuple(0D, maxAndNotMax.get(false).get(i).getOrigin());
            results.add(nonMax);
        }
        return results;
    };

    
    /**
     * Function to compute the derivatives from Mean Pooling.
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

        private Function<Set<PoolTuple>, Double> poolingMethod;

        
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
