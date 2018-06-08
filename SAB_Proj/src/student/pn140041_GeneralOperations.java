package student;

import operations.GeneralOperations;
import student.helper.SpExecutor;

public class pn140041_GeneralOperations implements GeneralOperations{

	@Override
	public void eraseAll() {
		SpExecutor.ExecuteEraseAll();
	}

}
