package utils;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;

import utils.models.Appointment;

public interface models {

	public class Message implements Serializable {
		public String message;

		public Message(String message){
			this.message=message;
		}
	}

	public class serverMessage extends Message
	{
		public Collection<Appointment> data;

		public serverMessage(String message, Collection<Appointment> result) {
			super(message);
			this.data= result;
		}

	}

	public class clientMessage extends Message{
		public Object data;
		public clientMessage(String message, Object data) {
			super(message);
			this.data=data;
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


	}

	class employee implements Serializable
	{
		public int id;
	}

	class doctor extends employee
	{
		String fName;
		String lName;
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

	}
}
