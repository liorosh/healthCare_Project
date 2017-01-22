package utils;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
/* the following interface contains classes for information exchange between server and the clients
 *	every class contains the necessary fields and functionality to support the required operations.
 * different types of constructors and getters are implemented to support all range of operations.
 * all classes implements Serializable in order to support writeObject() function that can only
 * handle primitive types and object that can transformed to a stream of bits.
 */
public interface models
{
	//class message is abstract to support children serverMessaage and clientMessage
	@SuppressWarnings("serial")
	public class Message implements Serializable{}

/* Class serverMessage is an object that holds information sent from the server to his clients.
 * the two members are an enum of serverMessages that cover the range of messages that can be sent from the server
 * and a collection of objects to hold the actual data sent from the server.
 * the Object type is selected to support a wide range of class to be sent : Appointments, Doctors and Strings
 * */
	@SuppressWarnings("serial")
	public class serverMessage extends Message
	{
		private Collection<Object> data;
		private serverMessages message;
		//Constructor
		public serverMessage(serverMessages message, Collection<Object> result)
		{
			this.setData(result);
			this.setMessage(message);
		}
		//Setters & Getters
		public serverMessages getMessage()
		{
			return message;
		}

		public void setMessage(serverMessages message)
		{
			this.message = message;
		}

		public Collection<Object> getData()
		{
			return data;
		}

		public void setData(Collection<Object> data)
		{
			this.data = data;
		}
	}

/* Class clientMessage is an object that holds information sent from the clients to the server.
 * The members are an enum of clientMessages that cover the range of messages send from a client to the server,
 * two object of data to support multiple data fields such as password and username
 * the fields are of type object again to support a wide range of objects: Appointment,doctors, strings and int
 */
	@SuppressWarnings("serial")
	public class clientMessage extends Message
	{
		private clientMessages clientmessage;
		private Object data;
		private Object additionalData;
		//Constructor
		public clientMessage(clientMessages message, Object data, Object i)
		{
			this.setClientmessage(message);
			this.setData(data);
			this.setAdditionalData(i);
		}
		//Setters & Getters
		public clientMessages getClientmessage()
		{
			return clientmessage;
		}

		public void setClientmessage(clientMessages clientmessage)
		{
			this.clientmessage = clientmessage;
		}

		public Object getData()
		{
			return data;
		}

		public void setData(Object data)
		{
			this.data = data;
		}

		public Object getAdditionalData()
		{
			return additionalData;
		}

		public void setAdditionalData(Object additionalData)
		{
			this.additionalData = additionalData;
		}
	}

/* Class Appointment represents a row in the SQL Table and holds all the information that represents an appointment
 * at the client level the appointment is used inside the lists and are sent as an actual object to the server.
 * this class implements a couple of different constructors and Getters to handle different uses for the same Class.
 */
	@SuppressWarnings("serial")
	public class Appointment implements Serializable
	{
		private String appTime;
		private String orderTime;
		private int insuredID;
		private final doctor doctor;
		private String patientFirstName;
		private String patientLastName;

		//Constructor
		public Appointment(String apptime , int ID, doctor doc)
		{
			//this constructor is used at client level, once an appointment is chosen
			//the current time is generated and saved as order time.
			this.appTime=apptime;
			DateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm");
			this.orderTime = df.format(Calendar.getInstance().getTime());
			this.setInsuredID(ID);
			this.doctor = doc;
		}
		//Constructor
		public Appointment(String apptime ,String ordertime ,int ID, doctor doc)
		{
			this.appTime=apptime;
			this.orderTime = ordertime;
			this.setInsuredID(ID);
			this.doctor = doc;
		}
		//Constructor
		public Appointment(String apptime,int ID, String first, String Last)
		{
			this.doctor=null;
			this.appTime=apptime;
			this.setInsuredID(ID);
			this.patientFirstName=first;
			this.patientLastName=Last;
		}
		//Constructor
		public Appointment(doctor doc, String appTime)
		{
			this.appTime=appTime;
			this.doctor = doc;
		}
		//Setters & Getters
		public String gettAppointmentTime()
		{
			return appTime;
		}
		//getter for showing information in the table
		public String getAppTime()
		{
			return appTime.substring(0,this.appTime.length()-5);
		}

		public void setAppTime(String appTime)
		{
			this.appTime = appTime;
		}

		public String getResidency()
		{
			return doctor.residency;
		}

		public void setResidency(String residency)
		{
			this.doctor.residency = residency;
		}

		//getter for showing information in the table
		public String getDoctorName()
		{
			return doctor.getFirstName()+" "+doctor.getLastName();
		}

		public void setDoctorName(String firstName)
		{
			this.doctor.setFirstName(firstName);
		}

		//getter for showing information in the table
		public String getInsuredFullName()
		{
			return this.patientFirstName+" "+ this.patientLastName;
		}

		public String getLocation()
		{
			return doctor.location;
		}

