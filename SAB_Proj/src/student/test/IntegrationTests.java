package student.test;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import student.pn140041_CityOperations;
import student.pn140041_CourierOperations;
import student.pn140041_CourierRequestOperation;
import student.pn140041_DistrictOperations;
import student.pn140041_PackageOperations;
import student.pn140041_UserOperations;
import student.pn140041_VehicleOperations;

public class IntegrationTests extends UnitTestBase {

	private static pn140041_UserOperations userOps = new pn140041_UserOperations();
	private static pn140041_VehicleOperations vehicleOps = new pn140041_VehicleOperations();
	private static pn140041_CourierOperations courierOps = new pn140041_CourierOperations();
	private static pn140041_CourierRequestOperation courierRequestOps = new pn140041_CourierRequestOperation();
	private static pn140041_CityOperations cityOps = new pn140041_CityOperations();
	private static pn140041_DistrictOperations districtOps = new pn140041_DistrictOperations();
	private static pn140041_PackageOperations packageOps = new pn140041_PackageOperations();

	private static final String username1 = "username1";
	private static final String username2 = "username2";
	private static final String licencePlate1 = "123456789";
	private static final String licencePlate2 = "987654321";
	private static int cityId;
	private static int districtId1;
	private static int districtId2;
	private static int districtId3;
	private static double distance12;
	private static double distance13;
	private static double distance23;

	@Override
	@Before
	public void setUp() throws Exception {
		super.setUp();

		assertTrue(userOps.insertUser(username1, "A", "B", "password123"));
		assertTrue(userOps.insertUser(username2, "A", "B", "password123"));
		assertTrue(vehicleOps.insertVehicle(licencePlate1, 0, BigDecimal.ONE));
		assertTrue(vehicleOps.insertVehicle(licencePlate2, 0, BigDecimal.ONE));
		cityId = cityOps.insertCity("A", "456");
		assertNotEquals(-1, cityId);
		districtId1 = districtOps.insertDistrict("A", cityId, 0, 0);
		assertNotEquals(-1, districtId1);
		districtId2 = districtOps.insertDistrict("B", cityId, 10, 0);
		assertNotEquals(-1, districtId2);
		districtId3 = districtOps.insertDistrict("C", cityId, 7, 13);
		assertNotEquals(-1, districtId3);
		assertTrue(courierOps.insertCourier(username1, licencePlate1));
		assertTrue(courierOps.insertCourier(username2, licencePlate1));
		distance12 = PackageOperationsTest.euclidean(0, 0, 10, 0);
		distance13 = PackageOperationsTest.euclidean(0, 0, 7, 13);
		distance23 = PackageOperationsTest.euclidean(10, 0, 7, 13);
	}
	
	@Test
	public void testGetSentPackages() {
		int packageId1;
		int packageId2;
		int packageId3;
		
		assertEquals(0, userOps.getSentPackages(username1).intValue());
		assertEquals(0, userOps.getSentPackages(username2).intValue());

		packageId1 = packageOps.insertPackage(districtId1, districtId1, username1, 0, BigDecimal.ONE);
		assertNotEquals(-1, packageId1);
		assertEquals(1, userOps.getSentPackages(username1).intValue());
		assertEquals(0, userOps.getSentPackages(username2).intValue());
		
		assertTrue(packageOps.acceptAnOffer(packageOps.insertTransportOffer(username1, packageId1, BigDecimal.ZERO)));
		assertEquals(1, userOps.getSentPackages(username1).intValue());
		assertEquals(0, userOps.getSentPackages(username2).intValue());
		
		assertEquals(packageId1, packageOps.driveNextPackage(username1));
		assertEquals(-1, packageOps.driveNextPackage(username1));
		assertEquals(1, userOps.getSentPackages(username1).intValue());
		assertEquals(0, userOps.getSentPackages(username2).intValue());
		
		
		
		packageId2 = packageOps.insertPackage(districtId1, districtId1, username2, 0, BigDecimal.ONE);
		assertNotEquals(-1, packageId2);
		assertEquals(1, userOps.getSentPackages(username1).intValue());
		assertEquals(1, userOps.getSentPackages(username2).intValue());
		
		assertTrue(packageOps.acceptAnOffer(packageOps.insertTransportOffer(username2, packageId2, BigDecimal.ZERO)));
		assertEquals(1, userOps.getSentPackages(username1).intValue());
		assertEquals(1, userOps.getSentPackages(username2).intValue());
		
		assertEquals(packageId2, packageOps.driveNextPackage(username2));
		assertEquals(-1, packageOps.driveNextPackage(username1));
		assertEquals(1, userOps.getSentPackages(username1).intValue());
		assertEquals(1, userOps.getSentPackages(username2).intValue());
		
		
		
		packageId3 = packageOps.insertPackage(districtId1, districtId1, username1, 0, BigDecimal.ONE);
		assertNotEquals(-1, packageId3);
		assertEquals(2, userOps.getSentPackages(username1).intValue());
		assertEquals(1, userOps.getSentPackages(username2).intValue());
		
		assertTrue(packageOps.acceptAnOffer(packageOps.insertTransportOffer(username2, packageId3, BigDecimal.ZERO)));
		assertEquals(2, userOps.getSentPackages(username1).intValue());
		assertEquals(1, userOps.getSentPackages(username2).intValue());
		
		assertEquals(packageId3, packageOps.driveNextPackage(username2));
		assertEquals(-1, packageOps.driveNextPackage(username1));
		assertEquals(2, userOps.getSentPackages(username1).intValue());
		assertEquals(1, userOps.getSentPackages(username2).intValue());
		
		
		checkStd();

	}
	
