package layers.pooling;

import static fundamentals.HelperLibrary.arrayAsList;
import static fundamentals.HelperLibrary.cloneList;
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

import com.google.inject.Inject;

import fundamentals.MDA;
import fundamentals.MDABuilder;
import fundamentals.MDAHelper;
import layers.ForwardOutputTuple;
import layers.ReverseOutputTuple;
import layers.pooling.PoolingLibrary.PoolingType;
import services.DimensionVerificationService;

public class PoolingLayer {

    private DimensionVerificationService dimensionsService;


    @Inject
    public PoolingLayer(DimensionVerificationService dimensionsService) {
        this.dimensionsService = dimensionsService;
    }


    /**
     * @param operand
     * @param poolSizes
     * @param poolingType
     * @return
     */
    public ForwardOutputTuple forward(MDA operand, List<Integer> poolSizes, PoolingType poolingType) {
        Map<List<Integer>, Set<PoolTuple>> pools = createInitialPooling(operand);

        pools = createPools(pools, poolSizes);

        MDA forward = createOutput(operand.getDimensions(), poolSizes, pools, poolingType);
        Map<List<Integer>, Map<List<Integer>, Double>> dOutByDIn = createdOutByDIn(pools, poolingType);
        ForwardOutputTuple result = new ForwardOutputTuple(forward, dOutByDIn, null);
        return result;
    }


    /**
     * @param pools
     * @param poolingType
     * @return
     */
    private Map<List<Integer>, Map<List<Integer>, Double>> createdOutByDIn(Map<List<Integer>, Set<PoolTuple>> pools,
            PoolingType poolingType) {
        Map<List<Integer>, Map<List<Integer>, Double>> dOutByDIn = new HashMap<>();
        
        for (Entry<List<Integer>, Set<PoolTuple>> entry : pools.entrySet()) {
            Map<List<Integer>, Double> mapping = new HashMap<>();
            Set<PoolTuple> results = poolingType.getDerivativeMethod().apply(entry.getValue());
            for (PoolTuple pool : results) {
                mapping.put(arrayAsList(pool.origin), pool.element);
            }
            dOutByDIn.put(entry.getKey(), mapping);
        }
        return dOutByDIn;
    }


    public MDA forwardNoTrain(MDA operand, List<Integer> poolSizes, PoolingType poolingType) {

        Map<List<Integer>, Set<PoolTuple>> pools = createInitialPooling(operand);

        pools = createPools(pools, poolSizes);

        return createOutput(operand.getDimensions(), poolSizes, pools, poolingType);
    }


    /**
     * TODO
     * 
     * @param inputDimensions
     * @param poolSizes
     * @return
     */
    public List<Integer> outputDimensions(List<Integer> inputDimensions, List<Integer> poolSizes) {

        dimensionsService.verify(inputDimensions, poolSizes);

        List<Integer> results = new ArrayList<>();

        IntStream.range(0, inputDimensions.size()).map(i -> floorDiv(inputDimensions.get(i), poolSizes.get(i)))
                .forEachOrdered(j -> results.add(j));

        return results;
    }


    /**
     * TODO
     */
    public ReverseOutputTuple reverse(MDA dLossByDOut, Map<List<Integer>, Map<List<Integer>, Double>> dOutByDIn,
            Map<List<Integer>, Map<List<Integer>, Double>> dOutByDFeature) {
        return null;
    };


    /**
     * @param inputDimensions
     * @param poolSizes
     * @param pools
     * @param poolingType
     * @return
     */
    private MDA createOutput(List<Integer> inputDimensions, List<Integer> poolSizes,
            Map<List<Integer>, Set<PoolTuple>> pools, PoolingType poolingType) {

        MDA output = new MDABuilder().withDimensions(outputDimensions(inputDimensions, poolSizes)).build();

        pools.entrySet().stream()
                .forEach(e -> MDAHelper.put(output, poolingType.getPoolingMethod().apply(e.getValue()), e.getKey()));

        return output;
    }


    /**
     * @param pools
     * @param poolSizes
     * @return
     */
    private Map<List<Integer>, Set<PoolTuple>> createPools(Map<List<Integer>, Set<PoolTuple>> pools,
            List<Integer> poolSizes) {

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
     * @param poolSizes
     * @param intermediateMapping
     * @param intermediateMapping2
     * @return
     */
    private IntConsumer computeIntermediateMapping(List<Integer> poolSizes,
            Map<List<Integer>, List<List<Integer>>> intermediateMapping,
            Map<List<Integer>, List<List<Integer>>> intermediateMapping2) {

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
     * @param intermediateMapping
     * @param intermediateMapping2
     * @return
     */
    private Consumer<? super Entry<List<Integer>, List<List<Integer>>>> updateMappings(
            Map<List<Integer>, List<List<Integer>>> intermediateMapping,
            Map<List<Integer>, List<List<Integer>>> intermediateMapping2) {

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
     * @param poolSizes
     * @param i
     * @return
     */
    private Function<? super List<Integer>, ? extends List<Integer>> collectByMappingToQuotient(List<Integer> poolSizes,
            int i) {
        return w -> {
            List<Integer> clone = cloneList(w);
            clone.set(i, floorDiv(w.get(i), poolSizes.get(i)));
            return clone;
        };
    }


    /**
     * @param operand
     * @return
     */
    private Map<List<Integer>, Set<PoolTuple>> createInitialPooling(MDA operand) {
        Map<List<Integer>, Set<PoolTuple>> pools = new HashMap<>();
        int[] position = new int[operand.getDimensions().size()];

        pools = build(pools, operand, position, operand.getDimensions().size() - 1);

        return pools;
    }


    /**
     * @param pools
     * @param operand
     * @param position
     * @param place
     * @return
     */
    private Map<List<Integer>, Set<PoolTuple>> build(Map<List<Integer>, Set<PoolTuple>> pools, MDA operand,
            int[] position, int place) {
        for (int i = 0; i < operand.getDimensions().get(place); i++) {
            position[place] = i;
            if (place != 0) {
                pools = build(pools, operand, position, place - 1);
            } else {
                Set<PoolTuple> pool = new HashSet<>();
                PoolTuple element = new PoolTuple(MDAHelper.get(operand, position), position.clone());
                pool.add(element);
                pools.put(arrayAsList(position), pool);
            }
        }
        return pools;
    }
    

    /**
     * A class to associate a value with its origin in the un-pooled MDA.
     * 
     * @author Brannan
     *
     */
    protected static class PoolTuple {

        private double element;
        private int[] origin;
        
        PoolTuple(double element, int[] origin) {
            this.element = element;
            this.origin = origin.clone();
        }
        
        /**
         * @return the element
         */
        public double getElement() {
            return element;
        }

        
        /**
         * @param element the element to set
         */
        public void setElement(double element) {
            this.element = element;
        }

        
        /**
         * @return the origin
         */
        public int[] getOrigin() {
            return origin.clone();
        }

        
        /**
         * @param origin the origin to set
         */
        public void setOrigin(int[] origin) {
            this.origin = origin.clone();
        }



        @Override
        public String toString() {
            return arrayAsList(origin).toString() + element;
        }
    }

}
