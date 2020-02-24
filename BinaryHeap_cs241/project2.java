import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;

public class project2 {
	private static Scanner scan = new Scanner(System.in);
	
	public static void main(String[] args) {
		
		String prompt = "Please select how to test the program:\n"
				+ "(1) 20 sets of 100 randomly generated integers\n"
				+ "(2) Fixed integer values 1-100\n"
				+ "Enter choice: ";
		int input = getChoice(prompt, 1, 2);
		System.out.println();
		mbradHeap heap;
		
		if(input == 1) {
			int[] arrRand;
			int[] swaps = new int[20];
			int swapsAvg = 0;
			int[] swapsOpt = new int[20];
			int swapsOptAvg = 0;
			
			for(int i = 0; i < 20; i++) {
				arrRand = createArrofInts(100, true);
				heap = new mbradHeap(arrRand.length);
				for(int j = 0; j < 100; j++) {
					heap.insert(arrRand[j]);
				}
				swaps[i] = heap.getSwapCount();
				swapsAvg += swaps[i];
				heap.resetSwapCount();
				heap.clear();
			}
			swapsAvg /= 20; //SwapsAvg currently has total divide by 20 for avg
			
			for(int i = 0; i < 20; i++) {
				arrRand = createArrofInts(100, true);
				heap = new mbradHeap(arrRand.length, arrRand);
				swapsOpt[i] = heap.getSwapCount();
				swapsOptAvg += swapsOpt[i];
				heap.resetSwapCount();
				heap.clear();
			}
			swapsOptAvg /= 20; //SwapsOptAvg currently has total divide by 20 for avg
			
			System.out.println("Average swaps for series of insertions " + swapsAvg);
			System.out.println("Average swaps for optimal method " + swapsOptAvg);			
		}else {
			int[] arrSeq = createArrofInts(100, false);
			heap = new mbradHeap(arrSeq.length);
			for(int j = 0; j < 100; j++) {
				heap.insert(arrSeq[j]);
			}
			
			System.out.print("Heap built using series of insertions: ");
			heap.printFirstTen();
			
			System.out.println("\nNumber of swaps: " + heap.getSwapCount());
			
			for(int i = 0; i < 10; i++) {
				heap.removeRoot();
			}
			System.out.print("Heap after 10 removals: ");
			heap.printFirstTen();
			System.out.println("\n");
		
			heap.resetSwapCount();
			heap.clear();
			
			heap = new mbradHeap(arrSeq.length, arrSeq);
			System.out.print("Heap built using series of insertions: ");
			heap.printFirstTen();
			System.out.println("\nNumber of swaps: " + heap.getSwapCount());
			for(int i = 0; i < 10; i++) {
				heap.removeRoot();
			}
			System.out.print("Heap after 10 removals: ");
			heap.printFirstTen();
			System.out.println();
			
			heap.resetSwapCount();
			heap.clear();
			
		}
		
		

	}
	
	//Utility function to create an array ints in either sequence from 1 to Size or Size Random unique ints. 
	private static int[] createArrofInts(int size, boolean random) {
		int[] tmp = new int[size];
		if(random) {
			//generate random ints fill tmp
			Random rand = new Random();
			tmp = rand.ints(1, 1001).distinct().limit(size).toArray(); //Range 1-1000 inclusive
		}else {
			//generate sequential ints from 1 to size 
			for(int i = 0; i < size; i++) {
				tmp[i] = i + 1; // Range 1-Size inclusive
			}
		}
		return tmp;
	}
	
	//Give prompt ask user for an integer choice in between low and high inclusive
	private static int getChoice(String prompt, int low, int high) {
		int choice = Integer.MIN_VALUE;
		boolean clean = false;
		while(!clean) {
			System.out.print(prompt);
			clean = true;
			try {
				choice = scan.nextInt();
			}catch(InputMismatchException e) {
				System.out.println("Choice must be an integer number between "
									+ low + " and " + high);
				clean = false;
				scan.next(); // Get rid of rest of line
			}
			//clean checks if problem was already found with inputMismatch
			if((choice > high || choice < low) && clean) {
				System.out.println("Choice must be an integer number between "
									+ low + " and " + high);
				clean = false;
			}
		}
		return choice;
	}
}
