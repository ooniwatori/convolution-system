package application;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class Scene2Controller implements Initializable {
	/* input signal and transformed signal.*/
	ArrayList<ArrayList<Double>> inpSignal = Scene1Controller.input.getInpSignal();
	ArrayList<ArrayList<Double>> outpSignal = Scene1Controller.input.getOutpSignal();
	/* scatter chart of input signal*/
	@FXML
	private ScatterChart<String, Double> inpGraph;
	/* scatter chart of transformed signal*/
	@FXML 
	private ScatterChart<String, Double> outpGraph;
	
	@FXML
	private Button backButton = new Button();
	
	/* display charts*/
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		/* input and transformed signal charts.*/
		XYChart.Series<String, Double> inpSeries = new XYChart.Series<>();
		XYChart.Series<String, Double> outpSeries = new XYChart.Series<>();
		/* set each data*/
		for (int i = 0; i < inpSignal.size(); ++i) {
			inpSeries.getData().add(new XYChart.Data<>(String.valueOf(inpSignal.get(i).get(0)), inpSignal.get(i).get(1)));
			
		}
		/* display input chart*/
		inpGraph.getData().add(inpSeries);
		/* set each data*/
		for(int i = 0; i <outpSignal.size(); ++i) {
			outpSeries.getData().add(new XYChart.Data<>(String.valueOf(outpSignal.get(i).get(0)), outpSignal.get(i).get(1)));
		}
		/* display input chart*/
		outpGraph.getData().add(outpSeries);
	}
	
	private Parent root;
	private Stage stage;
	private Scene scene;
	/* back button switch to scene1*/
	public void backButtonOnAction(ActionEvent event) throws IOException {
		/* switch to scene1*/
		root = FXMLLoader.load(getClass().getResource("Scene1.fxml"));
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	
}
