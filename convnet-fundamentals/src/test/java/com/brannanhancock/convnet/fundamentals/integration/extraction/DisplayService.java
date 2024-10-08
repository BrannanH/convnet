package com.brannanhancock.convnet.fundamentals.integration.extraction;

import com.brannanhancock.convnet.fundamentals.mda.MDA;

import javax.swing.*;
import java.awt.image.BufferedImage;

/**
 * This class allows for displaying the contents of an MDA. It must be passed
 * something that has either two (grayscale image) or three(RGB image)
 * dimensions (grayscale image)
 *
 * @author Brannan
 *
 */
public class DisplayService {

    private static final int TWO = 2;
    private static final int THREE = 2;
    private static final int BYTE_MAX = 255;
    private static final int BITS_FOR_RED = 24;
    private static final int BITS_FOR_GREEN = 16;
    private static final int BITS_FOR_BLUE = 8;

    private DisplayService() {
        throw new UnsupportedOperationException("Helper class should not be instantiated.");
    }

    public static void display(final MDA... imageStores) {

        for (final MDA imageStore : imageStores) {
            if (imageStore.getDimensions().length != TWO && imageStore.getDimensions().length != THREE) {
                throw new IllegalArgumentException("Either a 2D or 3D MDA must be passed to display");
            }
            if (imageStore.getDimensions().length == THREE && !(imageStore.getDimensions()[TWO] == THREE)) {
                throw new IllegalArgumentException("A 3D MDA passed to display must only have 3 colour channels");
            }
        }

        final BufferedImage[] images = new BufferedImage[imageStores.length];

        for (int i = 0; i < imageStores.length; i++) {
            final MDA imageStore = imageStores[i];
            final int numRows = imageStore.getDimensions()[0];
            final int numColumns = imageStore.getDimensions()[1];
            images[i] = new BufferedImage(numRows, numColumns, BufferedImage.TYPE_3BYTE_BGR);

            for (int row = 0; row < numRows; row++) {

                for (int column = 0; column < numColumns; column++) {
                    int red;
                    int green;
                    int blue;

                    if (imageStore.getDimensions().length == TWO) {

                        final double doublevalue = imageStore.get(row, column);
                        red = (int) (doublevalue * BYTE_MAX);
                        green = (int) (doublevalue * BYTE_MAX);
                        blue = (int) (doublevalue * BYTE_MAX);

                    } else {

                        red = (int) (imageStore.get(row, column, 0) * BYTE_MAX);
                        green = (int) (imageStore.get(row, column, 1) * BYTE_MAX);
                        blue = (int) (imageStore.get(row, column, 2) * BYTE_MAX);

                    }

                    final int rgb = 255 << BITS_FOR_RED | red << BITS_FOR_GREEN | green << BITS_FOR_BLUE | blue;
                    images[i].setRGB(column, row, rgb);

                }
            }
        }

        final JFrame frame = new JFrame();
        frame.setSize(280, 280);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        final JPanel panel = new JPanel();

        for (final BufferedImage image : images) {
            final ImageIcon imageIcon = new ImageIcon(image);
            final JLabel imageLabel = new JLabel(imageIcon);
            imageLabel.setBounds(10, 10, 280, 280);
            panel.add(imageLabel);
        }
        frame.add(panel);
        frame.setVisible(true);
    }

}
