package clientEmployee;

import java.io.IOException;
import java.util.Collection;

import client.client.ChatClient;
import client.common.ChatIF;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import utils.appointment;
import utils.models.*;




public class MainUIController implements ChatIF{
	public MainUIController() throws IOException{

	}
	ChatClient client;


    @FXML
    private Button btn;


    @FXML
    private Label welcome;

	@FXML
	private TableColumn<Appointment, String> namecol;

	@FXML
	private TableColumn<Appointment, String> appcol;

	@FXML
	private TableView<Appointment> table;



    @FXML
    void getapps(ActionEvent event) {
    employee employee=(employee) client.getUserSession();
	client.handleMessageFromClientUI(new clientMessage(clientMessages.getDoctorsAppointments,employee.id,null));

    }
@FXML
public void initialize(){
	System.out.println("i shouldnt print!");
}



	@Override
	public void display(Object message) {
		serverMessage Message= (serverMessage) message;

		if(Message.message.equals("docapps")){
		final ObservableList<Appointment> hour= FXCollections.observableArrayList();
		Appointment app;
		for(Object t:Message.data){
			app=(Appointment) t;
			hour.add(app);
		}
		appcol.setCellValueFactory(new PropertyValueFactory<Appointment, String>("appTime"));
		namecol.setCellValueFactory(new PropertyValueFactory<Appointment,String>("InsuredFullName"));
		Platform.runLater(new Runnable(){
			public void run(){
				table.setItems(hour);
			}
		});
	}
	}
	public Label getWelcome() {
		return welcome;
	}
	public void setWelcome(Label welcome) {
		this.welcome = welcome;
	}
}
