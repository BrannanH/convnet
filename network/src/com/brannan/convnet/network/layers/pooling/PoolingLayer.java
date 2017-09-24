package com.brannan.convnet.network.layers.pooling;

import static com.brannan.convnet.network.fundamentals.HelperLibrary.arrayAsList;
import static com.brannan.convnet.network.fundamentals.HelperLibrary.cloneList;
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

import com.brannan.convnet.network.fundamentals.MDA;
import com.brannan.convnet.network.fundamentals.MDABuilder;
import com.brannan.convnet.network.fundamentals.MDAHelper;
import com.brannan.convnet.network.layers.ForwardOutputTuple;
import com.brannan.convnet.network.layers.ReverseOutputTuple;
import com.brannan.convnet.network.layers.pooling.PoolingLibrary.PoolingType;
import com.brannan.convnet.network.services.DimensionVerificationService;
import com.google.inject.Inject;

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
    public ForwardOutputTuple forward(final MDA operand, final List<Integer> poolSizes, final PoolingType poolingType) {
        Map<List<Integer>, Set<PoolTuple>> pools = createInitialPooling(operand);

        pools = createPools(pools, poolSizes);

        MDA forward = computeOutput(outputDimensions(operand.getDimensions(), poolSizes), pools, poolingType);
        Map<List<Integer>, Map<List<Integer>, Double>> dOutByDIn = computeDOutByDIn(pools, poolingType);
        ForwardOutputTuple result = new ForwardOutputTuple(forward, dOutByDIn, null);
        return result;
    }


    /**
     * Used for testing the network rather than training it.
     * Compute the forward pass without computing any derivatives.
     * @param operand
     * @param poolSizes
     * @param poolingType
     * @return
     */
    public MDA forwardNoTrain(final MDA operand, final List<Integer> poolSizes, final PoolingType poolingType) {

        Map<List<Integer>, Set<PoolTuple>> pools = createInitialPooling(operand);

        pools = createPools(pools, poolSizes);

        return computeOutput(outputDimensions(operand.getDimensions(), poolSizes), pools, poolingType);
    }
    
    
    /**
     * Compute dLossByDIn from dLossByDOut and dOutByDIn
     * @param dLossByDOut
     * @param dOutByDIn
     * @param originalInputSize
     * @return
     */
    public ReverseOutputTuple reverse(final MDA dLossByDOut, final Map<List<Integer>, Map<List<Integer>, Double>> dOutByDIn,
            final List<Integer> originalInputSize) {

        // verify the dimensions in the derivative map are consistent
        dimensionsService.verifyDerivativeMap(dOutByDIn);

        // create dLossByDIn at the right size
        MDA dLossByDIn = new MDABuilder().withDimensions(originalInputSize).build();

        // for each location in dOutByDIn's keyset get dLossByDOut(location).
        // Multiply each double in the Value Map of dOutByDIn by it, and add
        // that to its location in dLossByDIn;
        for (Entry<List<Integer>, Map<List<Integer>, Double>> entry : dOutByDIn.entrySet()) {
            double coefficient = MDAHelper.get(dLossByDOut, entry.getKey());
            for (Entry<List<Integer>, Double> subEntry : entry.getValue().entrySet()) {
                MDAHelper.addTo(dLossByDIn, coefficient * subEntry.getValue(), subEntry.getKey());
            }
        }
        return new ReverseOutputTuple(dLossByDIn);
    };


    /**
     * Compute the output dimensions from the input dimensions and the pooling sizes
     * 
     * @param inputDimensions
     * @param poolSizes
     * @return
     */
    public List<Integer> outputDimensions(final List<Integer> inputDimensions, final List<Integer> poolSizes) {

        dimensionsService.verifyLeftBiggerThanRight(inputDimensions, poolSizes);

        List<Integer> results = new ArrayList<>();

        IntStream.range(0, inputDimensions.size()).map(i -> floorDiv(inputDimensions.get(i), poolSizes.get(i)))
                .forEachOrdered(j -> results.add(j));

        return results;
    }

    
    /**
     * Creates a pool of 1 for each element in the input MDA.
     * @param operand
     * @return
     */
    private Map<List<Integer>, Set<PoolTuple>> createInitialPooling(final MDA operand) {
        Map<List<Integer>, Set<PoolTuple>> pools = new HashMap<>();
        int[] position = new int[operand.getDimensions().size()];

        pools = build(pools, operand, position, operand.getDimensions().size() - 1);

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
        for (int i = 0; i < operand.getDimensions().get(place); i++) {
            position[place] = i;
            if (place != 0) {
                newPools = build(newPools, operand, position, place - 1);
            } else {
                Set<PoolTuple> pool = new HashSet<>();
                PoolTuple element = new PoolTuple(MDAHelper.get(operand, position), position.clone());
                pool.add(element);
                newPools.put(arrayAsList(position), pool);
            }
        }
        return newPools;
    }
    
    
    /**
     * Compute the poolings from the initial 1-1 mapping, and pooling sizes.
     * @param pools
     * @param poolSizes
     * @return
     */
    private Map<List<Integer>, Set<PoolTuple>> createPools(final Map<List<Integer>, Set<PoolTuple>> pools,
            final List<Integer> poolSizes) {

        // intermediateMapping = new HashMap<>();
        Map<List<Integer>, List<List<Integer>>> intermediateMapping2 = new HashMap<>();

        Map<List<Integer>, List<List<Integer>>> intermediateMapping = pools.keySet().stream()
                .collect(Collectors.groupingBy(w -> {
                    List<Integer> clone = cloneList(w);
                    clone.set(0, floorDiv(w.get(0), poolSizes.get(0)));
                    return clone;
                }));

        IntStream.range(1, poolSizes.size()).filter(i -> poolSizes.get(i) != 1)
                .forEach(computeIntermediateMapping(poolSizes, intermediateMapping, intermediateMapping2));

        Map<List<Integer>, Set<PoolTuple>> poolsToReturn = new HashMap<>();
        for (Entry<List<Integer>, List<List<Integer>>> entry : intermediateMapping.entrySet()) {
            poolsToReturn.put(entry.getKey(), new HashSet<PoolTuple>());
            for (List<Integer> location : entry.getValue()) {
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
    private IntConsumer computeIntermediateMapping(final List<Integer> poolSizes,
            final Map<List<Integer>, List<List<Integer>>> intermediateMapping,
            final Map<List<Integer>, List<List<Integer>>> intermediateMapping2) {

        return i -> {

            Map<List<Integer>, List<List<Integer>>> result = intermediateMapping.keySet().stream()
                    .collect(Collectors.groupingBy(collectByMappingToQuotient(poolSizes, i)));

            result.entrySet().stream().forEach(updateMappings(intermediateMapping, intermediateMapping2));

            intermediateMapping.clear();

            for (Entry<List<Integer>, List<List<Integer>>> entry : intermediateMapping2.entrySet()) {

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
    private Function<? super List<Integer>, ? extends List<Integer>> collectByMappingToQuotient(final List<Integer> poolSizes,
            final int i) {
        return w -> {
            List<Integer> clone = cloneList(w);
            clone.set(i, floorDiv(w.get(i), poolSizes.get(i)));
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

            for (List<Integer> location : entry.getValue()) {

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
    private MDA computeOutput(final List<Integer> outputDimensions,
            final Map<List<Integer>, Set<PoolTuple>> pools, final PoolingType poolingType) {

        MDA output = new MDABuilder().withDimensions(outputDimensions).build();

        pools.entrySet().stream()
                .forEach(e -> MDAHelper.put(output, poolingType.getPoolingMethod().applyAsDouble(e.getValue()), e.getKey()));

        return output;
    }


    /**
     * Compute the derivative of the output with respect to the input from the Poolings and the Pooling Type.
     * @param pools
     * @param poolingType
     * @return
     */
    private Map<List<Integer>, Map<List<Integer>, Double>> computeDOutByDIn(final Map<List<Integer>, Set<PoolTuple>> pools,
            final PoolingType poolingType) {
        Map<List<Integer>, Map<List<Integer>, Double>> dOutByDIn = new HashMap<>();
        
        for (Entry<List<Integer>, Set<PoolTuple>> entry : pools.entrySet()) {
            Map<List<Integer>, Double> mapping = new HashMap<>();
            Set<PoolTuple> results = poolingType.getDerivativeMethod().apply(entry.getValue());
            for (PoolTuple pool : results) {
                mapping.put(arrayAsList(pool.getOrigin()), pool.getElement());
            }
            dOutByDIn.put(entry.getKey(), mapping);
        }
        return dOutByDIn;
    }
}
