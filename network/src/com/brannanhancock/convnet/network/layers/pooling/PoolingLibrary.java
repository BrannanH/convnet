package com.brannanhancock.convnet.network.layers.pooling;

import static java.util.Collections.max;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.function.ToDoubleFunction;
import java.util.stream.Collectors;

/**
 *
 * @author Brannan R. Hancock
 *
 */
final class PoolingLibrary {

    private static final double EPSILON = 0.0000001;

    /**
     * Function to compute Max Pooling.
     */
    private static final ToDoubleFunction<Set<PoolTuple>> MAX_POOL = p -> max(p,
            (a, b) -> Double.compare(a.getElement(), b.getElement())).getElement();

    /**
     * Function to compute Mean Pooling.
     */
    private static final ToDoubleFunction<Set<PoolTuple>> MEAN_POOL = p -> p.stream().mapToDouble(a -> a.getElement())
            .summaryStatistics().getAverage();

    /**
     * Function to compute Median Pooling.
     */
    private static final ToDoubleFunction<Set<PoolTuple>> MEDIAN_POOL = p -> {
        final double[] sortedValues = p.stream().mapToDouble(a -> a.getElement()).sorted().toArray();
        return sortedValues[sortedValues.length % 2 == 0 ? sortedValues.length / 2 - 1
                : Math.floorDiv(sortedValues.length, 2)];
    };

    /**
     * Function to compute the derivatives from Max Pooling.
     */
    private static final Function<Set<PoolTuple>, Set<PoolTuple>> MAX_DERIVATIVE = p -> {
        final Set<PoolTuple> results = new HashSet<>();
        final double max = MAX_POOL.applyAsDouble(p);
        final Map<Boolean, List<PoolTuple>> maxAndNotMax = p.stream()
                .collect(Collectors.groupingBy(a -> Math.abs(a.getElement() - max) < EPSILON));
        final PoolTuple maxy = new PoolTuple(1D, maxAndNotMax.get(true).get(0).getOrigin());
        results.add(maxy);
        for (int i = 1; i < maxAndNotMax.get(true).size(); i++) {
            final PoolTuple nonMax = new PoolTuple(0D, maxAndNotMax.get(true).get(i).getOrigin());
            results.add(nonMax);
        }
        for (int i = 0; i < maxAndNotMax.get(false).size(); i++) {
            final PoolTuple nonMax = new PoolTuple(0D, maxAndNotMax.get(false).get(i).getOrigin());
            results.add(nonMax);
        }
        return results;
    };

    /**
     * Function to compute the derivatives from Mean Pooling.
     */
    private static final Function<Set<PoolTuple>, Set<PoolTuple>> MEAN_DERIVATIVE = p -> {
        final double derivative = p.size();
        final Set<PoolTuple> result = new HashSet<>();
        p.stream().forEach(a -> {
            final PoolTuple forward = new PoolTuple(1D / derivative, a.getOrigin());
            result.add(forward);
        });
        return result;
    };

    /**
     * @author Brannan R. Hancock
     *
     */
    public enum PoolingType {

        MAX(MAX_POOL, MAX_DERIVATIVE), MEAN(MEAN_POOL, MEAN_DERIVATIVE), MEDIAN(MEDIAN_POOL, MAX_DERIVATIVE);

        private final Function<Set<PoolTuple>, Set<PoolTuple>> derivativeMethod;

        private final ToDoubleFunction<Set<PoolTuple>> poolingMethod;


        /**
         * @param poolingMethod
         * @param derivativeMethod
         */
        PoolingType(final ToDoubleFunction<Set<PoolTuple>> poolingMethod,
                final Function<Set<PoolTuple>, Set<PoolTuple>> derivativeMethod) {
            this.poolingMethod = poolingMethod;
            this.derivativeMethod = derivativeMethod;
        }


        /**
         * @return the poolingMethod
         */
        public ToDoubleFunction<Set<PoolTuple>> getPoolingMethod() {
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
