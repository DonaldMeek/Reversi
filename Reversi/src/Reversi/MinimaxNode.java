package Reversi;
// Nodes for MinimaxTree
public class MinimaxNode {
	
	private int fitness; // Corners are worth 500 points, legal moves are worth 50 points, and board pieces are worth 1 point. Fitness is found by taking the points from legal moves and board pieces and subtracting the opponent's points from legal moves and board pieces
	private int legalMoves;
	private int [][] posCopy; // The board position where this node could make a move. This node's children's posCopys contain this node's possible moves 
	private int [][][] childrenPos; // One for each possible child. This node finds these values to define the children. The leftmost index is for the children and the remaining indices are for the board states.
	private boolean childrenAreSet;
	private MinimaxNode [] children;
	private MinimaxNode parent; 
	private boolean isRoot;
	private int numberOfChildren;
	private int depth;
	private int nodeTurn; // This value is 1 - Reversi.getUserChoice() for all nodes in this MinimaxTree 
	private int alpha; // Initialized at 4000, higher than any possible fitness
	private int beta; // Initialized at -4000, lower than any possible fitness

	// Root constructor 
	MinimaxNode(int compTurn)
	{
		alpha = 4000;
		beta = -4000;
		childrenAreSet = false;
		posCopy = new int[6][6];
		Board board = new Board();
		nodeTurn = compTurn;
		this.posCopy = board.getValues();
		this.depth = 0;
		isRoot = true; 	
		setDefaults();
	}
	
	// Child constructor
	MinimaxNode(MinimaxNode n, int[][] pos, int d)
	{
		alpha = 4000;
		beta = -4000;
		childrenAreSet = false;
		posCopy = new int[6][6];
		parent = n;
		nodeTurn = 1 - parent.getPlayerTurn(); // Turns alternate with each extra depth
		for (int i = 0; i < 6; i++) for (int j = 0; j < 6; j++) posCopy[i][j] = pos[i][j];
		this.depth = d;
		setDefaults();
		isRoot = false;
	}

	// Copy constructor that the computer can user to pass (following the Reversi rule where players can only pass if no move is available)
	// Child constructor
	MinimaxNode(MinimaxNode n)
	{
		alpha = 4000;
		beta = -4000;
		childrenAreSet = false;
		posCopy = new int[6][6];
		parent = n;
		nodeTurn = 1 - parent.getPlayerTurn(); // Turns alternate with each extra depth
		for (int i = 0; i < 6; i++) for (int j = 0; j < 6; j++) posCopy = parent.getPosCopy();
		this.depth = parent.getDepth() + 1;
		setDefaults();
		isRoot = false;
	}
	
	private void setDefaults()
	{
		children = new MinimaxNode[36];		
		childrenPos = new int[36][6][6];
		numberOfChildren = 0;
		legalMoves = 0;
		fitness = 0;
		setFitnessValues();	
	}
	
	// Checks for possible children, defines them, and checks if the solution is found. This is where the alpha-beta pruning happens 
	public void setChildren()
	{
		
		// If the computer has no legal moves, it must pass. 
		if (numberOfChildren == 0)
		{
			new MinimaxNode(this);
			numberOfChildren = 1;
			return;
		}
		
		MinimaxNode n = null;
		MinimaxNode nPar = null;
		
		// define each child
		int k = 0;
		for (; k < numberOfChildren; k++)
		{		
			n = new MinimaxNode(this, childrenPos[k], this.depth + 1);
			nPar = n.getParent();
			
			// Perform alpha-beta pruning by adjusting nPar's alpha and beta values
			if (nPar.getDepth() == 0)
			{
				if (k == 0) nPar.setAlpha(n.getFitness());
				if (nPar.getAlpha() <= n.getFitness()) 
				{
					nPar.setAlpha(n.getFitness());
				}
				else n = null;															
			}
			if (nPar.getDepth() == 1)
			{
				if (k == 0) nPar.setBeta(n.getFitness());
				if (nPar.getBeta() >= n.getFitness()) 
				{
					nPar.setBeta(n.getFitness());
				}
				else n = null;
			}
			
			// The root only has one child
			if (n != null && n.getDepth() == 1 && k > 0) 
			{
				numberOfChildren = 1;
				break; 
			}
			children[k] = n;			
		}
		
		// Adjust either the parent's alpha or beta value	
		if (nPar == null);
		else if (nPar.getDepth() == 0)
		{
		nPar.setBeta(nPar.getAlpha());
		}
		else if (nPar.getDepth() == 1)
		{
			nPar.setAlpha(nPar.getBeta());
		}
	
		childrenPos = null;
		childrenAreSet = true;
		MinimaxNode miniN = this; // Temporary object to recursively send up the minimax value
		sendFitnessUp(miniN.getChildAtIndex(0));
	}

	public int getNumberOfChildren()
	{
		return numberOfChildren;
	}
	
	public MinimaxNode getChildAtIndex(int index)
	{
		return children[index];
	}
		
	public int getDepth() {
		return depth;
	}
	
