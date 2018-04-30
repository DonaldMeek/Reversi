package Reversi;
// The Move class finds possible moves and makes them.
public class Move {
	
	private int[][] pos; // for positions on the board. This is a reference to Reversi.board.values
	private int [] xy;
	private boolean flipMode; // tells whether the function flips pieces after a user places a piece or simply searches for legal moves
	private boolean userMode;
	private boolean valid;
	private PieceList buffer;
	private PieceNode piece;
	private int flipXIndex;
	private int flipYIndex;
	private int playerTurn;
	

	Move(int[][] values, boolean flipMode, int playerTurn)
	{
		buffer = null;
		pos = new int[6][6];
		xy = new int[2];
		this.flipMode = flipMode;
		this.playerTurn = playerTurn;
		this.pos = values;
	}	
	
	Move(boolean flipMode, int playerTurn, int[][] values)
	{
		buffer = null;
		pos = new int[6][6];
		xy = new int[2];
		this.flipMode = flipMode;
		this.playerTurn = playerTurn;
		setPosVal(values);
	}
	
	// The playPiece function finds all sandwiched pieces, flips them, and returns false and repeats (in the Display class) if no pieces were flipped.
	public boolean playPiece(int[] xy)
	{
		this.xy = xy;
		valid = false; // This is set to true if any pieces are flipped in a flipping function
		
		try 
		{
			if ((pos[xy[0]][xy[1]] == 0 || pos[xy[0]][xy[1]] == 1)) throw new Exception(); // For when the user plays a piece that has already been played.

			flipUp();
			flipUpperRight();
			flipRight();
			flipLowerRight();
			flipDown();
			flipLowerLeft();
			flipLeft();
			flipUpperLeft();
			if (valid && flipMode)	setPiece(); // Place the piece on the board
			else if (!valid) throw new Exception();
		} 
		catch (Exception e)
		{
			if (userMode)	System.out.println("Illegal move. Please try again."); 
		}
		return valid;
	}
	
