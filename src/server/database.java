package server;

public interface database
{
	public final String SELECT_RESIDENCY = "SELECT DISTINCT EMPLOYEES.RESIDENCY FROM EMPLOYEES where employees.position='doctor' ";

	public final String SELECT_DOCTORS_BY_DATE = " SELECT E.EMPLOYEEID, E.FIRSTNAME, E.LASTNAME, E.BRANCH, E.Residency "
				+ "FROM EMPLOYEES E LEFT JOIN "
				+ "(SELECT PA.DOCTORID, MAX(APPTIME) AS LAST_APPTIME "
				+ "FROM PASTAPPOINTMENTS PA "
				+ "WHERE PA.INSUREDID = ? AND PA.RESIDENCY = ? "
				+ "GROUP BY PA.DOCTORID "
				+ ")PA ON E.EMPLOYEEID=PA.DOCTORID "
				+ "WHERE E.RESIDENCY = ? "
				+ "ORDER BY (LAST_APPTIME IS NOT NULL) DESC, LAST_APPTIME DESC ";

	public final String SELECT_FAMILY_DOCTOR= "select pa.familyDocID,e.firstName,e.lastName,e.branch,e.Residency from patients pa "
				+ " left join ( "
				+ " select e.firstName,e.lastName,e.employeeID,e.branch,e.Residency from employees e "
				+ " where e.Residency='family' "
				+ " group by e.employeeID "
				+ " ) e on pa.familyDocID=e.employeeID "
				+ " where pa.insuranceNumber=?; ";

	public final String MAKEAPPOINTMENT = "insert into scheduledappointments values (?,?,?,?,?,?,default)";

	public final String DELETE_APPOINTMENT = " delete from scheduledappointments where scheduledappointments.appTime = ? "
				+ "and scheduledappointments.doctorID = ?  and scheduledappointments.insuredID = ? ";

	public final String SHOW_TODAYS_APPOINTMENTS_BY_DOCTOR = "select sa.appTime ,sa.insuredID, p.firstName, p.lastName ,p.insuranceNumber "
				+ "from scheduledappointments sa left join "
				+ "(select p.firstName, p.lastName,p.insuranceNumber "
				+ "from patients p "
				+ "group by p.insuranceNumber "
				+ ")p on  p.insuranceNumber=sa.insuredID "
				+ "where sa.doctorID = ? and date(sa.appTime)=current_date() "
				+ "order by sa.appTime asc; ";

	public final String GET_FREE_FAMILY_APPOINTMENTS = "select ffp.appTime from familyfreeappointments ffp "
				+ "where ffp.appTime not in( "
				+ "select sa.appTime from scheduledappointments sa "
				+ "where sa.doctorID=?) and ffp.appTime>now() ";

	public final String GET_FREE_SPECIALIST_APPOINTMENTS = "select sfp.appTime from specialistfreeappointments sfp "
				+ "where sfp.appTime not in( "
				+ "select sa.appTime from scheduledappointments sa "
				+ "where sa.doctorID=?) and sfp.appTime>now() ";

	public final String UPDATE_SPECIALIST_APPOINTMENTS_TO_DATE = "delete from specialistfreeappointments where specialistfreeappointments.appTime<now() ";

	public final String UPDATE_FAMILY_APPOINTMENTS_TO_DATE = "delete from familyfreeappointments where familyfreeappointments.appTime<now() ";

	public final String FILL_FAMILY_APPOINTMENT_TEMPLATE = "insert into familyfreeappointments values (?)";

	public final String FILL_SPECIALIST_APPOINTMENT_TEMPLATE = "insert into specialistfreeappointments values (?); ";

	public final String LOGIN_PATIENT ="SELECT patients.insuranceNumber,patients.firstName,patients.lastName, patients.Email,patients.familyDocID FROM patients where patients.insuranceNumber = ? and patients.password = ? ";

	public final String LOGIN_EMPLOYEE = "SELECT employees.employeeID,employees.firstName,employees.lastName,employees.email ,employees.branch,employees.Residency FROM employees where employees.employeeID = ? and employees.password = ? ";

	public final String GET_APPOINTMENTS_BY_PATIENT = "SELECT sa.appTime,sa.orderTime,sa.insuredID,sa.location,sa.doctorID,sa.residency,sa.location "
				+" ,e.firstName , e.lastName "
				+" FROM scheduledappointments sa left join ( "
				+" select e.firstName, e.lastName,e.employeeID from employees e) e on sa.doctorID=e.employeeID "
				+ "where sa.insuredID=? ";

	public final String MOVE_APPOINTMENTS_TO_PAST = "insert into pastappointments select * from scheduledappointments "
				+" where scheduledappointments.appTime < now() ; ";

	public final String DELETE_FROM_SCHEDULED = "delete from scheduledappointments where scheduledappointments.appTime < now() ";

}