package Reversi;
// A 6-by-6 board where (0,0) is the top left coordinate and (6,6) is the bottom right
public class Board {
	private static int[][] values; // Indices set up as: [rows][columns]
	private static int count = 0;
	
	Board()
	{	
		if (count == 0)			
		{	
			values = new int[6][6];
		
			// Empty board positions contain the value 500
			for (int i = 0; i < 6; i++) for (int j = 0; j < 6; j++) values[i][j] = 500;
			values[2][2] = 0;
			values[2][3] = 1;
			values[3][2] = 1;
			values[3][3] = 0;
		}
		count++;
	}
	
	public int[][] getValues()
	{
		int [][] temp = new int[6][6];
		for (int i = 0; i < 6; i++) for (int j = 0; j < 6; j++) temp[i][j] = values[i][j];
		return temp;
	}
	
	public void setValues(int[][] v)
	{
		for (int i = 0; i < 6; i++) for (int j = 0; j < 6; j++) values[i][j] = v[i][j];
	}
}
