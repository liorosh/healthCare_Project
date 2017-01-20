package utils;

public class appointment {
	private String InsuredFullName;
	private String apptime;
	private String orderTime;
	private String residency;
	private String doctorName;
	public String getDoctorName() {
		return doctorName;
	}
	public void setDoctorName(String doctorName) {
		this.doctorName = doctorName;
	}
	private int location;
	public int getLocation() {
		return location;
	}
	public void setLocation(int location) {
		this.location = location;
	}
	public String getOrderTime() {
		return orderTime.substring(0,this.orderTime.length()-5);
	}
	public void setOrderTime(String orderTime) {
		this.orderTime = orderTime;
	}
	public String getResidency() {
		return residency;
	}
	public void setResidency(String residency) {
		this.residency = residency;
	}

	public void setApptime(String apptime)
	{
		this.apptime = apptime;
	}

	public String getApptime()
	{
		return apptime.substring(0,this.apptime.length()-5);
	}

	public appointment(String time, String name)
	{
		this.apptime=time;
		this.InsuredFullName=name;
	}
	public appointment(String time, String ordertime, String residency, String docname,int location) {
		this.apptime=time;
		this.orderTime=ordertime;
		this.residency=residency;
		this.doctorName=docname;
		this.location=location;
	}
}
