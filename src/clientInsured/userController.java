package clientInsured;

import java.io.IOException;
import java.lang.reflect.Array;
import java.net.URL;
import java.util.Collection;

import client.client.ChatClient;
import client.common.ChatIF;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.util.Callback;
import utils.models.*;


public class userController implements ChatIF{
		ChatClient client;

	    @FXML
	    private TableView<ObservableList> apptable;
		@FXML
	    private TableColumn<ObservableList, String> date;

	    @FXML
	    private TableColumn<ObservableList, String> res;

	    @FXML
	    private TableColumn<ObservableList, String> docName;

	    @FXML
	    private TableColumn<ObservableList, String> loc;

	    @FXML
	    private Button set;

	    @FXML
	    private TextField text;

	    @FXML
	    private ListView<String> table;

	    @FXML
	    void tryquery(ActionEvent event) {
	    	client.handleMessageFromClientUI(new clientMessage(clientMessages.getAppointments,null,null));
	    }

	    public void setclient(ChatClient client){this.client=client;}

		@Override
		public Collection<Object> display(Object message) {
			serverMessage temp= (serverMessage) message;
			text.setText((String) temp.message);
			ObservableList<Appointment> hour= FXCollections.observableArrayList();

			for(Object t:temp.data){
				hour.add((Appointment) t);
			}
			//Appointment app=(Appointment)temp.data;

			//this.docName.setCellValueFactory(new PropertyValueFactory(t.doctor.fName+t.doctor.lName));

			//this.docName.setCellValueFactory(new PropertyValueFactory<String,String>("fName"));
			//apptable.setItems(doctor,hour);
			docName.setVisible(true);
			return null;
		}
		@FXML
		public void initialize() throws IOException{

		}

		public void print(String d){
			System.out.print(d);
		}


}
