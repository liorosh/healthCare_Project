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

		public String getAppTime() {
			return appTime.substring(0,this.appTime.length()-5);
		}
		public void setAppTime(String appTime) {
			this.appTime = appTime;
		}
		public String orderTime;
		public int insuredID;
		public final doctor doctor;
		public String patientFirstName;
		public String patientLastName;
		public String getResidency() {
			return doctor.residency;
		}
		public void setResidency(String residency) {
			this.doctor.residency = residency;
		}

		public String getDoctorName() {
			return doctor.firstName+" "+doctor.lastName;
		}

		public void setDoctorName(String firstName) {
			this.doctor.firstName = firstName;
		}


		public void setInsuredFullName(String fullname)
		{
			//this.InsuredFullName = fullname;
		}
		public String getInsuredFullName()
		{
			return this.patientFirstName+" "+ this.patientLastName;
		}


		public int getLocation() {
			return doctor.location;
		}
		public void setLocation(int location) {
			doctor.location = location;
		}
		public Appointment(String apptime , int ID, doctor doc)
		{
			this.appTime=apptime;
			DateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm");
			this.orderTime = df.format(Calendar.getInstance().getTime());
			this.insuredID = ID;
			this.doctor = doc;
		}
		public Appointment(String apptime ,String ordertime ,int ID, doctor doc)
		{
			this.appTime=apptime;

			this.orderTime = ordertime;
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

		public String getOrderTime()
		{
			return orderTime.substring(0,this.orderTime.length()-5);
		}
		public void setOrderTime(String orderTime) {
			this.orderTime = orderTime;
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

		public String firstName;


		public String lastName;
		public user(String firstName, String lastName){
			this.firstName=firstName;
			this.lastName=lastName;
		}

		public user(){}
	}

	class patient extends user{
		public int insuredID;
		int docotrsID;
		String Email;
		public patient(int id, String firstName, String lastName,int docid,String mail) {
			super(firstName, lastName);
			this.insuredID=id;
			this.docotrsID=docid;
			this.Email=mail;
		}
	}




	class employee extends user
	{
		public int id;
		public String Email;
		public employee(String firstName, String lastName,int id, String mail) {
			super(firstName, lastName);
			this.id=id;
			this.Email=mail;
		}
		public employee(int id){
			this.id=id;
		}


	}

	class doctor extends employee
	{
		public String residency;

		public int location;

		public doctor(int id) {
			super(id);
		}
		public doctor(int id,String residnecy, int location, String first,String last,String mail) {
			super(first,last,id, mail);
			this.residency=residnecy;
			this.location=location;

		}
		public doctor(int id, String res, int branch) {
			super(id);
			this.residency=res;
			this.location=branch;
		}
		@Override
		public String toString(){
		    return (this.firstName+this.lastName);
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

		public familyDoctor(int id, String residency, int location, String firstname, String lastname, String mail) {
			super(id,residency,location,firstname,lastname,mail);
		}

		public familyDoctor(int id, String residency, int location, String firstname, String lastname) {
			super(id,residency,location,firstname,lastname,null);
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
		public specialistDoctor(int id, String residency, int location, String firstname, String lastname, String mail) {
			super(id,residency,location,firstname,lastname,mail);
		}
		public specialistDoctor(int id, String residency, int location, String firstname, String lastname) {
			super(id,residency,location,firstname,lastname,null);
		}

	}

	enum clientMessages{getResidency,detDoctorsList,getfreeAppointments,makeAppointment,cancelAppointment,Login,getDoctorsAppointments,employeeLogin,insuredLogin,getpatientAppointments};

}
