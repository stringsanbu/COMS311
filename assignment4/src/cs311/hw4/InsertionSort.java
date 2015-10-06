package cs311.hw4;

import java.util.Comparator;

public class InsertionSort<T> implements ISort<T> {

	@Override
	/**
	 * Abstract class for working with InsertionSort.
	 */
	public void sort(T[] arr, int start, int end, Comparator<T> comp) {
		// Some sanity checks to make sure people aren't messing around with us.
		if(arr == null || comp == null || arr.length <= 1 || start == end || end < start || start < 0 || end > arr.length-1) return;
		// OK, it appears the inputs are good. Let's begin.	

		// This is a temporary array just in case an error is found, we will set the array back to what it was.
		// It adds to the memory requirements, but is better for an all-encompassing algorithm.
		T[] temporaryArr = arr.clone();
		
		// Simple insertion sort loop. Best case is O(n), average and worse is O(n^2)
		// Outside loop handles initial settings of variables.
		for(int i = start+1; i <= end; i++){
			T t = arr[i];
			// The array shouldn't have null values, as it could cause issues with the comparator.
			if(t == null){
				arr = temporaryArr;
				return;
			}
			int j = i-1;
			// Internal loop for handling actual insertion methods.
			while(j >= start && comp.compare(arr[j], t) == 1){
				// The array shouldn't have null values, as it could cause issues with the comparator.
				if(arr[j+1] == null || arr[j] == null){
					arr = temporaryArr;
					return;
				}
				arr[j+1] = arr[j];
				j--;
			}
			arr[j+1] = t;
		}
	}
}
