package services;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;

import fundamentals.MultiD;

/**
 * This class exists to extract the data from the MNIST database.
 * @author Brannan
 *
 */
public class ExtractService {
	
	/**
	 * This method uses all 4 files from MNIST and extracts the information from them.
	 * The images are stored in two MDAs, the labels are stored in two arrays.
	 * @return
	 * @throws IOException
	 */
	public static FileTuple extract() throws IOException {
		
		DataInputStream dataInputStream = new DataInputStream(new FileInputStream("./lib/train-images.idx3-ubyte"));
		dataInputStream.readInt();
		int numImages = dataInputStream.readInt();
		int numRows = dataInputStream.readInt();
		int numColumns = dataInputStream.readInt();
		byte[] data = new byte[numImages*numRows*numColumns];
		dataInputStream.read(data, 0, numImages*numRows*numColumns);
		dataInputStream.close();
		MultiD imageStore = new MultiD(numRows, numColumns, numImages); 
		int count = 0;
		for(int image = 0; image < numImages; image++) {
			for(int row = 0; row < numRows; row++) {
				for(int column = 0; column < numColumns; column++) {
					imageStore.put( (double) ( ( (data[count]) &0xff) )/255 , row, column, image);
					count++;
				}
			}
		}
		
		FileTuple fileTuple = new FileTuple();
		fileTuple.trainingImages = imageStore;
		
		dataInputStream = new DataInputStream(new FileInputStream("./lib/t10k-images.idx3-ubyte"));
		dataInputStream.readInt();
		numImages = dataInputStream.readInt();
		numRows = dataInputStream.readInt();
		numColumns = dataInputStream.readInt();
		data = new byte[numImages*numRows*numColumns];
		dataInputStream.read(data, 0, numImages*numRows*numColumns);
		dataInputStream.close();
		imageStore = new MultiD(numRows, numColumns, numImages); 
		count = 0;
		for(int image = 0; image < numImages; image++) {
			for(int row = 0; row < numRows; row++) {
				for(int column = 0; column < numColumns; column++) {
					imageStore.put( (double) ( ( (data[count]) &0xff) )/255 , row, column, image);
					count++;
				}
			}
		}
		
		fileTuple.testingImages = imageStore;
		
		dataInputStream = new DataInputStream(new FileInputStream("./lib/train-labels.idx1-ubyte"));
		dataInputStream.readInt();
		int numLabels = dataInputStream.readInt();
		data = new byte[numLabels];
		dataInputStream.read(data, 0, numLabels);
		dataInputStream.close();
		double[] labelStore = new double[data.length];
		for(int i = 0; i < data.length; i++) {
			labelStore[i] = (double) ( (data[i]) &0xff);
		}
	
		fileTuple.trainingLabels = labelStore;
		
		dataInputStream = new DataInputStream(new FileInputStream("./lib/t10k-labels.idx1-ubyte"));
		dataInputStream.readInt();
		numLabels = dataInputStream.readInt();
		data = new byte[numLabels];
		dataInputStream.read(data, 0, numLabels);
		dataInputStream.close();
		labelStore = new double[data.length];
		for(int i = 0; i < data.length; i++) {
			labelStore[i] = (double) ( (data[i]) &0xff);
		}
	
		fileTuple.testingLabels = labelStore;
		return fileTuple;

	}

}

/**
 * This file tuple is made as the return type of the extract. It simply bundles
 * the MDAs and arrays together.
 * @author Brannan
 *
 */
class FileTuple {
	MultiD trainingImages;
	MultiD testingImages;
	double[] trainingLabels;
	double[] testingLabels;
}

