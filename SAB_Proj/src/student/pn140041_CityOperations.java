package student;

import java.util.List;

import operations.CityOperations;
import student.helper.SpExecutor;

public class pn140041_CityOperations implements CityOperations{

	@Override
	public int deleteCity(String... arg0) {
		
		// Success count.
		int cnt = 0;
		
		for(String cityName : arg0)
			if(SpExecutor.ExecuteDeleteCity(cityName) != -1)
				cnt++;
		
		return cnt;
	}

	@Override
	public boolean deleteCity(int arg0) {
		return SpExecutor.ExecuteDeleteCity(arg0);
	}

	@Override
	public List<Integer> getAllCities() {
		return SpExecutor.ExecuteGetAllCities();
	}

	@Override
	public int insertCity(String arg0, String arg1) {
		return SpExecutor.ExecuteInsertCity(arg0, arg1);
	}

}
