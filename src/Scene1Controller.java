package application;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class Scene1Controller implements Initializable {
	
	/* record error*/
	private boolean error = false;
	/* absolute file path*/
	private String filePath = null;
	
	/* panel of user input, amplitude, range, and label.*/
	@FXML
	private Label amplitudeLabel = new Label();
	@FXML
	private Label rangLabel = new Label();
	@FXML
	private TextField amplitudeTextField = new TextField();
	@FXML
	private TextField leftLimitTextField = new TextField();
	@FXML
	private TextField rightLimitTextField = new TextField();
	
	/* pane for contain above contents.*/
	@FXML 
	private Pane contentsPane = new Pane();
	/* label of select a system.*/
	@FXML 
	private Label selectSystemLabel;
	/* combo box of systems selection.*/
	@FXML
	private ComboBox<String> systemComboBox;
	private String[] systemChoice = {"Echo", "Reduction", "Expansion"};
	/* combo box of signal selection.*/
	@FXML
	private ComboBox<String> signalComboBox;
	private String[] signalChoice = {"Sin wave", "Cos wave", "Randomly generate", "Select CSV file"};
	/* button of file selector.*/
	@FXML
	private Button fileChooser = new Button();
	/* initialize panel.*/
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		error = false;
		filePath = null;
		contentsPane.setVisible(false);
		fileChooser.setVisible(false);
		systemComboBox.setItems(FXCollections.observableArrayList(systemChoice));
		signalComboBox.setItems(FXCollections.observableArrayList(signalChoice));
	}
	/* get string of system and signal combo box selection.*/
	@FXML
	public String selectSystem() {
		return systemComboBox.getSelectionModel().getSelectedItem();
	}
	@FXML
	public String selectInput() {
		return signalComboBox.getSelectionModel().getSelectedItem();
	}
	/* record absolute path of selected file .*/
	public void filechooserAction(ActionEvent event) {
		FileChooser fc = new FileChooser();
		File selectedFile = fc.showOpenDialog(null);
		
		if(selectedFile != null) {
			filePath = selectedFile.getAbsolutePath();
		}
	}
	/* input signal to transform*/
	public static SignalTransform input;
	private Stage stage;
	private Scene scene;
	private Parent root;
	/* select file or other selections.*/
	@FXML
	public void inputBoxAction(ActionEvent event) {
		if(selectInput() == "Select CSV file") {
			contentsPane.setVisible(false);
			fileChooser.setVisible(true);
		}
		else {
			fileChooser.setVisible(false);
			contentsPane.setVisible(true);
		}
	}
	/* execution button on action.*/
	public void executeButtonAction(ActionEvent event) throws IOException {
		error = false;
		/* No input selected*/
		if(selectInput() == null) {
			event.consume();
			Warning(stage, "No input has been select.");
		}
		else if(selectInput() == "Select CSV file" && filePath == null) {
			event.consume();
			Warning(stage, "No file has been selected.");
		}
		else if(selectSystem() == null) {
			event.consume();
			Warning(stage, "No system has been selected.");
		}
		else {
			/* Check input*/
			switch(selectInput()) {
			case "Select CSV file":
				input = new SignalTransform(filePath);
				break;
			default:
				double amp;
				int leftLimit;
				int rightLimit;
				try {
					amp = Double.parseDouble(amplitudeTextField.getText());
					leftLimit = Integer.parseInt(leftLimitTextField.getText());
					rightLimit = Integer.parseInt(rightLimitTextField.getText());
					if(leftLimit > rightLimit) {
						Warning(stage, "Range is invalid");
						break;
					}
				}
				catch(NumberFormatException e) {
					event.consume();
					Warning(stage, "Please enter the number.");
					error = true;
					break;
				}
				/* store each input signal*/
				switch(selectInput()) {
				case "Randomly generate":
					input = new SignalTransform(amp, leftLimit, rightLimit);
					break;
				case "Sin wave":
					input = new SignalTransform(selectInput(), amp, leftLimit, rightLimit);
					break;
				case "Cos wave":
					input = new SignalTransform(selectInput(), amp, leftLimit, rightLimit);
					break;
				}
				break;
			}
			if(!error) {
				/* do transform.*/
				switch(selectSystem()) {
				case "Echo":
					input.systemEcho();
					break;
				case "Reduction":
					input.systemReduction();
					break;
				case "Expansion":
					input.systemExpansion();
					break;
				}
				/* switch to scene2*/
				root = FXMLLoader.load(getClass().getResource("Scene2.fxml"));
				stage = (Stage)((Node)event.getSource()).getScene().getWindow();
				scene = new Scene(root);
				stage.setScene(scene);
				stage.show();
			}
		}
	}
	/* warning user error is happened.*/
	public void Warning(Stage stage, String s) {
		/* Alert*/
		Alert alert = new Alert(AlertType.WARNING, null);
		alert.setTitle("Warning");
		alert.setHeaderText(null);
		alert.setContentText(s);
		
		/* Dialog style*/
		DialogPane closeDialog = alert.getDialogPane();
		closeDialog.getStylesheets().add(getClass().getResource("closeDialogs.css").toExternalForm());
		closeDialog.getStyleClass().add("closeDialog");
		alert.showAndWait();
	}
}
