package clientInsured;

import java.io.IOException;
import java.util.Collection;
import client.client.ChatClient;
import client.common.ChatIF;
import javafx.application.Platform;
import javafx.beans.binding.BooleanBinding;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import utils.models.clientMessage;
import utils.models.clientMessages;
import utils.models.serverMessage;

public class PatientLoginUIController implements ChatIF{

	public PatientLoginUIController() throws IOException{

		client= new ChatClient("localhost",5555,this);
	}
	ChatClient client;

    @FXML
    private TextField idinput;

    @FXML
    private Button login;

    @FXML
    private Label idlbl;

    @FXML
    private TextField passinput;

    @FXML
    private Label passlbl;

    @FXML
    void login(ActionEvent event) {
		this.client.handleMessageFromClientUI(new clientMessage(clientMessages.insuredLogin,this.idinput.getText(),this.passinput.getText()));
    }



	@FXML
	public void initialize(){
		System.out.println("client??");

		BooleanBinding bb = new BooleanBinding() {
	        {
	            super.bind(idinput.textProperty(),
	            		passinput.textProperty());
	        }

	        @Override
	        protected boolean computeValue() {
	            return (idinput.getText().isEmpty()
	                    || passinput.getText().isEmpty());
	        }
	    };
    	this.login.disableProperty().bind(bb);




	}

	@Override
	public void display(Object message) {
		if(message instanceof String )
		{
			System.out.println("why?");

		}
		serverMessage temp= (serverMessage) message;
		if(temp.data==null)
		{
			System.out.println("not logged in");
	   		Platform.runLater(new Runnable(){
					public void run(){
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Login Failed");
				alert.setHeaderText("Wrong Password or ID");
				alert.setContentText("Please try again.");
				alert.showAndWait();
					}
				});
		}
		else
		{
			System.out.println("logged in");
	 	try {

    		FXMLLoader loader = new FXMLLoader();
    		loader.setLocation(getClass().getResource("InsuredmainUI.fxml"));
    		Parent home_page_parent = loader.load(/*getClass().getResource("InsuredmainUI.fxml").openStream()*/);
    		MainUIController MainUIController = (MainUIController) loader
    				.getController();
    		MainUIController.client=client;
    		this.client.setClient(MainUIController);
    		System.out.println("mainui");
    		Scene board = new Scene(home_page_parent);
    		Stage board_stage = (Stage)  login.getScene().getWindow();
    		Platform.runLater(new Runnable(){
				public void run(){
					board_stage.close();
					board_stage.setScene(board);
					board_stage.show();
				}
			});
    	//	board_stage.close();
		} catch(Exception e) {
			e.printStackTrace();
		}

	}


	}
}
