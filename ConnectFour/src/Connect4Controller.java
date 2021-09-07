import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;
import javafx.application.Platform;
import javafx.scene.control.Alert.AlertType;

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
public class Connect4Controller {
	private Connect4View view;
	private Connect4Model model;
	private Socket connection;
	private Connect4MoveMessage out;
	private boolean server;
	private ObjectOutputStream output;
	private ObjectInputStream input;
	private ServerSocket newServer;
	private int computer = 0;
	private Connect4MoveMessage temp;
	
	/**
	 * Initiating the Connect4Controller object by set model to its own field.
	 * @param model The model for the game. 
	 */
	public Connect4Controller (Connect4Model model) {
		this.model = model;
	}
	
	/**
	 * Check if the game over by checking if there exist any consecutive
	 * 4 disc that are filled by the same player, either row-wise, column-wise
	 * or diagonally.
	 * 
	 * <p>
	 * This function compares each user guessed answer to 
	 * the actual answer of the current play, return false if detect
	 * any non-equal chars, and return true if every user guessed chars 
	 * has been checked and no error has found.
	 * </p>
	 * 
	 * @return return true if all the mappings are all correct. False otherwise.
	 * @throws IOException 
	 */
	public boolean isGameOver(int lastplayer) throws IOException { 
		int[][] board = model.getBoard();
		if (checkRow(board, lastplayer) || checkCol(board, lastplayer) 
				|| checkDiag(board, lastplayer) || checkDiag2(board, lastplayer)) {	
			return true;
		}
		return false;
	}
	
	/**
	 * This function check if there are 4 connects of same color row-wise.
	 * @param board The 6 * 7 game board, with each filled in an int. 1 represent
	 * player1's move, 2 represent player2's move, anything else represent no fill yet.
	 * @param player An integer that represent the player. 1 represent player1, 2 represent 
	 * player two, any other numbers are invalid.
	 * @return return true if the 4 connected row-wise
	 */
	private boolean checkRow(int[][] board, int player) {
		for (int i = 0; i < model.ROW; i++) {		
			int count = 0;
			for (int j = 0; j < model.COL - 1; j++) {
				if (board[i][j] == player && board[i][j] == board[i][j+1]) {
					count++;
				} else {
					count = 0;
				}
				if (count >= 3) {
					return true;
				}
			}
		}
		return false;
	}
	public void giveView(Connect4View view) {
		this.view = view;
	}
	
