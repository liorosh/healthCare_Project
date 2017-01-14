package clientInsured;


import java.awt.event.ActionEvent;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;

public class MakeAppointmentController {

    @FXML
    private Button cFamily;

    @FXML
    private ComboBox<?> ListOfSpecialists;

    @FXML
    private Button cSpecialist;

    @FXML
    private ListView<?> SpecialDoctorsList;

    @FXML
    private Label DoctorLable;

    @FXML
    private Button MakeApp;

    @FXML
    void makeApp(MouseEvent event) {
    	MakeApp.setVisible(false);
    	DoctorLable.setVisible(true);
    	cFamily.setVisible(true);
    	cSpecialist.setVisible(true);
    	
    	
    }
    
   

}
