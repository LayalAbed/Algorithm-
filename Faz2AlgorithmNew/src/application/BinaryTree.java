package application;



public class BinaryTree  {
	private char character;
	private String huffCode;
	private BinaryTree left;
	private BinaryTree right;

	public BinaryTree() {
		character = '\0'; // '\0' represents the null character
		huffCode = "";
		left = right = null;

	}

	public BinaryTree(char ch, String huffCode) {
		this.character = ch;
		this.huffCode = huffCode;
		left = right = null;
	}

	public static BinaryTree addToBinarryTree(BinaryTree tree, String st,int ind, char ch) {
		if (ind < st.length()) {

			if (st.charAt(ind) == '0') {
				if (tree.left == null)
					tree.left = new BinaryTree();
				tree.left = addToBinarryTree(tree.left, st, ind + 1, ch);
			} else {
				if (tree.right == null)
					tree.right = new BinaryTree();
				tree.right = addToBinarryTree(tree.right, st, ind + 1, ch);
			}
			return tree;
		} else {
			tree.character = ch;
			return tree;
		}
	}

	public char getCharacter() {
		return character;
	}

	public String getHuffCode() {
		return huffCode;
	}

	public BinaryTree getLeft() {
		return left;
	}

	public BinaryTree getRight() {
		return right;
	}

	public void setCharacter(char character) {
		this.character = character;
	}

	public void setHuffCode(String huffCode) {
		this.huffCode = huffCode;
	}

	public void setLeft(BinaryTree left) {
		this.left = left;
	}

	public void setRight(BinaryTree right) {
		this.right = right;
	}
	boolean isLeeaf() {
		return this.left == null && this.right ==null;
		
	}

	



}
