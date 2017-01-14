package utils;

import java.util.Calendar;

public interface models {

	class Appointment
	{
		String appTime;
		final Calendar orderTime;
		final int insuredID;
		final doctor doctor;
		
		
		Appointment(int ID, doctor doc)
		{
			orderTime = Calendar.getInstance();
			insuredID = ID; 
			doctor = doc; 
		}
		
		void setAppTime(String time)
		{
			appTime = time;
		}
		
		public String getAppTime()
		{
			return appTime;
		}
		Calendar getOrderTime()
		{
			return orderTime;
		}
		
		doctor getDoctor()
		{
			return doctor;
		}
		
		int getInsuredId()
		{
			return insuredID;
		}
		
		
	}
	
	class employee
	{
		
		
	}
	
	class doctor extends employee
	{
		
	}
	
	class familyDoctor extends doctor
	{
		
	}
	
	class specialistDoctor extends doctor
	{
		
	}
	
	
	
	
}