		public void setLocation(String location)
		{
			doctor.location = location;
		}

		public String getAppOrderTime()
		{
			return orderTime;
		}

		//getter for showing information in the table
		public String getOrderTime()
		{
			return orderTime.substring(0,this.orderTime.length()-5);
		}

		public void setOrderTime(String orderTime)
		{
			this.orderTime = orderTime;
		}

		public doctor getDoctor()
		{
			return doctor;
		}

		public int getInsuredId()
		{
			return getInsuredID();
		}

		@Override
		public String toString()
		{
		    return (this.appTime);
		}

		public int getInsuredID()
		{
			return insuredID;
		}

		public void setInsuredID(int insuredID)
		{
			this.insuredID = insuredID;
		}
	}

/* Class user is a root Class to support different typs of users.
 * it is used as an userSession instance to recognize the current type of user connected.
 * it holds first and last name String that are common to all users
 */
	@SuppressWarnings("serial")
	class user implements Serializable
	{
		private String firstName;
		private String lastName;

		//Constructors
		public user(String firstName, String lastName)
		{
			this.setFirstName(firstName);
			this.setLastName(lastName);
		}

		//Setters & Getters
		public String getFirstName()
		{
			return firstName;
		}

		public void setFirstName(String firstName)
		{
			this.firstName = firstName;
		}

		public String getLastName()
		{
			return lastName;
		}

		public void setLastName(String lastName)
		{
			this.lastName = lastName;
		}
	}
/* Class Patient is used to hold patient information from the DB, saved as user session in the case of patient connection
 * it holds the InsuedID, familyDoctorID and email in addition to the first and last name inherited from User.
 */
	@SuppressWarnings("serial")
	class patient extends user
	{
		private int insuredID;
		private int docotrsID;
		private String Email;

		//Constructor
		public patient(int id, String firstName, String lastName,int docid,String mail)
		{
			super(firstName, lastName);
			this.setInsuredID(id);
			this.docotrsID=docid;
			this.Email=mail;
		}

		//Setters & Getters
		public int getInsuredID()
		{
			return insuredID;
		}

		public void setInsuredID(int insuredID)
		{
			this.insuredID = insuredID;
		}
	}

/* Class Employee is extending the user class and used to seperate patients from employee users
 * adds the employee ID and email and suplies infrastructure to the different kind of doctors and employees
 */
	@SuppressWarnings("serial")
	class employee extends user
	{
		private int id;
		private String Email;

		//Constructors
		public employee(String firstName, String lastName,int id, String mail)
		{
			super(firstName, lastName);
			this.setId(id);
			this.Email=mail;
		}
		//Setters & Getters

		public int getId()
		{
			return id;
		}

		public void setId(int id)
		{
			this.id = id;
		}
	}

/* Class doctor extends employee do seperate regular employees from doctors that has distinct features such as residency
 */
	@SuppressWarnings("serial")
	class doctor extends employee
	{
		private String residency;
		private String location;

		//Constructors
		public doctor(int id,String residnecy, String location, String first,String last,String mail)
		{
			super(first,last,id, mail);
			this.residency=residnecy;
			this.setLocation(location);
		}

		//Setters & Getters
		@Override
		public String toString()
		{
		    return (this.getFirstName()+this.getLastName());
		}

		public String getLocation()
		{
			return location;
		}

		public void setLocation(String location)
		{
			this.location = location;
		}
	}
//TODO fix all of the parameter names to proper maing sense variables.

/* The following Classes: familyDoctor and specialistDoctor used to represent both the doctors associated with appointment
 * and the user connected to the session, both implement the same constructor ang getters/setters.
 */
	@SuppressWarnings("serial")
	class familyDoctor extends doctor
	{
		public familyDoctor(int id, String residency, String location, String firstname, String lastname, String mail)
		{
			super(id,residency,location,firstname,lastname,mail);
		}

		public familyDoctor(int id, String residency, String location, String firstname, String lastname)
		{
			super(id,residency,location,firstname,lastname,null);
		}
	}

	@SuppressWarnings("serial")
	class specialistDoctor extends doctor
	{
		public specialistDoctor(int id, String residency, String location, String firstname, String lastname, String mail)
		{
			super(id,residency,location,firstname,lastname,mail);
		}
		public specialistDoctor(int id, String residency, String location, String firstname, String lastname)
		{
			super(id,residency,location,firstname,lastname,null);
		}

	}
/*
 * Both enums repsresent the range of messages exchanged between server and client.
 */
	enum serverMessages{loginSucces,loginFailure,residenciesList,doctorsList,freeAppointmentsList,doctorsAppointmentsList,scheduledPatientsAppointments, deleteSuccess,deleteFailure,appointmentSet, error,success};
	enum clientMessages{getResidency,detDoctorsList,getfreeAppointments,makeAppointment,cancelAppointment,Login,getDoctorsAppointments,employeeLogin,insuredLogin,getpatientAppointments};
}
