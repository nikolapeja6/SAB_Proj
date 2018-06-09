package student;

import java.util.List;

import operations.UserOperations;
import student.helper.SpExecutor;

public class pn140041_UserOperations implements UserOperations {

	@Override
	public int declareAdmin(String arg0) {
		return SpExecutor.ExecuteDeclareAdmin(arg0);
	}

	@Override
	public int deleteUsers(String... arg0) {
		int cnt = 0;
		for (String username : arg0)
			if (SpExecutor.ExecuteDeleteUser(username))
				cnt++;
		return cnt;
	}

	@Override
	public List<String> getAllUsers() {
		return SpExecutor.ExecuteGetAllUsers();
	}

	@Override
	public Integer getSentPackages(String... arg0) {
		Integer cnt = 0;
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
		return SpExecutor.ExecuteInsertUser(arg0, arg1, arg2, arg3);
	}

}
