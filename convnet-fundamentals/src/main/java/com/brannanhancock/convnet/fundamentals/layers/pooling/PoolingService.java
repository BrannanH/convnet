package com.brannanhancock.convnet.fundamentals.layers.pooling;

import com.brannanhancock.convnet.fundamentals.layers.DimensionVerificationService;
import com.brannanhancock.convnet.fundamentals.layers.ForwardOutputTuple;
import com.brannanhancock.convnet.fundamentals.layers.LayerService;
import com.brannanhancock.convnet.fundamentals.layers.ReversePassOutput;
import com.brannanhancock.convnet.fundamentals.layers.pooling.PoolingLibrary.PoolingType;
import com.brannanhancock.convnet.fundamentals.mda.MDA;
import com.brannanhancock.convnet.fundamentals.mda.MDABuilder;
import jakarta.inject.Inject;

import java.util.ArrayList;
import java.util.Arrays;
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

import static java.lang.Math.floorDiv;

/**
 *
 * @author Brannan R. Hancock
 *
 */
public class PoolingService implements LayerService<PoolingLayer>  {

    private final DimensionVerificationService dimensionsService;


    @Inject
    public PoolingService(final DimensionVerificationService dimensionsService) {
        this.dimensionsService = dimensionsService;
    }

    @Override
    public ForwardOutputTuple forward(PoolingLayer layer, MDA operand) {
        final Map<List<Integer>, Set<PoolTuple>> pools = createPools(operand, layer.getPoolSizes());

        final MDA forward = computeOutput(outputDimensionsFor(layer), pools, layer.getPoolingType());
        final MDA dOutByDIn = computeDOutByDIn(layer, pools, layer.getPoolingType());
        return new ForwardOutputTuple(layer, forward, dOutByDIn, null);
    }

    @Override
    public MDA forwardNoTrain(PoolingLayer layer, MDA operand) {
        final Map<List<Integer>, Set<PoolTuple>> pools = createPools(operand, layer.getPoolSizes());

        return computeOutput(outputDimensionsFor(layer), pools, layer.getPoolingType());
    }

    @Override
    public ReversePassOutput reverse(ForwardOutputTuple resultFromForwardPass, MDA dLossByDOut) {

        // create dLossByDIn at the right size
        final MDABuilder dLossByDInBuilder = new MDABuilder(resultFromForwardPass.getLayer().getInputDimensions());
        recursivePopulateResultBySideEffect(dLossByDInBuilder, dLossByDOut, resultFromForwardPass, 0, new int[resultFromForwardPass.getdOutByDIn().getDimensions().length]);

//        /* for each location in dOutByDIn's keyset get dLossByDOut(location).
//         Multiply each double in the Value Map of dOutByDIn by it, and add
//         that to its location in dLossByDIn; */
//        for (final Entry<List<Integer>, Map<List<Integer>, Double>> entry : resultFromForwardPass.getdOutByDIn().entrySet()) {
//            final double coefficient = dLossByDOut.get(entry.getKey());
//            for (final Entry<List<Integer>, Double> subEntry : entry.getValue().entrySet()) {
//                dLossByDInBuilder.withAmountAddedToDataPoint(coefficient * subEntry.getValue(), subEntry.getKey().stream().mapToInt(Integer::intValue).toArray());
//            }
//        }
        return new ReversePassOutput(resultFromForwardPass.getLayer(), null, dLossByDInBuilder.build());
    }

