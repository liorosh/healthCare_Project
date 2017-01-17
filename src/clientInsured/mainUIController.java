package clientInsured;

import java.io.IOException;
import java.util.Collection;

import client.client.ChatClient;
import client.common.ChatIF;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;

public class mainUIController implements ChatIF{
	public mainUIController() throws IOException{

		client= new ChatClient("localhost",5555,this);
	}
	@FXML
	Parent appointments;
	@FXML
	Parent deleteTab;

	@FXML
	userController deleteTabController;

	@FXML
	SetAppointmentsSystemGUI makeTabController;

	ChatClient client;
	SetAppointmentsSystem logic;

	@FXML
	Tab set;
	@FXML
	Tab myapp;

	@FXML
	public void changeTab(ActionEvent event){

	}
	@Override
	public Collection<Object> display(Object message) {
		// TODO Auto-generated method stub
		return null;
	}

	@FXML
    private TextField text;

	@FXML
	private TabPane tabs;

	@FXML
	public void initialize() throws IOException
	{
		System.out.println(deleteTabController);
		System.out.println(deleteTabController);
		/*deleteTabController.client=client;
		client.setClient(deleteTabController);*/
		makeTabController.client=client;
		client.setClient(makeTabController);
		tabs.getSelectionModel().selectedItemProperty().addListener(
			    new ChangeListener<Tab>() {
			        @Override
			        public void changed(ObservableValue<? extends Tab> ov, Tab set, Tab myapp) {
			        	if(tabs.getSelectionModel().getSelectedItem().getId().equals("myapp")){
			        		deleteTabController.client=client;
			        		client.setClient(deleteTabController);
			        		System.out.print("1");
			        	}
			        	else if(tabs.getSelectionModel().getSelectedItem().getId().equals("set")){
			        		makeTabController.client=client;
			        		client.setClient(makeTabController);
			        		System.out.print("2");
			        	}
			        }
			    }
			);
	}
}