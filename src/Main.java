package application;
	
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.DialogPane;
import javafx.scene.image.Image;


public class Main extends Application {
	@Override
	public void start(Stage stage) {
		try {
			/* Set windows to scene1*/
			Parent root = FXMLLoader.load(getClass().getResource("Scene1.fxml"));
			Scene scene = new Scene(root);
			/* Style*/
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			/* icon*/
			Image icon = new Image("icon.png");
			
			stage.getIcons().add(icon);
			stage.setTitle("LTISystem");
			stage.setResizable(false);
			
			stage.setScene(scene);
			stage.show();
			
			/* Exit on click*/
			stage.setOnCloseRequest(event -> {
				event.consume();
				closeApp(stage);
				});
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/* When close the application*/
	public void closeApp(Stage stage) {
		
		/* OK, Cancel button.*/
		ButtonType OKButton = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
		ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
		
		/* Alert*/
		Alert alert = new Alert(AlertType.CONFIRMATION, null, OKButton, cancelButton);
		alert.setTitle("Confirmation");
		alert.setHeaderText(null);
		alert.setContentText("Do you want to close the application?");
		
		/* Dialog style*/
		DialogPane closeDialog = alert.getDialogPane();
		closeDialog.getStylesheets().add(getClass().getResource("closeDialogs.css").toExternalForm());
		closeDialog.getStyleClass().add("closeDialog");
		
		/* Input*/
		if(alert.showAndWait().get() == OKButton) {
			stage.close();
		}
	}
	public static void main(String[] args) {
		launch(args);
	}
}
