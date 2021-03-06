package clientDoctor;

import client.client.ChatClient;
import client.common.ChatIF;
import javafx.application.Platform;
import javafx.collections.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import utils.models.*;


public class DoctorMainUIController implements ChatIF
{
	//client object for sending messages to server.
	ChatClient client;

	//GUI objects, including table columns and welcome label
    @FXML
     Label welcome;

	@FXML
	 TableColumn<Appointment, String> namecol;

	@FXML
	 TableColumn<Appointment, String> appcol;

	@FXML
	 TableView<Appointment> table;

    @FXML
    private Button checkNew;
/*
 * display function is reposible on getting messages from server.
 * in this case its only job is to display the doctors appointments once doctor is logged in
 */
	@Override
	public void display(Object message)
	{
		serverMessage Message= (serverMessage) message;
		if(serverMessages.doctorsAppointmentsList==Message.getMessage())
		{	//doctorAppointments is being inserted with appointments received from the server and is inserted into table
			ObservableList<Appointment> doctorAppointments= FXCollections.observableArrayList();
			Appointment app;
			for(Object t:Message.getData())//loop over appointments
			{
				app=(Appointment) t;
				doctorAppointments.add(app);
			}//insert values into the corresponding colums
			appcol.setCellValueFactory(new PropertyValueFactory<Appointment, String>("appTime"));
			namecol.setCellValueFactory(new PropertyValueFactory<Appointment,String>("InsuredFullName"));
			Platform.runLater(new Runnable()
			{
				public void run()
				{
					table.setItems(doctorAppointments);
				}
			});
		}
	}

	@FXML

	public Label getWelcome()
	{
		return welcome;
	}
/*fetchNewAppointments is responsible for updating the list of appointments in
 * case any appointment was scheduled after the doctor has signed in
 */
    @FXML
    void fetchNewAppointments(ActionEvent event) {
    	employee employee=(employee) client.getUserSession();
		client.handleMessageFromClientUI(new clientMessage(clientMessages.getDoctorsAppointments,employee.getId(),null));
    }
}
