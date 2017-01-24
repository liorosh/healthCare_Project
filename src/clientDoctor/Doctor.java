package clientDoctor;

import java.net.URL;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;

public class Doctor extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			URL url = getClass().getResource("DocLoginUI.fxml");
			AnchorPane pane = FXMLLoader.load( url );
			Scene scene = new Scene( pane );
			 // setting the stage
			primaryStage.setOnCloseRequest(e -> System.exit(0));
			primaryStage.setScene(scene);
			primaryStage.setTitle( "Good Health Employees" );
			primaryStage.show();

		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}
