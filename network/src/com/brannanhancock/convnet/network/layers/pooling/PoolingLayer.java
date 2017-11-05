package com.brannanhancock.convnet.network.layers.pooling;

import static com.brannanhancock.convnet.fundamentals.HelperLibrary.arrayAsList;
import static com.brannanhancock.convnet.fundamentals.HelperLibrary.cloneList;
import static java.lang.Math.floorDiv;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntConsumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.inject.Inject;

import com.brannanhancock.convnet.fundamentals.HelperLibrary;
import com.brannanhancock.convnet.fundamentals.MDA;
import com.brannanhancock.convnet.fundamentals.MDABuilder;
import com.brannanhancock.convnet.network.layers.ForwardOutputTuple;
import com.brannanhancock.convnet.network.layers.pooling.PoolingLibrary.PoolingType;
import com.brannanhancock.convnet.network.services.DimensionVerificationService;

/**
 *
 * @author Brannan R. Hancock
 *
 */
public class PoolingLayer {

    private final DimensionVerificationService dimensionsService;


    @Inject
    public PoolingLayer(final DimensionVerificationService dimensionsService) {
        this.dimensionsService = dimensionsService;
    }


    /**
     * Computes the forward pass of the network in training phase, thus also computing all necessary
     * derivatives to update all relevant values in the network.
     * @param operand
     * @param poolSizes
     * @param poolingType
     * @return
     */
    public ForwardOutputTuple forward(final MDA operand, final int[] poolSizes, final PoolingType poolingType) {
        final Map<List<Integer>, Set<PoolTuple>> pools = createPools(operand, poolSizes);

        final MDA forward = computeOutput(outputDimensions(operand.getDimensions(), poolSizes), pools, poolingType);
        final Map<List<Integer>, Map<List<Integer>, Double>> dOutByDIn = computeDOutByDIn(pools, poolingType);
        return new ForwardOutputTuple(forward, dOutByDIn, null);
    }


    /**
     * Used for testing the network rather than training it.
     * Compute the forward pass without computing any derivatives.
     * @param operand
     * @param poolSizes
     * @param poolingType
     * @return
     */
    public MDA forwardNoTrain(final MDA operand, final int[] poolSizes, final PoolingType poolingType) {

        final Map<List<Integer>, Set<PoolTuple>> pools = createPools(operand, poolSizes);

        return computeOutput(outputDimensions(operand.getDimensions(), poolSizes), pools, poolingType);
    }


    /**
     * Compute dLossByDIn from dLossByDOut and dOutByDIn
     * @param dLossByDOut
     * @param dOutByDIn
     * @param originalInputSize
     * @return
     */
    public MDA reverse(final MDA dLossByDOut, final Map<List<Integer>, Map<List<Integer>, Double>> dOutByDIn,
            final int[] originalInputSize) {

        // verify the dimensions in the derivative map are consistent
        dimensionsService.verifyDerivativeMap(dOutByDIn);

        // create dLossByDIn at the right size
        final MDABuilder dLossByDInBuilder = new MDABuilder(originalInputSize);

        /* for each location in dOutByDIn's keyset get dLossByDOut(location).
         Multiply each double in the Value Map of dOutByDIn by it, and add
         that to its location in dLossByDIn; */
        for (final Entry<List<Integer>, Map<List<Integer>, Double>> entry : dOutByDIn.entrySet()) {
            final double coefficient = dLossByDOut.get(entry.getKey());
            for (final Entry<List<Integer>, Double> subEntry : entry.getValue().entrySet()) {
                dLossByDInBuilder.withAmountAddedToDataPoint(coefficient * subEntry.getValue(), HelperLibrary.listAsArray(subEntry.getKey()));
            }
        }
        return dLossByDInBuilder.build();
    }


    /**
     * Compute the output dimensions from the input dimensions and the pooling sizes
     *
     * @param inputDimensions
     * @param poolSizes
     * @return
     */
    public int[] outputDimensions(final int[] inputDimensions, final int[] poolSizes) {

        dimensionsService.verifyLeftBiggerThanRight(inputDimensions, poolSizes);

        final int[] results = new int[inputDimensions.length];

        for (int i = 0; i < inputDimensions.length; i++) {
            results[i] = floorDiv(inputDimensions[i], poolSizes[i]);
        }

        return results;
    }


    /**
     * Creates a pool of 1 for each element in the input MDA.
     * @param operand
     * @return
     */
    private Map<List<Integer>, Set<PoolTuple>> createInitialPooling(final MDA operand) {
        Map<List<Integer>, Set<PoolTuple>> pools = new HashMap<>();
        final int[] position = new int[operand.getDimensions().length];

        pools = build(pools, operand, position, operand.getDimensions().length - 1);

        return pools;
    }


    /**
     * A recursive method to facilitate creating a pooling of 1 for each element in the input MDA.
     * @param pools
     * @param operand
     * @param position
     * @param place
     * @return
     */
    private Map<List<Integer>, Set<PoolTuple>> build(final Map<List<Integer>, Set<PoolTuple>> pools, final MDA operand,
            final int[] position, final int place) {
        Map<List<Integer>, Set<PoolTuple>> newPools = new HashMap<>();
        newPools.putAll(pools);
        for (int i = 0; i < operand.getDimensions()[place]; i++) {
            position[place] = i;
            if (place != 0) {
                newPools = build(newPools, operand, position, place - 1);
            } else {
                final Set<PoolTuple> pool = new HashSet<>();
                final PoolTuple element = new PoolTuple(operand.get(position), position.clone());
                pool.add(element);
                newPools.put(arrayAsList(position), pool);
            }
        }
        return newPools;
    }


