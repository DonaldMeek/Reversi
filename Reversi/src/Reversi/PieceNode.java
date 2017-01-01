package Reversi;
// The PieceNode class is a node used by PieceList
public class PieceNode {

	private int value;
	private int[] coordinates; // Stores the x,y coordinates of a board piece.
	private PieceNode left;
	private PieceNode right;
	
	PieceNode()
	{
		left = null;
		value = 0;
		right = null;
		coordinates = new int[2];
		coordinates[0] = 500; // for debugging
		coordinates[1] = 500;
	}
	
	public void setValue(int value) {
		this.value = value;
	}

	public void setCoordinates(int[] coordinates) {
		this.coordinates = coordinates;
	}
	public void setLeft(PieceNode left) {
		this.left = left;
	}

	public void setRight(PieceNode right) {
		this.right = right;
	}
	
	public int getValue() {
		return value;
	}

	public int[] getCoordinates() {
		return coordinates;
	}

	public PieceNode getLeft() {
		return left;
	}

	public PieceNode getRight() {
		return right;
	}	
}
