package server;
// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com

import java.io.IOException;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Collection;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import utils.models.*;
/*import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;*/
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
  public void handleMessageFromClient
    (/*enum Requests,*/Object msg, ConnectionToClient client)
  {
	 clientMessage message=(clientMessage) msg;
	 serverMessage results = null;
	switch(message.clientmessage){
	case insuredLogin:
		try{
		Collection<Object> result= logic.loginUser(Integer.parseInt(message.data.toString()), message.additionalData.toString(), clientMessages.insuredLogin);
		if(null!=result)
			results= new serverMessage("loginSucces", result);
		else
			results= new serverMessage("loginfailed", result);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		break;
	case employeeLogin:
		try{
		Collection<Object> result= logic.loginUser(Integer.parseInt(message.data.toString()), message.additionalData.toString(), clientMessages.employeeLogin);
		if(null!=result)
			results= new serverMessage("loginSucces", result);
		else
			results= new serverMessage("loginfailed", result);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		break;
	case getResidency:
		try {
			Collection<Object> result=logic.getResidency();
			results= new serverMessage("residencies",result);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		break;
	case detDoctorsList:
		try {
			Collection<Object> result=logic.getDoctors(message.data.toString(),Integer.parseInt(message.additionalData.toString()) );
			results= new serverMessage("doctors",result);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		break;
	case getfreeAppointments:
		try {
			Collection<Object> result = logic.getavailableAppointments((doctor) message.data);
			results= new serverMessage("appointments",result);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		break;

	case makeAppointment:
		try {
			boolean answer=logic.makeAppointments((Appointment) message.data);
			results=new serverMessage("makeAppSuccess",null);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		break;
	case getDoctorsAppointments:
		try {
			Collection<Object> result = logic.getDoctorsAppointmets((int) message.data);
			results=new serverMessage("docapps",result);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		break;
		case getpatientAppointments:
		Collection<Object> result;
		try {
			result = logic.getPatientsApoointments((int) message.data);
			results=new serverMessage("myapps",result);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		break;
	default:
		break;
	}


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
	 /* Calendar today = Calendar.getInstance();
	  today.set(Calendar.HOUR_OF_DAY, 00);
	  today.set(Calendar.MINUTE, 00);
	  today.set(Calendar.SECOND, 0);
		new Timer().schedule(
			    new TimerTask() {
					@Override
					public void run() {
						try {
							logic.updatefreeAppointments();

							System.out.println("yes i did!");
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

			    },today.getTime() , TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS));*/
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