    private void recursivePopulateResultBySideEffect(MDABuilder dLossByDInBuilder, MDA dLossByDOut, ForwardOutputTuple resultFromForwardPass, int dimension, int[] dLossByDInCoordinate) {
        if (dimension == resultFromForwardPass.getdOutByDIn().getDimensions().length) {
            // coordinates array is fully populated
            int numCoordinatesInInputSpace = resultFromForwardPass.getLayer().getInputDimensions().length;
            int[] coordinatesInOutputSpace = Arrays.copyOfRange(dLossByDInCoordinate, 0, dLossByDInCoordinate.length - numCoordinatesInInputSpace);
            double dLossByDOutCoefficient = dLossByDOut.get(coordinatesInOutputSpace);
            dLossByDInBuilder.withAmountAddedToDataPoint(dLossByDOutCoefficient * resultFromForwardPass.getdOutByDIn().get(dLossByDInCoordinate), Arrays.copyOfRange(dLossByDInCoordinate, dLossByDInCoordinate.length - numCoordinatesInInputSpace, dLossByDInCoordinate.length));
            return;
        }
        for (int i = 0; i < resultFromForwardPass.getdOutByDIn().getDimensions()[dimension]; i++) {
            dLossByDInCoordinate[dimension] = i;
            recursivePopulateResultBySideEffect(dLossByDInBuilder, dLossByDOut, resultFromForwardPass, dimension + 1, dLossByDInCoordinate);
        }
    }

    @Override
    public int[] outputDimensionsFor(PoolingLayer layer) {
        if(!dimensionsService.verifyLeftBiggerThanRight(layer.getInputDimensions(), layer.getPoolSizes())) {
            throw new IllegalArgumentException("The input passed to the pooling layer is not correctly configured.");
        }

        final int[] results = new int[layer.getInputDimensions().length];

        for (int i = 0; i < layer.getInputDimensions().length; i++) {
            results[i] = floorDiv(layer.getInputDimensions()[i], layer.getPoolSizes()[i]);
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
                newPools.put(Arrays.stream(position).boxed().toList(), pool);
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
                    final List<Integer> clone = new ArrayList(w);
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

            result.entrySet().forEach(updateMappings(intermediateMapping, intermediateMapping2));

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
        return (List<Integer> w) -> {
            final List<Integer> clone = new ArrayList(w);
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
     * @param outputDimensions
     * @param pools
     * @param poolingType
     * @return
     */
    private MDA computeOutput(final int[] outputDimensions,
            final Map<List<Integer>, Set<PoolTuple>> pools, final PoolingType poolingType) {

        final MDABuilder outputBuilder = new MDABuilder(outputDimensions);

        pools.entrySet()
                .forEach(e -> outputBuilder.withDataPoint(poolingType.getPoolingMethod().applyAsDouble(e.getValue()), e.getKey().stream().mapToInt(Integer::intValue).toArray()));

        return outputBuilder.build();
    }


    /**
     * Compute the derivative of the output with respect to the input from the Poolings and the Pooling Type.
     * @param pools
     * @param poolingType
     * @return
     */
    private MDA computeDOutByDIn(final PoolingLayer layer, final Map<List<Integer>, Set<PoolTuple>> pools,
            final PoolingType poolingType) {
//        final Map<List<Integer>, Map<List<Integer>, Double>> dOutByDIn = new HashMap<>();
        int[] outputSize = outputDimensionsFor(layer);
        int[] dOutByDInSize = IntStream.concat(Arrays.stream(outputSize), Arrays.stream(layer.getInputDimensions())).toArray();
        MDABuilder dOutByDIn = new MDABuilder(dOutByDInSize);
        int[] coordinates = new int[dOutByDInSize.length];

        for (final Entry<List<Integer>, Set<PoolTuple>> entry : pools.entrySet()) {
            for (int i = 0; i < entry.getKey().size(); i++) {
                coordinates[i] = entry.getKey().get(i);
            }
            final Set<PoolTuple> results = poolingType.getDerivativeMethod().apply(entry.getValue());
            for (final PoolTuple pool : results) {
                for (int i = 0; i < pool.getOrigin().length; i++) {
                    coordinates[entry.getKey().size() + i] = pool.getOrigin()[i];
                }
                dOutByDIn.withDataPoint(pool.getElement(), coordinates);
            }
        }
        return dOutByDIn.build();
    }
}
