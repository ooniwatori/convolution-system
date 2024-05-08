package application;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.ArrayList;

public class SignalTransform {
	/* input signal and transformed signal.*/
	private ArrayList<ArrayList<Double>> inpSignal = new ArrayList<ArrayList<Double>>();
	private ArrayList<ArrayList<Double>> outpSignal = new ArrayList<ArrayList<Double>>();
	/* file to read*/
	private File file;
	/* size of input signal and transform signal*/
	private int size = 0;
	private int sizeOutp = 0;
	/* input signal and transformed signal.*/
	private double[][] inputSignal;
	private double[][] outputSignal;
	/* random number*/
	private SecureRandom randomNum = new SecureRandom();
	public SignalTransform() {	
	}
	/* generate input signal randomly.*/
	public SignalTransform(double amp, int leftLimit, int rightLimit) {
		size = rightLimit - leftLimit + 1;
		inputSignal = new double[size][2];
		int index = leftLimit;
		for(int i = 0; i < size; ++i, ++index) {
			inputSignal[i][0] = index;
			inputSignal[i][1] = amp * ((double)randomNum.nextInt(1001) / 1000.0);
		}
	}
	/* generate sin, cos wave input signal*/
	public SignalTransform(String type, double amp, int leftLimit, int rightLimit) {
		size = rightLimit - leftLimit + 1;
		inputSignal = new double[size][2];
		int index = leftLimit;
		switch(type) {
		case "Sin wave":
			for(int i = 0; i < size; ++i, ++index) {
				inputSignal[i][0] = index;
				inputSignal[i][1] = amp * Math.sin(index);
			}
			break;
		case "Cos wave":
			for(int i = 0; i < size; ++i, ++index) {
				inputSignal[i][0] = index;
				inputSignal[i][1] = amp * Math.cos(index);
			}
			break;
		}
	}
	/* get input signal from csv file.*/
	public SignalTransform(String filePath) throws IOException{
		/* get file path*/
		file = new File(filePath);
		BufferedReader br = null;
		br = new BufferedReader(new FileReader(file));
		/* temporary*/
		String line;
		String[] data;
		
		/* read line of file*/
		while ((line = br.readLine()) != null) {
			/* split each data then store*/
			data = line.split(",");
			ArrayList<Double> temp = new ArrayList<>();
			temp.add(Double.valueOf(data[0]));
			temp.add(Double.valueOf(data[1]));
			inpSignal.add(temp);
			size++;
		}	
		/* Close buffered reader*/
		try {
			br.close();
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
		}
		inputSignal = new double[size][2];
		for(int i = 0; i < size; ++i) {
			for(int j = 0; j < 2; ++j) {
				inputSignal[i][j] = inpSignal.get(i).get(j);
				
				
			}
		}
	}
	/* calculate size of transformed signal.*/
	public int sizeCalc(double[][] h, double[][] intputSignal) {
		return (int)(inputSignal[inputSignal.length - 1][0] - inputSignal[0][0] + h[h.length - 1][0] - h[0][0] + 1);
	}
	/* system of echo*/
	public void systemEcho(){
		double[][] h = {{0, 1}, {5, 0.3}, {7, 0.1}};
		sizeOutp = sizeCalc(h, inputSignal);
		outputSignal = new double[sizeOutp][2];
		
		convolution(h, inputSignal);
		storeSignal();
	}
	/* system of reduction*/
	public void systemReduction(){
		double[][] h = {{0, 0.5}};
		sizeOutp = sizeCalc(h, inputSignal);
		outputSignal = new double[sizeOutp][2];
		
		convolution(h, inputSignal);
		storeSignal();
	}
	/* system fo expansion*/
	public void systemExpansion(){
		double[][] h = {{0, 1.5}};
		sizeOutp = sizeCalc(h, inputSignal);
		outputSignal = new double[sizeOutp][2];
		
		convolution(h, inputSignal);
		storeSignal();
	}
	/* store transformed signal*/
	public void storeSignal() {
		for(int i = 0; i < sizeOutp; ++i) {
			ArrayList<Double> temp = new ArrayList<Double>();
			temp.add(outputSignal[i][0]);
			temp.add(outputSignal[i][1]);
			outpSignal.add(temp);
		}
	}
	/* convolution*/
	public void convolution(double[][] h, double[][] inputSignal) {
		/* index of transformed signal*/
		int n = (int)(inputSignal[0][0] + h[0][0]);
		for(int i = 0; i < sizeOutp; ++i, ++n) {
			outputSignal[i][0] = n;
			/* last index of system signal*/
			int k = (int)h[h.length - 1][0];
			/* system and input signal index*/
			int hIndex = h.length - 1;
			int sIndex = 0;
			for(int j = n - k; j < sizeOutp; ++j) {
				try {
					/* find next valid value.*/
					while(j > n - h[hIndex][0])
						hIndex--;
					while(j > inputSignal[sIndex][0])
						sIndex++;
					if(j == n - h[hIndex][0] && j == inputSignal[sIndex][0]) {
						outputSignal[i][1] += h[hIndex--][1] * inputSignal[sIndex++][1];
					}
					
				}
				catch(ArrayIndexOutOfBoundsException e) {
					/* any invalid result.*/
					break;
				}
				
			}
		}
	}
	/* store generated signal*/
	public ArrayList<ArrayList<Double>> getInpSignal(){
		for(int i = 0; i < size; ++i) {
			ArrayList<Double> temp = new ArrayList<>();
			temp.add(inputSignal[i][0]);
			temp.add(inputSignal[i][1]);
			inpSignal.add(temp);
		}
		return inpSignal;
	}
	/* get transformed signal*/
	public ArrayList<ArrayList<Double>> getOutpSignal(){
		return outpSignal;
	}
}