    /**
     * Compute the poolings from the initial operand, and pooling sizes.
     * @param operand
     * @param poolSizes
     * @return
     */
    private Map<List<Integer>, Set<PoolTuple>> createPools(final MDA operand,
            final int[] poolSizes) {

        final Map<List<Integer>, Set<PoolTuple>> pools = createInitialPooling(operand);
        final Map<List<Integer>, List<List<Integer>>> intermediateMapping2 = new HashMap<>();

        final Map<List<Integer>, List<List<Integer>>> intermediateMapping = pools.keySet().stream()
                .collect(Collectors.groupingBy(w -> {
                    final List<Integer> clone = cloneList(w);
                    clone.set(0, floorDiv(w.get(0), poolSizes[0]));
                    return clone;
                }));

        IntStream.range(1, poolSizes.length).filter(i -> poolSizes[i] != 1)
                .forEach(computeIntermediateMapping(poolSizes, intermediateMapping, intermediateMapping2));

        final Map<List<Integer>, Set<PoolTuple>> poolsToReturn = new HashMap<>();
        for (final Entry<List<Integer>, List<List<Integer>>> entry : intermediateMapping.entrySet()) {
            poolsToReturn.put(entry.getKey(), new HashSet<PoolTuple>());
            for (final List<Integer> location : entry.getValue()) {
                poolsToReturn.get(entry.getKey()).addAll(pools.get(location));
            }
        }
        return poolsToReturn;
    }


    /**
     * A Consumer to map all elements which will be pooled together, to their new grouping.
     * @param poolSizes
     * @param intermediateMapping
     * @param intermediateMapping2
     * @return
     */
    private IntConsumer computeIntermediateMapping(final int[] poolSizes,
            final Map<List<Integer>, List<List<Integer>>> intermediateMapping,
            final Map<List<Integer>, List<List<Integer>>> intermediateMapping2) {

        return i -> {

            final Map<List<Integer>, List<List<Integer>>> result = intermediateMapping.keySet().stream()
                    .collect(Collectors.groupingBy(collectByMappingToQuotient(poolSizes, i)));

            result.entrySet().stream().forEach(updateMappings(intermediateMapping, intermediateMapping2));

            intermediateMapping.clear();

            for (final Entry<List<Integer>, List<List<Integer>>> entry : intermediateMapping2.entrySet()) {

                intermediateMapping.put(entry.getKey(), entry.getValue());
            }
            intermediateMapping2.clear();
        };
    }


    /**
     * Maps each position to its correct pooling.
     * @param poolSizes
     * @param i
     * @return
     */
    private Function<? super List<Integer>, ? extends List<Integer>> collectByMappingToQuotient(final int[] poolSizes,
            final int i) {
        return w -> {
            final List<Integer> clone = cloneList(w);
            clone.set(i, floorDiv(w.get(i), poolSizes[i]));
            return clone;
        };
    }


    /**
     * Consumer to add all pooled contributions to a grouping.
     * @param intermediateMapping
     * @param intermediateMapping2
     * @return
     */
    private Consumer<? super Entry<List<Integer>, List<List<Integer>>>> updateMappings(
            final Map<List<Integer>, List<List<Integer>>> intermediateMapping,
            final Map<List<Integer>, List<List<Integer>>> intermediateMapping2) {

        return entry -> {

            if (!intermediateMapping2.containsKey(entry.getKey())) {

                intermediateMapping2.put(entry.getKey(), new ArrayList<List<Integer>>());
            }

            for (final List<Integer> location : entry.getValue()) {

                intermediateMapping2.get(entry.getKey()).addAll(intermediateMapping.get(location));
            }
        };
    }


    /**
     * Compute the output from the Poolings and the Pooling Type.
     * @param inputDimensions
     * @param poolSizes
     * @param pools
     * @param poolingType
     * @return
     */
    private MDA computeOutput(final int[] outputDimensions,
            final Map<List<Integer>, Set<PoolTuple>> pools, final PoolingType poolingType) {

        final MDABuilder outputBuilder = new MDABuilder(outputDimensions);

        pools.entrySet().stream()
                .forEach(e -> outputBuilder.withDataPoint(poolingType.getPoolingMethod().applyAsDouble(e.getValue()), HelperLibrary.listAsArray(e.getKey())));

        return outputBuilder.build();
    }


    /**
     * Compute the derivative of the output with respect to the input from the Poolings and the Pooling Type.
     * @param pools
     * @param poolingType
     * @return
     */
    private Map<List<Integer>, Map<List<Integer>, Double>> computeDOutByDIn(final Map<List<Integer>, Set<PoolTuple>> pools,
            final PoolingType poolingType) {
        final Map<List<Integer>, Map<List<Integer>, Double>> dOutByDIn = new HashMap<>();

        for (final Entry<List<Integer>, Set<PoolTuple>> entry : pools.entrySet()) {
            final Map<List<Integer>, Double> mapping = new HashMap<>();
            final Set<PoolTuple> results = poolingType.getDerivativeMethod().apply(entry.getValue());
            for (final PoolTuple pool : results) {
                mapping.put(arrayAsList(pool.getOrigin()), pool.getElement());
            }
            dOutByDIn.put(entry.getKey(), mapping);
        }
        return dOutByDIn;
    }
}
