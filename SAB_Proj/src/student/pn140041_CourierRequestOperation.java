package student;

import java.util.List;

import operations.CourierRequestOperation;
import student.helper.SpExecutor;

public class pn140041_CourierRequestOperation implements CourierRequestOperation {

	@Override
	public boolean changeVehicleInCourierRequest(String arg0, String arg1) {
		return SpExecutor.ExecuteChangeVehicleInCourierRequest(arg0, arg1);
	}

	@Override
	public boolean deleteCourierRequest(String arg0) {
		return SpExecutor.ExecuteDeleteCourierRequest(arg0);
	}

	@Override
	public List<String> getAllCourierRequests() {
		return SpExecutor.ExecuteGetAllCourierRequests();
	}

	@Override
	public boolean grantRequest(String arg0) {
		return SpExecutor.ExecuteGrantCourierRequest(arg0);
	}

	@Override
	public boolean insertCourierRequest(String arg0, String arg1) {
		return SpExecutor.ExecuteInserCourierRequest(arg0, arg1);
	}

}
