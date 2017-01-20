package clientInsured;

import java.io.IOException;
import java.util.Collection;
import client.client.ChatClient;
import client.common.ChatIF;
import javafx.application.Platform;
import javafx.collections.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import utils.appointment;
import utils.models.*;


public class UserController implements ChatIF{
		ChatClient client;

	    @FXML
	    private TableView<appointment> apptable;
		@FXML
	    private TableColumn<appointment, String> date;

	    @FXML
	    private TableColumn<appointment, String> res;

	    @FXML
	    private TableColumn<appointment, String> docName;

	    @FXML
	    private TableColumn<appointment, String> loc;

	    @FXML
	    private TableColumn<appointment, String> orderTime;

	    @FXML
	    private Button set;

	    @FXML
	    private TextField text;

	    @FXML
	    private ListView<String> table;

	    @FXML
	    void tryquery(ActionEvent event) {
	    	patient patient=(utils.models.patient) client.getUserSession();
	    	client.handleMessageFromClientUI(new clientMessage(clientMessages.getpatientAppointments,patient.insuredID,null));
	    }

	    public void setclient(ChatClient client){this.client=client;}

		@Override
		public void display(Object message) {
			serverMessage temp= (serverMessage) message;
			text.setText((String) temp.message);
			if(temp.message.equals("myapps"))
			{
				ObservableList<appointment> hour= FXCollections.observableArrayList();
				Appointment app;
				for(Object t:temp.data)
				{
					app=(Appointment) t;
					hour.add(new appointment(app.appTime,app.orderTime,app.doctor.residency,app.getDoctor().firstName/*+" "+app.getDoctor().lastName*/,app.doctor.location));
				}
				date.setCellValueFactory(new PropertyValueFactory<appointment, String>("apptime"));
				orderTime.setCellValueFactory(new PropertyValueFactory<appointment,String>("orderTime"));
				docName.setCellValueFactory(new PropertyValueFactory<appointment, String>("doctorName"));
				loc.setCellValueFactory(new PropertyValueFactory<appointment,String>("location"));
				res.setCellValueFactory(new PropertyValueFactory<appointment, String>("residency"));

				Platform.runLater(new Runnable()
				{
					public void run(){
						apptable.setItems(hour);
					}
				});
			}
		}
		@FXML
		public void initialize() throws IOException{

		}


	    @FXML
	    void print(MouseEvent event) {
	    System.out.println(apptable.getSelectionModel().getSelectedItem().getdoctorName());

	    }


}
