package server;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import ocsf.server.*;

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

	public void temp(ConnectionToClient client){
    	ResultSet rs;
		PreparedStatement stmt;
		try
		{
            Class.forName("com.mysql.jdbc.Driver").newInstance();
        } catch (Exception ex) {/* handle the error*/}

        try
        {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/softeng","root","1234");
            //Connection conn = DriverManager.getConnection("jdbc:mysql://192.168.3.68/test","root","Root");
            System.out.println("SQL connection succeed");
            stmt = conn.prepareStatement(database.SELECT_DOCTORS_APPOINTMENTS);
            rs = stmt.executeQuery();
            while (rs.next()){
            	try {
					client.sendToClient("flight number 387 is now priced at: "+rs.getString(1));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
    }
        catch (SQLException e) {e.printStackTrace();}
	}

}
