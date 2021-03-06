package com.brannanhancock.convnet.integration.extraction;

import com.brannanhancock.convnet.fundamentals.MDA;

/**
 * This file tuple is made as the return type of the extract. It simply bundles
 * the MDAs and arrays together.
 *
 * @author Brannan
 *
 */
public class FileTuple {

    private final MDA images;
    private final double[] labels;


    FileTuple(final MDA images, final double[] labels) {
        this.images = images;
        this.labels = labels;
    }


    /**
     * @return the images
     */
    public MDA getImages() {
        return images;
    }


    /**
     * @return the labels
     */
    public double[] getLabels() {
        return labels.clone();
    }
}
