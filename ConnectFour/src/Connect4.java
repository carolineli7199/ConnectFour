import javafx.application.Application;
/**
 * 
 * @author Mario Marquez, Yixin Li
 * 
 * This class is main method for this connect4 game. Two users plays on an 6 * 7 play board.
 * Two players take turns to drop discs each with an different color. 
 * The game is over if there are four connects of disc in the same color row-wise, 
 * column-wise, and diagonally.
 * 
 *
 */
public class Connect4 {
	public static void main(String[] args) { 
		Application.launch(Connect4View.class, args);
		
	}
}
