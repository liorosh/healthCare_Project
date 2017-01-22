package clientEmployee;

import java.io.IOException;
import client.client.ChatClient;
import client.common.ChatIF;
import javafx.application.Platform;
import javafx.beans.binding.BooleanBinding;
import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import utils.models.*;

public class LoginUIController implements ChatIF{

	public LoginUIController() throws IOException{
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
/*
 * once login button is prressed the inputs from the password and login id are being sent to the server for validation
 */
    @FXML
    void login(ActionEvent event)
    {
		this.client.handleMessageFromClientUI(new clientMessage(clientMessages.employeeLogin,this.idinput.getText(),this.passinput.getText()));
    }
/*
 *initialize sets bind between the two text inputs and disables the option of sending null values to the server.
 */
	@FXML
	public void initialize()
	{
		BooleanBinding bb = new BooleanBinding()
		{
	        {
	            super.bind(idinput.textProperty(),
	            		passinput.textProperty());
	        }
	        @Override
	        protected boolean computeValue()
	        {
	            return (idinput.getText().isEmpty()|| passinput.getText().isEmpty());
	        }
	    };
    	this.login.disableProperty().bind(bb);
	}
/*
 * display is getting the login return values back from the server:
 *if login is successful then it creates the next screen and sets the chatclient to the next controller, that wat incoming messages
 *from the server will be redirected to the correct controller.
 *it also sends the the get doctors appointments query for getting the doctors appointments for that day.
 *if login has failed a popup is displayed alerting the situation and giving the user another chance.
 */
	@Override
	public void display(Object message)
	{
		if(message instanceof String )
		{
			System.out.println("why?");
		}
		serverMessage serverMessage= (serverMessage) message;
		switch(serverMessage.getMessage())
		{
		case loginFailure:
			Platform.runLater(new Runnable()
			{
				public void run()
				{//show popup
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("Login Failed");
					alert.setHeaderText("Wrong Password or ID");
					alert.setContentText("Please try again.");
					alert.showAndWait();
				}
			});
			break;
		case loginSucces:
			try {
					//setting a new stage
					FXMLLoader loader = new FXMLLoader();
					loader.setLocation(getClass().getResource("DocmainUI.fxml"));
					Parent home_page_parent = loader.load();
					//crating the new controller and sending the next request to server.
					MainUIController MainUIController = (MainUIController) loader.getController();
					employee employee=(employee) client.getUserSession();
					client.handleMessageFromClientUI(new clientMessage(clientMessages.getDoctorsAppointments,employee.getId(),null));
					MainUIController.client = client;
					//set welcome label.
					MainUIController.getWelcome().setText("Welcome Docotor "+ client.getUserSession().getFirstName()+ " "+ client.getUserSession().getLastName());
					this.client.setClient(MainUIController);
					Scene board = new Scene(home_page_parent);
					Stage board_stage = (Stage)  login.getScene().getWindow();
					//switch to the new scene and close login screen.
					Platform.runLater(new Runnable()
					{
						public void run()
						{
							board_stage.close();
							board_stage.setScene(board);
							board_stage.show();
						}
					});
				} catch(Exception e) {
					e.printStackTrace();
				}
			break;
		default:
			break;
		}

	}
}
