import java.io.IOException;
import java.net.Socket;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
/**
 * 
 * @author Mario Marquez, Yixin Li
 * 
 * This class is the controller that connects the user interface and 
 * It set up all the graphics after user click the new game button. It also 
 * sets the eventHandler for each components. The user will first click New game
 * button that allows them to choose to act as either client or server, and then
 * choose to play themselves or let the AI to take the roll.
 *
 */
public class Setup extends Stage{
	private Dialog<Boolean> message = new Dialog<Boolean>();
	private VBox stuff = new VBox();
	private HBox selections1 = new HBox();
	private HBox selections2 = new HBox();
	private Label words = new Label("Create:");
	private Label words2 = new Label("Play as:");
	private RadioButton select = new RadioButton();
	private RadioButton select2 = new RadioButton();
	private RadioButton select3 = new RadioButton();
	private RadioButton select4 = new RadioButton();
	private Button okay = new Button("Ok");
	private Button cancel = new Button("Cancel");
	private HBox textBoxes = new HBox();
	private HBox buttons = new HBox();
	private Label server = new Label("Server");
	private Label port = new Label("Port");
	private TextField info1 = new TextField();
	private TextField info2 = new TextField();
	private String option1 = new String();
	private String option2 = new String();
	private Connect4Controller controller;
	
	
	/**
	 * This con
	 * @param controller The Connenct4Controller instance for the 
	 * current game.
	 */
	public Setup(Connect4Controller controller) {
		this.controller = controller;
		message.initModality(Modality.APPLICATION_MODAL);
		message.setTitle("Network Setup");
		bunchStuff();
		events();
	}
	
	/**
	 * 
	 * @return return the message of the Dialog
	 */
	public Dialog<Boolean> get() {
		return message;
	}
	
	/**
	 * Set up the components for the Dialog that triggered
	 * after user clicked new game button.
	 */
	public void bunchStuff() {
		
		select.setPadding(new Insets(0, 10, 0, 0));
		select2.setPadding(new Insets(0, 10, 0, 0));
		select3.setPadding(new Insets(0, 10, 0, 0));
		select4.setPadding(new Insets(0, 10, 0, 0));
		words.setPadding(new Insets(0, 10, 0, 0));
		words2.setPadding(new Insets(0, 10, 0, 0));
		select.setText("Server");
		select2.setText("Client");
		select3.setText("Human");
		select4.setText("Computer");
		info1.setText("localhost");
		info2.setText("4000");
		selections1.setPadding(new Insets(10));
		selections2.setPadding(new Insets(10));
		textBoxes.setPadding(new Insets(10));
		buttons.setPadding(new Insets(0, 10, 0, 5));
		server.setPadding(new Insets(0, 10, 0, 0));
		port.setPadding(new Insets(0, 10, 0, 10));
		buttons.setSpacing(10);
		buttons.getChildren().addAll(okay, cancel);
		textBoxes.getChildren().addAll(server, info1, port, info2);
		selections1.getChildren().addAll(words, select, select2);
		selections2.getChildren().addAll(words2, select3, select4);
		stuff.getChildren().addAll(selections1, selections2, textBoxes, buttons);
		message.getDialogPane().setContent(stuff);
	}
	
	/**
	 * Set up the eventHandler for all the components setup
	 * in the game.
	 * @throws NumberFormatException
	 */
	public void events() throws NumberFormatException {
		select.setOnAction((event) -> {
			select2.setSelected(false);
			option1 = select.getText();
		});
		select2.setOnAction((event) -> {
			select.setSelected(false);
			option1 = select2.getText();
		});
		select3.setOnAction((event) -> {
			select4.setSelected(false);
			option2 = select3.getText();
		});
		select4.setOnAction((event) -> {
			select3.setSelected(false);
			option2 = select4.getText();
		});
		okay.setOnAction((event) -> {
			String serverName = info1.getText();
			int portNum = Integer.parseInt(info2.getText());
			controller.setupInfo(option1, option2, serverName, portNum);
			message.setResult(true);
			message.close();
		});
		cancel.setOnAction((event) -> {
			message.setResult(true);
			message.close();
		});
		message.getDialogPane().getScene().getWindow().setOnCloseRequest((event) -> {
			message.setResult(true);
			message.close();
		});
	}
}
