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

public class SetAppointmentsSystemGUI implements ChatIF
{
	//chatclient instance to communicate with server.
	ChatClient client;
	//GUI Objects defined in FXML
	//lists hold the actual object of the information and sent as is to the server.
    @FXML
    private DatePicker datePicker;

    @FXML
    private ListView<doctor> docList;

    @FXML
    private ListView<String> resList;

	@FXML
    private Label reslbl;

    @FXML
    private ListView<Appointment> hourList;

    @FXML
    private Button setApp;

    //appointments holds the received appointments from server in order to remove booked days from datePicker.
    private Collection<Appointment> appointments;
    //objects getter inorder to keep encapsulation

    public ListView<String> getResList()
    {
    	return resList;
    }
    public DatePicker getDatePicker()
    {
    	return datePicker;
    }

    public ListView<doctor> getDocList()
    {
    	return docList;
    }

    public ListView<Appointment> getHourList()
    {
    	return hourList;
    }

/* reslistener is defined to make sure a residency is picked before sending a message to the server.
 * it is also responsible on refreshing the doctors list by sending a message to the server once a residency is chosen.
 */
    @FXML
    void reslistener(MouseEvent event)
    {
		String selectedResidency = resList.getSelectionModel().getSelectedItem();
		if(null == selectedResidency)
			return;
		//get the user id from session
		patient patient = (patient)client.getUserSession();
		//send a message with the residency
		client.handleMessageFromClientUI(new clientMessage(clientMessages.detDoctorsList,selectedResidency,patient.getInsuredID()));
		//reset all gui elements to default view
		hourList.getItems().remove(0,hourList.getItems().size());
		docList.setDisable(false);
		this.datePicker.setValue(null);
		this.setApp.setVisible(false);
    	this.hourList.setVisible(false);
    	this.datePicker.setDisable(true);
    }

/* doclistener is a listener to select a doctor and get his appointments upon choosing.
 * once a selection has changed it refreshse the appointments accordingly
 */
    @FXML
    void doclistener(MouseEvent event)
    {
    	if(null != this.docList.getSelectionModel().getSelectedItem())
    	{
	    	doctor selectedDoctor = docList.getSelectionModel().getSelectedItem();
	    	//compose a client message and send to server.
	    	client.handleMessageFromClientUI(new clientMessage(clientMessages.getfreeAppointments,selectedDoctor,null));
	    	//update all of the relevant GUI elements
	    	this.datePicker.setValue(null);
	    	hourList.getItems().remove(0,hourList.getItems().size());
	    	this.setApp.setVisible(false);
	    	this.hourList.setVisible(true);
	    	this.datePicker.setDisable(false);
    	}
    }

/* applistener only listens to a selection of a certain time from appointments list
 * once an appointment is chosen it displays a button for actual booking of the appointment
 */
    @FXML
    void applistener(MouseEvent event)
    {
    	if(null != this.hourList.getSelectionModel().getSelectedItem())
    	{
	    	this.setApp.setVisible(true);
    	}
    }

/* appSet is a button handler for sending the appointments from the client to the server
 * it gets userid from the session stored in chatclient
 */
    @FXML
    void appSet(ActionEvent event)
    {
    	if(null != this.hourList.getSelectionModel().getSelectedItem())
    	{
    		//get the selected appointment and the user id from session
	    	Appointment selectedAppointment=this.hourList.getSelectionModel().getSelectedItem();
	    	patient session=(patient)this.client.getUserSession();
	    	//set userid to the appointment
	    	selectedAppointment.setInsuredID(session.getInsuredID());
	    	//compose client message and send to server
	    	client.handleMessageFromClientUI(new clientMessage(clientMessages.makeAppointment,selectedAppointment,null));
    	}
    }

/* initialize is responsible for initializing the correct date format to be used in datepicker.
 * It also sets the appointments into the listener and uses it to remove fully booked days from the calendar.
 */
    @FXML
    public void initialize()
    {
    	//date picker listener to handle date choosing, once a date is picked it displays the available appointments in that date.
    	this.datePicker.setOnAction(event -> {
        LocalDate date = this.datePicker.getValue();
        ObservableList<Appointment> appointments = FXCollections.observableArrayList();
        for(Appointment t:this.appointments)
        {
    	   if (null != date)
    	   {
	    	   String dateCompare = date.toString();
	    	   if(dateCompare.equals(t.gettAppointmentTime().substring(0, 10)))
	    	   {
	    		   appointments.add(t);
	    	   }
	   		}
        }
        //GUI object change from EDT
		Platform.runLater(new Runnable()
		{
			public void run()
			{
		        hourList.getItems().remove(0,hourList.getItems().size());
		        hourList.setItems(appointments);
			}});
    	});
    	//set converter is changing the date format to the date format we wish to be shown in the app.
    	datePicker.setConverter(new StringConverter<LocalDate>()
    	{
    	    private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
    	    @Override
    	    public String toString(LocalDate localDate)
    	    {
    	        if(localDate == null)
    	            return "";
    	        return dateTimeFormatter.format(localDate);
    	    }
    	    @Override
    	    public LocalDate fromString(String dateString)
    	    {
    	        if(dateString == null || dateString.trim().isEmpty())
    	        {
    	            return null;
    	        }
    	        return LocalDate.parse(dateString,dateTimeFormatter);
    	    }
    	});
    }
/* getDayCellFactory is responsible for removing unavailable days from calendar
 * either from the past, too fat into the future depending on the doctor or day
 * over booked and theres no need to give them as an option.
 */
    private Callback<DatePicker, DateCell> getDayCellFactory(Set<String> onlyDates)
    {

        final Callback<DatePicker, DateCell> dayCellFactory = new Callback<DatePicker, DateCell>()
        {
            @Override
            public DateCell call(final DatePicker datePicker)
            {
                return new DateCell()
                {
                    @Override
                    public void updateItem(LocalDate item, boolean empty)
                    {
                        super.updateItem(item, empty);
                        // Disable totally booked days from Calendar
                        setDisable(true);
                        setStyle("-fx-background-color: #ffc0cb;");
                        //casting from various time formats.
                        SimpleDateFormat justDate = new SimpleDateFormat("yyyy-MM-dd");
                        Calendar cal=Calendar.getInstance();
                        //after disabling all of the calendar the options are being open according to the availability
                        for(String app:onlyDates)
                		{	//casting from one date object to the other.
                        	try
                        	{
								cal.setTime(justDate.parse(app));
							} catch (ParseException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
                        	if(item.isEqual(LocalDate.parse(justDate.format(cal.getTime()))))
                        	{
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

/* display is responsible for getting messages back from the server.
 * case are according to the need from that controller.
 */
	@Override
	public void display(Object message)
	{
		if(message instanceof String )
		{
			System.out.println("why?");
		}
		//cast to server Message
		serverMessage serverMessage = (serverMessage) message;
		switch(serverMessage.getMessage())
		{
			case residenciesList:	//case residencies list, display the list in the correspoding list.
				ObservableList<String> hour = FXCollections.observableArrayList();
				for(Object t:serverMessage.getData())
				{
					hour.add(t.toString());
					System.out.print(t.toString());
				}
				//GUI object are being changed from EDT Alone.
				Platform.runLater(new Runnable()
				{
					public void run()
					{
						resList.setItems(hour);
					}
				});
				break;
			case doctorsList:	//case doctors list, display the list in the correspoding list.
				ObservableList<doctor> doctors = FXCollections.observableArrayList();
				for(Object t:serverMessage.getData())
				{
					doctors.add((doctor) t);
					System.out.println(t);
				}
				//GUI object are being changed from EDT Alone.
				Platform.runLater(new Runnable()
				{
					public void run()
					{
						docList.getItems().remove(0,docList.getItems().size());
						docList.setItems(doctors);
					}
				});
				break;
			case freeAppointmentsList:	//case appointments list, display the list in the correspoding list.
				ObservableList<Appointment> appointments= FXCollections.observableArrayList();
				Set<String> onlyDates = new HashSet<String>();
				Appointment appt;
				for(Object t:serverMessage.getData())
				{
					appt= (Appointment) t;
					appointments.add(appt);
					//get unique set of dates with no duplicates
					onlyDates.add(appt.gettAppointmentTime().substring(0, 10));
				}
				this.appointments=appointments;
				//GUI object are being changed from EDT Alone.
				Platform.runLater(new Runnable()
				{
					public void run()
					{	//send the appointments to dayCellFactory
						Callback<DatePicker, DateCell> dayCellFactory= getDayCellFactory(onlyDates);
						datePicker.setDayCellFactory(dayCellFactory);
					}
				});
				break;
			case appointmentSet:	//case appointment was set successfuly
				//GUI object are being changed from EDT Alone.
				Platform.runLater(new Runnable()
				{	//display pop reminding the appoointment details to the user and after the popup is closed the item is removed from list.
					public void run()
					{
						Appointment selectedAppointment = hourList.getSelectionModel().getSelectedItem();
						Alert alert = new Alert(AlertType.INFORMATION);
						alert.setTitle("Appointment Was Set");
						alert.setHeaderText("Your Appointment details:");
						alert.setContentText("Doctor: "+selectedAppointment.getDoctor().getFirstName() + " " + selectedAppointment.getDoctor().getLastName()
								 + "\n" + "Time: " + selectedAppointment.gettAppointmentTime() + "\n" +  "Location: " + selectedAppointment.getDoctor().getLocation());
						alert.showAndWait();
						hourList.getItems().remove(hourList.getSelectionModel().getSelectedItem());
					}
				});
				break;
			case error:		//case error, in case of a double booking attempt.
				//GUI object are being changed from EDT Alone.
				Platform.runLater(new Runnable()
				{
					// a popup window is being displayed alerting the user an error was made and remove the appointments from the list.
					public void run()
					{
						Alert alert = new Alert(AlertType.ERROR);
						alert.setTitle("Error Occured");
				    	alert.setHeaderText("Please Try Again");
				    	alert.setContentText("It Appears that this Appointment was taken.\nPlease try a Different one.");
				    	alert.showAndWait();
						hourList.getItems().remove(hourList.getSelectionModel().getSelectedItem());
						hourList.getSelectionModel().clearSelection();
					}
				});
    		break;
		default:
			break;
		}
	}
}