import java.io.IOException;
import java.util.Observable;
import java.util.Observer;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 * 
 * @author Mario Marquez, Yixin Li
 * 
 * This class is the view that provide the interface for the user.
 * it present a 6 * 7 play board. It set up all the graphics for the board and
 * the eventHandler for each components. The user will first click New game
 * button that allows them to choose to act as either client or server, and
 * choose to play themselves or allow the AI to take the roll.
 *
 *
 */
public class Connect4View extends Application implements Observer {
	private Connect4Model model = new Connect4Model();
	private Connect4Controller controller = new Connect4Controller(model);
	private BorderPane window = new BorderPane();
	private GridPane mainBoard = new GridPane();
	private Menu file = new Menu("File");
	private MenuItem newGame = new MenuItem("New Game"); 
	private MenuBar menus = new MenuBar();
	private int player = 1;
	private Alert alert = new Alert(AlertType.NONE);
	private static final int ROW = 6;
	private static final int COL = 7;
	private boolean computer = false;
	private boolean yourTurn = false;
	private Circle[][] discs;
			
	 
	/**
	 * The constructor add the model as its observer.
	 */
	public Connect4View() {
		model.addObserver(this);
		controller.giveView(this);
	}
	
	/**
	 * This funtion will be called first at the start of the game.
	 */
	@Override
	public void start(Stage stage) throws Exception {
		stage.setTitle("Connect 4");
		file.getItems().add(newGame);
		menus.getMenus().add(file);
		window.setTop(menus);
		discs = new Circle[ROW][COL];
		buildBoard();
		if(computer == true) {
			controller.computerTurn();
		}
		mouseEvents(stage);
		Scene scene = new Scene(window);
		stage.setScene(scene);
		stage.show();
	}
	
	/*
	 * This method is used to initially build the board with blank white spaces 
	 * and a blue background.
	 */
	public void buildBoard () {
		mainBoard.setHgap(8.0);
		mainBoard.setVgap(8.0);;
		mainBoard.setPadding(new Insets(8));
		mainBoard.setBackground(new Background(new BackgroundFill(Color.BLUE, null, null)));
		for(int j = 0; j < ROW; j++) {
			for(int i = 0; i < COL; i++) {
				Circle spots = new Circle();
				spots.setFill(Color.WHITE);
				spots.setRadius(20);
				discs[j][i] = spots;
				mainBoard.add(discs[j][i], i, j);
			}
		}
		window.setCenter(mainBoard);
		mainBoard.setDisable(true);
	}
	/*
	 * This method sets the current player of the game.
	 * 
	 * @param player, The next player.
	 */
	public void setPlayer(int player) {
		this.player = player;
	}
	/*
	 * This will return the current player.
	 * 
	 * @return, This returns the current player.
	 */
	public int getPlayer() {
		return player;
	}
	
	/*
	 * This sets the game to an AI game based on player selection.
	 * 
	 * @param AI, The boolean to set the game vs. computer or not.
	 */
	public void setAI(boolean AI) {
		computer = AI;
	}
	
	/*
	 * This is my mouse event handler, because the game does not have any key 
	 * events, this is the only view event handler.  This goes into effect when
	 * a person either takes a turn or starts a new game.
	 */
	private void mouseEvents(Stage stage) {
		mainBoard.setOnMouseClicked((event) -> {
			double x = event.getX();
			double width = mainBoard.widthProperty().get();
			int num = (int) width/COL;
			int colMove = (int) x/num;
			try {
				controller.setCol(colMove, player);
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
		
		newGame.setOnAction((event) -> {
			Setup game = new Setup(controller);
			game.get().showAndWait();
			buildBoard();
			mainBoard.setDisable(false);
			game.get().close();
		});
	}

	/**
	 * This function is called by the setcol() function in the 
	 * model to update the view when the model is updated.
	 */
	@Override
	public void update(Observable arg0, Object arg1) {
		if(arg1 == null) {
			alert.setAlertType(AlertType.ERROR);
			alert.setContentText("Column full, pick somewhere else!");
			alert.setHeaderText("Error");
			alert.showAndWait();
			
		} else {
			Connect4MoveMessage move = (Connect4MoveMessage) arg1;
			int x = move.getRow();
			int y = move.getColumn();
			int player = move.getColor();

			if (player == 1) {
				discs[x][y].setFill(Color.RED);
			} else if (player == 2) {
				discs[x][y].setFill(Color.YELLOW);
			} else {
				System.out.println(player + " NOT LEGAL player!");
			}
			this.player = (this.player == 1) ? 2 : 1;
			
			try {
				if(controller.isGameOver(player)) {	
					alert.setAlertType(AlertType.INFORMATION);
					alert.setContentText("You Win!");
					alert.setHeaderText("Message");
					alert.showAndWait();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
