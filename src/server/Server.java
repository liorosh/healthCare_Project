package server;
// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com

import java.io.IOException;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.TimeUnit;
import utils.models.*;
import ocsf.server.*;

/**
 * This class overrides some of the methods in the abstract
 * superclass in order to give more functionality to the server.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;re
 * @author Fran&ccedil;ois B&eacute;langer
 * @author Paul Holden
 * @version July 2000
 */
public class Server extends AbstractServer
{
	//Singleton instance to handle the logic and the actual eonnection to the database and queries.
	//the sever only deals with routing messages back and forth from the server amd clients
	 serverLogic logic;


  //Class variables *************************************************

  /**
   * The default port to listen on.
   */
  final public static int DEFAULT_PORT = 5555;

  //Constructors ****************************************************

  /**
   * Constructs an instance of the echo server.
   *
   * @param port The port number to connect on.
   */
  public Server(int port)
  {
    super(port);
  }
  public Server(int port,serverLogic logic)
  {
	  super(port);
	  this.logic=logic;
  }


  //Instance methods ************************************************

  /**
   * This method handles any messages received from the client.
   *
   * @param msg The message received from the client.
   * @param client The connection from which the message originated.
   */
  /*
   *msg is always of type clientMessage that holds the message and the data needed to execute the action asked
   *data is the itself, whether its a doctor object or an appointments object
      */
  public void handleMessageFromClient
    (Object msg, ConnectionToClient client)
  {
	 clientMessage message=(clientMessage) msg;
	 //initializing a serverMessage object to send back to the user.
	 serverMessage results = null;
	switch(message.clientmessage){
	//login is divided into two seperate cases of employee and insured beacuse of the two types are seperated in database
	case insuredLogin:
		try{
		Collection<Object> result= logic.loginUser(Integer.parseInt(message.data.toString()), message.additionalData.toString(), clientMessages.insuredLogin);
		if(null!=result)	//handling the response from the database, either successful or failure
			results= new serverMessage(serverMessages.loginSucces, result);
		else
			results= new serverMessage(serverMessages.loginFailure, result);
		} catch (SQLException e1) {

			e1.printStackTrace();
		}
	break;
	case employeeLogin:
		try{
		Collection<Object> result= logic.loginUser(Integer.parseInt(message.data.toString()), message.additionalData.toString(), clientMessages.employeeLogin);
		if(null!=result)	//handling the response from the database, either successful or failure
			results= new serverMessage(serverMessages.loginSucces, result);
		else
			results= new serverMessage(serverMessages.loginFailure, result);
		} catch (SQLException e1) {

			e1.printStackTrace();
		}
	break;
	//case of getting all the available doctor residencies available.
	case getResidency:
		try {
			Collection<Object> result=logic.getResidency();
			results= new serverMessage(serverMessages.residenciesList,result);
		} catch (SQLException e1) {

			e1.printStackTrace();
		}
	break;
	//getting doctors list according to the chosen residency.
	case detDoctorsList:
		try {
			Collection<Object> result=logic.getDoctors(message.data.toString(),Integer.parseInt(message.additionalData.toString()) );
			results= new serverMessage(serverMessages.doctorsList,result);
		} catch (SQLException e1) {

			e1.printStackTrace();
		}
	break;
	//getting doctors free appointments after a doctor is chosen
	case getfreeAppointments:
		try {
			Collection<Object> result = logic.getavailableAppointments((doctor) message.data);
			results= new serverMessage(serverMessages.freeAppointmentsList,result);
		} catch (SQLException e1) {

			e1.printStackTrace();
		}
	break;
	//setting the appointment chosen and received from the client
	case makeAppointment:
		try {
			boolean appSetResult=logic.makeAppointments((Appointment) message.data);
			if(appSetResult)
				results=new serverMessage(serverMessages.appointmentSet,null);
		} catch (SQLException e1)
		{//catch SQL exception of double booking and send back an error message
			results=new serverMessage(serverMessages.error,null);
		}
	break;
	//getting the doctors appointments for today
	case getDoctorsAppointments:
		try {
			Collection<Object> result = logic.getDoctorsAppointmets((int) message.data);
			results=new serverMessage(serverMessages.doctorsAppointmentsList,result);
		} catch (SQLException e1) {

			e1.printStackTrace();
		}
	break;
	//getting the patients future scheduled appointments
	case getpatientAppointments:
		try {
			Collection<Object> result = logic.getPatientsApoointments((int) message.data);
			results=new serverMessage(serverMessages.scheduledPatientsAppointments,result);
		} catch (SQLException e1) {

			e1.printStackTrace();
		}
	break;
	//getting the appointment a user wished to cancel and send the request to the database.
	case cancelAppointment:
		try {
			boolean deleteResult=logic.deleteAppointment((Appointment)message.data, (int)message.additionalData);
			if(deleteResult){
				results=new serverMessage(serverMessages.deleteSuccess, null);
			}
			else
				results=new serverMessage(serverMessages.deleteFailure, null);
		} catch (SQLException e1) {

			e1.printStackTrace();
		}
	default:
	break;
	}
//after a message has been made it is sent back to the requesting client.
	 try {
		 client.sendToClient(results);
	 } catch (IOException e) {
		 // TODO Auto-generated catch block
		 e.printStackTrace();
	 }
  }


  /**
   * This method overrides the one in the superclass.  Called
   * when the server starts listening for connections.
   */
  protected void serverStarted()
  {
	  //implementing a timed call to the update function in order to update database at the end of each day.
	  Calendar today = Calendar.getInstance();
	  today.set(Calendar.HOUR_OF_DAY, 0);
	  today.set(Calendar.MINUTE, 3);
	  today.set(Calendar.SECOND, 0);
		new Timer().schedule(
			    new TimerTask() {
					@Override
					public void run() {
						try {
							logic.updatefreeAppointments();
							System.out.println("executing Midnight Maintenance...");
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

			    },today.getTime() , TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS));
    System.out.println
      ("Server listening for connections on port " + getPort());
  }

  /**
   * This method overrides the one in the superclass.  Called
   * when the server stops listening for connections.
   */
  protected void serverStopped()
  {
    System.out.println
      ("Server has stopped listening for connections.");
  }

  //Class methods ***************************************************

  /**
   * This method is responsible for the creation of
   * the server instance (there is no UI in this phase).
   *
   * @param args[0] The port number to listen on.  Defaults to 5555
   *          if no argument is entered.
   */
  public static void main(String[] args)
  {
    int port = 0; //Port to listen on

    try
    {
      port = Integer.parseInt(args[0]); //Get port from command line
    }
    catch(Throwable t)
    {
      port = DEFAULT_PORT; //Set port to 5555
    }
    serverLogic logic =serverLogic.getInstance();	//singleton use og logic functions in server
    Server sv = new Server(port,logic);

    //logic.initializeAppointments();
    try
    {
      sv.listen(); //Start listening for connections
    }
    catch (Exception ex)
    {
      System.out.println("ERROR - Could not listen for clients!");
    }
  }
}
//End of EchoServer class
