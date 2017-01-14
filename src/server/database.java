package server;

public interface database
{
	public final String SELECT_DOCTORS_BY_RESIDENCY = "SELECT EMPLOYEES.FIRSTNAME, EMPLOYEES.LASTNAME, EMPLOYEES.EMPLOYEEID,EMPLOYEES.BRANCH FROM EMPLOYEES WHERE EMPLOYEES.RESIDENCY = ?";

	public final String SELECT_RESIDENCY = "SELECT DISTINCT EMPLOYEES.RESIDENCY FROM EMPLOYEES";

	public final String SELECT_DOCTORS_APPOINTMENTS = "SELECT * FROM freeappointments WHERE DOCTORID = ? ";

	public final String SELECT_DOCTORS_BY_DATE = " SELECT E.EMPLOYEEID, E.FIRSTNAME, E.LASTNAME, E.BRANCH, E.Residency "
				 + "FROM EMPLOYEES E LEFT JOIN "
				 + "(SELECT PA.DOCTORID, MAX(APPTIME) AS LAST_APPTIME "
				 + "FROM PASTAPPOINTMENTS PA "
				 + "WHERE PA.INSUREDID = ? AND PA.RESIDENCY = ? "
				 + "GROUP BY PA.DOCTORID "
				 + ")PA ON E.EMPLOYEEID=PA.DOCTORID "
				 + "WHERE E.RESIDENCY = ? "
				 + "ORDER BY (LAST_APPTIME IS NOT NULL) DESC, LAST_APPTIME DESC ";

	public final String MAKEAPPOINTMENT= "insert into scheduledappointments values (?,?,?,?,?,?,default)";

	public final String DELETE_APPOINTMENT =" delete from scheduledappointments where scheduledappointments.appTime = ? "
				+"and scheduledappointments.doctorID = ?  and scheduledappointments.insuredID = ? ";

	public final String SHOW_TODAYS_APPOINTMENTS_BY_DOCTOR="select sa.appTime ,sa.insuredID, p.firstName, p.lastName ,p.insuranceNumber "
				+ "from scheduledappointments sa left join "
				+ "(select p.firstName, p.lastName,p.insuranceNumber "
				+ "from patients p "
				+ "group by p.insuranceNumber "
				+ ")p on  p.insuranceNumber=sa.insuredID "
				+ "where sa.doctorID = ? and date(sa.appTime)=current_date() "
				+ "order by sa.appTime asc; ";
}