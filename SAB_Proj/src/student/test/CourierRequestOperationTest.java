package student.test;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import student.pn140041_CourierOperations;
import student.pn140041_CourierRequestOperation;
import student.pn140041_UserOperations;
import student.pn140041_VehicleOperations;

public class CourierRequestOperationTest extends UnitTestBase {

	private static pn140041_UserOperations userOps = new pn140041_UserOperations();
	private static pn140041_VehicleOperations vehicleOps = new pn140041_VehicleOperations();
	private static pn140041_CourierOperations courierOps = new pn140041_CourierOperations();
	private static pn140041_CourierRequestOperation courierRequestOps = new pn140041_CourierRequestOperation();
	
	private static final String username1 = "username1";
	private static final String username2 = "username2";
	private static final String licencePlate1 = "123456789";
	private static final String licencePlate2 = "987654321";
	
	@Override
	@Before
	public void setUp() throws Exception {
		super.setUp();

		assertTrue(userOps.insertUser(username1, "A", "B", "password123"));
		assertTrue(userOps.insertUser(username2, "A", "B", "password123"));
		assertTrue(vehicleOps.insertVehicle(licencePlate1, 0, BigDecimal.ONE));
		assertTrue(vehicleOps.insertVehicle(licencePlate2, 0, BigDecimal.ONE));
	}
	
	
	@Test
	public void testChangeVehicleInCourierRequestEmpty() {
		assertFalse(courierRequestOps.changeVehicleInCourierRequest(null, null));
		assertFalse(courierRequestOps.changeVehicleInCourierRequest("", null));
		assertFalse(courierRequestOps.changeVehicleInCourierRequest(null, ""));
		assertFalse(courierRequestOps.changeVehicleInCourierRequest("", ""));
		assertFalse(courierRequestOps.changeVehicleInCourierRequest("A", "123"));
		assertFalse(courierRequestOps.changeVehicleInCourierRequest(username1, "123"));
		assertFalse(courierRequestOps.changeVehicleInCourierRequest("A", licencePlate1));
		assertFalse(courierRequestOps.changeVehicleInCourierRequest(username1, licencePlate1));
		
		checkStd();
	}
	
	@Test 
	public void testDeleteCourierRequestEmpty(){
		assertFalse(courierRequestOps.deleteCourierRequest(null));
		assertFalse(courierRequestOps.deleteCourierRequest(""));
		assertFalse(courierRequestOps.deleteCourierRequest("A"));
		assertFalse(courierRequestOps.deleteCourierRequest(username1));
		
		checkStd();
	}
	
	@Test
	public void testGetAllCourierRequestsEmpty(){
		assertEquals(0, courierRequestOps.getAllCourierRequests().size());
		
		checkStd();
	}
	
	@Test 
	public void testGrantRequestEmpty(){
		assertFalse(courierRequestOps.grantRequest(null));
		assertFalse(courierRequestOps.grantRequest(""));
		assertFalse(courierRequestOps.grantRequest("A"));
		assertFalse(courierRequestOps.grantRequest(username1));

		checkStd();
	}
	
	@Test 
	public void testInsertCourierRequestSingle(){
		assertTrue(courierRequestOps.insertCourierRequest(username1, licencePlate1));
		
		checkStd();
	}
	
	@Test 
	public void testInsertCourierRequestMultiple(){
		assertTrue(courierRequestOps.insertCourierRequest(username1, licencePlate1));
		assertTrue(courierRequestOps.insertCourierRequest(username2, licencePlate1));
	
		checkStd();
	}
	
	@Test 
	public void testGetAllCourierRequests(){
		List<String> result = null;
		
		result = courierRequestOps.getAllCourierRequests();
		assertEquals(0, result.size());
		
		assertTrue(courierRequestOps.insertCourierRequest(username1, licencePlate1));
		
		result = courierRequestOps.getAllCourierRequests();
		assertEquals(1, result.size());
		assertTrue(result.contains(username1));
		assertFalse(result.contains(username2));
		
		assertTrue(courierRequestOps.insertCourierRequest(username2, licencePlate1));
		
		result = courierRequestOps.getAllCourierRequests();
		assertEquals(2, result.size());
		assertTrue(result.contains(username1));
		assertTrue(result.contains(username2));		
	
		checkStd();
	}
	
	@Test 
	public void testInsertRequestTwoTimes(){
		assertTrue(courierRequestOps.insertCourierRequest(username1, licencePlate1));	
		assertFalse(courierRequestOps.insertCourierRequest(username1, licencePlate2));	
	
		checkStd();
	}
	
