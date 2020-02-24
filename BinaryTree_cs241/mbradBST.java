

class mbradBST {
	
	class Node{
		int data;
		Node lChild, rChild;
		
		public Node(int data) {
			this.data = data;
			lChild = null;
			rChild = null;
		}
	}
	
	Node root;
	
	//Constructor for an empty tree
	mbradBST(){
		root = null;
	}
	
	//Constructor with a single data value for root
	mbradBST(int rootData){
		addNode(rootData);
	}	
	
	//Search for a value in the tree
	public Node Search(Node subRoot, int valueToSearchFor) {
		if(subRoot == null || subRoot.data == valueToSearchFor) {
			return subRoot;
		}else if(valueToSearchFor > subRoot.data) {
			return Search(subRoot.rChild, valueToSearchFor);
		}
		return Search(subRoot.lChild, valueToSearchFor);			
	}
	
	//InOrder Traversal
	public void inOrder(Node subRoot){
		if(subRoot != null) {
			inOrder(subRoot.lChild);
			System.out.print(subRoot.data + " ");
			inOrder(subRoot.rChild);
		}
	}

	//PreOrder Traversal
	public void preOrder(Node subRoot){
		if(subRoot != null) {
			System.out.print(subRoot.data + " ");
			preOrder(subRoot.lChild);
			preOrder(subRoot.rChild);
		}
	}
	
	//PostOrder Traversal
	public void postOrder(Node subRoot){
		if(subRoot != null) {
			postOrder(subRoot.lChild);
			postOrder(subRoot.rChild);
			System.out.print(subRoot.data + " ");
			
		}
	}
	
	//LevelOrder Traversal
	public void levelOrder(Node subRoot){
		int height = getHeight(subRoot);
		String tmp = "";
		for (int i = 1; i <= height; i++) {
			tmp = tmp + singleLevelOrder(subRoot, i);
		}
		System.out.print(tmp);
	}
	
	//Returns the string of a single level of Level Order
	private String singleLevelOrder(Node subRoot, int curLevel) {
		//if Leaf return null string
		if(subRoot == null) {
			return "";
		}
		//if root return data of node
		if(curLevel == 1) {
			return (subRoot.data + " ");
		}
		//if above root get roots left child and right child data and return them
		else if(curLevel > 1) {
			return (singleLevelOrder(subRoot.lChild, curLevel - 1) + singleLevelOrder(subRoot.rChild, curLevel - 1));
		}
		return "";					
	}
	
	//Function to get the height of a subtree
	public int getHeight(Node subRoot){
		//If leaf return 0
		if (subRoot == null)
			return 0;
		
		//Get Height of Children
		int lChildHeight = getHeight(subRoot.lChild);
		int rChildHeight = getHeight(subRoot.rChild);
		
		//Return the height of longest child branch + 1 for the root node level of this subTree
		if(lChildHeight > rChildHeight)
			return (lChildHeight + 1);
		else 
			return (rChildHeight + 1);
	}
	
	//Remove data from root of whole tree
	public void deleteNode(int dataToDelete) {
		root = deleteNode(root, dataToDelete);
	}
	
	public Node deleteNode(Node subRoot, int dataToDelete) {
		//If SubTree empty
		if(subRoot == null) {
			return subRoot;
		}
		
		if(dataToDelete > subRoot.data) {
			subRoot.rChild = deleteNode(subRoot.rChild, dataToDelete);
		} else if(dataToDelete < subRoot.data) {
			subRoot.lChild = deleteNode(subRoot.lChild, dataToDelete);
		}
		
		//Found dataToDelete in node, delete node
		//Case1: Leaf just delete Case2: one child 
		//Case3: two children keep leftmost in right subtree
		else {
			if(subRoot.lChild == null) {
				//One child, right (or no children)
				return subRoot.rChild;
			}else if(subRoot.rChild == null) {
				return subRoot.lChild;
			}
			
			//Two Children get leftmost child in right subtree assign to subRoot
			subRoot.data = (getMin(subRoot.rChild)).data;
			//Recurse and delete leftmost child in right subtree
			subRoot.rChild = deleteNode(subRoot.rChild, subRoot.data);
			
		}
		
		return subRoot;		
	}
	
	//Function to get Minimum node of a subtree
	public Node getMin(Node subRoot) {
		if(subRoot.lChild == null) {
			return subRoot;
		}else {
			return getMin(subRoot.lChild);
		}
	}
	
	//Function to get Maximum node of a subtree
	public Node getMax(Node subRoot) {
		if(subRoot.rChild == null) {
			return subRoot;
		}else {
			return getMax(subRoot.rChild);
		}
	}
	
	//Utility Function to get Successor by data
	public int getSuccessor(int data) {
		return (getSuccessor(Search(root, data)).data);
	}
	
	//Function to get the InOrder Successor of a node
	public Node getSuccessor(Node subRoot) {
		if(subRoot.rChild == null) {
			//Find nearest parent who is a left child and get that parent
			Node Curr; 
			Node Successor = null;
			Curr = root;
			while (Curr != subRoot) {
				if(Curr.data > subRoot.data) {
					Successor = Curr;
					Curr = Curr.lChild;
				}else {
					Curr = Curr.rChild;
				}
			}
			
			return Successor;
			
		}else {
			return getMin(subRoot.rChild);
		}
	}
	
	//Utility Function to get Predecessor by data
	public int getPredecessor(int data) {
		return (getPredecessor(Search(root, data)).data);
	}
	
	//Function to get the InOrder Predecessor of a node
	public Node getPredecessor(Node subRoot) {
		if(subRoot.lChild == null) {
			//Find nearest parent who is a right child and get that parent
			Node Curr; 
			Node Predecessor = null;
			Curr = root;
			while (Curr != subRoot) {
				if(Curr.data > subRoot.data) {
					Curr = Curr.lChild;
				}else {
					Predecessor = Curr;
					Curr = Curr.rChild;
				}
			}
			
			return Predecessor;			
			
		}else {
			return getMax(subRoot.lChild);
		}
	}
	
	//Add data to root of whole tree
	public void addNode(int dataToAdd) {
		root = addNode(root, dataToAdd);
	}
	
	/* Recursive function for adding data to tree from subTree rooted at node subRoot
	 * will return that subtree.
	 * Should add data at leaf of subtree or if data is redundant will ignore data and
	 * return the subtree.
	*/
	private Node addNode(Node subRoot, int dataToAdd) {
		
		//Bottom of Subtree (or empty tree) add data to subRoot and return root ref
		if(subRoot == null) {
			subRoot = new Node(dataToAdd);
			return subRoot;
		}
		
		//Go down left or right subtree till you find a leaf to add to
		if(dataToAdd < subRoot.data) {
			subRoot.lChild = addNode(subRoot.lChild, dataToAdd);
		}
		else if(dataToAdd > subRoot.data) {
			subRoot.rChild = addNode(subRoot.rChild, dataToAdd);
		}
		
		//If dataToAdd already exists in tree don't add anything to tree and return that tree.
		return subRoot;
	}
}
