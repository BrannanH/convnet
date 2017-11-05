package com.brannanhancock.convnet.network.services;

import com.brannanhancock.convnet.fundamentals.MDA;
import com.brannanhancock.convnet.fundamentals.MDABuilder;

/**
 * This class is used to extract a subset of an MDA.
 *
 * @author Brannan
 *
 */
public class ExtractDimensionService {


    /**
     * This method allows the specification of a dimension, and values of that
     * dimension which are to be extracted into a new MDA. It validates the
     * arguments provided, constructs the new MDA. If only one instance of the
     * dimension is given, the resulting MDA will have one fewer dimension,
     * however if multiple instances are chosen to be extracted, the dimension
     * specified in the returned MDA will have length equal to the number of
     * instances specified.
     *
     * @param multiD
     * @param dimension
     * @param instancesToExtract
     * @return
     */
    public static MDA subsetOf(final MDA multiD, final int dimension, final int... instancesToExtract) {

        validateDimension(multiD, dimension);

        for (final int instance : instancesToExtract) {

            validateInstance(multiD, dimension, instance);

        }

        final int[] dimensions = constructDimensions(dimension, instancesToExtract, multiD.getDimensions());
        MDABuilder result = new MDABuilder(dimensions);

        for (final int instance : instancesToExtract) {

            result = populate(result, multiD, dimension, instance, dimensions);
        }
        return result.build();
    }


    /**
     * This method constructs the integer array to specify the dimensions for
     * the returned MDA.
     *
     * @param dimension
     * @param instancesToExtract
     * @param originalsDimensions
     * @return
     */
    private static int[] constructDimensions(final int dimension, final int[] instancesToExtract,
            final int[] originalsDimensions) {

        int[] dimensionsToPersist;

        if (instancesToExtract.length == 1) {

            dimensionsToPersist = new int[originalsDimensions.length - 1];
            int count = 0;

            for (int i = 0; i < originalsDimensions.length; i++) {

                if (i != dimension) {

                    dimensionsToPersist[count] = originalsDimensions[i];
                    count++;

                }

            }

        } else {

            dimensionsToPersist = new int[originalsDimensions.length];

            for (int i = 0; i < originalsDimensions.length; i++) {

                if (i == dimension) {

                    dimensionsToPersist[i] = instancesToExtract.length;

                } else {

                    dimensionsToPersist[i] = originalsDimensions[i];

                }

            }
        }

        return dimensionsToPersist;
    }


    /**
     * This method sets up the call to the recursive operation used to copy
     * between the two MDAs.
     *
     * @param result
     * @param multiD
     * @param dimension
     * @param instance
     * @return
     */
    private static MDABuilder populate(final MDABuilder resultBuilder, final MDA multiD, final int dimension, final int instance, final int[] dimensions) {

        final int[] position = new int[multiD.getDimensions().length];
        position[dimension] = instance;

        return recursivePopulate(resultBuilder, multiD, dimension, instance, position, multiD.getDimensions().length - 1, dimensions);

    }


    /**
     * This method calls itself recursively and is used to populate the
     * Extracted MDA. It acts differently depending on the place argument. If
     * the place is equal to the dimension, no changes are made to the position
     * array, and it calls this method with place decremetned one. If place is
     * any value other than 0 or the specified dimension, it iterates through
     * each value this place in the position array can have, and calls this
     * method recursively with each of these possible values. If place = 0,
     * every element in the position array has been specified, so we have the
     * location of an element in the original MDA which needs to be copied to
     * the extracted MDA. This builds up the toPlace array, specifying where in
     * the extraction MDA this element will be placed.
     *
     * @param resultBuilder
     * @param multiD
     * @param dimension
     * @param instance
     * @param position
     * @param place
     * @return
     */
    private static MDABuilder recursivePopulate(final MDABuilder resultBuilder, final MDA multiD, final int dimension, final int instance,
            final int[] position, final int place, final int[] dimensions) {

        if (place == 0) {

            for (int j = 0; j < dimensions[0]; j++) {

                position[place] = j;
                int[] toPlace = new int[dimensions.length];

                if (dimensions.length == multiD.getDimensions().length - 1) {

                    for (int i = 0; i < multiD.getDimensions().length - 1; i++) {

                        if (i < dimension) {

                            toPlace[i] = position[i];

                        } else {

                            toPlace[i] = position[i + 1];

                        }

                    }
                } else {

                    toPlace = position;

                }

                resultBuilder.withDataPoint(multiD.get(position), toPlace);
            }
            position[0] = 0;
            return resultBuilder;
        } else if (place == dimension) {

            recursivePopulate(resultBuilder, multiD, dimension, instance, position, place - 1, dimensions);
        } else {

            for (int i = 0; i < multiD.getDimensions()[place]; i++) {

                position[place] = i;
                recursivePopulate(resultBuilder, multiD, dimension, instance, position, place - 1, dimensions);
            }
        }
        return resultBuilder;
    }


    /**
     * validates the Dimension argument passed to this service
     *
     * @param multiD
     * @param dimension
     */
    private static void validateDimension(final MDA multiD, final int dimension) {

        if (dimension > multiD.getDimensions().length - 1 || dimension < 0) {

            throw new IllegalArgumentException(
                    "Specified dimension does not exist. Possible dimensions are between 0 and "
                            + (multiD.getDimensions().length - 1));
        }
    }


    /**
     * validates each element in the instances argument passed to the service.
     *
     * @param multiD
     * @param dimension
     * @param instance
     */
    private static void validateInstance(final MDA multiD, final int dimension, final int instance) {

        if (instance > multiD.getDimensions()[dimension] - 1 || instance < 0) {

            throw new IllegalArgumentException("Specified instance of dimension " + dimension
                    + "does not exist. It exists between 0 and " + (multiD.getDimensions()[dimension] - 1));
        }
    }
}
