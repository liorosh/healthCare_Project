package clientEmployee;

import java.net.URL;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;

public class Employee extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			//ChatClient client= new ChatClient("localhost",5555,this);
			URL url = getClass().getResource("DocLoginUI.fxml");
			AnchorPane pane = FXMLLoader.load( url );
			Scene scene = new Scene( pane );
			 // setting the stage
			primaryStage.setScene(scene);
			primaryStage.setTitle( "Hello World Demo" );
			primaryStage.show();

		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}