	@Test 
	public void testDeleteCourierRequest(){
		List<String> result = null;
		
		result = courierRequestOps.getAllCourierRequests();
		assertEquals(0, result.size());
		
		assertTrue(courierRequestOps.insertCourierRequest(username1, licencePlate1));	
		assertTrue(courierRequestOps.insertCourierRequest(username2, licencePlate1));
		
		result = courierRequestOps.getAllCourierRequests();
		assertEquals(2, result.size());
		assertTrue(result.contains(username1));
		assertTrue(result.contains(username2));	
		
		assertTrue(courierRequestOps.deleteCourierRequest(username1));
		
		result = courierRequestOps.getAllCourierRequests();
		assertEquals(1, result.size());
		assertFalse(result.contains(username1));
		assertTrue(result.contains(username2));	
		
		assertTrue(courierRequestOps.deleteCourierRequest(username2));

		result = courierRequestOps.getAllCourierRequests();
		assertEquals(0, result.size());
		assertFalse(result.contains(username1));
		assertFalse(result.contains(username2));	
	
		checkStd();
	}
	
	@Test 
	public void testDeleteCourierRequestTwoTimes(){
		List<String> result = null;
		
		result = courierRequestOps.getAllCourierRequests();
		assertEquals(0, result.size());
		
		assertTrue(courierRequestOps.insertCourierRequest(username1, licencePlate1));	
		assertTrue(courierRequestOps.insertCourierRequest(username2, licencePlate1));
		
		result = courierRequestOps.getAllCourierRequests();
		assertEquals(2, result.size());
		assertTrue(result.contains(username1));
		assertTrue(result.contains(username2));	
		
		assertTrue(courierRequestOps.deleteCourierRequest(username1));
		
		result = courierRequestOps.getAllCourierRequests();
		assertEquals(1, result.size());
		assertFalse(result.contains(username1));
		assertTrue(result.contains(username2));	
		
		assertFalse(courierRequestOps.deleteCourierRequest(username1));
		
		result = courierRequestOps.getAllCourierRequests();
		assertEquals(1, result.size());
		assertFalse(result.contains(username1));
		assertTrue(result.contains(username2));	

		assertTrue(courierRequestOps.deleteCourierRequest(username2));

		
		result = courierRequestOps.getAllCourierRequests();
		assertEquals(0, result.size());
		assertFalse(result.contains(username1));
		assertFalse(result.contains(username2));	
	
		checkStd();
	}
	
	
	@Test 
	public void testChangeVehicleInCourierRequest(){	
		assertTrue(courierRequestOps.insertCourierRequest(username1, licencePlate1));	
		assertTrue(courierRequestOps.changeVehicleInCourierRequest(username1, licencePlate2));
	
		checkStd();
	}
	
	@Test 
	public void testChangeVehicleInCourierRequestInvalid(){	
		assertTrue(courierRequestOps.insertCourierRequest(username1, licencePlate1));	
		assertFalse(courierRequestOps.changeVehicleInCourierRequest(username1, "159"));
		assertFalse(courierRequestOps.changeVehicleInCourierRequest(username2, licencePlate1));

		checkStd();
	}
	
	@Test 
	public void testGrantRequest(){	
		List<String> result = null;

		result = courierRequestOps.getAllCourierRequests();
		assertEquals(0, result.size());
		
		assertTrue(courierRequestOps.insertCourierRequest(username1, licencePlate1));	
		assertTrue(courierRequestOps.insertCourierRequest(username2, licencePlate1));
		
		assertEquals(0, courierOps.getAllCouriers().size());

		result = courierRequestOps.getAllCourierRequests();
		assertEquals(2, result.size());
		assertTrue(result.contains(username1));
		assertTrue(result.contains(username1));
		
		assertTrue(courierRequestOps.grantRequest(username1));
		
		result = courierRequestOps.getAllCourierRequests();
		assertEquals(1, result.size());
		assertFalse(result.contains(username1));
		assertTrue(result.contains(username2));
		assertEquals(1, courierOps.getAllCouriers().size());
		
		assertTrue(courierRequestOps.grantRequest(username2));

		result = courierRequestOps.getAllCourierRequests();
		assertEquals(0, result.size());
		assertFalse(result.contains(username1));
		assertFalse(result.contains(username2));
		assertEquals(2, courierOps.getAllCouriers().size());

		checkStd();
	}
	
	@Test 
	public void testInsertCourierRequestForExistingCourier(){	
		assertTrue(courierOps.insertCourier(username1, licencePlate1));
		assertFalse(courierRequestOps.insertCourierRequest(username1, licencePlate2));
		
		checkStd();
	}
	
	private static void checkStd(){
		List<String> users = userOps.getAllUsers();
		
		assertEquals(2, users.size());
		assertTrue(users.contains(username1));
		assertTrue(users.contains(username2));
		
		List<String> vehicles = vehicleOps.getAllVehichles();
		assertEquals(2, vehicles.size());
		assertTrue(vehicles.contains(licencePlate1));
		assertTrue(vehicles.contains(licencePlate2));
	}
}
