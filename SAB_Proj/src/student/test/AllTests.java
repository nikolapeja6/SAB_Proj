package student.test;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ CityOperationsTest.class, CourierOperationTest.class, CourierRequestOperationTest.class,
		DistrictOperationTest.class, PackageOperationsTest.class, UserOperationTest.class, VehicleOperationTest.class,
		IntegrationTests.class })
public class AllTests {

}
