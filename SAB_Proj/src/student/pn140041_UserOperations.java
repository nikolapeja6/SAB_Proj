package student;

import java.util.List;

import operations.UserOperations;
import student.helper.SpExecutor;

public class pn140041_UserOperations implements UserOperations {

	@Override
	public int declareAdmin(String arg0) {
		if(arg0 == null || arg0.isEmpty())
			return 2;
		return SpExecutor.ExecuteDeclareAdmin(arg0);
	}

	@Override
	public int deleteUsers(String... arg0) {
		int cnt = 0;
		
		if(arg0 == null)
			return 0;
		
		for (String username : arg0){
			if(username == null)
				continue;
			if (SpExecutor.ExecuteDeleteUser(username))
				cnt++;
		}
		return cnt;
	}

	@Override
	public List<String> getAllUsers() {
		return SpExecutor.ExecuteGetAllUsers();
	}

	@Override
	public Integer getSentPackages(String... arg0) {
		Integer cnt = 0;
		
		if(arg0 == null || arg0.length == 0)
			return null;
		
		for (String username : arg0) {
			Integer result = SpExecutor.ExecuteGetSentPackages(username);
			if (result == null)
				return null;
			cnt += result;
		}
		return cnt;
	}

	@Override
	public boolean insertUser(String arg0, String arg1, String arg2, String arg3) {
		
		if(arg0 == null || arg1 == null || arg2 == null || arg3 == null)
			return false;
		
		if(arg0.isEmpty() || arg1.isEmpty() || arg2.isEmpty() || arg3.isEmpty())
			return false;
		if(Character.isLowerCase(arg1.charAt(0)) || Character.isLowerCase(arg2.charAt(0)) || arg3.length() < 8 || !arg3.matches(".*\\d+.*") || !arg3.matches(".*\\p{Alpha}+.*"))
			return false;
		
		return SpExecutor.ExecuteInsertUser(arg0, arg1, arg2, arg3);
	}

}
