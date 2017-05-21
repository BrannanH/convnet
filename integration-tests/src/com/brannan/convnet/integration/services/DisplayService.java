package com.brannan.convnet.integration.services;

import static com.brannan.convnet.network.fundamentals.MDAHelper.get;

import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.brannan.convnet.network.fundamentals.MDA;


/**
 * This class allows for displaying the contents of an MDA.
 * It must be passed something that has either two (grayscale image) or three(RGB image) dimensions (grayscale image)
 * @author Brannan
 *
 */
public class DisplayService {
	
	static public void display(MDA... imageStores) {
		
		for(MDA imageStore: imageStores) {
			if(imageStore.getDimensions().size() != 2 && imageStore.getDimensions().size() != 3) {
				throw(new IllegalArgumentException("Either a 2D or 3D MDA must be passed to display"));
			}
			if(imageStore.getDimensions().size() == 3 && !(imageStore.getDimensions().get(2) == 3)) {
				throw(new IllegalArgumentException("A 3D MDA passed to display must only have 3 colour channels"));
			}
		}
		
		BufferedImage[] images = new BufferedImage[imageStores.length];
		
		for(int i = 0; i < imageStores.length; i++) {
		MDA imageStore = imageStores[i];
		int numRows = imageStore.getDimensions().get(0);
		int numColumns = imageStore.getDimensions().get(1);
		images[i] = new BufferedImage(numRows, numColumns, BufferedImage.TYPE_3BYTE_BGR);
		
			for(int row = 0; row < numRows; row++) {
			  
				for(int column = 0; column < numColumns; column++) {
					int red;
					int green;
					int blue;
					
					if(imageStore.getDimensions().size() == 2) {
					  
						double doublevalue = get(imageStore, row, column);
						red = (int) (doublevalue*255);
						green = (int) (doublevalue*255);
						blue = (int) (doublevalue*255);
					  
					} else {
					  
						red = (int) (get(imageStore, row, column, 0)*255);
						green = (int) (get(imageStore, row, column, 1)*255);
						blue = (int) (get(imageStore, row, column, 2)*255);
					  
				  }
				  
				  int rgb = 255 << 24 | red << 16 | green << 8 | blue;
				  images[i].setRGB(column, row, rgb);
				  
				}
			}
		}
		  
		JFrame frame = new JFrame();
		frame.setSize(280, 280);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JPanel panel = new JPanel();
		
		for(BufferedImage image: images) {
			ImageIcon imageIcon = new ImageIcon(image);
			JLabel imageLabel = new JLabel(imageIcon);
			imageLabel.setBounds(10, 10, 280, 280);
			panel.add(imageLabel);
		}
		frame.add(panel);
		frame.setVisible(true);
	}

}
