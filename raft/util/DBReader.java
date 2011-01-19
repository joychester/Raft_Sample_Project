package raft.util;



import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.sql.Date;
import java.util.List;

//Establish a connection to a MSSQL database using JDBC.

public class DBReader {
	
	//Connect to SQL server 2005 for us
	static String constring = "com.microsoft.sqlserver.jdbc.SQLServerDriver"; 
	private static String perflogFlag = LoadPara.getGlobalParam("perfflag");
	
	static Connection conn = null;
	static Statement stmt = null;
	static ResultSet rs = null;
	static List<String> dataset = new ArrayList<String>();

	
	public void Getconnection( String url, String dbname, String pwd){
		
		try
	    {
	      // Step 1: Load the JDBC driver. 
			Class.forName(constring);
			
	      // Step 2: Establish the connection to the database. 
			conn = DriverManager.getConnection(url,dbname,pwd); 
		    
	    }
	    catch (Exception e)
	    {
	      System.err.println("Can not get DB connection from " + url); 
	      System.err.println(e.getMessage()); 
	    }
	  } 
	
	public List<String> Query( String sqlstatement,String coloumnname) throws SQLException{

	    String SQL = sqlstatement;
	    stmt = conn.createStatement();
	    rs = stmt.executeQuery(SQL);
	    
	    while (rs.next()) {
	    	dataset.add(rs.getString(coloumnname));
	    }
	    return dataset;
	}
	
	public void InstertPermLog(Date startTime, String transactionName, String tableName) throws SQLException{
		String perfFlag = perflogFlag;
		if (perfFlag.equalsIgnoreCase("on")){
			long a = startTime.getTime();
			
			java.sql.Date currentTime = new Date(new java.util.Date().getTime());
			long b = currentTime.getTime();
			long responseTime = b - a ;
			
			java.sql.Timestamp time1 = new java.sql.Timestamp(new java.util.Date().getTime());		
			String startTimeStamp = time1.toString();
			
			PreparedStatement sqlStatement = conn.prepareStatement("INSERT INTO " + tableName +" (Starttime, Transactionname, responsetime) VALUES (?, ?, ?);");

			sqlStatement.setString(1, startTimeStamp);
			sqlStatement.setString(2, transactionName);
			sqlStatement.setLong(3, responseTime);
			
			sqlStatement.execute();
			sqlStatement.close();
		}
		else return ;
		
	}

	
	public void Close(){

         if (rs != null) try { rs.close(); rs= null;} catch(Exception e) {}
         if (stmt != null) try { stmt.close(); stmt= null;} catch(Exception e) {}
         if (conn != null) try { conn.close(); conn=null;} catch(Exception e) {}

	}
}



    
