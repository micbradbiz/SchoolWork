import java.util.Scanner;

public class project1 {
	static Scanner sc = new Scanner(System.in);
	
	public static void main(String[] args) {
		//51 29 68 90 36 40 22 59 44 99 77 60 27 83 15 75 3
		//Program initialization
		mbradBST tree = new mbradBST();
		userGetInitialTreeData(tree);
		printHelp();
		String[] input = {""};
		
		//Main program loop
		while(!input[0].equalsIgnoreCase("E")) {
			input = getCommand();
			switch(input[0].toUpperCase()) {
			case "I":
				if(input.length <= 1) {
					System.out.println("Command must be followed with a number. Ex: I 45");
				} else if(!isNumeric(input[1])) {
					System.out.println(input[1] + " is not a number, Ex: I 45");
				} else {
					tree.addNode(Integer.parseInt(input[1]));
					printInOrder(tree);
				}
				break;
			case "D":
				if(input.length <= 1) {
					System.out.println("Command must be followed with a number. Ex: D 45");
				} else if(!isNumeric(input[1])) {
					System.out.println(input[1] + " is not a number, Ex: D 45");
				} else {
					tree.deleteNode(Integer.parseInt(input[1]));
					printInOrder(tree);
				}
				break;
			case "P":
				if(input.length <= 1) {
					System.out.println("Command must be followed with a number. Ex: P 45");
				} else if(!isNumeric(input[1])) {
					System.out.println(input[1] + " is not a number, Ex: P 45");
				} else {
					printPredecessor(tree, Integer.parseInt(input[1]));
				}
				break;
			case "S":
				if(input.length <= 1) {
					System.out.println("Command must be followed with a number. Ex: S 45");
				} else if(!isNumeric(input[1])) {
					System.out.println(input[1] + " is not a number, Ex: S 45");
				} else {
					printSuccessor(tree, Integer.parseInt(input[1]));
				}
				break;
			case "R":
				printInOrder(tree);
				break;
			case "U":
				printPreOrder(tree);
				break;
			case "Y":
				printPostOrder(tree);
				break;
			case "L":
				printLevelOrder(tree);
				break;
			case "H":
				printHelp();
				break;
			default:
				break;
			}
		}
		
		System.out.println("Thank you for using my program!");
		
	}
	
	//Function to check that a string value is numeric
	private static boolean isNumeric(String string) {
		if(string == null)
			return false;
		char[] temp = string.toCharArray();
		if(temp.length == 0)
			return false;
		int i = 0;
		if(temp[0] == '-' && temp.length > 1)
			i = 1;
		for(; i < temp.length; i++) {
			if(!Character.isDigit(temp[i]))
				return false;
		}
		return true;
	}
	
	//Utility function to get User input for next command
	private static String[] getCommand() {
		System.out.print("Command? ");
		String[] tmp = sc.nextLine().split(" ");
		return tmp;
	}

	//Utility function to get Initial sequence of values for tree
	private static void userGetInitialTreeData(mbradBST tree){
		System.out.println("Please enter initial sequence of values:");
		String input = sc.nextLine();
		String[] tmp = input.split(" ");
		for(int i = 0; i < tmp.length; i++) {
			if(isNumeric(tmp[i]))
				tree.addNode(Integer.parseInt(tmp[i]));
			else
				System.out.println(tmp[i] + " is not an int, disregarding.");
		}
		printPreOrder(tree);
		printInOrder(tree);
		printPostOrder(tree);
		printLevelOrder(tree);
	}
	
	//Utility function to print the help menu
	private static void printHelp() {
		System.out.println("\tI Insert a value\n\tD Delete a value"
				+ "\n\tP Find predecessor\n\tS Find Successor\n\t"
				+ "R inOrder Traversal\n\tU preOrder Traversal\n\t"
				+ "Y postOrder Traversal\n\tL levelOrder Traversal\n\t"
				+ "E Exit the program\n\tH Display this message");
	}
	
	//Utility function to print the predecessor, formatted 
	private static void printPredecessor(mbradBST tree, int data) {
		System.out.println("Predecessor to " + data + ": " + tree.getPredecessor(data));
	}
	
	//Utility function to print the successor, formatted
	private static void printSuccessor(mbradBST tree, int data) {
		System.out.println("Successor to " + data + ": " + tree.getSuccessor(data));
	}
	
	//Utility function to print In order Traversal, formatted
	private static void printInOrder(mbradBST tree) {
		System.out.print("inOrder: ");
		tree.inOrder(tree.root);
		System.out.println();
	}
	
	//Utility function to print pre order Traversal, formatted
	private static void printPreOrder(mbradBST tree) {
		System.out.print("preOrder: ");
		tree.preOrder(tree.root);
		System.out.println();
	}
	
	//Utility function to print post order Traversal, formatted
	private static void printPostOrder(mbradBST tree) {
		System.out.print("postOrder: ");
		tree.postOrder(tree.root);
		System.out.println();
	}
	
	//Utility function to print level order Traversal, formatted
	private static void printLevelOrder(mbradBST tree) {
		System.out.print("levelOrder: ");
		tree.levelOrder(tree.root);
		System.out.println();
	}

}
