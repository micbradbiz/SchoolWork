import java.util.Arrays;

class mbradHeap {
	
	private int[] heapArr;
	private int lastIndex;
	private int swapCount = 0;
	static final int defaultCap = 25;
	
	
	public mbradHeap() {
		this(defaultCap);
		lastIndex = 0;
	}
	
	//Our heap starts at Index 1 so its size should be the desired capacity + 1
	public mbradHeap(int capacity) {
		heapArr = new int[capacity + 1];
		lastIndex = 0 ;
	}
	
	/*Optimal method for creating a heap via an unordered array entries
	 *Capacity can be larger than the size of entries but if it's smaller the array will be 
	 *the size of entries + 1. 
	 */
	public mbradHeap(int capacity, int[] entries) {
		if(capacity >= entries.length)
		{
			heapArr = new int[capacity + 1];
		}else {
			heapArr = new int[entries.length + 1];
		}
		System.arraycopy(entries, 0, heapArr, 1, entries.length);
		lastIndex = entries.length;
		int lastNonLeaf = lastIndex / 2; //Truncation of ints = flooring
		while(lastNonLeaf > 0) {
			downHeapRecursive(lastNonLeaf);
			lastNonLeaf--;
		}
	}
	
	public int getSwapCount() {
		return swapCount;
	}
	
	public void resetSwapCount() {
		swapCount = 0;
	}
	
	public boolean isEmpty() {
		return (lastIndex < 1);
	}
	
	public int getSize() {
		return lastIndex;
	}
	
	//Erase set values to 0 and reset Last Index
	public void clear() {
		while(lastIndex > -1) {
			heapArr[lastIndex] = 0;
			lastIndex--;
		}
		lastIndex = 0;
	}
	
	//Insert data at leaf then perform upHeap from that leaf.
	public void insert(int newData) {
		lastIndex++;
		checkCap();
		int nIndex = lastIndex;
		heapArr[nIndex] = newData;
		upHeap(nIndex);
	}
	
	//Print complete underlying array and Last Index
	public void print() {
		System.out.println(Arrays.toString(heapArr) + " --LastIndex " + lastIndex );
	}
	
	//Print first ten values of the heap itself
	public void printFirstTen() {
		for(int i = 1; i < 11; i++) { // starts at Index 1
			System.out.print(heapArr[i] + ", ");
		}
		System.out.print("...");
	}
	
	//Remove the root from the heap using downheap
	public int removeRoot() {
		if(lastIndex == 1) {
			lastIndex = 0;
			return heapArr[1];			
		}else if(lastIndex < 1){
			//Empty return abnormal number
			return Integer.MIN_VALUE;
		}
		
		int root = heapArr[1];
		heapArr[1] = heapArr[lastIndex];
		lastIndex--;
		downHeapRecursive(1);
		return root;
	}
	
	//Private
	
	//Utility function to check the capacity of the underlying array
	private void checkCap() {
		if(lastIndex >= heapArr.length) {
			heapArr = Arrays.copyOf(heapArr, (2 * heapArr.length));
		}
	}
	
	//upHeap operation from subRoot index
	private void upHeap(int subRoot) {
		int pIndex = getParentInd(subRoot);
		while(heapArr[subRoot] > heapArr[pIndex] && pIndex != 0) {
			swap(pIndex, subRoot);
			subRoot = pIndex;
			pIndex = getParentInd(subRoot);
		}
	}
	
	//Iterative downHeap operation from subRoot index
	private void downHeap(int subRoot) {
		int holdOnValue = heapArr[subRoot];
		boolean Done = false;
		int lChild = getLeftInd(subRoot);
		int rChild = getRightInd(subRoot);
		while(lChild <= lastIndex && !Done) {
			int larger = lChild; //Assume left
			if(rChild <= lastIndex && (heapArr[rChild] > heapArr[larger])) {
				larger = rChild; //bad assumption
			}
			
			if(holdOnValue < heapArr[larger]) {
				heapArr[subRoot] = heapArr[larger];
				swapCount++;
				subRoot = larger;
				lChild = getLeftInd(subRoot);
				rChild = getRightInd(subRoot);
			}else {
				Done = true;
			}
			
			heapArr[subRoot] = holdOnValue;
		}
	}
	
	//Recursive downHeap operation from subRoot index
	private void downHeapRecursive(int subRoot) {
		int largest = subRoot;
		int lChild = getLeftInd(subRoot);
		int rChild = getRightInd(subRoot);
		
		if(lChild <= lastIndex && (heapArr[lChild] > heapArr[largest])) {
			largest = lChild;
		}
		if(rChild <= lastIndex && (heapArr[rChild] > heapArr[largest])) {
			largest = rChild;
		}
		
		if(largest != subRoot) {
			swap(subRoot, largest);
			downHeapRecursive(largest);
		}
				
	}
	
	//Utility function to swap an item from swapFromIndex to swapToIndex, will increment swapCount
	private void swap(int swapToIndex, int swapFromIndex) {
		int temp = heapArr[swapToIndex];
		heapArr[swapToIndex] = heapArr[swapFromIndex];
		heapArr[swapFromIndex] = temp;
		swapCount++;
	}
	
	//Utility function to get Left Child Index 
	private int getLeftInd(int nodeIndex) {
		return (nodeIndex * 2);
	}
	
	//Utility function to get Right Child Index 
	private int getRightInd(int nodeIndex) {
		return (nodeIndex * 2 + 1);
	}
	
	//Utility function to get Parent Index
	private int getParentInd(int nodeIndex) {
		return (nodeIndex / 2);
	}
}
