package com.brannan.convnet.integration.extraction;

import static com.brannan.convnet.network.fundamentals.MDAHelper.put;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;

import com.brannan.convnet.network.fundamentals.MDA;
import com.brannan.convnet.network.fundamentals.MDABuilder;

/**
 * This class exists to extract the data from the MNIST database.
 * 
 * @author Brannan
 *
 */
public class ExtractMnistService {

    public FileTuple extractImages(final ImageSet imageSet, final int numberOfImages, final SampleType sampleType) throws IOException {

        int[] wantedImageIndices = sampleType.getGenerator().apply(numberOfImages, imageSet.getImagesInSet());

        DataInputStream dataInputStream = new DataInputStream(new FileInputStream(imageSet.getImageLocation()));

        dataInputStream.readInt(); // clears the magic number

        int numImages = dataInputStream.readInt();
        int numRows = dataInputStream.readInt();
        int numColumns = dataInputStream.readInt();
        byte[] data = new byte[numImages * numRows * numColumns];

        int flags = dataInputStream.read(data, 0, numImages * numRows * numColumns);
        if (flags != 0) {
            dataInputStream.close();
            throw new IOException();
        }
        dataInputStream.close();

        MDA imageStore = new MDABuilder().withDimensions(numRows, numColumns, wantedImageIndices.length).build();
        int count = 0;
        for (int image = 0; image < wantedImageIndices.length; image++) {
            int start = wantedImageIndices[image] * numRows * numColumns;
            count = start;
            for (int row = 0; row < numRows; row++) {
                for (int column = 0; column < numColumns; column++) {
                    put(imageStore, (double) ((data[count]) & 0xff) / 255, row, column, image);
                    count++;
                }
            }
        }

        dataInputStream = new DataInputStream(new FileInputStream(imageSet.getLabelLocation()));
        dataInputStream.readInt();
        int numLabels = dataInputStream.readInt();
        data = new byte[numLabels];
        flags = dataInputStream.read(data, 0, numLabels);
        if (flags != 0) {
            dataInputStream.close();
            throw new IOException();
        }
        dataInputStream.close();
        double[] labelStore = new double[wantedImageIndices.length];
        for (int i = 0; i < labelStore.length; i++) {
            labelStore[i] = (data[wantedImageIndices[i]]) & 0xff;
        }

        return new FileTuple(imageStore, labelStore);
    }
}
