package services;

import static fundamentals.MDAHelper.get;
import static fundamentals.MDAHelper.put;

import java.util.List;

import fundamentals.MDA;
import fundamentals.MDABuilder;

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
    public static MDA subsetOf(final MDA multiD, int dimension, int... instancesToExtract) {

        validateDimension(multiD, dimension);

        for (int instance : instancesToExtract) {

            validateInstance(multiD, dimension, instance);

        }

        MDA result = new MDABuilder()
                .withDimensions(constructDimensions(dimension, instancesToExtract, multiD.getDimensions())).build();

        for (int instance : instancesToExtract) {

            result = populate(result, multiD, dimension, instance);
        }
        return result;
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
    private static int[] constructDimensions(int dimension, int[] instancesToExtract,
            List<Integer> originalsDimensions) {

        int[] dimensionsToPersist;

        if (instancesToExtract.length == 1) {

            dimensionsToPersist = new int[originalsDimensions.size() - 1];
            int count = 0;

            for (int i = 0; i < originalsDimensions.size(); i++) {

                if (i != dimension) {

                    dimensionsToPersist[count] = originalsDimensions.get(i);
                    count++;

                }

            }

        } else {

            dimensionsToPersist = new int[originalsDimensions.size()];

            for (int i = 0; i < originalsDimensions.size(); i++) {

                if (i == dimension) {

                    dimensionsToPersist[i] = instancesToExtract.length;

                } else {

                    dimensionsToPersist[i] = originalsDimensions.get(i);

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
    private static MDA populate(final MDA result, final MDA multiD, final int dimension, final int instance) {

        int[] position = new int[multiD.getDimensions().size()];
        position[dimension] = instance;

        return recursivePopulate(result, multiD, dimension, instance, position, multiD.getDimensions().size() - 1);

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
     * @param result
     * @param multiD
     * @param dimension
     * @param instance
     * @param position
     * @param place
     * @return
     */
    private static MDA recursivePopulate(final MDA result, final MDA multiD, final int dimension, final int instance,
            final int[] position, int place) {

        if (place == 0) {

            for (int j = 0; j < result.getDimensions().get(0); j++) {

                position[place] = j;
                int[] toPlace = new int[result.getDimensions().size()];

                if (result.getDimensions().size() == multiD.getDimensions().size() - 1) {

                    for (int i = 0; i < multiD.getDimensions().size() - 1; i++) {

                        if (i < dimension) {

                            toPlace[i] = position[i];

                        } else if (i >= dimension) {

                            toPlace[i] = position[i + 1];

                        }

                    }
                } else {

                    toPlace = position;

                }

                put(result, get(multiD, position), toPlace);
            }
            position[0] = 0;
            return result;
        } else if (place == dimension) {

            recursivePopulate(result, multiD, dimension, instance, position, place - 1);
        } else {

            for (int i = 0; i < multiD.getDimensions().get(place); i++) {

                position[place] = i;
                recursivePopulate(result, multiD, dimension, instance, position, place - 1);
            }
        }
        return result;
    }


    /**
     * validates the Dimension argument passed to this service
     * 
     * @param multiD
     * @param dimension
     */
    private static void validateDimension(final MDA multiD, final int dimension) {

        if (dimension > multiD.getDimensions().size() - 1 || dimension < 0) {

            throw new IllegalArgumentException(
                    "Specified dimension does not exist. Possible dimensions are between 0 and "
                            + (multiD.getDimensions().size() - 1));
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

        if (instance > multiD.getDimensions().get(dimension) - 1 || instance < 0) {

            throw new IllegalArgumentException("Specified instance of dimension " + dimension
                    + "does not exist. It exists between 0 and " + (multiD.getDimensions().get(dimension) - 1));
        }
    }
}
