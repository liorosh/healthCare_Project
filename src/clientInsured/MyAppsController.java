package clientInsured;

import client.client.ChatClient;
import client.common.ChatIF;
import javafx.application.Platform;
import javafx.collections.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import utils.models.*;


public class MyAppsController implements ChatIF
{
		ChatClient client;

		//GUI objects.
	    @FXML
	    private TableView<Appointment> apptable;

		@FXML
	    private TableColumn<Appointment, String> date;

	    @FXML
	    private TableColumn<Appointment, String> res;

	    @FXML
	    private TableColumn<Appointment, String> docName;

	    @FXML
	    private TableColumn<Appointment, String> loc;

	    @FXML
	    private TableColumn<Appointment, String> orderTime;

	    @FXML
	    private Button deleteApp;

/* cancel is a handler for the cancel appointment app
 * according to the selected row it sends the appointment to the server for cancellation
 * */
	    @FXML
	    void cancel(ActionEvent event)
	    {
	    	if(!(null==this.apptable.getSelectionModel().getSelectedItem()))	//make sure null will not be sent to server
	    	{
	    		patient patient=(patient) client.getUserSession();	//get user id for verification purposes.
	    		this.client.handleMessageFromClientUI(new clientMessage(clientMessages.cancelAppointment,this.apptable.getSelectionModel().getSelectedItem(),patient.getInsuredID()));
	    	}
	    }
/* display is in charge of getting messages from server and handle the data, put in place in the correct GUI  object
 * param is a server message object that holds the data and the message attached to it.
 */
		@Override
		public void display(Object message)
		{		//cast to server message type.
			serverMessage serverMessage= (serverMessage) message;
			switch(serverMessage.getMessage())
			{
			case deleteSuccess:		//case delete, no data returned. appointment is remove from list
				this.apptable.getItems().remove(this.apptable.getSelectionModel().getSelectedItem());
				this.apptable.getSelectionModel().clearSelection();
				break;
			case scheduledPatientsAppointments:		//case getting the patients appointments.
				ObservableList<Appointment> hour= FXCollections.observableArrayList();
				Appointment app;
				for(Object t:serverMessage.getData())
				{
					app=(Appointment) t;
					hour.add(app);
				}	//setting every column with the correspoding data
				date.setCellValueFactory(new PropertyValueFactory<Appointment, String>("appTime"));
				orderTime.setCellValueFactory(new PropertyValueFactory<Appointment,String>("orderTime"));
				docName.setCellValueFactory(new PropertyValueFactory<Appointment, String>("doctorName"));
				loc.setCellValueFactory(new PropertyValueFactory<Appointment,String>("location"));
				res.setCellValueFactory(new PropertyValueFactory<Appointment, String>("residency"));
				Platform.runLater(new Runnable()
				{	//changing GUI Objects is performed on EDT alone.
					public void run(){
						apptable.setItems(hour);
					}
				});
			default:
				break;
			}
		}
	}