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
	private static serverLogic instance=null;
	 serverLogic()
	 {
	 }
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
 * */
	public void initializeAppointments()
	{
		PreparedStatement stmt;
		try
		{
          Class.forName("com.mysql.jdbc.Driver").newInstance();
		} catch (Exception ex) {/* handle the error*/}
		try
		{
			Calendar calobj ;
	      	Calendar calobjPlus90 = Calendar.getInstance();
	      	Calendar calobjPlus28 = Calendar.getInstance();
	      	calobjPlus90.add(Calendar.DATE, 91);
	      	calobjPlus28.add(Calendar.DATE, 29);
	      	SimpleDateFormat  df = new SimpleDateFormat("yyyy/MM/dd HH:mm");
			Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/softeng","root","1234");
			//Connection conn = DriverManager.getConnection("jdbc:mysql://192.168.3.68/test","root","Root");
			System.out.println("SQL connection succeed");
			calobj = Calendar.getInstance();
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
	      }
	      catch (SQLException e) {e.printStackTrace();}
}
	public boolean makeAppointments(Appointment appointment) throws SQLException
	{
		try
		{
          Class.forName("com.mysql.jdbc.Driver").newInstance();
		} catch (Exception ex) {/* handle the error*/}
		Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/softeng","root","1234");
		PreparedStatement stmt=conn.prepareStatement(database.MAKEAPPOINTMENT);
		stmt.setString(1, appointment.appTime);
		stmt.setString(2, appointment.orderTime);
		stmt.setInt(3, appointment.insuredID);
		stmt.setInt(4, appointment.getDoctor().location);
		stmt.setInt(5, appointment.getDoctor().id);
		stmt.setString(6, appointment.getDoctor().residency);
		stmt.execute();
		return true;

	}
	public Collection<doctor> getDoctors(String residency,int insuredId) throws SQLException
	{
		Collection<doctor> doctors=new ArrayList<doctor>();
		try
		{
          Class.forName("com.mysql.jdbc.Driver").newInstance();
		} catch (Exception ex) {/* handle the error*/}
		Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/softeng","root","1234");
		PreparedStatement stmt=conn.prepareStatement(database.SELECT_DOCTORS_BY_DATE);
		stmt.setInt(1,insuredId);
		stmt.setString(2, residency);
		stmt.setString(3, residency);
		ResultSet rs= stmt.executeQuery();

		while(rs.next()){
			doctors.add(new doctor(rs.getInt(1), residency, rs.getInt(4), rs.getString(2), rs.getString(3)));
			//System.out.println(rs.getString(1)+ " " + rs.getString(2)+ " " + rs.getString(3) + " " + rs.getString(4) + " " + rs.getString(5));
		}
		return doctors;
	}
	public boolean deleteAppointment(String appTime, int doctorId, int insuredId) throws SQLException
	{
		try
		{
          Class.forName("com.mysql.jdbc.Driver").newInstance();
		} catch (Exception ex) {/* handle the error*/}
		Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/softeng","root","1234");
		PreparedStatement stmt=conn.prepareStatement(database.DELETE_APPOINTMENT);
		stmt.setString(1, appTime);
		stmt.setInt(2, doctorId);
		stmt.setInt(3, insuredId);
		if(stmt.execute())
			return true;
		return false;
	}
	public void getAppointmets(int doctorId) throws SQLException
	{
		try
		{
          Class.forName("com.mysql.jdbc.Driver").newInstance();
		} catch (Exception ex) {/* handle the error*/}
		Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/softeng","root","1234");
		PreparedStatement stmt=conn.prepareStatement(database.SHOW_TODAYS_APPOINTMENTS_BY_DOCTOR);
		stmt.setInt(1,doctorId);
		ResultSet rs=stmt.executeQuery();
		while(rs.next()){
			System.out.println(rs.getString(1)+" "+ rs.getString(2)+ " "+ rs.getString(3)+" "+ rs.getString(4));
		}
	}
	public Collection<Appointment> getavailableAppointments(doctor doctorId) throws SQLException
	{
		Collection<Appointment> availableAppointments = new ArrayList<Appointment>();
		PreparedStatement stmt = null;
		ResultSet rs;
		try
		{
          Class.forName("com.mysql.jdbc.Driver").newInstance();
		} catch (Exception ex) {/* handle the error*/}
		Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/softeng","root","1234");
		if(doctorId instanceof familyDoctor)
		stmt = conn.prepareStatement(database.GET_FREE_FAMILY_APPOINTMENTS);
		else if(doctorId instanceof specialistDoctor)
		stmt=conn.prepareStatement(database.GET_FREE_SPECIALIST_APPOINTMENTS);
		stmt.setInt(1, doctorId.id);
		rs = stmt.executeQuery();
		while(rs.next())
		{
			availableAppointments.add(new Appointment(doctorId,rs.getString(1)));
		}
		return availableAppointments;
	/*get all the appointments available depending on the doctor*/
	}
	public void updatefreeAppointments() throws SQLException
	{/*add another day in three months*/

		try
		{
          Class.forName("com.mysql.jdbc.Driver").newInstance();
		} catch (Exception ex) {/* handle the error*/}
		Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/softeng","root","1234");
		fillEveryDay(91,20,conn);
		fillEveryDay(29,15,conn);
	}
	public void fillEveryDay(int dayToInsert, int appInterval, Connection conn) throws SQLException{
		PreparedStatement stmt = null;
		SimpleDateFormat  df = new SimpleDateFormat("yyyy/MM/dd HH:mm");
		Calendar dayToAdd = Calendar.getInstance();
		if(91==dayToInsert)
			stmt=conn.prepareStatement(database.UPDATE_SPECIALIST_APPOINTMENTS_TO_DATE);
		else if(29==dayToInsert)
			stmt=conn.prepareStatement(database.UPDATE_FAMILY_APPOINTMENTS_TO_DATE);
		stmt.execute();
		dayToAdd.add(Calendar.DATE, dayToInsert);
		while(dayToAdd.get(Calendar.DAY_OF_WEEK)>Calendar.THURSDAY && dayToAdd.get(Calendar.DAY_OF_WEEK)<=Calendar.SATURDAY)
		{
			dayToAdd.add(Calendar.DATE, 1);
		}
		dayToAdd.set(Calendar.HOUR_OF_DAY, 8);
		dayToAdd.set(Calendar.MINUTE, 0);
		while(dayToAdd.get(Calendar.HOUR_OF_DAY)<16)
		{
			stmt=conn.prepareStatement(database.FILL_SPECIALIST_APPOINTMENT_TEMPLATE);
			stmt.setString(1, df.format(dayToAdd.getTime()));
			stmt.execute();
			dayToAdd.add(Calendar.MINUTE, appInterval);
		}
	}
	public boolean loginUser(int loginId, String password, int userFlag) throws SQLException
	{
		PreparedStatement stmt=null;

		try
	{
        Class.forName("com.mysql.jdbc.Driver").newInstance();
		} catch (Exception ex) {/* handle the error*/}
		Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/softeng","root","1234");
		if(1==userFlag)
			stmt= conn.prepareStatement(database.LOGIN_EMPLOYEE);
		else if (2==userFlag)
			stmt= conn.prepareStatement(database.LOGIN_PATIENT);
		stmt.setInt(1, loginId);
		stmt.setString(2, password);
		return stmt.execute();
	}
	public Collection<Appointment> getPatientsApoointments(int insuredId) throws SQLException
	{
		Collection<Appointment> patientsAppointments = new ArrayList<Appointment>();
		PreparedStatement stmt = null;
		ResultSet rs;
		try
		{
          Class.forName("com.mysql.jdbc.Driver").newInstance();
		} catch (Exception ex) {/* handle the error*/}
		Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/softeng","root","1234");
		stmt=conn.prepareStatement(database.GET_APPOINTMENTS_BY_PATIENT);
		stmt.setInt(1, insuredId);
		rs = stmt.executeQuery();
		while (rs.next())
		{
			patientsAppointments.add(new Appointment(rs.getString(1),insuredId,new doctor(rs.getInt(5),rs.getString(6),rs.getInt(7),
																							rs.getString(8),rs.getString(9))));
		}
		return patientsAppointments;
	}

}