	@Test
	public void testGetCourierWithStatus(){
		List<String> result = null;
		int packageId1;
		int packageId2;
		int packageId3;
		
		result = courierOps.getCouriersWithStatus(0);
		assertEquals(2, result.size());
		assertTrue(result.contains(username1));
		assertTrue(result.contains(username2));
		result = courierOps.getCouriersWithStatus(1);
		assertEquals(0, result.size());
		
		packageId1 = packageOps.insertPackage(districtId1, districtId1, username1, 0, BigDecimal.ONE);
		assertNotEquals(-1, packageId1);
		assertTrue(packageOps.acceptAnOffer(packageOps.insertTransportOffer(username1, packageId1, BigDecimal.ZERO)));
	
		result = courierOps.getCouriersWithStatus(0);
		assertEquals(2, result.size());
		assertTrue(result.contains(username1));
		assertTrue(result.contains(username2));
		result = courierOps.getCouriersWithStatus(1);
		assertEquals(0, result.size());
		
		assertEquals(packageId1, packageOps.driveNextPackage(username1));
		result = courierOps.getCouriersWithStatus(0);
		assertEquals(2, result.size());
		assertTrue(result.contains(username1));
		assertTrue(result.contains(username2));
		result = courierOps.getCouriersWithStatus(1);
		assertEquals(0, result.size());
		
		packageId2 = packageOps.insertPackage(districtId1, districtId1, username1, 0, BigDecimal.ONE);
		packageId3 = packageOps.insertPackage(districtId1, districtId1, username1, 0, BigDecimal.ONE);
		assertNotEquals(-1, packageId2);
		assertNotEquals(-1, packageId3);
		assertTrue(packageOps.acceptAnOffer(packageOps.insertTransportOffer(username1, packageId2, BigDecimal.ZERO)));
		assertTrue(packageOps.acceptAnOffer(packageOps.insertTransportOffer(username1, packageId3, BigDecimal.ZERO)));

		result = courierOps.getCouriersWithStatus(0);
		assertEquals(2, result.size());
		assertTrue(result.contains(username1));
		assertTrue(result.contains(username2));
		result = courierOps.getCouriersWithStatus(1);
		assertEquals(0, result.size());
		
		assertEquals(packageId2, packageOps.driveNextPackage(username1));
		result = courierOps.getCouriersWithStatus(0);
		assertEquals(1, result.size());
		assertFalse(result.contains(username1));
		assertTrue(result.contains(username2));
		result = courierOps.getCouriersWithStatus(1);
		assertEquals(1, result.size());
		assertTrue(result.contains(username1));
		
		assertEquals(packageId3, packageOps.driveNextPackage(username1));
		result = courierOps.getCouriersWithStatus(0);
		assertEquals(2, result.size());
		assertTrue(result.contains(username1));
		assertTrue(result.contains(username2));
		result = courierOps.getCouriersWithStatus(1);
		assertEquals(0, result.size());

		checkStd();
	}
	
	@Test
	public void testCourierConfiltVehicle(){
		List<String> result = null;
		int packageId1;
		int packageId2;
		int packageId3;
		
		packageId1 = packageOps.insertPackage(districtId1, districtId1, username1, 0, BigDecimal.ONE);
		assertNotEquals(-1, packageId1);
		assertTrue(packageOps.acceptAnOffer(packageOps.insertTransportOffer(username1, packageId1, BigDecimal.ZERO)));
	
		packageId2 = packageOps.insertPackage(districtId1, districtId1, username1, 0, BigDecimal.ONE);
		assertNotEquals(-1, packageId1);
		assertTrue(packageOps.acceptAnOffer(packageOps.insertTransportOffer(username1, packageId2, BigDecimal.ZERO)));
	
		packageId3 = packageOps.insertPackage(districtId1, districtId1, username1, 0, BigDecimal.ONE);
		assertNotEquals(-1, packageId1);
		assertTrue(packageOps.acceptAnOffer(packageOps.insertTransportOffer(username2, packageId3, BigDecimal.ZERO)));
	
		assertEquals(packageId1, packageOps.driveNextPackage(username1));
		assertEquals(-2, packageOps.driveNextPackage(username2));
		assertEquals(packageId2, packageOps.driveNextPackage(username1));
		assertEquals(packageId3, packageOps.driveNextPackage(username2));

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

		List<Integer> cities = cityOps.getAllCities();
		assertEquals(1, cities.size());
		assertTrue(cities.contains(cityId));

		List<Integer> districts = districtOps.getAllDistricts();
		assertEquals(3, districts.size());
		assertEquals(3, districtOps.getAllDistrictsFromCity(cityId).size());
		assertTrue(districts.contains(districtId1));
		assertTrue(districts.contains(districtId2));
		assertTrue(districts.contains(districtId3));

		List<String> couriers = courierOps.getAllCouriers();
		assertEquals(2, couriers.size());
		assertTrue(couriers.contains(username1));
		assertTrue(couriers.contains(username2));
	}

}
