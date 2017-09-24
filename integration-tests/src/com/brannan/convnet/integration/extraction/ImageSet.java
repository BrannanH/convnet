package com.brannan.convnet.integration.extraction;

public enum ImageSet {

    TRAINING("./lib/train-images.idx3-ubyte", "./lib/train-labels.idx1-ubyte", 60000),

    TESTING("./lib/t10k-images.idx3-ubyte", "./lib/t10k-labels.idx1-ubyte", 10000);

    private final String imageLocation;

    private final String labelLocation;

    private final int numberOfImagesInSet;


    /**
     * @param poolingMethod
     * @param derivativeMethod
     */
    ImageSet(final String imageLocation, final String labelLocation, final int imagesInSet) {
        this.imageLocation = imageLocation;
        this.labelLocation = labelLocation;
        this.numberOfImagesInSet = imagesInSet;
    }


    /**
     * @return the imageLocation
     */
    public String getImageLocation() {
        return imageLocation;
    } 


    /**
     * @return the labelLocation
     */
    public String getLabelLocation() {
        return labelLocation;
    }


    /**
     * @return the imagesInSet
     */
    public int getImagesInSet() {
        return numberOfImagesInSet;
    }
}
