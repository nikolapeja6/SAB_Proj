package student;

import java.math.BigDecimal;
import java.util.List;

import operations.VehicleOperations;
import student.helper.SpExecutor;

public class pn140041_VehicleOperations implements VehicleOperations {

	@Override
	public boolean changeConsumption(String arg0, BigDecimal arg1) {
		return SpExecutor.ExecuteChangeFuelConsumption(arg0, arg1);
	}

	@Override
	public boolean changeFuelType(String arg0, int arg1) {
		return SpExecutor.ExecuteChangeFuelType(arg0, arg1);
	}

	@Override
	public int deleteVehicles(String... arg0) {
		int cnt = 0;
		for (String licence : arg0)
			if (SpExecutor.ExecuteDeleteVehicle(licence))
				cnt++;
		return cnt;
	}

	@Override
	public List<String> getAllVehichles() {
		return SpExecutor.ExecuteGetAllVehicles();
	}

	@Override
	public boolean insertVehicle(String arg0, int arg1, BigDecimal arg2) {
		return SpExecutor.ExecuteInsertVehicle(arg0, arg1, arg2);
	}

}
