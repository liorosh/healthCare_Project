package utils;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.TimerTask;

import utils.models.Appointment;

public interface models {

	public class Message implements Serializable {

	}

	public class serverMessage extends Message
	{

		public Collection<Object> data;
		public Object message;

		public serverMessage(String message, Collection<Object> result) {
			this.data= result;
			this.message=message;
		}



	}

	public class clientMessage extends Message{
		public clientMessages clientmessage;
		public Object data;
		public Object additionalData;
		public clientMessage(clientMessages message, Object data, Object i) {
			this.clientmessage=message;
			this.data=data;
			this.additionalData=i;
		}

	}





	public class Appointment implements Serializable
	{
		public String appTime;
		public String orderTime;
		public int insuredID;
		public final doctor doctor;
		public String patientFirstName;
		public String patientLastName;

		public Appointment(String apptime , int ID, doctor doc)
		{
			this.appTime=apptime;
			DateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm");
			this.orderTime = df.format(Calendar.getInstance().getTime());
			this.insuredID = ID;
			this.doctor = doc;
		}

		public Appointment(String apptime,int ID, String first, String Last){
			this.doctor=null;
			this.appTime=apptime;
			this.insuredID=ID;
			this.patientFirstName=first;
			this.patientLastName=Last;
		}
		public Appointment(doctor doc, String appTime)
		{
			this.appTime=appTime;
			this.doctor = doc;
		}
		public void setAppTime(String time)
		{
			appTime = time;
		}

		public String getAppTime()
		{
			return appTime;
		}
		public String getOrderTime()
		{
			return orderTime;
		}

		public doctor getDoctor()
		{
			return doctor;
		}

		public int getInsuredId()
		{
			return insuredID;
		}
		@Override
		public String toString(){
		    return (this.appTime);
		}

	}

	class user implements Serializable{
		int id;
		String firstName;
		String lastName;
		public user(int id,String firstName, String lastName){
			this.id=id;
			this.firstName=firstName;
			this.lastName=lastName;
		}
	}

	class employee implements Serializable
	{
		public int id;
	}

	class doctor extends employee
	{
		public String fName;
		public String lName;
		public String residency;
		public int location;
		public doctor(int i) {
			this.id=i;
		}
		public doctor(int id,String residnecy, int location, String first,String last) {
			this.id=id;
			this.residency=residnecy;
			this.location=location;
			this.fName=first;
			this.lName=last;
		}
		public doctor(int id, String res, int branch) {
			this.id=id;
			this.residency=res;
			this.location=branch;
		}
		@Override
		public String toString(){
		    return (this.fName+this.lName);
		}
	}

	class familyDoctor extends doctor
	{

		public familyDoctor(int i) {
			super(i);
			// TODO Auto-generated constructor stub
		}

		public familyDoctor(int i, String string, int j) {
			super(i,string,j);
		}

		public familyDoctor(int id, String residency, int location, String firstname, String lastname) {
			super(id,residency,location,firstname,lastname);
		}

	}

	class specialistDoctor extends doctor
	{

		public specialistDoctor(int i)
		{
			super(i);
			// TODO Auto-generated constructor stub
		}
		public specialistDoctor(int i, String string, int j) {
			super(i,string,j);
		}
		public specialistDoctor(int id, String residency, int location, String firstname, String lastname) {
			super(id,residency,location,firstname,lastname);
		}

	}

	enum clientMessages{getResidency,detDoctorsList,getAppointments,makeAppointment,cancelAppointment,Login};

}
