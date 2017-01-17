package clientInsured;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import client.client.ChatClient;
import client.common.ChatIF;
import utils.models.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

public class SetAppointmentsSystemGUI implements ChatIF{

public SetAppointmentsSystemGUI() throws IOException{


}
	ChatClient client;
	SetAppointmentsSystem logic;


   /* @FXML
    private TextField text;*/

    @FXML
    private Button cFamily;

    @FXML
    private ComboBox<?> ListOfSpecialists;

    @FXML
    private Button cSpecialist;

    @FXML
    private ListView<String> SpecialDoctorsList;

    @FXML
    private Label DoctorLable;

    @FXML
    private Button MakeApp;

    @FXML
    private ListView<String> presentAppList;

    @FXML
    private ListView<String> table;

    @FXML
    private TabPane tabs;

    @FXML
    private Tab delete;


    @FXML
    public void initialize() {
    }

    private void showAppointments(int doctorID){
    	presentAppList.setVisible(true);
    	ObservableList<String> items = presentAppList.getItems();
    	ArrayList<Appointment> list= logic.getAvailableAppointments(doctorID);
    	for (Appointment app : list )
    	{
    		items.add(app.getAppTime());
    	}
    }

    private void showDoctorsList(String residency){
    	SpecialDoctorsList.setVisible(true);
    	ObservableList<String> items = SpecialDoctorsList.getItems();
    	ArrayList<doctor> list = logic.getDoctorsList(residency) ;
    	for (doctor doc : list ){
    		items.add(doc.toString());
    	}
    }


    @FXML
    void makeApp(ActionEvent event) {


    	//client.client.handleMessageFromClientUI(new clientMessage("123",new Appointment("2017-01-20 08:00",3,new familyDoctor(1,"boobs",1))));

		client.handleMessageFromClientUI(new clientMessage("A",null));
		//serverMessage temp=(serverMessage)client.message;
		//System.out.print(temp.message+" from the gui");
    	//MakeApp.setVisible(false);
    	/*DoctorLable.setVisible(true);
    	cFamily.setVisible(true);
    	cSpecialist.setVisible(true);*/


    }

    @FXML
    void cFamilyDoc(ActionEvent event) {
    	cFamily.setVisible(false);
    	cSpecialist.setVisible(false);
      	DoctorLable.setVisible(false);
      	showAppointments(2);


    }

    @FXML
    void cSpecialistDoc(ActionEvent event) {
    	cFamily.setVisible(false);
    	cSpecialist.setVisible(false);
    	DoctorLable.setVisible(false);

    }

	@Override
	public Collection<Object> display(Object message) {
		serverMessage temp= (serverMessage) message;
		//text.setText(temp.message);
		ObservableList<String> hour= FXCollections.observableArrayList();
		for(Appointment t:temp.data){
			hour.add(t.appTime);
		}
		table.setItems(hour);
		return null;
	}
}


