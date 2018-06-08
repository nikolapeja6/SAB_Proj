package student.helper;

import java.sql.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


/**
 * Helper class for executing stored procedures.
 * 
 * @author Peja
 *
 */
public class SpExecutor {

	//////////////////////////////////////
	// Constants.
	//////////////////////////////////////
	/**
	 * String format for calling a stored procedure.
	 */
	private static final String executeSpStatementFormat 	= "{call dbo.%s(%s)}";
	
	/**
	 * Single parameter element.
	 */
	private static final String singleParameter 			= "?";
	
	/**
	 * Multiple parameter element.
	 */
	private static final String parameterElement			 = ",?";

	//////////////////////////////////////
	// Public methods.
	//////////////////////////////////////
	/**
	 * Executed the spInsertCity stored procedure.
	 * 
	 * @param cityName
	 * @param postalCode
	 * @return
	 */
	public static int ExecuteInsertCity(String cityName, String postalCode) {
		
		HashMap<Integer, Object> returnValues = ExecuteStoredProc("spInsertCity", cityName, postalCode, OutputParameters.Integer, OutputParameters.String);
		
		String message = (String)returnValues.get(4);
		
		if((Integer)returnValues.get(3) == -1 || !message.isEmpty())
			Logger.Log(message);
		
		return (Integer)returnValues.get(3);
	}
	
	/**
	 * Executed the spGetAllCities stored procedure.
	 * 
	 * @return
	 */
	public static List<Integer> ExecuteGetAllCities(){
		
		HashMap<Integer, Object> returnValues = ExecuteStoredProc("spGetAllCities", OutputParameters.String);
		List<Integer> id = new LinkedList<>();
		
		String idsAgregated = (String)returnValues.get(1);
		if(idsAgregated == null || idsAgregated.isEmpty())
			return id;
		
		String[] idsStrings = idsAgregated.split(",");
		for(String idString : idsStrings)
			id.add(Integer.parseInt(idString));
		
		return id;
	}
	
	/**
	 * Executed the spDeleteCityId stored procedure.
	 * 
	 * @param id
	 * @return
	 */
	public static boolean ExecuteDeleteCity(int id){
		
		HashMap<Integer, Object> returnValues = ExecuteStoredProc("spDeleteCityId", id, OutputParameters.Boolean, OutputParameters.String);
		
		boolean status = (Boolean)returnValues.get(2);
		if(!status){
			String message = (String)returnValues.get(3);
			Logger.Log(message);
		}
		
		return status;
	}
	
	/**
	 * Executed the spDeleteCityName stored procedure.
	 * 
	 * @param id
	 * @return
	 */
	public static int ExecuteDeleteCity(String name){
		
		HashMap<Integer, Object> returnValues = ExecuteStoredProc("spDeleteCityName", name, OutputParameters.Integer, OutputParameters.String);
		
		int id = (Integer)returnValues.get(2);
		if(id == -1){
			String message = (String)returnValues.get(3);
			Logger.Log(message);
		}
		
		return id;
	}
	
	/**
	 * Erases all data from all tables in the database.
	 */
	public static void ExecuteEraseAll(){
		ExecuteStoredProc("spEraseAll");
	}
	
	//////////////////////////////////////
	// Private methods.
	//////////////////////////////////////
	
	/**
	 * Executed a stored procedure with the given parameters (both input and output.)
	 * @param spName Name of the stored procedure.
	 * @param parameters Input parameters are passed by actual values, and output parameters are passed by passing an OutputParaneters object of the appropriate type.
	 * @return HashMap where keys are integers representing the positions of the output parameters and the values are objects of the appropriate type representing the return values.
	 */
	private static HashMap<Integer, Object> ExecuteStoredProc(String spName, Object...parameters ) {
		// Fetch connection.
		Connection connection = DbInterface.getConnection();

		// Check if valid.
		//Assert.assertNotNull(connection);

		try {
			// Prepare statement.
			CallableStatement pstmt = getPreparedStatement(connection, spName, parameters);

			// Execute.
			pstmt.execute();

			// Extract results.
			HashMap<Integer, Object> ret = extractResultsFromResultSet(pstmt, parameters);
			
			// Closing statements and connections.
			pstmt.close();
			connection.close();
			
			return ret;
		}

		catch (Exception e) {
			e.printStackTrace();
		}

		return extractResultsFromResultSet(null, null);
	}

	//////////////////////////////////////
	// Helper methods.
	//////////////////////////////////////
	/**
	 * Enum used for describing and passing output parameters to other methods in the class.
	 * The values of the enums are the corresponding values of the java.sql.Types constants.
	 * @author Peja
	 *
	 */
	private enum OutputParameters{
		Integer(java.sql.Types.INTEGER),
		String(java.sql.Types.VARCHAR),
		Double(java.sql.Types.DOUBLE),
		Boolean(java.sql.Types.BOOLEAN);
		
