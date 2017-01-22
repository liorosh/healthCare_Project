package utils;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
/* the following interface contains
 *
 */
public interface models
{

	@SuppressWarnings("serial")
	public class Message implements Serializable{}

	@SuppressWarnings("serial")
	public class serverMessage extends Message
	{
		private Collection<Object> data;
		private serverMessages message;

		public serverMessage(serverMessages message, Collection<Object> result)
		{
			this.setData(result);
			this.setMessage(message);
		}

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

	@SuppressWarnings("serial")
	public class clientMessage extends Message
	{
		private clientMessages clientmessage;
		private Object data;
		private Object additionalData;

		public clientMessage(clientMessages message, Object data, Object i)
		{
			this.setClientmessage(message);
			this.setData(data);
			this.setAdditionalData(i);
		}

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

	@SuppressWarnings("serial")
	public class Appointment implements Serializable
	{
		private String appTime;
		private String orderTime;
		private int insuredID;
		private final doctor doctor;
		private String patientFirstName;
		private String patientLastName;

		public String gettAppointmentTime()
		{
			return appTime;
		}

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

		public String getDoctorName()
		{
			return doctor.getFirstName()+" "+doctor.getLastName();
		}

		public void setDoctorName(String firstName)
		{
			this.doctor.setFirstName(firstName);
		}

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

		public Appointment(String apptime , int ID, doctor doc)
		{
			this.appTime=apptime;
			DateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm");
			this.orderTime = df.format(Calendar.getInstance().getTime());
			this.setInsuredID(ID);
			this.doctor = doc;
		}

		public Appointment(String apptime ,String ordertime ,int ID, doctor doc)
		{
			this.appTime=apptime;

			this.orderTime = ordertime;
			this.setInsuredID(ID);
			this.doctor = doc;
		}

		public Appointment(String apptime,int ID, String first, String Last)
				{
			this.doctor=null;
			this.appTime=apptime;
			this.setInsuredID(ID);
			this.patientFirstName=first;
			this.patientLastName=Last;
		}

		public Appointment(doctor doc, String appTime)
		{
			this.appTime=appTime;
			this.doctor = doc;
		}

		public String getAppOrderTime()
		{
			return orderTime;
		}

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

	@SuppressWarnings("serial")
	class user implements Serializable
	{
		private String firstName;
		private String lastName;

		public user(String firstName, String lastName)
		{
			this.setFirstName(firstName);
			this.setLastName(lastName);
		}

		public user(){}

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

	@SuppressWarnings("serial")
	class patient extends user
	{
		private int insuredID;
		private int docotrsID;
		private String Email;

		public patient(int id, String firstName, String lastName,int docid,String mail)
		{
			super(firstName, lastName);
			this.setInsuredID(id);
			this.docotrsID=docid;
			this.Email=mail;
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




	@SuppressWarnings("serial")
	class employee extends user
	{
		private int id;
		private String Email;

		public employee(String firstName, String lastName,int id, String mail)
		{
			super(firstName, lastName);
			this.setId(id);
			this.Email=mail;
		}

		public employee(int id)
		{
			this.setId(id);
		}

		public int getId()
		{
			return id;
		}

		public void setId(int id)
		{
			this.id = id;
		}
	}

	@SuppressWarnings("serial")
	class doctor extends employee
	{
		private String residency;
		private String location;

		public doctor(int id)
		{
			super(id);
		}
		public doctor(int id,String residnecy, String location, String first,String last,String mail)
		{
			super(first,last,id, mail);
			this.residency=residnecy;
			this.setLocation(location);

		}

		public doctor(int id, String res, String branch)
		{
			super(id);
			this.residency=res;
			this.setLocation(branch);
		}

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
	@SuppressWarnings("serial")
	class familyDoctor extends doctor
	{
		public familyDoctor(int i){
			super(i);
		}

		public familyDoctor(int i, String string, String j)
		{
			super(i,string,j);
		}

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
		public specialistDoctor(int i)
		{
			super(i);
		}
		public specialistDoctor(int i, String string, String j)
		{
			super(i,string,j);
		}
		public specialistDoctor(int id, String residency, String location, String firstname, String lastname, String mail)
		{
			super(id,residency,location,firstname,lastname,mail);
		}
		public specialistDoctor(int id, String residency, String location, String firstname, String lastname)
		{
			super(id,residency,location,firstname,lastname,null);
		}

	}
	enum serverMessages{loginSucces,loginFailure,residenciesList,doctorsList,freeAppointmentsList,doctorsAppointmentsList,scheduledPatientsAppointments, deleteSuccess,deleteFailure,appointmentSet, error,success};
	enum clientMessages{getResidency,detDoctorsList,getfreeAppointments,makeAppointment,cancelAppointment,Login,getDoctorsAppointments,employeeLogin,insuredLogin,getpatientAppointments};
}
