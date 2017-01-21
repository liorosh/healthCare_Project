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


public class UserController implements ChatIF
{
		ChatClient client;

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
	    private Button deleteapp;

	    @FXML
	    void cancel(ActionEvent event)
	    {
	    	if(!(null==this.apptable.getSelectionModel().getSelectedItem()))
	    	{
	    		patient patient=(utils.models.patient) client.getUserSession();
	    		this.client.handleMessageFromClientUI(new clientMessage(clientMessages.cancelAppointment,this.apptable.getSelectionModel().getSelectedItem(),patient.insuredID));
	    	}
	    }

	    public void setclient(ChatClient client){this.client=client;}

		@Override
		public void display(Object message)
		{
			serverMessage serverMessage= (serverMessage) message;
			switch(serverMessage.message)
			{
			case deleteSuccess:
				this.apptable.getItems().remove(this.apptable.getSelectionModel().getSelectedItem());
				this.apptable.getSelectionModel().clearSelection();
				break;
			case scheduledPatientsAppointments:
				ObservableList<Appointment> hour= FXCollections.observableArrayList();
				Appointment app;
				for(Object t:serverMessage.data)
				{
					app=(Appointment) t;
					hour.add(app);
				}
				date.setCellValueFactory(new PropertyValueFactory<Appointment, String>("appTime"));
				orderTime.setCellValueFactory(new PropertyValueFactory<Appointment,String>("orderTime"));
				docName.setCellValueFactory(new PropertyValueFactory<Appointment, String>("doctorName"));
				loc.setCellValueFactory(new PropertyValueFactory<Appointment,String>("location"));
				res.setCellValueFactory(new PropertyValueFactory<Appointment, String>("residency"));
				Platform.runLater(new Runnable()
				{
					public void run(){
						apptable.setItems(hour);
					}
				});
			default:
				break;
			}
		}
	}




