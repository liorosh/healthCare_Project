package clientInsured;


import java.text.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import client.client.*;
import client.common.*;
import utils.models.*;
import javafx.application.Platform;
import javafx.collections.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.util.*;

public class SetAppointmentsSystemGUI implements ChatIF{

	ChatClient client;
	SetAppointmentsSystem logic;

    @FXML
    public DatePicker datePicker;

    @FXML
    private ListView<doctor> docList;

    @FXML
    private ListView<String> resList;

    public ListView<String> getResList() {
		return resList;
	}
	@FXML
    private Label reslbl;

    public DatePicker getDatePicker() {
		return datePicker;
	}

	public ListView<doctor> getDocList() {
		return docList;
	}

	public ListView<Appointment> getHourList() {
		return hourList;
	}

	private Collection<Appointment>appointments;

    @FXML
    private ListView<Appointment> hourList;


    @FXML
    private Button setApp;

    @FXML
    void reslistener(MouseEvent event) {
		String selectedResidency=resList.getSelectionModel().getSelectedItem();
		if(null==selectedResidency)
			return;
		patient patient=(patient)client.getUserSession();
		client.handleMessageFromClientUI(new clientMessage(clientMessages.detDoctorsList,selectedResidency,patient.insuredID));
		hourList.getItems().remove(0,hourList.getItems().size());
		docList.setDisable(false);
		this.datePicker.setValue(null);
		this.setApp.setVisible(false);
    	this.hourList.setVisible(false);
    	this.datePicker.setDisable(true);
    }



    @FXML
    void doclistener(MouseEvent event) {
    	if(null!=this.docList.getSelectionModel().getSelectedItem())
    	{
    	doctor selectedDoctor=docList.getSelectionModel().getSelectedItem();
    	System.out.println(selectedDoctor);
    	client.handleMessageFromClientUI(new clientMessage(clientMessages.getfreeAppointments,selectedDoctor,null));
    	this.datePicker.setValue(null);
    	hourList.getItems().remove(0,hourList.getItems().size());
    	this.setApp.setVisible(false);
    	this.hourList.setVisible(true);
    	this.datePicker.setDisable(false);
    	}

    }

    @FXML
    void applistener(MouseEvent event)
    {
    	if(null!=this.hourList.getSelectionModel().getSelectedItem())
    	{
    	Appointment selectedAppointment=this.hourList.getSelectionModel().getSelectedItem();
    	System.out.println(selectedAppointment);
    	this.setApp.setVisible(true);
    	}
    }

    @FXML
    void appSet(ActionEvent event) {
    	if(null!=this.hourList.getSelectionModel().getSelectedItem())
    	{
    	Appointment selectedAppointment=this.hourList.getSelectionModel().getSelectedItem();
    	patient session=(patient)this.client.getUserSession();
    	selectedAppointment.insuredID=session.insuredID;
    	client.handleMessageFromClientUI(new clientMessage(clientMessages.makeAppointment,selectedAppointment,null));
    	Alert alert = new Alert(AlertType.INFORMATION);
    	alert.setTitle("Appointment Was Set");
    	alert.setHeaderText("Your Appointment details:");
    	alert.setContentText("Doctor: "+selectedAppointment.doctor.firstName+" "+selectedAppointment.doctor.lastName
			+"\n"+"Time: "+selectedAppointment.appTime+"\n"+ "Location: "+ selectedAppointment.doctor.location);

    	alert.showAndWait();
    	this.hourList.getItems().remove(this.hourList.getSelectionModel().getSelectedItem());
    	}
    }

    @FXML
    public void initialize()
    {
    	datePicker.setOnAction(event -> {
        LocalDate date = datePicker.getValue();
        ObservableList<Appointment> appointments= FXCollections.observableArrayList();
        for(Appointment t:this.appointments)
        {
    	   if (null!=date){
    	   String dateCompare=date.toString();
    	   if(dateCompare.equals(t.appTime.substring(0, 10)))
    	   {
    		   appointments.add(t);
    	   }
	   	}
        }
        hourList.getItems().remove(0,hourList.getItems().size());
        hourList.setItems(appointments);
    	});
    	datePicker.setConverter(new StringConverter<LocalDate>()
    	{
    	    private DateTimeFormatter dateTimeFormatter=DateTimeFormatter.ofPattern("yyyy/MM/dd");

    	    @Override
    	    public String toString(LocalDate localDate)
    	    {
    	        if(localDate==null)
    	            return "";
    	        return dateTimeFormatter.format(localDate);
    	    }

    	    @Override
    	    public LocalDate fromString(String dateString)
    	    {
    	        if(dateString==null || dateString.trim().isEmpty())
    	        {
    	            return null;
    	        }
    	        return LocalDate.parse(dateString,dateTimeFormatter);
    	    }
    	});
    }

    private Callback<DatePicker, DateCell> getDayCellFactory(Set<String> onlyDates) {

        final Callback<DatePicker, DateCell> dayCellFactory = new Callback<DatePicker, DateCell>() {
            @Override
            public DateCell call(final DatePicker datePicker) {
                return new DateCell() {
                    @Override
                    public void updateItem(LocalDate item, boolean empty) {
                        super.updateItem(item, empty);
                        // Disable totall booked days from Calendar
                        setDisable(true);
                        setStyle("-fx-background-color: #ffc0cb;");
                        SimpleDateFormat justDate= new SimpleDateFormat("yyyy-MM-dd");
                        Calendar cal=Calendar.getInstance();
                        for(String app:onlyDates){
                        	try {
								cal.setTime(justDate.parse(app));
							} catch (ParseException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
                        	if(item.isEqual(LocalDate.parse(justDate.format(cal.getTime())))){
                        		  setDisable(false);
                                  setStyle(null);
                        	}
                        }
                    }
                };
            }
        };
        return dayCellFactory;
    }

	@Override
	public void display(Object message) {
		if(message instanceof String ){
			System.out.println("why?");

		}

		serverMessage temp= (serverMessage) message;
		if(temp.message.equals("residencies")){
		ObservableList<String> hour= FXCollections.observableArrayList();
		for(Object t:temp.data){
			hour.add(t.toString());
			System.out.print(t.toString());
		}
		Platform.runLater(new Runnable(){
			public void run(){
			resList.setItems(hour);
			}
		});
		}
		else if(temp.message.equals("doctors")){
		ObservableList<doctor> doctors= FXCollections.observableArrayList();
		for(Object t:temp.data){
			doctors.add((doctor) t);
			System.out.println(t);
		}
		Platform.runLater(new Runnable(){
			public void run(){
				docList.getItems().remove(0,docList.getItems().size());
				docList.setItems(doctors);
			}
		});
		}
		else if(temp.message.equals("appointments"))
		{
			ObservableList<Appointment> appointments= FXCollections.observableArrayList();
			Set<String> onlyDates=new HashSet<String>();
			Appointment appt;
			for(Object t:temp.data){
				appt= (Appointment) t;
				appointments.add(appt);
				//get unique set of dates with no duplicates
				onlyDates.add(appt.appTime.substring(0, 10));
			}
			this.appointments=appointments;
			Platform.runLater(new Runnable(){
				public void run(){
					 Callback<DatePicker, DateCell> dayCellFactory= getDayCellFactory(onlyDates);
					 datePicker.setDayCellFactory(dayCellFactory);
				}
			});
		}

	}

}

