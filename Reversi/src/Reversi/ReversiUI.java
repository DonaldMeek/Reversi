package Reversi;
// The ReversiUI class takes user input and displays the board
import java.util.InputMismatchException;
import java.util.Scanner;

public class ReversiUI
{
	public Scanner input;
	private int userChoice;
	private boolean inputCorrect;
	private int [][] boardValues; // A copy of the current values on the board
	private int [] xy; // x,y coordinate for user input 
	
	ReversiUI()
	{
		 input = new Scanner(System.in);
		 inputCorrect = false;
		 userChoice = 500; // For debugging
		 boardValues = new int[6][6];
		 xy = new int[2];
	}
	
	public int blackOrWhite(Board board)
	{
		
		do {
			try {
				System.out.println("Enter 1 to play first and play black or enter 0 to play second and play white.");
				userChoice = input.nextInt();
				if (userChoice != 0 && userChoice != 1) throw new InputMismatchException();
				inputCorrect = true;
			} catch (Exception e) {
				System.out.println("Error: The value entered was not valid.");
			} 
			input.nextLine();
		} while (!inputCorrect);
		System.out.println();
		return userChoice;
	}
	
	public int[] xyInput()
	{
		inputCorrect = false;
		do {
			try {
				System.out.println("Enter the y coordinate for your answer. 0 is at the top and 5 at the bottom.");
				xy[0] = input.nextInt();
				System.out.println("Enter the x coordinate for your answer. 0 at the far left and 5 at the far right.");
				xy[1] = input.nextInt();
				if (xy[0] < 0 || xy[0] > 5 || xy[1] < 0 || xy[1] > 5) throw new InputMismatchException();
				inputCorrect = true;
			} catch (Exception e) {
				System.out.println("Error: The value entered was not valid.");
			} 
			input.nextLine();
		} while (!inputCorrect);
		System.out.println();
		return xy;
	}
	
	public void showBoard(Board board)
	{
		boardValues = board.getValues();
		for (int j = 0; j < 6; j++) 
		{
			for (int i = 0; i < 6; i++)
			{
				if (boardValues[j][i] != 500) System.out.print(boardValues[j][i] + " ");
				else System.out.print("X ");
			}
			System.out.println();
		}
		System.out.println();
	}
	
	public int getUserChoice()
	{
		return userChoice;
	}
}
