package clientInsured;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Optional;
import java.util.Timer;
import java.util.TimerTask;

import client.client.ChatClient;
import client.common.ChatIF;
import javafx.application.Platform;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;
import utils.models.clientMessage;
import utils.models.clientMessages;
import utils.models.serverMessage;

public class mainUIController implements ChatIF{
	public mainUIController() throws IOException{

		client= new ChatClient("localhost",5555,this);
	}
	ChatClient client;
	SetAppointmentsSystem logic;
	@FXML
	Parent appointments;
	@FXML
	Parent deleteTab;

	@FXML
	userController deleteTabController;

	@FXML
	SetAppointmentsSystemGUI makeTabController;
	   @FXML
	    private TextField password;

	    @FXML
	    private Label idLabel;

	    @FXML
	    private Label passlabel;

	    @FXML
	    private TextField insuredID;

	    @FXML
	     private Button log;

	    @FXML
	    void Login(ActionEvent event) {
	    	this.client.handleMessageFromClientUI(new clientMessage(clientMessages.Login,insuredID.getText(),password.getText()));

	    }





	    @FXML
	    public void insertlisten(ActionEvent event)
	    {

	    	if(null!=this.insuredID && null!=this.password)
	    		this.log.setDisable(false);
	    	else
	    		this.log.setDisable(true);
	    }
    @FXML
    Tab login;
	@FXML
	Tab set;
	@FXML
	Tab myapp;

	@FXML
	public void changeTab(ActionEvent event){

	}
	@Override
	public Collection<Object> display(Object message) {
		if(message instanceof String ){
			System.out.println("why?");
			return null;
		}

		serverMessage temp= (serverMessage) message;
		if(temp.data==null){
			System.out.println("not logged in");
		}
		else{
			System.out.println("logged in");
			Platform.runLater(new Runnable(){
				public void run(){
					tabs.getSelectionModel().select(1);
					tabs.getTabs().remove(0);

				}
			});
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


		return null;
	}

	@FXML
	AnchorPane anchor;

	@FXML
    private TextField text;

	@FXML
	private TabPane tabs;

	@FXML
	public void initialize() throws IOException
	{
	    BooleanBinding bb = new BooleanBinding() {
	        {
	            super.bind(insuredID.textProperty(),
	            		password.textProperty());
	        }

	        @Override
	        protected boolean computeValue() {
	            return (insuredID.getText().isEmpty()
	                    || password.getText().isEmpty());
	        }
	    };
    	this.log.disableProperty().bind(bb);
		//getLogin();
		//makeTabController.client=client;
		//client.setClient(makeTabController);
		/*makeTabController.client.handleMessageFromClientUI(new clientMessage(clientMessages.getResidency,null,null));
		tabs.getSelectionModel().selectedItemProperty().addListener(
			    new ChangeListener<Tab>() {
			        @Override
			        public void changed(ObservableValue<? extends Tab> ov, Tab set, Tab myapp) {
			        	if(tabs.getSelectionModel().getSelectedItem().getId().equals("myapp")){
			        		deleteTabController.client=client;
			        		client.setClient(deleteTabController);
			        		makeTabController.getDatePicker().setValue(null);
			        		makeTabController.getDocList().getItems().remove(0, makeTabController.getDocList().getItems().size());
			        		makeTabController.getHourList().getItems().remove(0,makeTabController.getHourList().getItems().size());
			        		makeTabController.getResList().getSelectionModel().clearSelection();
			        		System.out.print("1");
			        	}
			        	else if(tabs.getSelectionModel().getSelectedItem().getId().equals("set")){
			        	makeTabController.client=client;
			        		client.setClient(makeTabController);
			        		System.out.print("2");
			        	}
			        }
			    }
			);*/
	}
	public TabPane getTabs() {
		return tabs;
	}
	public void setTabs(TabPane tabs) {
		this.tabs = tabs;
	}
}