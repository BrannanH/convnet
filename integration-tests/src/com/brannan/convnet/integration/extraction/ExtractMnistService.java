package com.brannan.convnet.integration.extraction;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;

import com.brannan.convnet.network.fundamentals.MDABuilder;

/**
 * This class exists to extract the data from the MNIST database.
 *
 * @author Brannan
 *
 */
public class ExtractMnistService {

    public FileTuple extractImages(final ImageSet imageSet, final int numberOfImages, final SampleType sampleType) throws IOException {

        final int[] wantedImageIndices = sampleType.getGenerator().applyAsArray(numberOfImages, imageSet.getImagesInSet());

        DataInputStream dataInputStream = new DataInputStream(new FileInputStream(imageSet.getImageLocation()));

        dataInputStream.readInt(); // clears the magic number

        final int numImages = dataInputStream.readInt();
        final int numRows = dataInputStream.readInt();
        final int numColumns = dataInputStream.readInt();
        byte[] data = new byte[numImages * numRows * numColumns];

        int flags = dataInputStream.read(data, 0, numImages * numRows * numColumns);
        if (flags != 0) {
            dataInputStream.close();
            throw new IOException();
        }
        dataInputStream.close();

        final MDABuilder imageStoreBuilder = new MDABuilder(numRows, numColumns, wantedImageIndices.length);
        int count = 0;
        for (int image = 0; image < wantedImageIndices.length; image++) {
            final int start = wantedImageIndices[image] * numRows * numColumns;
            count = start;
            for (int row = 0; row < numRows; row++) {
                for (int column = 0; column < numColumns; column++) {
                    imageStoreBuilder.withDataPoint((double) ((data[count]) & 0xff) / 255, row, column, image);
                    count++;
                }
            }
        }

        dataInputStream = new DataInputStream(new FileInputStream(imageSet.getLabelLocation()));
        dataInputStream.readInt();
        final int numLabels = dataInputStream.readInt();
        data = new byte[numLabels];
        flags = dataInputStream.read(data, 0, numLabels);
        if (flags != 0) {
            dataInputStream.close();
            throw new IOException();
        }
        dataInputStream.close();
        final double[] labelStore = new double[wantedImageIndices.length];
        for (int i = 0; i < labelStore.length; i++) {
            labelStore[i] = (data[wantedImageIndices[i]]) & 0xff;
        }

        return new FileTuple(imageStoreBuilder.build(), labelStore);
    }
}