	/**
	 * This function check if there are 4 connects of same color col-wise.
	 * @param board The 6 * 7 game board, with each filled in an int. 1 represent
	 * player1's move, 2 represent player2's move, anything else represent no fill yet.
	 * @param player An integer that represent the player. 1 represent player1, 2 represent 
	 * player two, any other numbers are invalid.
	 * @return return true if the 4 connected row-wise
	 */
	private boolean checkCol(int[][] board, int player) {
		for (int j = 0; j < model.COL; j++) {
			
			int count = 0;
			for (int i = 0; i < model.ROW - 1; i++) {
				if (board[i][j] == player && board[i][j] == board[i + 1][j]) {
					count++;
				} else {
					count = 0;
				}
				if (count >= 3) {
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * This function check if there are 4 connects of same color  diagonally (right-left).
	 * @param board The 6 * 7 game board, with each filled in an int. 1 represent
	 * player1's move, 2 represent player2's move, anything else represent no fill yet.
	 * @param player An integer that represent the player. 1 represent player1, 2 represent 
	 * player two, any other numbers are invalid.
	 * @return return true if the 4 connected row-wise
	 */
	private boolean checkDiag(int[][] board, int player) {
		int colStart = model.COL - 4;
		int count = 0;
		
		while (colStart > 0) {
			
			int i = 0;
			int j = colStart;
			while (j < model.COL - 1) {
				if (board[i][j] == player && board[i][j] == board[i + 1][j + 1]) {
					count++;
				} else {
					count = 0;
				}
				if (count >= 3) {
					return true;
				}
				i++;
				j++;
			}
			colStart--;
		}
		
		int rowStart = model.ROW - 4;
		count = 0;
		while (rowStart >= 0) {
			
			int i = rowStart;
			int j = 0;
			while (i < model.ROW - 1) {
				
				if (board[i][j] == player && board[i][j] == board[i + 1][j + 1]) {
					count++;
				} else {
					count = 0;
				}
				if (count >= 3) {
					return true;
				}
				i++;
				j++;
			}
			rowStart--;
		}
		return false;
	}
	
	/**
	 * This function check if there are 4 connects of same color diagonally (left-right).
	 * @param board The 6 * 7 game board, with each filled in an int. 1 represent
	 * player1's move, 2 represent player2's move, anything else represent no fill yet.
	 * @param player An integer that represent the player. 1 represent player1, 2 represent 
	 * player two, any other numbers are invalid.
	 * @return return true if the 4 connected row-wise
	 */
	private boolean checkDiag2(int[][] board, int player) {
		int rowStart = model.ROW - 3;
		int count = 0;
		
		while (rowStart < model.ROW) {
			
			int i = rowStart;
			int j = 0;
			while (i > 0) {
				if (board[i][j] == player && board[i][j] == board[i - 1][j + 1]) {
					count++;
				} else {
					count = 0;
				}
				if (count >= 3) {
					return true;
				}
				i--;
				j++; 
			}
			rowStart++;
		}
		
		
		int colStart = model.COL - 4;
		count = 0;
		
		while (colStart > 0) {
			int i = model.ROW - 1;
			int j = colStart;
			while (j < model.COL - 1) {
				if (board[i][j] == player && board[i][j] == board[i - 1][j + 1]) {
					count++;
				} else {
					count = 0;
				}
				if (count >= 3) {
					return true;
				}
				i--;
				j++;
			}
			colStart--;
		}
		return false;
	}
	
	
	/**
	 * Set the one dics into the col that was passed in as a parameter.
	 * by calling the setCol() function in model class.
	 * 
	 * It then sent the current move to another end, and create a new 
	 * Thread to wait for another move from the opponent.
	 * @param col The colume number range from 0-6 that the current player want
	 * to fill in.
	 * @param player An integer that represent the player. 1 represent player1, 2 represent 
	 * player two, any other numbers are invalid.
	 * @throws IOException
	 */
	public void setCol(int col, int player) throws IOException {
		//updates model and sends move.
		if(server == true) {
			model.setCol(col, player);
			out = model.currMove();
			output = new ObjectOutputStream(connection.getOutputStream());
			output.writeObject(out);

			new Thread(() -> {
				try {
					input = new ObjectInputStream(connection.getInputStream());
					Connect4MoveMessage temp = (Connect4MoveMessage) input.readObject();
					Platform.runLater(() -> {
						model.setCol(temp.getColumn(), temp.getColor());
					});
				} catch (IOException | ClassNotFoundException e) {
					e.printStackTrace();
				}
			}).start();
			
		} else if(server == false) {
			output = new ObjectOutputStream(connection.getOutputStream());
			new Thread(() -> {
				try {
					input = new ObjectInputStream(connection.getInputStream());
					Connect4MoveMessage temp = (Connect4MoveMessage) input.readObject();
					model.setCol(temp.getColumn(), temp.getColor());
					model.setCol(col, player);
					out = model.currMove();
					output.writeObject(out);
				} catch (IOException | ClassNotFoundException e) {
					e.printStackTrace();
				}
			}).start();
		}
	}
	
	/**
	 * Set the one dics into the board by allowing AI to randomly pick 
	 * a valid location. The set the disc by calling the setCol() function in model class.
	 * 
	 * It then sent the current move to another end, and create a new 
	 * Thread to wait for another move from the opponent.
	 * 
	 * Create a new Thread to 
	 * @throws IOException
	 */
	public void computerTurn() throws IOException {
		Random rand = new Random();
		if(view.getPlayer() == 1) {
			computer = 2;
		} else {
			computer = 1;
		}
		if(server == true) {
			model.setCol(rand.nextInt(model.COL), computer);
			out = model.currMove();
			output.writeObject(out);
			new Thread(() -> {
				try {
					Connect4MoveMessage temp = (Connect4MoveMessage) input.readObject();
					model.setCol(temp.getColumn(), temp.getColor());
				} catch (IOException | ClassNotFoundException e) {
					e.printStackTrace();
				}
			}).start();
		} else if(server == false) {
			new Thread(() -> {
				try {
					Connect4MoveMessage temp = (Connect4MoveMessage) input.readObject();
					model.setCol(temp.getColumn(), temp.getColor());
					model.setCol(rand.nextInt(model.COL), computer);
					out = model.currMove();
					output.writeObject(out);
				} catch (IOException | ClassNotFoundException e) {
					e.printStackTrace();
				}
			}).start();
		}
	}
	
	/**
	 * This function print the broad.
	 * @param board
	 */
	public void printboard(int[][] board) {
		for (int i = 0; i < model.ROW; i++) {
			for (int j = 0; j < model.COL; j++) {
				System.out.print(board[i][j] + " ");
			}
			System.out.println();
		}
	}

	/**
	 * This function gets info from the dialog box of New Game.
	 * @param socketType can either be a Server or a Client.
	 * @param person The roll of current player. Can either be a Computer or a Human.
	 * @param serverName The name of the server, the default value will be 'localhost'
	 * @param portNum The num of the port that the sever and client should connect to,
	 * the default value should be 4000.
	 */
	public void setupInfo(String socketType, String person, String serverName, int portNum) {	
		try {
			setup(socketType, person, serverName, portNum);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * This function sets either server or client sockets and sets players.
	 * @param socketType can either be a Server or a Client.
	 * @param person The roll of current player. Can either be a Computer or a Human.
	 * @param serverName The name of the server, the default value will be 'localhost'
	 * @param portNum The num of the port that the sever and client should connect to,
	 * the default value should be 4000.
	 * @throws IOException
	 */
	public void setup(String socketType, String person, String serverName, int portNum) throws IOException {
		if(socketType.equals("Server")) {
			newServer = new ServerSocket(portNum);
			connection = newServer.accept();
			server = true;
			view.setPlayer(1);
			
		} else if(socketType.equals("Client")) {
			connection = new Socket(serverName, portNum);
			server = false;
			view.setPlayer(2);
		}
		if(person.equals("Computer")) {
			view.setAI(true);
		} else {
			view.setAI(false);
		}
	}
}
	

