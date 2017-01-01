package Reversi;
// The Reversi class is the main class which sets up other classes, controls how each turn is made, and performs 
// end-of-game procedures.
public class Reversi 
{
	Board board;
	ReversiUI UI;
	Move move;
	MinimaxTree mT;
	int[][] values; // Positions on the board
	int[] xy;
	private int userChoice;
	private int passedTurns; // The number of turns in a row that have been passed
	private boolean firstMove; // used to allow the user or the computer to go first
	private boolean playerCanMove;
	private boolean fullBoard;
	private int playerTurn; // 0 if it's white's turn and 1 if it's black's turn
	private boolean skippedComputerTurn;

	Reversi()
	{
		firstMove = true;
		board = new Board();
		UI = new ReversiUI();
		values = new int[6][6];
		xy = new int[2];
		values = board.getValues();
		userChoice = UI.blackOrWhite(board);
		playerTurn = userChoice;
		fullBoard = false;
		skippedComputerTurn = false;
	}
	
	public void nextTurn()
	{
		values = board.getValues();
		move = new Move(values, false, playerTurn);
	
		// Find if the user must pass
		mustPass();
		if (passedTurns == 2) endOfGame(); // End the game with two consecutive passed turns
		
		// If the computer's turn was skipped during the previous turn:
		if (skippedComputerTurn) System.out.println("I have no possible moves, so we ship my turn.\n");	

		// Show the board for the user
		if (!firstMove || !(userChoice == 0)) UI.showBoard(board);

		// Find if the board is full
		fullBoard = true;
		for (int i = 0; i < 6; i++) 
		{
			xy[0] = i;
			for (int j = 0; j < 6; j++)
			{
				xy[1] = j;
				if (values[i][j] == 500) fullBoard = false; // The board is not full if there is an empty space
			}
		}
		if (fullBoard) endOfGame();
		
		// For the user
		do 
		{
			if (!playerCanMove) 
			{
				System.out.println("You have no possible moves, so we ship your turn.\n");
				break;
			}
			if (firstMove && userChoice == 0) break;		
								
			// Let the user move
			move.setUserMode(true);
			move.setFlipMode(true);
			xy = UI.xyInput();
		} while(!move.playPiece(xy)); // Repeats if the move is not legal
		firstMove = false;
		board.setValues(values);
		if (playerCanMove) UI.showBoard(board);
		playerTurn = 1 - playerTurn;

		// For the computer
		move.setUserMode(false);
		move.setPlayerTurn(playerTurn);
		
		// Check if the computer can make a move, and if not, check if the game is over
		mustPass();
		if (playerCanMove == false) 
		{
			if (passedTurns == 2) endOfGame();
			skippedComputerTurn = true;		
			playerTurn = 1 - playerTurn; 
			return;
		}	
		
		// Let the computer make a move
		mT = new MinimaxTree(playerTurn); 
		values = mT.iterativeDeepeningSearch();
		board.setValues(values);
		playerTurn = 1 - playerTurn; 
		
		// The game is over when neither player can move
		passedTurns = 0; // There have not been two passed moves in a row if the program gets to this line
	}
	
	public void setUserChoice(int c)
	{
		userChoice = c;
	}
	
	public int getUserChoice()
	{
		return userChoice;
	}
	
	public int getPlayerTurn() 
	{
		return playerTurn;
	}
	
	public int[][] getValuesCopy() {
		int[][] v = new int[6][6];
		for (int i = 0; i < 6; i++) for (int j = 0; j < 6; j++) v[i][j] = values[i][j];
		return v;
	}
	
	public void play()
	{
		for (int i = 0; i < 50; i++) nextTurn();
	}
	
	public void mustPass()
	{
		playerCanMove = false;
		for (int i = 0; i < 6; i++)
		{
			xy[0] = i;
			for (int j = 0; j < 6; j++) 
			{
				xy[1] = j;
				if (move.playPiece(xy)) playerCanMove = true;
			}
		}
		if (playerCanMove == false) passedTurns++;
	}
	
	private void endOfGame()
	{
		int computerPieces = 0, 
				userPieces = 0;
		
		for (int i = 0; i < 6; i++) 
		{
			for (int j = 0; j < 6; j++)
			{
				if (values[i][j] == 1) computerPieces++;
				if (values[i][j] == 0) userPieces++;
			}
		}
		if (computerPieces > userPieces) System.out.println("I win!");
		else if (userPieces > computerPieces) System.out.println("You win!");
		else if (userPieces == computerPieces) System.out.println("It's a tie.");
		System.exit(0);
	}
	
	public static void main(String[] args) 
	{
		new Reversi().play();
	}
}
