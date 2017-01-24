 package clientInsured;

import java.io.IOException;
import client.client.ChatClient;
import client.common.ChatIF;
import javafx.beans.value.*;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import utils.models.*;


public class PatientMainUIController implements ChatIF
{
	//chatclient instance to communicate with server.
	ChatClient client;

	//GUI objects, including table columns and welcome label
	/*@FXML
	private Parent appointments;

	@FXML
	private Parent deleteTab;*/

	@FXML
	private MyAppsController deleteTabController;

	@FXML
	private SetAppointmentsSystemGUI makeTabController;

	@FXML
	private Tab set;

	@FXML
	private Tab myapp;

	@FXML
	private Label namelbl;

	/*@FXML
	private AnchorPane anchor;*/

	@FXML
	private TabPane tabs;

/* initialize is creating a tab listener to detect tab change and pass the client to the controller in focus.
 * depending on the tab it resets the GUI and asks for updated info from server.
 * mainUI has no logic in it, it is simply enclosing the controllers of the tabs and handles client passing.
 */
	@FXML
	public void initialize() throws IOException
	{
		getMakeTabController().client=client;
		this.set.setDisable(false);
		this.myapp.setDisable(false);
		getTabs().getSelectionModel().selectedItemProperty().addListener(
			new ChangeListener<Tab>()
			{
		        @Override
		        public void changed(ObservableValue<? extends Tab> ov, Tab set, Tab myapp)
		        {
		        	if(getTabs().getSelectionModel().getSelectedItem().getId().equals("myapp"))
		        	{	//set client to myappointment tab, reset all of the current tab fields and initialize next tab data
		        		deleteTabController.client = client;
		        		client.setClient(deleteTabController);
		        		patient patient=(patient) client.getUserSession();
		        		deleteTabController.client.handleMessageFromClientUI(new clientMessage(clientMessages.getpatientAppointments,patient.getInsuredID(),null));
		        		getMakeTabController().getDatePicker().setValue(null);
		        		getMakeTabController().getDocList().getItems().remove(0, getMakeTabController().getDocList().getItems().size());
		        		getMakeTabController().getHourList().getItems().remove(0,getMakeTabController().getHourList().getItems().size());
		        		getMakeTabController().getResList().getSelectionModel().clearSelection();
		        	}
		        	else if(getTabs().getSelectionModel().getSelectedItem().getId().equals("set"))
		        	{	//set client to set appointment tab and initialize data.
		        		getMakeTabController().client = client;
		        		client.setClient(getMakeTabController());
		        		getMakeTabController().client.handleMessageFromClientUI(new clientMessage(clientMessages.getResidency,null,null));
		        	}
		        }
		    }
		);
	}
//object getters
	public Label getNamelbl()
	{
		return namelbl;
	}

	public TabPane getTabs()
	{
		return tabs;
	}

	@Override
	public void display(Object message){}
	public SetAppointmentsSystemGUI getMakeTabController() {
		return makeTabController;
	}
}