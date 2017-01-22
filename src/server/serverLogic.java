package server;

//import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;

import utils.models.*;

//import ocsf.server.*;

public class serverLogic
{
	//serverLogic is a singleton and is only created once
	private static serverLogic instance=null;
	 serverLogic(){}
	 public static serverLogic getInstance()
	 {
		 if (null==instance)
		 {
			 instance=new serverLogic();
		 }
		 return instance;
	 }
/*
 * initializes appointments templates in two tables.
 * one table is for specialists appointments.
 * the second is for family doctors appointments.
 * excluding weekends, every work day is defined as an 8-16 workday.
 * appointments are initialized in time intervals according to the type of doctor
 * */
	public void initializeAppointments() throws SQLException
	{
		PreparedStatement stmt = null;
		try
		{
          Class.forName("com.mysql.jdbc.Driver").newInstance();
		} catch (Exception ex) {/* handle the error*/}

			Calendar calobj ;
	      	Calendar calobjPlus90 = Calendar.getInstance();
	      	Calendar calobjPlus28 = Calendar.getInstance();
	      	calobjPlus90.add(Calendar.DATE, 91);
	      	calobjPlus28.add(Calendar.DATE, 29);
	      	//establish connetion to database.
	      	SimpleDateFormat  df = new SimpleDateFormat("yyyy/MM/dd HH:mm");
			Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/softeng","root","1234");
			System.out.println("SQL connection succeed");
			calobj = Calendar.getInstance();
			//in case of a family resident update 28 days ahead.
			while(calobj.before(calobjPlus28))
			{
				if (calobj.get(Calendar.DAY_OF_WEEK)<Calendar.FRIDAY && calobj.get(Calendar.DAY_OF_WEEK)>=Calendar.SUNDAY)
				{
					calobj.set(Calendar.HOUR_OF_DAY, 8);
					calobj.set(Calendar.MINUTE, 0);
					while(calobj.get(Calendar.HOUR_OF_DAY)<16)
					{
						stmt=conn.prepareStatement(database.FILL_FAMILY_APPOINTMENT_TEMPLATE);
						stmt.setString(1, df.format(calobj.getTime()));
						stmt.execute();
						calobj.add(Calendar.MINUTE, 15);
					}
				}
				calobj.add(Calendar.DATE, 1);
			}
			calobj = Calendar.getInstance();
			//in case of a specialist update 90 days ahead.
			while(calobj.before(calobjPlus90))
			{
				if (calobj.get(Calendar.DAY_OF_WEEK)<Calendar.FRIDAY && calobj.get(Calendar.DAY_OF_WEEK)>=Calendar.SUNDAY)
				{
					calobj.set(Calendar.HOUR_OF_DAY, 8);
					calobj.set(Calendar.MINUTE, 0);
					while(calobj.get(Calendar.HOUR_OF_DAY)<16)
					{
						stmt=conn.prepareStatement(database.FILL_SPECIALIST_APPOINTMENT_TEMPLATE);
						stmt.setString(1, df.format(calobj.getTime()));
						stmt.execute();
						calobj.add(Calendar.MINUTE, 20);
					}
				}
				calobj.add(Calendar.DATE, 1);
			}
			stmt.close();
			conn.close();
}

/* makeAppointments is used to set an appointment in the database.
 * @param appointment holds all of the details needed in order to insert an appointment to the DB.
 * it returns true on success or throws an sql exception.
 */
	public boolean makeAppointments(Appointment appointment) throws SQLException
	{
		try
		{//establish connetion to database.
          Class.forName("com.mysql.jdbc.Driver").newInstance();
		} catch (Exception ex) {/* handle the error*/}
		Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/softeng","root","1234");
		PreparedStatement stmt=conn.prepareStatement(database.MAKEAPPOINTMENT);
		 //inserting the values to the query and execute
		stmt.setString(1, appointment.gettAppointmentTime());
		stmt.setString(2, appointment.getAppOrderTime());
		stmt.setInt(3, appointment.getInsuredID());
		stmt.setString(4, appointment.getDoctor().getLocation());
		stmt.setInt(5, appointment.getDoctor().getId());
		stmt.setString(6, appointment.getResidency());
		stmt.execute();
		stmt.close();
		conn.close();
		return true;
	}

/* getDoctors is used to get a list of doctors according to the residency chosen
 * params are the residency and the id of the user who asked it in order to display the list in a certain order.
 * return value is a collection of doctor objects that holds all the info necessary.
 */
	public Collection<Object> getDoctors(String residency,int insuredId) throws SQLException
	{
		Collection<Object> doctors=new ArrayList<Object>();
		PreparedStatement stmt;
		//establishing connection to DB
		try
		{
			Class.forName("com.mysql.jdbc.Driver").newInstance();
		} catch (Exception ex) {/* handle the error*/}
		Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/softeng","root","1234");
		if(residency.equals("family"))
		{	//in case of family doctor only associated doctor is needed
			stmt=conn.prepareStatement(database.SELECT_FAMILY_DOCTOR);
			stmt.setInt(1,insuredId);
		}
		else
		{	//in case of specialist list order varies according to previous appointments
			stmt=conn.prepareStatement(database.SELECT_DOCTORS_BY_DATE);
			stmt.setInt(1,insuredId);
			stmt.setString(2, residency);
			stmt.setString(3, residency);
		}
		ResultSet rs= stmt.executeQuery();
		//insert results into collection of doctors depending of the sort.
		while(rs.next())
		{
			if(residency.equals("family"))
			doctors.add(new familyDoctor(rs.getInt(1), residency, rs.getString(4), rs.getString(2)+" ", rs.getString(3)));
			else
			doctors.add(new specialistDoctor(rs.getInt(1), residency, rs.getString(4), rs.getString(2)+" ", rs.getString(3)));
			//System.out.println(rs.getString(1)+ " " + rs.getString(2)+ " " + rs.getString(3) + " " + rs.getString(4) + " " + rs.getString(5));
		}
		rs.close();
		stmt.close();
		conn.close();
		return doctors;
	}

/* deleteAppointment is used to cancel an appointment on request from the user.
 * params are the object of the appointment a user wishes to cancel and the id of the user booked to that appointment
 * the user id is required upon request the id send is the id from the session,
 *  and that way no user can change other users appointments
 *  return value is boolean that notifies cancellation succeded or an exception throw
 */
	public boolean deleteAppointment(Appointment appointment, int insuredId) throws SQLException
	{
		try
		{		//establishing connection to DB
          Class.forName("com.mysql.jdbc.Driver").newInstance();
		} catch (Exception ex) {/* handle the error*/}
		Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/softeng","root","1234");
		PreparedStatement stmt=conn.prepareStatement(database.DELETE_APPOINTMENT);
		//inserting values needed for query
		stmt.setString(1, appointment.gettAppointmentTime());
		stmt.setInt(2, appointment.getDoctor().getId());
		stmt.setInt(3, insuredId);
		stmt.execute();
		stmt.close();
		conn.close();
		return true;
	}

/* getDoctorsAppointmets is used to display a doctor's their appointment for the specific workday.
 * 	params are the doctor id which is the user id that is connected
 * return value is collection of appointment objects.
 */
	public Collection<Object> getDoctorsAppointmets(int doctorId) throws SQLException
	{
		Collection<Object> doctorsAppointments= new ArrayList<Object>();
		try
		{	//establishing connection to DB
          Class.forName("com.mysql.jdbc.Driver").newInstance();
		} catch (Exception ex) {/* handle the error*/}
		Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/softeng","root","1234");
		PreparedStatement stmt=conn.prepareStatement(database.SHOW_TODAYS_APPOINTMENTS_BY_DOCTOR);
		stmt.setInt(1,doctorId);
		ResultSet rs=stmt.executeQuery();
		//organize appointments in the collection to be returned.
		while(rs.next())
		{
			doctorsAppointments.add(new Appointment(rs.getString(1),rs.getInt(2),rs.getString(3),rs.getString(4)));
		}
		rs.close();
		stmt.close();
		conn.close();
		return doctorsAppointments;
	}

/*getavailableAppointments is used to show a patient with available appointments to a chosen doctor
 * param is an object of the doctor chosen by the user.
 * 	return value is a collection of appointments sorted in ascending order.
 */
	public Collection<Object> getavailableAppointments(doctor doctorId) throws SQLException
	{
		Collection<Object> availableAppointments = new ArrayList<Object>();
		PreparedStatement stmt = null;
		ResultSet rs;
		try
		{	//establishing connection to DB
          Class.forName("com.mysql.jdbc.Driver").newInstance();
		} catch (Exception ex) {/* handle the error*/}
		Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/softeng","root","1234");
		if(doctorId instanceof familyDoctor)	//in case of a family doctor
			stmt = conn.prepareStatement(database.GET_FREE_FAMILY_APPOINTMENTS);
		else if(doctorId instanceof specialistDoctor)	//in case of a specialist doctor
			stmt=conn.prepareStatement(database.GET_FREE_SPECIALIST_APPOINTMENTS);
		stmt.setInt(1, doctorId.getId());
		rs = stmt.executeQuery();
		//organize the appointments in a coolection and send back to client
		while(rs.next())
		{
			availableAppointments.add(new Appointment(doctorId,rs.getString(1)));
		}
		rs.close();
		stmt.close();
		conn.close();
		return availableAppointments;
	/*get all the appointments available depending on the doctor*/
	}

/* updatefreeAppointments is used for adding new appointments to templates at the end of 3 months or 4 weekds
 * depending on the doctor,the function is called automticallly at midnight.
 * it add anothe day at the end of each scheduling era, deletes today's templates and moves all of todays scheduled appointments
 * to the past appointments table for archiving.
 */
	public void updatefreeAppointments() throws SQLException
	{
		try
		{		//establishing connection to DB
          Class.forName("com.mysql.jdbc.Driver").newInstance();
		} catch (Exception ex) {/* handle the error*/}
		Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/softeng","root","1234");
		//two calls to fill anothe day in the templates, one for a family doctor and one for the rest.
		fillEveryDay(91,20,conn);
		fillEveryDay(29,15,conn);
		//move all of today's appointments to the past appointments table.
		PreparedStatement stmt= conn.prepareStatement(database.MOVE_APPOINTMENTS_TO_PAST);
		stmt.execute();
		//delete today's appointments from the scheduled table.
		stmt=conn.prepareStatement(database.DELETE_FROM_SCHEDULED);
		stmt.execute();
		stmt.close();
		conn.close();
	}

/* fillEveryDay is an accessory function to fill appointments to a given day
 * params are: dayToInsert which gives the leap from current day, appInterval to distinguish from family or specialist
 * and set the right appointments intervals. conn is the connection to the database.
 * return void.
 */
	public void fillEveryDay(int dayToInsert, int appInterval, Connection conn) throws SQLException
	{
		//setting proper time format to match DB
		PreparedStatement stmt = null;
		SimpleDateFormat  df = new SimpleDateFormat("yyyy/MM/dd HH:mm");
		Calendar dayToAdd = Calendar.getInstance();
		//delete todays appointment from templates
		if(91==dayToInsert)
			stmt=conn.prepareStatement(database.UPDATE_SPECIALIST_APPOINTMENTS_TO_DATE);
		else if(29==dayToInsert)
			stmt=conn.prepareStatement(database.UPDATE_FAMILY_APPOINTMENTS_TO_DATE);
		stmt.execute();
		//insert new appointments times at the given date with leap from current day.
		dayToAdd.add(Calendar.DATE, dayToInsert);
		while(dayToAdd.get(Calendar.DAY_OF_WEEK)>Calendar.THURSDAY && dayToAdd.get(Calendar.DAY_OF_WEEK)<=Calendar.SATURDAY)
		{
			dayToAdd.add(Calendar.DATE, 1);
		}
		if(91==dayToInsert)
			stmt=conn.prepareStatement(database.FILL_SPECIALIST_APPOINTMENT_TEMPLATE);
		else if(29==dayToInsert)
			stmt=conn.prepareStatement(database.FILL_FAMILY_APPOINTMENT_TEMPLATE);
		//initializes the start time to 08:00 AM
		dayToAdd.set(Calendar.HOUR_OF_DAY, 8);
		dayToAdd.set(Calendar.MINUTE, 0);
		//set appointments in given intervals
		while(dayToAdd.get(Calendar.HOUR_OF_DAY)<16)
		{
			stmt.setString(1, df.format(dayToAdd.getTime()));
			stmt.execute();
			dayToAdd.add(Calendar.MINUTE, appInterval);
		}
		stmt.close();
	}

/* loginUseris used to check the credentials input from the user at login
 * params are the id and the password input from the user along the a user flag the stands for the table to look the user in:
 * either patients or employees.
 * return value is the information of the user in case of successful login or null in the case of unsuccessful login.
 */
	public Collection<Object> loginUser(int loginId, String password, clientMessages userType) throws SQLException
	{
		Collection<Object> userreturned = new ArrayList<Object>();
		PreparedStatement stmt=null;
		user loggedInUser = null;
		try		//establishing connection to DB
		{
			Class.forName("com.mysql.jdbc.Driver").newInstance();
		} catch (Exception ex) {/* handle the error*/}
		Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/softeng","root","1234");
		if(clientMessages.employeeLogin==userType)	//case employee
			stmt= conn.prepareStatement(database.LOGIN_EMPLOYEE);
		else if (clientMessages.insuredLogin==userType)	//case patient
			stmt= conn.prepareStatement(database.LOGIN_PATIENT);
		stmt.setInt(1, loginId);
		stmt.setString(2, password);
		ResultSet rs = stmt.executeQuery();
		if(rs.next())
		{	//sets the user object to its specific type: employee or patient and saves the inforamtion into it.
			if(clientMessages.insuredLogin==userType)
				loggedInUser= new patient(rs.getInt(1),rs.getString(2),rs.getString(3),rs.getInt(5),rs.getString(4));
			else if (clientMessages.employeeLogin==userType)
			{
				if(rs.getString(5).equals("family"))
					loggedInUser= new familyDoctor(rs.getInt(1),rs.getString(6),rs.getString(5),rs.getString(2),rs.getString(3),rs.getString(4));
				else
					loggedInUser= new specialistDoctor(rs.getInt(1),rs.getString(6),rs.getString(5),rs.getString(2),rs.getString(3),rs.getString(4));
			}
			userreturned.add(loggedInUser);
			stmt.close();
			conn.close();
			rs.close();
			return userreturned;	//case login successful
		}
		stmt.close();
		conn.close();
		rs.close();
		return null;	//case login failed
	}

/* getPatientsApoointments is used to get patients appointments and display to the user.
 * 	param is the insured id asking the appointments.
 * return value is the users appointments
 */
	public Collection<Object> getPatientsApoointments(int insuredId) throws SQLException
	{
		Collection<Object> patientsAppointments = new ArrayList<Object>();
		PreparedStatement stmt = null;
		ResultSet rs;
		try
		{	//establishing connection to DB
          Class.forName("com.mysql.jdbc.Driver").newInstance();
		} catch (Exception ex) {/* handle the error*/}
		Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/softeng","root","1234");
		stmt=conn.prepareStatement(database.GET_APPOINTMENTS_BY_PATIENT);
		stmt.setInt(1, insuredId);
		rs = stmt.executeQuery();
		// creating a collection of appointments with the specific doctor type associated with the appointment
		while (rs.next())
		{
			if(rs.getString(5).equals("family"))
			patientsAppointments.add(new Appointment(rs.getString(1),rs.getString(2),insuredId,new familyDoctor(rs.getInt(5),rs.getString(6),rs.getString(7),
																							rs.getString(8),rs.getString(9))));
			else
				patientsAppointments.add(new Appointment(rs.getString(1),rs.getString(2),insuredId,new specialistDoctor(rs.getInt(5),rs.getString(6),rs.getString(7),
						rs.getString(8),rs.getString(9))));
		}
		rs.close();
		stmt.close();
		conn.close();
		return patientsAppointments;
	}

/* getResidency is uset to get of all of the available residencies that gives services to patients
 * 	its called upon tab switch in the patient gui.
 * return value is a collection of strings listing the doctors choices offered
 */
	public Collection<Object> getResidency() throws SQLException{
		try
		{		//establishing connection to DB
			Class.forName("com.mysql.jdbc.Driver").newInstance();
		} catch (Exception ex) {/* handle the error*/}
		Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/softeng","root","1234");
		PreparedStatement stmt=conn.prepareStatement(database.SELECT_RESIDENCY);
		ResultSet rs=stmt.executeQuery();
		Collection<Object> residencies = new ArrayList<Object>();
		//create collection to be sent back to the user.
		while(rs.next())
		{
			residencies.add(rs.getString(1));
		}
		rs.close();
		stmt.close();
		conn.close();
		return residencies;
	}
}
