package clientInsured;

import java.util.ArrayList;

import utils.*;
import utils.models.Appointment;
import utils.models.doctor;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;

public class SetAppointmentsSystemGUI {

	SetAppointmentsSystem logic;
	
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
    public void initialize() {
    	
    }
    private void showAppointments(int doctorID){
    	presentAppList.setVisible(true);
    	ObservableList<String> items = presentAppList.getItems();
    	ArrayList<Appointment> list= logic.getAvailableAppointments(doctorID);
    	for (Appointment app : list ){
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
    void makeApp(MouseEvent event) {
    	MakeApp.setVisible(false);
    	DoctorLable.setVisible(true);
    	cFamily.setVisible(true);
    	cSpecialist.setVisible(true);
    	
    	
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
    
   

   

}
