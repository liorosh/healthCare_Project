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
import javafx.scene.layout.GridPane;
import javafx.util.Pair;
import utils.models.clientMessage;
import utils.models.clientMessages;
import utils.models.serverMessage;

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
		if(message instanceof String ){
			System.out.println("why?");
			return null;
		}

		serverMessage temp= (serverMessage) message;
		if(temp.data==null){
			System.out.print("not logged in");
		this.getLogin();
		}
		else{
			System.out.print("logged in");
			makeTabController.client=client;
			client.setClient(makeTabController);
			this.set.setDisable(false);
			this.myapp.setDisable(false);
			makeTabController.client.handleMessageFromClientUI(new clientMessage(clientMessages.getResidency,null,null));
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
				);
		}


		return null;
	}

	@FXML
    private TextField text;

	@FXML
	private TabPane tabs;


	private void getLogin()
	{
		// Create the custom dialog.
		Dialog<Pair<String, String>> dialog = new Dialog<>();
		dialog.setTitle("Login Dialog");
		dialog.setHeaderText("Look, a Custom Login Dialog");

		// Set the icon (must be included in the project).
		//dialog.setGraphic(new ImageView(this.getClass().getResource("login.png").toString()));

		// Set the button types.
		ButtonType loginButtonType = new ButtonType("Login", ButtonData.OK_DONE);
		dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

		// Create the username and password labels and fields.
		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(20, 150, 10, 10));

		TextField username = new TextField();
		username.setPromptText("Username");
		PasswordField password = new PasswordField();
		password.setPromptText("Password");

		grid.add(new Label("Username:"), 0, 0);
		grid.add(username, 1, 0);
		grid.add(new Label("Password:"), 0, 1);
		grid.add(password, 1, 1);

		// Enable/Disable login button depending on whether a username was entered.
		Node loginButton = dialog.getDialogPane().lookupButton(loginButtonType);
		loginButton.setDisable(true);

		// Do some validation (using the Java 8 lambda syntax).
		username.textProperty().addListener((observable, oldValue, newValue) -> {
		    loginButton.setDisable(newValue.trim().isEmpty());
		});

		dialog.getDialogPane().setContent(grid);

		// Request focus on the username field by default.
		Platform.runLater(() -> username.requestFocus());

		// Convert the result to a username-password-pair when the login button is clicked.
		dialog.setResultConverter(dialogButton -> {
		    if (dialogButton == loginButtonType) {
		    	this.client.handleMessageFromClientUI(new clientMessage(clientMessages.Login,username.getText(),password.getText()));
		        //return new Pair<>(username.getText(), password.getText());
		    }
		    return null;
		});

		Optional<Pair<String, String>> result = dialog.showAndWait();

		result.ifPresent(usernamePassword -> {
		    System.out.println("Username=" + usernamePassword.getKey() + ", Password=" + usernamePassword.getValue());
		});
		/*Dialog<String> dialog= new Dialog<>();
		Label label1 = new Label("Name: ");
		Label label2 = new Label("Phone: ");
		TextField text1 = new TextField();
		TextField text2 = new TextField();
		GridPane grid = new GridPane();
		Button buttonTypeOk = new Button("Okay");
		buttonTypeOk.setDisable(true);
		grid.add(label1, 1, 1);
		grid.add(text1, 2, 1);
		grid.add(label2, 1, 2);
		grid.add(text2, 2, 2);
		grid.add(buttonTypeOk,3,3);
		dialog.getDialogPane().setContent(grid);
		buttonTypeOk.setOnAction(ActionEvent event){

		}

		dialog.showAndWait();*/

	}
	@FXML
	public void initialize() throws IOException
	{
		getLogin();
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
}