	    private final int code;
		OutputParameters(int code) { this.code = code; }
	    public int getValue() { return code; }
	}
	
	/**
	 * Generates the SQL statement string needed to execute a stored procedure.
	 * @param spName Name of the stored procedure.
	 * @param numOfParameters Number of parameters (both input and output) of the stored procedure.
	 * @return
	 */
	private static String getExecSpStatement(String spName, int numOfParameters) {
		String parametersAppendex = numOfParameters > 0 ? singleParameter + new String(new char[numOfParameters - 1]).replace("\0", parameterElement): "";
		
		return String.format(executeSpStatementFormat, spName, parametersAppendex);
	}
	
	/**
	 * Prepares the stored procedure call statement and inserts all the input output parameters.
	 * @param connection
	 * @param spName Name of the stored procedure.
	 * @param parameters Input parameters are passed by actual values, and output parameters are passed by passing an OutputParaneters object of the appropriate type.
	 * @return
	 */
	private static CallableStatement getPreparedStatement(Connection connection, String spName, Object... parameters) {
		CallableStatement pstmt = null;

		try {
			pstmt = connection.prepareCall(getExecSpStatement(spName, parameters.length));
			
			int i = 0;
			// Add input parameters.
			for(Object obj : parameters){
				i++;
				
				if(obj instanceof Integer){
					// int
					pstmt.setInt(i, (Integer) obj);
				}else if(obj instanceof String){
					// string
					pstmt.setString(i, (String) obj);
				}else if(obj instanceof OutputParameters){
					// Output parameters.
					OutputParameters type = (OutputParameters) obj;
					
					pstmt.registerOutParameter(i, type.getValue());
					
				}else{
					// unknown
					throw new UnknownError("Unsupported type.");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return pstmt;
	}

	/**
	 * Extracts the results into a dictionary with  keys being indexes of output parameters and values are output parameter values of appropriate types.
	 * @param pstmt 
	 * @param parameters Input parameters are passed by actual values, and output parameters are passed by passing an OutputParaneters object of the appropriate type.
	 * @return
	 */
	private static HashMap<Integer, Object> extractResultsFromResultSet(CallableStatement pstmt, Object... parameters) {
		
		// Extract output parameters.
		HashMap<Integer, Object> outputParameters = new HashMap<>();
		try {
			for (int i = 0; i < parameters.length;) {
				Object obj = parameters[i++];
				if (obj instanceof OutputParameters) {
					OutputParameters parameter = (OutputParameters) obj;

					switch (parameter) {
					case Integer:
						outputParameters.put(i, pstmt.getInt(i));
						break;
					case String:
						outputParameters.put(i, pstmt.getString(i));
						break;
					case Double:
						outputParameters.put(i, pstmt.getDouble(i));
						break;
					case Boolean:
						outputParameters.put(i, pstmt.getBoolean(i));
						break;
					default:
						throw new UnknownError("Unkwnown type");
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return outputParameters;
	}

	//////////////////////////////////////
	// Testing.
	//////////////////////////////////////
	/**
	 * Main method used for simple positive path testing.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("Starting test...");
		System.out.println();

		/*
		HashMap<Integer, Object> result = ExecuteStoredProc("spGetAllCities", OutputParameters.String);
		Iterator it = result.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry pair = (Map.Entry) it.next();
			System.out.print(pair.getKey() + "=" + pair.getValue() + "; ");
		}		
		*/
		for(int id : ExecuteGetAllCities()){
			System.out.print(id+", ");
		}
		
		System.out.println();
		System.out.println();

		/*
		result = ExecuteStoredProc("spInsertCity", "SomeCity1", "12345678", OutputParameters.Integer, OutputParameters.String);
		it = result.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry pair = (Map.Entry) it.next();
			System.out.print(pair.getKey() + "=" + pair.getValue() + "; ");
		}
		*/
		
		ExecuteDeleteCity("SomeCity2");
		
		int retId = ExecuteInsertCity("SomeCity2", "123");
		System.out.println("ID="+retId);
		
		System.out.println();
		System.out.println();
		
		ExecuteDeleteCity(retId);
		
		System.out.println();
		System.out.println();
		
		/*
		result = ExecuteStoredProc("spGetAllCities", OutputParameters.String);
		it = result.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry pair = (Map.Entry) it.next();
			System.out.print(pair.getKey() + "=" + pair.getValue() + "; ");
		}
		*/
		for(int id : ExecuteGetAllCities()){
			System.out.print(id+", ");
		}

		System.out.println();
		System.out.println("Finished.");
	}
}
