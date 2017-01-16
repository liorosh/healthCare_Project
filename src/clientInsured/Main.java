package clientInsured;

import java.net.URL;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import client.ClientConsole;

public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			//TODO send client to client's Controller
			//ClientConsole client= new ClientConsole("localhost",5555);

			 // constructing our scene
			 URL url = getClass().getResource("MakeAppoint.fxml");
			 AnchorPane pane = FXMLLoader.load( url );
			 Scene scene = new Scene( pane );
			 // setting the stage
			 primaryStage.setScene( scene );
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
