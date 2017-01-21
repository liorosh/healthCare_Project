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


public class MainUIController implements ChatIF
{
	ChatClient client;

	@FXML
	Parent appointments;

	@FXML
	Parent deleteTab;

	@FXML
	UserController deleteTabController;

	@FXML
	SetAppointmentsSystemGUI makeTabController;

	@FXML
	Tab set;

	@FXML
	Tab myapp;

	@FXML
	Label namelbl;


	@FXML
	AnchorPane anchor;

	@FXML
	private TabPane tabs;


	@FXML
	public void initialize() throws IOException
	{
		System.out.println("mainui");
		makeTabController.client=client;
		this.set.setDisable(false);
		this.myapp.setDisable(false);
		getTabs().getSelectionModel().selectedItemProperty().addListener(
			new ChangeListener<Tab>()
			{
		        @Override
		        public void changed(ObservableValue<? extends Tab> ov, Tab set, Tab myapp)
		        {
		        	if(getTabs().getSelectionModel().getSelectedItem().getId().equals("myapp"))
		        	{
		        		deleteTabController.client=client;
		        		client.setClient(deleteTabController);
		        		patient patient=(patient) client.getUserSession();
		        		deleteTabController.client.handleMessageFromClientUI(new clientMessage(clientMessages.getpatientAppointments,patient.insuredID,null));
		        		makeTabController.getDatePicker().setValue(null);
		        		makeTabController.getDocList().getItems().remove(0, makeTabController.getDocList().getItems().size());
		        		makeTabController.getHourList().getItems().remove(0,makeTabController.getHourList().getItems().size());
		        		makeTabController.getResList().getSelectionModel().clearSelection();
		        		System.out.print("1");
		        	}
		        	else if(getTabs().getSelectionModel().getSelectedItem().getId().equals("set"))
		        	{
		        		makeTabController.client=client;
		        		client.setClient(makeTabController);
		        		makeTabController.client.handleMessageFromClientUI(new clientMessage(clientMessages.getResidency,null,null));
		        		System.out.print("2");
		        	}
		        }
		    }
		);
	}

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
}