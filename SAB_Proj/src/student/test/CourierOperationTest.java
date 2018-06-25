package student.test;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import student.pn140041_CourierOperations;
import student.pn140041_UserOperations;
import student.pn140041_VehicleOperations;

public class CourierOperationTest extends UnitTestBase {

	private static pn140041_CourierOperations courierOps = new pn140041_CourierOperations();
	private static pn140041_UserOperations userOps = new pn140041_UserOperations();
	private static pn140041_VehicleOperations vehicleOps = new pn140041_VehicleOperations();

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
	public void testGetAllCouriserEmpty() {
		assertEquals(0, courierOps.getAllCouriers().size());

		checkStd();
	}

	@Test
	public void testDeleteCourierEmpty() {
		assertFalse(courierOps.deleteCourier(""));
		assertFalse(courierOps.deleteCourier(null));
		assertFalse(courierOps.deleteCourier("AAA"));

		checkStd();
	}

	@Test
	public void testDeleteCourierInvalid() {
		assertFalse(courierOps.deleteCourier(username1));

		checkStd();
	}

	@Test
	public void testGetAverageCourierProfitEmpty() {
		assertEquals(BigDecimal.ZERO, courierOps.getAverageCourierProfit(-1));
		assertEquals(BigDecimal.ZERO, courierOps.getAverageCourierProfit(0));
		assertEquals(BigDecimal.ZERO, courierOps.getAverageCourierProfit(10));

		checkStd();
	}

	@Test
	public void testGetCouriersWithStatusEmpty() {
		assertEquals(0, courierOps.getCouriersWithStatus(-1).size());
		assertEquals(0, courierOps.getCouriersWithStatus(0).size());
		assertEquals(0, courierOps.getCouriersWithStatus(1).size());
		assertEquals(0, courierOps.getCouriersWithStatus(2).size());
		assertEquals(0, courierOps.getCouriersWithStatus(3).size());
		assertEquals(0, courierOps.getCouriersWithStatus(4).size());

		checkStd();
	}

	@Test
	public void testInsertCourierSingle() {
		assertTrue(courierOps.insertCourier(username1, licencePlate1));

		checkStd();
	}

	@Test
	public void testInsertCourierMultiple() {
		assertTrue(courierOps.insertCourier(username1, licencePlate1));
		assertTrue(courierOps.insertCourier(username2, licencePlate2));

		checkStd();
	}

	@Test
	public void testInsertCourierMultipleSameVehicle() {
		assertTrue(courierOps.insertCourier(username1, licencePlate1));
		assertTrue(courierOps.insertCourier(username2, licencePlate1));

		checkStd();
	}

	@Test
	public void testGetCourierWithStatus() {
		assertEquals(0, courierOps.getCouriersWithStatus(0).size());
		assertEquals(0, courierOps.getCouriersWithStatus(1).size());

		assertTrue(courierOps.insertCourier(username1, licencePlate1));
		assertEquals(1, courierOps.getCouriersWithStatus(0).size());
		assertEquals(0, courierOps.getCouriersWithStatus(1).size());

		assertTrue(courierOps.insertCourier(username2, licencePlate1));
		assertEquals(2, courierOps.getCouriersWithStatus(0).size());
		assertEquals(0, courierOps.getCouriersWithStatus(1).size());

		checkStd();
	}

	@Test
	public void testGetAllCouriers() {
		List<String> result = null;

		result = courierOps.getAllCouriers();
		assertEquals(0, result.size());
		assertFalse(result.contains(username1));
		assertFalse(result.contains(username2));

		assertTrue(courierOps.insertCourier(username1, licencePlate1));
		result = courierOps.getAllCouriers();
		assertEquals(1, result.size());
		assertTrue(result.contains(username1));
		assertFalse(result.contains(username2));

		assertTrue(courierOps.insertCourier(username2, licencePlate1));
		result = courierOps.getAllCouriers();
		assertEquals(2, result.size());
		assertTrue(result.contains(username1));
		assertTrue(result.contains(username2));
		checkStd();
	}

	@Test
	public void testGetAverageCourierProfit() {
		assertTrue(courierOps.insertCourier(username1, licencePlate1));
		assertTrue(courierOps.insertCourier(username2, licencePlate1));

		assertEquals(BigDecimal.ZERO, courierOps.getAverageCourierProfit(0));

		checkStd();
	}

	@Test
	public void testDeleteCourier() {
		List<String> result = null;

		assertTrue(courierOps.insertCourier(username1, licencePlate1));
		assertTrue(courierOps.insertCourier(username2, licencePlate1));

		result = courierOps.getAllCouriers();
		assertEquals(2, courierOps.getCouriersWithStatus(0).size());
		assertEquals(0, courierOps.getCouriersWithStatus(1).size());
		assertEquals(2, result.size());
		assertTrue(result.contains(username1));
		assertTrue(result.contains(username2));

		assertTrue(courierOps.deleteCourier(username1));

		result = courierOps.getAllCouriers();
		assertEquals(1, courierOps.getCouriersWithStatus(0).size());
		assertEquals(0, courierOps.getCouriersWithStatus(1).size());
		assertEquals(1, result.size());
		assertFalse(result.contains(username1));
		assertTrue(result.contains(username2));

		assertTrue(courierOps.deleteCourier(username2));

		result = courierOps.getAllCouriers();
		assertEquals(0, courierOps.getCouriersWithStatus(0).size());
		assertEquals(0, courierOps.getCouriersWithStatus(1).size());
		assertEquals(0, result.size());
		assertFalse(result.contains(username1));
		assertFalse(result.contains(username2));

		checkStd();
	}

	@Test
	public void testDeleteCourierTwoTimes() {
		List<String> result = null;

		assertTrue(courierOps.insertCourier(username1, licencePlate1));
		assertTrue(courierOps.insertCourier(username2, licencePlate1));

		result = courierOps.getAllCouriers();
		assertEquals(2, courierOps.getCouriersWithStatus(0).size());
		assertEquals(0, courierOps.getCouriersWithStatus(1).size());
		assertEquals(2, result.size());
		assertTrue(result.contains(username1));
		assertTrue(result.contains(username2));

		assertTrue(courierOps.deleteCourier(username1));

		result = courierOps.getAllCouriers();
		assertEquals(1, courierOps.getCouriersWithStatus(0).size());
		assertEquals(0, courierOps.getCouriersWithStatus(1).size());
		assertEquals(1, result.size());
		assertFalse(result.contains(username1));
		assertTrue(result.contains(username2));

		assertFalse(courierOps.deleteCourier(username1));

		result = courierOps.getAllCouriers();
		assertEquals(1, courierOps.getCouriersWithStatus(0).size());
		assertEquals(0, courierOps.getCouriersWithStatus(1).size());
		assertEquals(1, result.size());
		assertFalse(result.contains(username1));
		assertTrue(result.contains(username2));

		assertTrue(courierOps.deleteCourier(username2));

		result = courierOps.getAllCouriers();
		assertEquals(0, courierOps.getCouriersWithStatus(0).size());
		assertEquals(0, courierOps.getCouriersWithStatus(1).size());
		assertEquals(0, result.size());
		assertFalse(result.contains(username1));
		assertFalse(result.contains(username2));

		checkStd();
	}

	private static void checkStd() {
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
