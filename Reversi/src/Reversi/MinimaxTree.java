package Reversi;
/* The MinimaxTree class uses the minimax algorithm with iterative deepening and alpha-beta pruning. 
* A heuristic fitness measure is based on the number of moves available and the number 
* of pieces and the number of corner pieces, where each legal move is worth 50 pieces and each 
* corner is worth 500 pieces
* The search terminates after 5 seconds.
*/
public class MinimaxTree {

	private MinimaxNode root = null; 	
	private boolean finished;
	private int[][] solutionState = new int[6][6];
	private static long startTime; // Used to tell whether 5 seconds has passed.
	
	MinimaxTree(int computerTurn)
	{
		root = new MinimaxNode(computerTurn);
	}
	
	// Checks if 5 seconds has passed after adding an extra depth. Returns a board state
	public int [][] iterativeDeepeningSearch()
	{	
		startTime = System.currentTimeMillis();
		for (int lim = 0; true; lim++)
		{		
			depthLimitedSearch(root, lim);
			if (finished == true) return solutionState; // Return the board state with the minimax value									
		}
	}
	
	private void depthLimitedSearch(MinimaxNode n, int limit)
	{
		if (finished == true) return;
		if (limit <= 0) return;
		if (n == null) return;
		if ((System.currentTimeMillis() - startTime) > 5000)
		{
			for (int i = 0; i < root.getNumberOfChildren(); i++)
			{
				if (root.getChildFitness(i) == root.getFitness()) 
				{
					finished = true;
					solutionState = root.getChildAtIndex(i).getPosCopy();
					return; // Return the board state with the minimax value								
				}
			}			
		}
		if (!n.getChildrenAreSet()) n.setChildren(); 
		for (int i = 0; i < n.getNumberOfChildren(); i++)
		{
			depthLimitedSearch(n.getChildAtIndex(i), limit - 1); 
		}
		return;
	}
	
	// For Debugging 
	private void printTree(MinimaxNode r)
	{
		MinimaxNode n = r;
		if (n != null && n.getChildAtIndex(0) != null) {
			System.out.println("\nParent\nFitness: " + n.getFitness() + "\nLegal Moves: " + n.getNumberOfChildren() + "\nDepth: " + n.getDepth()  +  "\n\n");	
			System.out.println("Parent:");
			for (int a = 0; a < 6; a++) {
				for (int b = 0; b < 6; b++) {
					if (n.getPosCopy()[a][b] == 0 || n.getPosCopy()[a][b] == 1)
						System.out.print(n.getPosCopy()[a][b] + " ");
					else
						System.out.print("X ");
				}
				System.out.println();
			}	
			System.out.println();					
					
			System.out.print("Children: \n");					
			for (int a = 0;  a< 6; a++)	
			{
				for (int legI = 0; legI< n.getNumberOfChildren() && n.getChildAtIndex(legI) != null; legI++)
				{
					for (int b = 0; b< 6; b++)
					{				
						if (n.getChildAtIndex(legI).getPosCopy()[a][b] == 0 || n.getChildAtIndex(legI).getPosCopy()[a][b] == 1) System.out.print(n.getChildAtIndex(legI).getPosCopy()[a][b] + " ");
						else System.out.print("X ");
					}
					System.out.print("   ");
				}
				System.out.print("\n");
			}
				
			for (int j = 0; j < n.getNumberOfChildren(); j++)
			{
				printTree(n.getChildAtIndex(j));
			}
		}				
	}
}