	private void flipUp()
	{
		buffer = new PieceList();
		flipXIndex = xy[0];
		for (flipYIndex = xy[1] - 1; flipYIndex >= 0; flipYIndex--)
		{
			if (pos[flipXIndex][flipYIndex] == playerTurn) // Base case for when we find the sandwiching piece
			{
				if (buffer.isEmpty()) break; // Break if a user's piece is directly above the piece the user chose. 
				valid = true;
				if (flipMode)	flip();				
				break;
			}
			if (pos[flipXIndex][flipYIndex] == (1 - playerTurn))	buffer.add(pos[flipXIndex][flipYIndex], flipXIndex, flipYIndex); 
			else break;
		}	
	}	
	private void flipUpperRight()
	{
		buffer = new PieceList();
		flipXIndex = xy[0] + 1;
		for (flipYIndex = xy[1] - 1; flipYIndex >= 0 && flipXIndex <= 5; flipYIndex--)
		{
			if (pos[flipXIndex][flipYIndex] == playerTurn) // Base case for when we find the sandwiching piece
			{
				if (buffer.isEmpty()) break;
				valid = true;
				if (flipMode)	flip();				
				break;
			}
			if (pos[flipXIndex][flipYIndex] == (1 - playerTurn))	buffer.add(pos[flipXIndex][flipYIndex], flipXIndex, flipYIndex); 
			else break;
			flipXIndex++;
		}
	}	
	private void flipRight()
	{
		buffer = new PieceList();
		flipYIndex = xy[1];
		for (flipXIndex = xy[0] + 1; flipXIndex <= 5; flipXIndex++)
		{
			if (pos[flipXIndex][flipYIndex] == playerTurn) // Base case for when we find the sandwiching piece
			{
				if (buffer.isEmpty()) break;  
				valid = true;
				if (flipMode)	flip();				
				break;
			}
			if (pos[flipXIndex][flipYIndex] == (1 - playerTurn))	buffer.add(pos[flipXIndex][flipYIndex], flipXIndex, flipYIndex); 
			else break;
		}
	}
	private void flipLowerRight()
	{
		buffer = new PieceList();
		flipXIndex = xy[0] + 1;
		for (flipYIndex = xy[1] + 1; flipYIndex <= 5 && flipXIndex <= 5; flipYIndex++)
		{
			if (pos[flipXIndex][flipYIndex] == playerTurn) // Base case for when we find the sandwiching piece
			{
				if (buffer.isEmpty()) break; 
				valid = true;
				if (flipMode)	flip();				
				break;
			}
			if (pos[flipXIndex][flipYIndex] == (1 - playerTurn))	buffer.add(pos[flipXIndex][flipYIndex], flipXIndex, flipYIndex); // Add the piece to the buffer if it is adjacent and it's an opponent's piece
			else break;
			flipXIndex++;
		}
	}
	private void flipDown()
	{
		buffer = new PieceList();
		flipXIndex = xy[0];
		for (flipYIndex = xy[1] + 1; flipYIndex <= 5; flipYIndex++)
		{
			if (pos[flipXIndex][flipYIndex] == playerTurn) // Base case for when we find the sandwiching piece
			{
				if (buffer.isEmpty()) break;
				valid = true;
				if (flipMode)	flip();				
				break;
			}
			if (pos[flipXIndex][flipYIndex] == (1 - playerTurn))	buffer.add(pos[flipXIndex][flipYIndex], flipXIndex, flipYIndex); 
			else break;
		}
	}
	private void flipLowerLeft()
	{
		buffer = new PieceList();
		flipXIndex = xy[0] - 1;
		for (flipYIndex = xy[1] + 1; flipYIndex <= 5 && flipXIndex >= 0; flipYIndex++)
		{
			if (pos[flipXIndex][flipYIndex] == playerTurn) // Base case for when we find the sandwiching piece
			{
				if (buffer.isEmpty()) break;
				valid = true;
				if (flipMode)	flip();				
				break;
			}
			if (pos[flipXIndex][flipYIndex] == (1 - playerTurn))	buffer.add(pos[flipXIndex][flipYIndex], flipXIndex, flipYIndex); 
			else break;
			flipXIndex--;
		}
	}
	private void flipLeft()
	{
		buffer = new PieceList();
		flipYIndex = xy[1];
		for (flipXIndex = xy[0] - 1; flipXIndex >= 0; flipXIndex--)
		{
			if (pos[flipXIndex][flipYIndex] == playerTurn) // Base case for when we find the sandwiching piece
			{
				if (buffer.isEmpty()) break;
				valid = true;
				if (flipMode)	flip();				
				break;
			}
			if (pos[flipXIndex][flipYIndex] == (1 - playerTurn))	buffer.add(pos[flipXIndex][flipYIndex], flipXIndex, flipYIndex); 
			else break;
		}
	}
	private void flipUpperLeft()
	{
		buffer = new PieceList();
		flipXIndex = xy[0] - 1;
		for (flipYIndex = xy[1] - 1; flipYIndex >= 0 && flipXIndex >= 0; flipYIndex--)
		{
			if (pos[flipXIndex][flipYIndex] == playerTurn) // Base case for when we find the sandwiching piece
			{
				if (buffer.isEmpty()) break;
				valid = true;
				if (flipMode)	flip();				
				break;
			}
			if (pos[flipXIndex][flipYIndex] == (1 - playerTurn))	buffer.add(pos[flipXIndex][flipYIndex], flipXIndex, flipYIndex); 
			else break;
			flipXIndex--;
		}
	}
	
	private void flip()
	{
		PieceNode piece = new PieceNode();
		int [] tempCoordinates = new int[2];
		
		for (int i = 0; i < buffer.size(); i++) 
		{
			piece = buffer.at(i, piece);
			tempCoordinates = piece.getCoordinates();
			pos[tempCoordinates[0]][tempCoordinates[1]] = 1 - pos[tempCoordinates[0]][tempCoordinates[1]];
		}
	}
	
	public void setPosVal(int [][] copy)
	{
		pos = new int[6][6];
		for (int i = 0; i < 6; i++) for (int j = 0; j < 6; j++)	pos[i][j] = copy[i][j];
	}
	
	public int[][] getPosVal()
	{
		int [][] copy = new int[6][6];
		for (int i = 0; i < 6; i++) for (int j = 0; j < 6; j++)	copy[i][j] = pos[i][j];
		return copy;
	}
	
	public void setPosRef(int [][] copy)
	{
		pos = copy;
	}
	
	public int getPlayerTurn() {
		return playerTurn;
	}

	public void setPlayerTurn(int playerTurn) {
		this.playerTurn = playerTurn;
	}
	
	public boolean isFlipMode() {
		return flipMode;
	}

	public void setFlipMode(boolean flipMode) {
		this.flipMode = flipMode;
	}
	
	public boolean isUserMode() {
		return userMode;
	}

	public void setUserMode(boolean userMode) {
		this.userMode = userMode;
	}
	
	private void setPiece()
	{
		pos[xy[0]][xy[1]] = playerTurn;
	}
}
