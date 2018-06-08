package student.helper;

import java.sql.*;

public class DbInterface {

	//////////////////////////////////////
	// Constants.
	//////////////////////////////////////
	
	private static final String serverName 		= "localhost";
	private static final String instanceName	= ".";
	private static final int 	port			= 1433;
	private static final String dbName 			= "pn140041";
	
	private static final String connectionStringFormat = "jdbc:sqlserver://%s\\%s:%d;databaseName=%s;integratedSecurity=true";

	private static final String connectionString = String.format(connectionStringFormat, serverName, instanceName, port, dbName);

	//////////////////////////////////////
	// Methods.
	//////////////////////////////////////
	
	static{
		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} 
	}
	
	/**
	 * Fetches the connection to the Database.
	 * @return
	 */
	public static Connection getConnection(){
		Connection connection = null;
		
		try{
			connection = DriverManager.getConnection(connectionString);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return connection;
	}
	
	
	//////////////////////////////////////
	// Testing.
	//////////////////////////////////////
	/**
	 * Main method used for testing. 
	 * @param args
	 */
	public static void main(String[] args){
		System.out.println("ConnectionString='"+connectionString+"'");
		Connection con = getConnection();

		//Assert.assertNotNull(con);
		System.out.println("Finished");
	}
}
