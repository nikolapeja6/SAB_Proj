package student;

import java.util.List;

import operations.DistrictOperations;
import student.helper.SpExecutor;

public class pn140041_DistrictOperations implements DistrictOperations {

	@Override
	public int deleteAllDistrictsFromCity(String arg0) {
		return SpExecutor.ExecuteDeleteAllDistrictsFromCityName(arg0);
	}

	@Override
	public boolean deleteDistrict(int arg0) {
		return SpExecutor.ExecuteDeleteDistrictId(arg0);
	}

	@Override
	public int deleteDistricts(String... arg0) {
		int cnt = 0;
		for(String districtName : arg0)
			if(SpExecutor.ExecuteDeleteDistrictName(districtName))
				cnt++;
		return cnt;
	}

	@Override
	public List<Integer> getAllDistricts() {
		return SpExecutor.ExecuteGetAllDistricts();
	}

	@Override
	public List<Integer> getAllDistrictsFromCity(int arg0) {
		return SpExecutor.ExecuteGetAllDistrictFromCity(arg0);
	}

	@Override
	public int insertDistrict(String arg0, int arg1, int arg2, int arg3) {
		return SpExecutor.ExecuteInsertDistrict(arg0, arg1, arg2, arg3);
	}
}
