package Reversi;
// The PieceList class provides a double linked list to keep track of the pieces that need to be flipped 
public class PieceList 
{	
	private PieceNode head;
	
	PieceList()
	{
		head = null;
	}
	
	public void add(int num, int x, int y)
	{
		PieceNode n = new PieceNode();
		PieceNode t = new PieceNode();
		
		if (this.isEmpty())
		{
			head = n;
		}
		else 
		{
			t = head;
			while(t.getRight() != null)	t = t.getRight();
			n.setLeft(t);
			t.setRight(n);
		}
		n.setValue(num);
		int [] tempXY = {x,y};
		n.setCoordinates(tempXY);
	}
	
	// Returns a value for a given index
	public int at(int index)
	{
		if (this.isEmpty()) return 0;
		if (index > (this.size() - 1)) return -1; // Out of bounds
		PieceNode n = new PieceNode();
		n = head;
		for (int i = 0; i < index; i++)
		{
			n = n.getRight();
		}
		return n.getValue();
	}
	
	// Returns a value and gives the array by reference for a given index
	public int at(int index, int [] xy)
	{
		if (this.isEmpty()) return 0;
		if (index > (this.size() - 1)) return -1; // Out of bounds
		PieceNode n = new PieceNode();
		n = head;
		for (int i = 0; i < index; i++)
		{
			n = n.getRight();
		}
		xy[0] = n.getCoordinates()[0];
		xy[1] = n.getCoordinates()[1];
		return n.getValue();
	}
	
	// Returns a node at a given index
	public PieceNode at(int index, PieceNode n)
	{
		// Consider using nested exceptions if used in a different context for a different project
		if (this.isEmpty()) 
		{	
			System.out.println("Error: Attempt to access an empty list");
			System.exit(0);
		}
		if (index > (this.size() - 1)) 
		{
			System.out.println("Error: Index out of bounds");
			System.exit(0);
		}
		n = head;
		for (int i = 0; i < index; i++)
		{
			n = n.getRight();
		}
		return n;
	}
	
	public int size()
	{
		if (this.isEmpty()) return 0;
		PieceNode n = new PieceNode();
		n = head;
		int i = 0;
		while (n != null)
		{
			n = n.getRight();
			i++; 
		}
		return i;
	}
	
	public boolean isEmpty()
	{
		if (head == null) return true;
		else return false;		
	}	
}
