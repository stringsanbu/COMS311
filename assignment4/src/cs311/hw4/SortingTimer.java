package cs311.hw4;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.concurrent.ThreadLocalRandom;


public class SortingTimer {
	
	private static final int ITERATIONS = 1000;
	private static int bestCaseSize = 0;
	private static int worseCaseSize = 0;
	private static int averageCaseSize = 0;
	
	public static void main(String[] args) throws IOException {
		IntComparator comp = new IntComparator();
		InsertionSort<Integer> sorter = new InsertionSort<Integer>();
		ArrayList<String[]> timeArray = new ArrayList<String[]>();
		
		/*
		 * Best case
		 */
		for(int i = 0; i < ITERATIONS; i++){
			Integer[] arr = bestCaseGenerator();
			String[] infoArr = new String[4];
			// infoArr[4] = Arrays.toString(arr);
			long startTime = System.currentTimeMillis();
			sorter.sort(arr, 0, arr.length-1, comp);
			long endTime = System.currentTimeMillis();
			infoArr[0] = Long.toString(startTime);
			infoArr[1] = Long.toString(endTime);
			infoArr[2] = "BestCase";
			infoArr[3] = Integer.toString(arr.length);
			// infoArr[5] = Arrays.toString(arr);
			
			timeArray.add(infoArr);
		}
		
		/*
		 * Best case
		 */
		for(int i = 0; i < ITERATIONS; i++){
			Integer[] arr = worseCaseGenerator();
			String[] infoArr = new String[4];
			//infoArr[4] = Arrays.toString(arr);
			long startTime = System.currentTimeMillis();
			sorter.sort(arr, 0, arr.length-1, comp);
			long endTime = System.currentTimeMillis();
			infoArr[0] = Long.toString(startTime);
			infoArr[1] = Long.toString(endTime);
			infoArr[2] = "WorstCase";
			infoArr[3] = Integer.toString(arr.length);
			//infoArr[5] = Arrays.toString(arr);
			
			timeArray.add(infoArr);
		}
		
		/*
		 * Best case
		 */
		for(int i = 0; i < ITERATIONS; i++){
			Integer[] arr = averageCaseGenerator();
			String[] infoArr = new String[4];
			//infoArr[4] = Arrays.toString(arr);
			long startTime = System.currentTimeMillis();
			sorter.sort(arr, 0, arr.length-1, comp);
			long endTime = System.currentTimeMillis();
			infoArr[0] = Long.toString(startTime);
			infoArr[1] = Long.toString(endTime);
			infoArr[2] = "averageCase";
			infoArr[3] = Integer.toString(arr.length);
			//infoArr[5] = Arrays.toString(arr);
			
			timeArray.add(infoArr);
		}
		
		FileWriter writer = new FileWriter("output.txt"); 
		for(String[] str: timeArray) {
			for(String string: str) {
				writer.write(string+"%");
			}
			writer.write(System.getProperty( "line.separator" ));
		}
		writer.close();		
	}
	
	/*
	 * A helper class used to generate an array of random integers.
	 * This puts all of the items in the in an already sorted order.
	 */
	private static Integer[] bestCaseGenerator(){
		/*
		 * We'll constant fold this to make things easy.
		 */
		ThreadLocalRandom random = ThreadLocalRandom.current();
		int min = 0;
		int max = 10000;
		int size = 50000;
		Integer[] intArr = new Integer[size];
		for(int i = 0; i < size; i++){
			int currentInt = random.nextInt(min, max+1);
			min = currentInt+1;
			max=max+2;
			intArr[i] = new Integer(currentInt);
		}
		bestCaseSize++;
		return intArr;
	}
	
	/*
	 * A helper class used to generate an array of random integers.
	 * This puts all of the items in the in an backwards sorted order.
	 */
	private static Integer[] worseCaseGenerator(){
		/*
		 * We'll constant fold this to make things easy.
		 */
		ThreadLocalRandom random = ThreadLocalRandom.current();
		int min = 0;
		int max = 10000;
		int size = 50000;
		Integer[] intArr = new Integer[size];
		for(int i = 0; i < size; i++){
			int currentInt = random.nextInt(min, max);
			max = currentInt-1;
			min = min-2;
			intArr[i] = new Integer(currentInt);
		}
		worseCaseSize++;
		return intArr;
	}

	
	private static Integer[] averageCaseGenerator(){
		/*
		 * We'll constant fold this to make things easy.
		 */
		ThreadLocalRandom random = ThreadLocalRandom.current();
		int min = 0;
		int max = 10000;
		int size = 50000;
		Integer[] intArr = new Integer[size];
		for(int i = 0; i < size; i++){
			int currentInt = random.nextInt(min, max);
			intArr[i] = new Integer(currentInt);
		}
		averageCaseSize++;
		return intArr;
	}
	
}

/**
 * A comparator used to compare 2 integers. 
 * @author Mason
 *
 */
class IntComparator implements Comparator<Integer> {
	    @Override
	    public int compare(Integer i1, Integer i2) {
	        return i1 > i2 ? 1 : i1 < i2 ? -1 : 0;
	    }
}