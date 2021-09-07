import java.util.Observable;
/**
 * 
 * @author Mario Marquez, Yixin Li
 * 
 * This class is the controller that connects the user interface and 
 * and the model, which is a 6 * 7 play board. It first set up the current game
 * for client or server. It triggers the model to set a new disc into a column, sent 
 * the current move to another end, and create a new Thread to wait for another move
 * from the opponent.
 *
 * It checks if the game is over by check if there are four connects of disc
 * in the same color row-wise, column-wise, and diagonally.
 *
 */
public class Connect4Model extends Observable{
	private int[][] board;
	private Connect4MoveMessage playerMove;
	
	public final int ROW = 6;
	public final int COL = 7;
	
	public Connect4Model (){
		board = new int[ROW][COL];
		
	}
	
	/**
	 * This function set a placement for the current player. The player at the current 
	 * turn choose a column that he/she want to place a disc in. The disc will drop until 
	 * it reaches the bottom or another previously filled disc. This function set a int
	 * that represent the player's disc. 
	 * 
	 * @param col The column number that the player want to fill in disc. (0-6)
	 * @param player An integer that represent the player. 1 represent player1, 2 represent 
	 * player two, any other numbers are invalid.
	 */
	public void setCol(int col, int player) {
		for (int i = ROW -1; i >= 0; i--) {
			if (board[i][col] != 1 && board[i][col] != 2) {
				board[i][col] = player;
				setChanged();	
				playerMove = new Connect4MoveMessage(i, col, player);
		    	notifyObservers(playerMove);	
		    	return;
			}
		}
		// For ERROR message if column is full.
		setChanged();
		notifyObservers();
	}
	
	/**
	 * This function returns the 6 * 7 game board. 
	 * @return
	 */
	public int[][] getBoard() {
		return board;
	}
	
	/**
	 * This function return the current move by 
	 * an instance of Connect4MoveMessage.
	 * @return an instance of Connect4MoveMessage that represent the 
	 * current move.
	 */
	public Connect4MoveMessage currMove() {
		return playerMove;
	}
	
	/**
	 * Set the change in Model and call update() in view.
	 * @param move
	 */
	public void updateUI(Connect4MoveMessage move) {
		System.out.println(move.getColor());
		setChanged();
		notifyObservers(move);
	}
}

