package clientInsured;

import java.io.IOException;
import java.util.Collection;
import client.client.ChatClient;
import client.common.ChatIF;
import javafx.application.Platform;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.value.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import utils.models.*;


public class MainUIController implements ChatIF{
	public MainUIController() throws IOException{

		client= new ChatClient("localhost",5555,this);
	}
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
	@Override
	public void display(Object message)
	{

	}

	@FXML
	AnchorPane anchor;

	@FXML
	private TabPane tabs;

	@FXML
	public void initialize() throws IOException
	{
		System.out.println("mainui");
		makeTabController.client=client;
		client.setClient(makeTabController);
		this.set.setDisable(false);
		this.myapp.setDisable(false);
		makeTabController.client.handleMessageFromClientUI(new clientMessage(clientMessages.getResidency,null,null));
		getTabs().getSelectionModel().selectedItemProperty().addListener(
			new ChangeListener<Tab>() {
		        @Override
		        public void changed(ObservableValue<? extends Tab> ov, Tab set, Tab myapp) {
		        	if(getTabs().getSelectionModel().getSelectedItem().getId().equals("myapp")){
		        		deleteTabController.client=client;
		        		client.setClient(deleteTabController);
		        		patient patient=(utils.models.patient) client.getUserSession();
		        		deleteTabController.client.handleMessageFromClientUI(new clientMessage(clientMessages.getpatientAppointments,patient.insuredID,null));
		        		makeTabController.getDatePicker().setValue(null);
		        		makeTabController.getDocList().getItems().remove(0, makeTabController.getDocList().getItems().size());
		        		makeTabController.getHourList().getItems().remove(0,makeTabController.getHourList().getItems().size());
		        		makeTabController.getResList().getSelectionModel().clearSelection();
		        		System.out.print("1");
		        	}
		        	else if(getTabs().getSelectionModel().getSelectedItem().getId().equals("set")){
		        	makeTabController.client=client;
		        		client.setClient(makeTabController);
		        		System.out.print("2");
		        	}
		        }
		    }
		);
	}
	public TabPane getTabs() {
		return tabs;
	}
	public void setTabs(TabPane tabs) {
		this.tabs = tabs;
	}
}