package student.helper;

import java.io.IOException;
import java.nio.file.*;

public class Logger {
	
	//////////////////////////////////////
	// Constants.
	//////////////////////////////////////
	private static final String logFilePath;
	
	private static final boolean silent = false;
	
	//////////////////////////////////////
	// Methods.
	//////////////////////////////////////
	static{
		logFilePath = "log.txt";
		try {
			Files.write( Paths.get(logFilePath), "Starting to log...\n".getBytes(), StandardOpenOption.CREATE);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * Logs the string into the log file.
	 * @param data
	 */
	public static void Log(String data){
		if(!silent){
			System.out.println("***Log: "+data+"***");
		}
		data = data+"\n";
		
		try {
			Files.write( Paths.get(logFilePath), data.getBytes(), StandardOpenOption.APPEND);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