	// For each node n with children, if n's depth is even this function sets n's fitness to the fitness of it's most-fit child, and if n's depth is odd this function sets n's fitness to the fitness of it's least fit child.
	public void sendFitnessUp(MinimaxNode tempN)
	{
		// Base case
		if (tempN == null || tempN.getParent() == null) return;
		
		tempN = tempN.getParent();
		
		int highestChildFitness = tempN.getChildFitness(0), 
				lowestChildFitness = tempN.getChildFitness(0);
		
		// Nodes with even depth pick the highest fitness from their children
		if (tempN.getDepth() % 2 == 0) 
		{
			for (MinimaxNode child : tempN.children)
			{
				if (child == null){
					tempN.setFitness(highestChildFitness);
					break;
				}
				if (child.getFitness() > highestChildFitness) highestChildFitness = child.getFitness();
			}
		}
		
		// Nodes with odd depth pick the lowest fitness from their children
		if (tempN.getDepth() % 2 == 1) 
		{
			for (MinimaxNode child : tempN.children)
			{
				if (child == null)
				{
					tempN.setFitness(lowestChildFitness);
					break;
				}
				if (child.getFitness() < lowestChildFitness) lowestChildFitness = child.getFitness();
			}
		}
		
		// Move up the tree 
		tempN.sendFitnessUp(tempN);
	}
	
	public boolean getIsRoot()
	{
		return isRoot;
	}
	
	public MinimaxNode getParent()
	{
		return parent;
	}

	public int getPlayerTurn() {
		return nodeTurn;
	}

	public int getAlpha()
	{
		return alpha;
	}
	
	public int getBeta()
	{
		return beta;
	}
	
	public void setAlpha(int a)
	{
		alpha = a;
	}
	
	public void setBeta(int b)
	{
		beta = b;
	}
	
	public void setPlayerTurn(int playerTurn) {
		this.nodeTurn = playerTurn;
	}
	
	public int[][] getPosCopy() {
		int [][] copy = new int[6][6];
		for (int i = 0; i < 6; i++) for (int j = 0; j < 6; j++)	copy[i][j] = posCopy[i][j];
		return copy;
	}

	public void setPosCopy(int[][] pos) {
		for (int i = 0; i < 6; i++) for (int j = 0; j < 6; j++)	this.posCopy[i][j] = pos[i][j];
	}

	public int getFitness() {
		return fitness;
	}
	
	public int getChildFitness(int index)
	{
		return this.children[index].getFitness();
	}

	public void setFitness(int fitness) {
		this.fitness = fitness;
	}
	
	public boolean getChildrenAreSet()
	{
		return childrenAreSet;
	}
	
	// This function counts the legal moves for the player who's turn it is, and subtracts the legal moves of the opposite player.
	// Used to set fitness, number of legal moves (same as the number of children), and the chilrden's board states
	public void setFitnessValues()
	{
		fitness = 0;
		Move move = new Move(false, nodeTurn, posCopy);
		int [] xy = new int[2];
		boolean isLegal = false;
		
		// Find possible moves
		for (int i = 0; i < 6; i++)
		{
			xy[0] = i;
			for (int j = 0; j < 6; j++)
			{
				xy[1] = j;
				isLegal = move.playPiece(xy); // Check if it is a legal move
				
				// Define the children's posValues, which contain the parent's changed board states for each legal move
				if (isLegal) 
				{
					for (int k = 0; k < 6; k++) for (int h = 0; h < 6; h++)	childrenPos[legalMoves][k][h] = posCopy[k][h];	
					move.setPosRef(childrenPos[legalMoves]);
					move.setFlipMode(true);
					move.playPiece(xy);
					move.setPosRef(posCopy);
					move.setFlipMode(false);
					legalMoves++;				
				}
			}
		}
		
		// Find the oponent's possible moves
		move.setPosVal(posCopy);
		move.setFlipMode(false);
		fitness = 50*legalMoves;
		move.setPlayerTurn(1 - nodeTurn);
		for (int i = 0; i < 6; i++)
		{
			xy[0] = i;
			for (int j = 0; j < 6; j++)
			{
				xy[1] = j;
				isLegal = move.playPiece(xy);
				if (isLegal) fitness -= 50;
			}
		}
		numberOfChildren = legalMoves;
		
		// Find the number of board pieces to increment and decrement fitness.
		for (int h = 0; h < 6; h++)
		{
			for (int j = 0; j < 6; j++)
			{
				if (posCopy[h][j] == nodeTurn) fitness++;
				if (posCopy[h][j] == 1 - nodeTurn) fitness--;
			}
		}
		
		// Find the corner pieces to increment and decrement fitness.
		if (posCopy[0][0] == nodeTurn) fitness += 500;
		else if (posCopy[0][0] == 1 - nodeTurn) fitness -= 500;
		if (posCopy[5][0] == nodeTurn) fitness += 500;
		else if (posCopy[5][0] == 1 - nodeTurn) fitness -= 500;	
		if (posCopy[0][5] == nodeTurn) fitness += 500;
		else if (posCopy[0][5] == 1 - nodeTurn) fitness -= 500;
		if (posCopy[5][5] == nodeTurn) fitness += 500;
		else if (posCopy[5][5] == 1 - nodeTurn) fitness -= 500;			
	}
}
