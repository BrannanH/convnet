package services;

import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import fundamentals.MultiD;

/**
 * This class allows for displaying the contents of an MDA.
 * It must be passed something that has either two (grayscale image) or three(RGB image) dimensions (grayscale image)
 * @author Brannan
 *
 */
public class DisplayService {
	
	static public void display(MultiD... imageStores) {
		
		for(MultiD imageStore: imageStores) {
			if(imageStore.getDimensions().length != 2 && imageStore.getDimensions().length != 3) {
				throw(new IllegalArgumentException("Either a 2D or 3D MDA must be passed to display"));
			}
			if(imageStore.getDimensions().length == 3 && imageStore.getDimensions()[2] != 3) {
				throw(new IllegalArgumentException("A 3D MDA passed to display must only have 3 colour channels"));
			}
		}
		
		BufferedImage[] images = new BufferedImage[imageStores.length];
		
		for(int i = 0; i < imageStores.length; i++) {
		MultiD imageStore = imageStores[i];
		int numRows = imageStore.getDimensions()[0];
		int numColumns = imageStore.getDimensions()[1];
		images[i] = new BufferedImage(numRows, numColumns, BufferedImage.TYPE_3BYTE_BGR);
		
			for(int row = 0; row < numRows; row++) {
			  
				for(int column = 0; column < numColumns; column++) {
					int red;
					int green;
					int blue;
					
					if(imageStore.getDimensions().length == 2) {
					  
						double doublevalue = imageStore.get(row, column);
						red = (int) (doublevalue*255);
						green = (int) (doublevalue*255);
						blue = (int) (doublevalue*255);
					  
					} else {
					  
						red = (int) (imageStore.get(row, column, 0)*255);
						green = (int) (imageStore.get(row, column, 1)*255);
						blue = (int) (imageStore.get(row, column, 2)*255);
					  
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
