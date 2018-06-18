package student;

import java.math.BigDecimal;
import java.util.List;

import operations.CourierOperations;
import student.helper.SpExecutor;

public class pn140041_CourierOperations implements CourierOperations {

	@Override
	public boolean deleteCourier(String arg0) {
		if(arg0 == null || arg0.isEmpty())
			return false;
		return SpExecutor.ExecuteDeleteCourier(arg0);
	}

	@Override
	public List<String> getAllCouriers() {
		return SpExecutor.ExecuteGetAllCouriers();
	}

	@Override
	public BigDecimal getAverageCourierProfit(int arg0) {
		return SpExecutor.ExecuteGetAverageCourierProfit(arg0);
	}

	@Override
	public List<String> getCouriersWithStatus(int arg0) {
		return SpExecutor.ExecuteGetCouriersWithStatus(arg0);
	}

	@Override
	public boolean insertCourier(String arg0, String arg1) {
		return SpExecutor.ExecuteInsertCourier(arg0, arg1);
	}

}
