package clientInsured;

import java.io.IOException;
import java.net.URL;
import java.util.Collection;

import client.client.ChatClient;
import client.common.ChatIF;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import utils.models.Appointment;
import utils.models.clientMessage;
import utils.models.serverMessage;

public class userController implements ChatIF{

	public userController() throws IOException
	{


	}
		ChatClient client;

		AnchorPane parent;
	    @FXML
	    private Button set;

	    @FXML
	    private TextField text;

	    @FXML
	    private ListView<String> table;

	    @FXML
	    void tryquery(ActionEvent event) {
	    	client.handleMessageFromClientUI(new clientMessage("A",null));
	    }

	    public void setclient(ChatClient client){this.client=client;}

		@Override
		public Collection<Object> display(Object message) {
			serverMessage temp= (serverMessage) message;
			text.setText(temp.message);
			ObservableList<String> hour= FXCollections.observableArrayList();
			for(Appointment t:temp.data){
				hour.add(t.appTime);
			}
			table.setItems(hour);
			return null;
		}
		@FXML
		public void initialize() throws IOException{

		}

		public void print(String d){
			System.out.print(d);
		}


}
