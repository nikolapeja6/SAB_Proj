package student.test;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import operations.PackageOperations;
import operations.PackageOperations.Pair;
import student.pn140041_CityOperations;
import student.pn140041_CourierOperations;
import student.pn140041_CourierRequestOperation;
import student.pn140041_DistrictOperations;
import student.pn140041_PackageOperations;
import student.pn140041_UserOperations;
import student.pn140041_VehicleOperations;
import student.helper.Logger;

public class PackageOperationsTest extends UnitTestBase {

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
		assertTrue(courierOps.insertCourier(username2, licencePlate2));
		distance12 = euclidean(0, 0, 10, 0);
		distance13 = euclidean(0, 0, 7, 13);
		distance23 = euclidean(10, 0, 7, 13);
	}

	@Test
	public void testAcceptOfferEmpty() {
		assertFalse(packageOps.acceptAnOffer(-1));
		assertFalse(packageOps.acceptAnOffer(5));

		checkStd();
	}

	@Test
	public void testChangeTypeEmpty() {
		assertFalse(packageOps.changeType(-1, -1));
		assertFalse(packageOps.changeType(1, -1));
		assertFalse(packageOps.changeType(1, 3));
		assertFalse(packageOps.changeType(1, 1));

		checkStd();
	}

	@Test
	public void testChangeWeightEmpty() {
		assertFalse(packageOps.changeWeight(-1, BigDecimal.valueOf(-1)));
		assertFalse(packageOps.changeWeight(1, BigDecimal.valueOf(-1)));
		assertFalse(packageOps.changeWeight(1, BigDecimal.valueOf(0)));
		assertFalse(packageOps.changeWeight(1, null));
		assertFalse(packageOps.changeWeight(1, BigDecimal.valueOf(5)));

		checkStd();
	}

	@Test
	public void testDeletePackageEmpty() {
		assertFalse(packageOps.deletePackage(-1));
		assertFalse(packageOps.deletePackage(1));

		checkStd();
	}

	@Test
	public void testDriveNextPackageEmpty() {
		assertEquals(-1, packageOps.driveNextPackage(username1));
		assertEquals(-1, packageOps.driveNextPackage(username2));
		assertEquals(-2, packageOps.driveNextPackage("SOME_RANDOM_STRING"));
		assertEquals(-2, packageOps.driveNextPackage(null));

		checkStd();
	}

	@Test
	public void testGetAcceptanceTimeEmpty() {
		assertNull(packageOps.getAcceptanceTime(-1));
		assertNull(packageOps.getAcceptanceTime(1));

		checkStd();
	}

	@Test
	public void testGetAllOffersEmpty() {
		assertEquals(0, packageOps.getAllOffers().size());

		checkStd();
	}

	@Test
	public void testGetOffersForPackageEmpty() {
		assertEquals(0, packageOps.getAllOffersForPackage(-1).size());
		assertEquals(0, packageOps.getAllOffersForPackage(1).size());

		checkStd();
	}

	@Test
	public void testGetAllPackagesEmpty() {
		assertEquals(0, packageOps.getAllPackages().size());

		checkStd();
	}

	@Test
	public void testGetAllPackagesWithTypeEmpty() {
		assertEquals(0, packageOps.getAllPackagesWithSpecificType(-1).size());
		assertEquals(0, packageOps.getAllPackagesWithSpecificType(0).size());
		assertEquals(0, packageOps.getAllPackagesWithSpecificType(1).size());

		checkStd();
	}

	@Test
	public void testGetDeliveryStatusEmpty() {
		assertNull(packageOps.getDeliveryStatus(-1));
		assertNull(packageOps.getDeliveryStatus(1));

		checkStd();
	}

	@Test
	public void testGetPriceOfDeliveryEmpty() {
		assertNull(packageOps.getPriceOfDelivery(-1));
		assertNull(packageOps.getPriceOfDelivery(1));

		checkStd();
	}

	@Test
	public void testInsertPackageSingle() {
		int id = packageOps.insertPackage(districtId1, districtId2, username1, 0, BigDecimal.ONE);
		assertNotEquals(-1, id);

		List<Integer> result = packageOps.getAllPackages();
		assertEquals(1, result.size());
		assertTrue(result.contains(id));

		checkStd();
	}

	@Test
	public void testInsertPackageMultiple() {
		List<Integer> result = null;

		result = packageOps.getAllPackages();
		assertEquals(0, result.size());

		int id1 = packageOps.insertPackage(districtId1, districtId2, username1, 0, BigDecimal.ONE);
		assertNotEquals(-1, id1);

		result = packageOps.getAllPackages();
		assertEquals(1, result.size());
		assertTrue(result.contains(id1));

		int id2 = packageOps.insertPackage(districtId2, districtId3, username2, 0, BigDecimal.TEN);
		assertNotEquals(-1, id2);

		result = packageOps.getAllPackages();
		assertEquals(2, result.size());
		assertTrue(result.contains(id1));
		assertTrue(result.contains(id2));

		checkStd();
	}

	@Test
	public void testInsertOfferSingle() {
		List<Integer> result = null;

		int id1 = packageOps.insertPackage(districtId1, districtId2, username1, 0, BigDecimal.ONE);
		int id2 = packageOps.insertPackage(districtId2, districtId3, username2, 0, BigDecimal.TEN);

		assertNotEquals(-1, id1);
		assertNotEquals(-1, id2);
		result = packageOps.getAllPackages();
		assertEquals(2, result.size());
		assertTrue(result.contains(id1));
		assertTrue(result.contains(id2));

		int id3 = packageOps.insertTransportOffer(username1, id1, BigDecimal.valueOf(12));
		assertNotEquals(-1, id3);
		result = packageOps.getAllOffers();
		assertEquals(1, result.size());
		assertTrue(result.contains(id3));

		assertTrue(packageOps.acceptAnOffer(id3));

		result = packageOps.getAllOffers();
		assertEquals(0, result.size());

		checkStd();
	}

	@Test
	public void testInsertOfferMultipleDifferentPackages() {
		List<Integer> result = null;
		List<Pair<Integer, BigDecimal>> res = null;

		int id1 = packageOps.insertPackage(districtId1, districtId2, username1, 0, BigDecimal.ONE);
		int id2 = packageOps.insertPackage(districtId2, districtId3, username2, 0, BigDecimal.TEN);

		assertNotEquals(-1, id1);
		assertNotEquals(-1, id2);
		result = packageOps.getAllPackages();
		assertEquals(2, result.size());
		assertTrue(result.contains(id1));
		assertTrue(result.contains(id2));

		int id3 = packageOps.insertTransportOffer(username1, id1, BigDecimal.valueOf(2));
		int id4 = packageOps.insertTransportOffer(username1, id2, BigDecimal.valueOf(4));

		assertNotEquals(-1, id3);
		assertNotEquals(-1, id4);
		result = packageOps.getAllOffers();
		assertEquals(2, result.size());
		assertTrue(result.contains(id3));
		assertTrue(result.contains(id4));
		res = packageOps.getAllOffersForPackage(id1);
		assertEquals(1, res.size());
		res = packageOps.getAllOffersForPackage(id2);
		assertEquals(1, res.size());

		assertTrue(packageOps.acceptAnOffer(id3));

		result = packageOps.getAllOffers();
		assertEquals(1, result.size());
		assertFalse(result.contains(id3));
		assertTrue(result.contains(id4));
		res = packageOps.getAllOffersForPackage(id1);
		assertEquals(0, res.size());
		res = packageOps.getAllOffersForPackage(id2);
		assertEquals(1, res.size());

		assertTrue(packageOps.acceptAnOffer(id4));

		result = packageOps.getAllOffers();
		assertEquals(0, result.size());
		assertFalse(result.contains(id3));
		assertFalse(result.contains(id4));
		res = packageOps.getAllOffersForPackage(id1);
		assertEquals(0, res.size());
		res = packageOps.getAllOffersForPackage(id2);
		assertEquals(0, res.size());

		checkStd();
	}

	@Test
	public void testInsertOfferMultipleSamePackages() {
		List<Integer> result = null;
		List<Pair<Integer, BigDecimal>> res = null;

		int id1 = packageOps.insertPackage(districtId1, districtId2, username1, 0, BigDecimal.ONE);
		int id2 = packageOps.insertPackage(districtId2, districtId3, username2, 0, BigDecimal.TEN);

		assertNotEquals(-1, id1);
		assertNotEquals(-1, id2);
		result = packageOps.getAllPackages();
		assertEquals(2, result.size());
		assertTrue(result.contains(id1));
		assertTrue(result.contains(id2));

		int id3 = packageOps.insertTransportOffer(username1, id1, BigDecimal.valueOf(2));
		int id4 = packageOps.insertTransportOffer(username1, id1, BigDecimal.valueOf(4));

		assertNotEquals(-1, id3);
		assertNotEquals(-1, id4);
		result = packageOps.getAllOffers();
		assertEquals(2, result.size());
		assertTrue(result.contains(id3));
		assertTrue(result.contains(id4));
		res = packageOps.getAllOffersForPackage(id1);
		assertEquals(2, result.size());
		res = packageOps.getAllOffersForPackage(id2);
		assertEquals(0, res.size());

		assertTrue(packageOps.acceptAnOffer(id3));

		result = packageOps.getAllOffers();
		assertEquals(0, result.size());
		assertFalse(result.contains(id3));
		assertFalse(result.contains(id4));
		res = packageOps.getAllOffersForPackage(id1);
		assertEquals(0, res.size());

		assertFalse(packageOps.acceptAnOffer(id4));

		checkStd();
	}

	@Test
	public void testChangePackageType() {
		int id = packageOps.insertPackage(districtId1, districtId2, username1, 0, BigDecimal.ONE);

		assertNotEquals(-1, id);

		assertTrue(packageOps.changeType(id, 0));
		assertTrue(packageOps.changeType(id, 1));
		assertTrue(packageOps.changeType(id, 2));
		assertFalse(packageOps.changeType(id, 3));
		assertFalse(packageOps.changeType(id, -1));

		checkStd();
	}

	@Test
	public void testChangePackageWeight() {
		int id = packageOps.insertPackage(districtId1, districtId2, username1, 0, BigDecimal.ONE);

		assertNotEquals(-1, id);

		assertTrue(packageOps.changeWeight(id, BigDecimal.valueOf(1)));
		assertTrue(packageOps.changeWeight(id, BigDecimal.valueOf(10)));
		assertTrue(packageOps.changeWeight(id, BigDecimal.valueOf(100)));
		assertFalse(packageOps.changeWeight(id, BigDecimal.valueOf(-1)));
		assertFalse(packageOps.changeWeight(id, BigDecimal.valueOf(0)));

		checkStd();
	}

	@Test
	public void testGetAcceptanceTimeSingle() {
		int idPackage = packageOps.insertPackage(districtId1, districtId2, username1, 0, BigDecimal.ONE);

		assertNotEquals(-1, idPackage);

		int idOffer = packageOps.insertTransportOffer(username1, idPackage, BigDecimal.ONE);

		assertNotEquals(-1, idOffer);

		Date acceptanceDate = packageOps.getAcceptanceTime(idPackage);
		assertNull(acceptanceDate);

		assertTrue(packageOps.acceptAnOffer(idOffer));
		acceptanceDate = packageOps.getAcceptanceTime(idPackage);
		assertNotNull(acceptanceDate);

		checkStd();
	}

	@Test
	public void testGetAcceptanceTimeMultiple() {
		int idPackage1 = packageOps.insertPackage(districtId1, districtId2, username1, 0, BigDecimal.ONE);
		int idPackage2 = packageOps.insertPackage(districtId1, districtId2, username1, 0, BigDecimal.ONE);

		assertNotEquals(-1, idPackage1);
		assertNotEquals(-1, idPackage2);

		int idOffer1 = packageOps.insertTransportOffer(username1, idPackage1, BigDecimal.ONE);
		int idOffer2 = packageOps.insertTransportOffer(username1, idPackage2, BigDecimal.ONE);

		assertNotEquals(-1, idOffer1);
		assertNotEquals(-1, idOffer2);

		Date acceptanceDate1 = packageOps.getAcceptanceTime(idPackage1);
		Date acceptanceDate2 = packageOps.getAcceptanceTime(idPackage2);

		assertNull(acceptanceDate1);
		assertNull(acceptanceDate2);

		assertTrue(packageOps.acceptAnOffer(idOffer1));

		acceptanceDate1 = packageOps.getAcceptanceTime(idPackage1);
		acceptanceDate2 = packageOps.getAcceptanceTime(idPackage2);

		assertNotNull(acceptanceDate1);
		assertNull(acceptanceDate2);

		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		assertTrue(packageOps.acceptAnOffer(idOffer2));

		acceptanceDate1 = packageOps.getAcceptanceTime(idPackage1);
		acceptanceDate2 = packageOps.getAcceptanceTime(idPackage2);

		assertNotNull(acceptanceDate1);
		assertNotNull(acceptanceDate2);

		Logger.Log("" + acceptanceDate1.getTime());
		Logger.Log("" + acceptanceDate2.getTime());

		assertTrue(acceptanceDate1.before(acceptanceDate2));

		checkStd();
	}

	@Test
	public void testDeletePackage1() {
		List<Integer> result = null;

		int idPackage1 = packageOps.insertPackage(districtId1, districtId2, username1, 0, BigDecimal.ONE);
		int idPackage2 = packageOps.insertPackage(districtId1, districtId2, username1, 0, BigDecimal.ONE);

		result = packageOps.getAllPackages();

		assertEquals(2, result.size());
		assertTrue(result.contains(idPackage1));
		assertTrue(result.contains(idPackage2));

		assertTrue(packageOps.deletePackage(idPackage1));

		result = packageOps.getAllPackages();
		assertEquals(1, result.size());
		assertFalse(result.contains(idPackage1));
		assertTrue(result.contains(idPackage2));

		assertFalse(packageOps.deletePackage(idPackage1));

		result = packageOps.getAllPackages();
		assertEquals(1, result.size());
		assertFalse(result.contains(idPackage1));
		assertTrue(result.contains(idPackage2));

		assertTrue(packageOps.deletePackage(idPackage2));

		result = packageOps.getAllPackages();
		assertEquals(0, result.size());
		assertFalse(result.contains(idPackage1));
		assertFalse(result.contains(idPackage2));

		checkStd();
	}

	@Test
	public void testDeletePackage2() {
		List<Integer> result = null;

		int idPackage1 = packageOps.insertPackage(districtId1, districtId2, username1, 0, BigDecimal.ONE);
		int idPackage2 = packageOps.insertPackage(districtId1, districtId2, username1, 0, BigDecimal.ONE);

		int idOffer1 = packageOps.insertTransportOffer(username1, idPackage1, BigDecimal.ONE);
		int idOffer2 = packageOps.insertTransportOffer(username1, idPackage2, BigDecimal.ONE);

		result = packageOps.getAllOffers();

		assertEquals(2, result.size());
		assertTrue(result.contains(idOffer1));
		assertTrue(result.contains(idOffer2));

		assertTrue(packageOps.deletePackage(idPackage1));

		result = packageOps.getAllOffers();
		assertEquals(1, result.size());
		assertFalse(result.contains(idOffer1));
		assertTrue(result.contains(idOffer2));

		assertTrue(packageOps.deletePackage(idPackage2));

		result = packageOps.getAllOffers();
		assertEquals(0, result.size());
		assertFalse(result.contains(idOffer1));
		assertFalse(result.contains(idOffer2));

		checkStd();
	}

	@Test
	public void testGetPriceOfDelivery() {
		int idPackage;
		BigDecimal actualPrice;
		BigDecimal expectedPrice;

		BigDecimal[] weights = new BigDecimal[] { BigDecimal.ONE, BigDecimal.TEN, BigDecimal.valueOf(3),
				BigDecimal.valueOf(7), BigDecimal.valueOf(123), BigDecimal.valueOf(987) };

		for (int type = 0; type < 3; type++) {
			for (int i = 0; i < weights.length; i++) {
				idPackage = packageOps.insertPackage(districtId1, districtId2, username1, type, weights[i]);
				BigDecimal percent = BigDecimal.valueOf(1);
				packageOps.acceptAnOffer(packageOps.insertTransportOffer(username1, idPackage, percent));
				actualPrice = packageOps.getPriceOfDelivery(idPackage);
				expectedPrice = getPackagePrice(type, weights[i], distance12, percent);

				assertTrue(actualPrice.compareTo(expectedPrice.multiply(new BigDecimal(1.05D))) <= 0);
				assertTrue(actualPrice.compareTo(expectedPrice.multiply(new BigDecimal(0.95D))) >= 0);
			}
		}

		for (int type = 0; type < 3; type++) {
			for (int i = 0; i < weights.length; i++) {
				idPackage = packageOps.insertPackage(districtId1, districtId3, username1, type, weights[i]);
				BigDecimal percent = BigDecimal.valueOf(2);
				packageOps.acceptAnOffer(packageOps.insertTransportOffer(username1, idPackage, percent));
				actualPrice = packageOps.getPriceOfDelivery(idPackage);
				expectedPrice = getPackagePrice(type, weights[i], distance13, percent);

				assertTrue(actualPrice.compareTo(expectedPrice.multiply(new BigDecimal(1.05D))) <= 0);
				assertTrue(actualPrice.compareTo(expectedPrice.multiply(new BigDecimal(0.95D))) >= 0);
			}
		}

		for (int type = 0; type < 3; type++) {
			for (int i = 0; i < weights.length; i++) {
				idPackage = packageOps.insertPackage(districtId2, districtId3, username1, type, weights[i]);
				BigDecimal percent = BigDecimal.valueOf(5);
				packageOps.acceptAnOffer(packageOps.insertTransportOffer(username1, idPackage, percent));
				actualPrice = packageOps.getPriceOfDelivery(idPackage);
				expectedPrice = getPackagePrice(type, weights[i], distance23, percent);

				assertTrue(actualPrice.compareTo(expectedPrice.multiply(new BigDecimal(1.05D))) <= 0);
				assertTrue(actualPrice.compareTo(expectedPrice.multiply(new BigDecimal(0.95D))) >= 0);
			}
		}

		for (int type = 0; type < 3; type++) {
			for (int i = 0; i < weights.length; i++) {
				idPackage = packageOps.insertPackage(districtId1, districtId1, username1, type, weights[i]);
				BigDecimal percent = BigDecimal.valueOf(10);
				packageOps.acceptAnOffer(packageOps.insertTransportOffer(username1, idPackage, percent));
				actualPrice = packageOps.getPriceOfDelivery(idPackage);
				expectedPrice = getPackagePrice(type, weights[i], 0, percent);

				assertTrue(actualPrice.compareTo(expectedPrice.multiply(new BigDecimal(1.05D))) <= 0);
				assertTrue(actualPrice.compareTo(expectedPrice.multiply(new BigDecimal(0.95D))) >= 0);
			}
		}

		checkStd();
	}

	@Test
	public void testGetAllPackagesWithType() {

		int id1 = packageOps.insertPackage(districtId1, districtId1, username1, 0, BigDecimal.ONE);
		int id2 = packageOps.insertPackage(districtId1, districtId1, username1, 0, BigDecimal.ONE);

		int id3 = packageOps.insertPackage(districtId1, districtId1, username1, 1, BigDecimal.ONE);
		int id4 = packageOps.insertPackage(districtId1, districtId1, username1, 1, BigDecimal.ONE);

		int id5 = packageOps.insertPackage(districtId1, districtId1, username1, 2, BigDecimal.ONE);
		int id6 = packageOps.insertPackage(districtId1, districtId1, username1, 2, BigDecimal.ONE);

		assertNotEquals(-1, id1);
		assertNotEquals(-1, id2);
		assertNotEquals(-1, id3);
		assertNotEquals(-1, id4);
		assertNotEquals(-1, id5);
		assertNotEquals(-1, id6);

		List<Integer> result = null;

		result = packageOps.getAllPackagesWithSpecificType(0);
		assertEquals(2, result.size());
		assertTrue(result.contains(id1));
		assertTrue(result.contains(id2));

		result = packageOps.getAllPackagesWithSpecificType(1);
		assertEquals(2, result.size());
		assertTrue(result.contains(id3));
		assertTrue(result.contains(id4));

		result = packageOps.getAllPackagesWithSpecificType(2);
		assertEquals(2, result.size());
		assertTrue(result.contains(id5));
		assertTrue(result.contains(id6));

		assertTrue(packageOps.changeType(id1, 1));
		assertTrue(packageOps.changeType(id2, 2));

		result = packageOps.getAllPackagesWithSpecificType(1);
		assertEquals(3, result.size());
		assertTrue(result.contains(id3));
		assertTrue(result.contains(id4));
		assertTrue(result.contains(id1));

		result = packageOps.getAllPackagesWithSpecificType(2);
		assertEquals(3, result.size());
		assertTrue(result.contains(id5));
		assertTrue(result.contains(id6));
		assertTrue(result.contains(id2));

		assertTrue(packageOps.changeType(id1, 2));
		assertTrue(packageOps.changeType(id3, 2));
		assertTrue(packageOps.changeType(id4, 2));

		result = packageOps.getAllPackagesWithSpecificType(0);
		assertEquals(0, result.size());

		result = packageOps.getAllPackagesWithSpecificType(1);
		assertEquals(0, result.size());

		result = packageOps.getAllPackagesWithSpecificType(2);
		assertEquals(6, result.size());
		assertTrue(result.contains(id1));
		assertTrue(result.contains(id2));
		assertTrue(result.contains(id3));
		assertTrue(result.contains(id4));
		assertTrue(result.contains(id5));
		assertTrue(result.contains(id6));

		checkStd();
	}

	@Test
	public void testGetDriveEmpty() {
		List<Integer> result = null;

		result = packageOps.getDrive(username1);
		assertEquals(0, result.size());

		int idPackage1 = packageOps.insertPackage(districtId1, districtId2, username1, 0, BigDecimal.ONE);
		int idPackage2 = packageOps.insertPackage(districtId1, districtId2, username1, 0, BigDecimal.ONE);

		assertNotEquals(-1, idPackage1);
		assertNotEquals(-1, idPackage2);
		result = packageOps.getDrive(username1);
		assertEquals(0, result.size());

		int idOffer1 = packageOps.insertTransportOffer(username1, idPackage1, BigDecimal.ONE);
		int idOffer2 = packageOps.insertTransportOffer(username1, idPackage2, BigDecimal.ONE);

		assertNotEquals(-1, idOffer1);
		assertNotEquals(-1, idOffer2);
		result = packageOps.getDrive(username1);
		assertEquals(0, result.size());

		assertTrue(packageOps.acceptAnOffer(idOffer1));

		assertNotEquals(-1, idOffer1);
		assertNotEquals(-1, idOffer2);
		result = packageOps.getDrive(username1);
		assertEquals(0, result.size());

		checkStd();
	}

	@Test
	public void testDriving() {
		List<Integer> result = null;

		int idPackage1;
		int idPackage2;
		int idPackage3;
		int idPackage4;

		int idOffer1;
		int idOffer2;
		int idOffer3;
		int idOffer4;

		result = packageOps.getDrive(username1);
		assertEquals(0, result.size());
		result = packageOps.getDrive(username2);
		assertEquals(0, result.size());

		idPackage1 = packageOps.insertPackage(districtId1, districtId2, username1, 0, BigDecimal.ONE);
		idPackage2 = packageOps.insertPackage(districtId1, districtId2, username1, 0, BigDecimal.ONE);
		idPackage3 = packageOps.insertPackage(districtId1, districtId2, username1, 0, BigDecimal.ONE);
		idPackage4 = packageOps.insertPackage(districtId1, districtId2, username1, 0, BigDecimal.ONE);

		assertNotEquals(-1, idPackage1);
		assertNotEquals(-1, idPackage2);
		assertNotEquals(-1, idPackage3);
		assertNotEquals(-1, idPackage4);

		assertEquals(0, packageOps.getDeliveryStatus(idPackage1).intValue());
		assertEquals(0, packageOps.getDeliveryStatus(idPackage2).intValue());
		assertEquals(0, packageOps.getDeliveryStatus(idPackage3).intValue());
		assertEquals(0, packageOps.getDeliveryStatus(idPackage4).intValue());

		idOffer1 = packageOps.insertTransportOffer(username1, idPackage1, BigDecimal.ONE);
		idOffer2 = packageOps.insertTransportOffer(username2, idPackage3, BigDecimal.ONE);
		idOffer3 = packageOps.insertTransportOffer(username1, idPackage4, BigDecimal.ONE);
		idOffer4 = packageOps.insertTransportOffer(username1, idPackage4, BigDecimal.ONE);

		result = packageOps.getDrive(username1);
		assertEquals(0, result.size());
		result = packageOps.getDrive(username2);
		assertEquals(0, result.size());

		assertNotEquals(-1, idOffer1);
		assertNotEquals(-1, idOffer2);
		assertNotEquals(-1, idOffer3);
		assertNotEquals(-1, idOffer4);

		assertEquals(0, packageOps.getDeliveryStatus(idPackage1).intValue());
		assertEquals(0, packageOps.getDeliveryStatus(idPackage2).intValue());
		assertEquals(0, packageOps.getDeliveryStatus(idPackage3).intValue());
		assertEquals(0, packageOps.getDeliveryStatus(idPackage4).intValue());

		assertTrue(packageOps.acceptAnOffer(idOffer1));
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		assertTrue(packageOps.acceptAnOffer(idOffer3));
		assertTrue(packageOps.acceptAnOffer(idOffer2));

		assertEquals(1, packageOps.getDeliveryStatus(idPackage1).intValue());
		assertEquals(0, packageOps.getDeliveryStatus(idPackage2).intValue());
		assertEquals(1, packageOps.getDeliveryStatus(idPackage3).intValue());
		assertEquals(1, packageOps.getDeliveryStatus(idPackage4).intValue());

		result = packageOps.getDrive(username1);
		assertEquals(0, result.size());
		assertEquals(BigDecimal.ZERO, courierOps.getAverageCourierProfit(0));
		result = packageOps.getDrive(username2);
		assertEquals(0, result.size());

		assertEquals(idPackage1, packageOps.driveNextPackage(username1));
		assertEquals(BigDecimal.ZERO, courierOps.getAverageCourierProfit(0));
		assertEquals(idPackage3, packageOps.driveNextPackage(username2));
		assertNotEquals(BigDecimal.ZERO, courierOps.getAverageCourierProfit(0));
		result = packageOps.getDrive(username1);
		assertEquals(1, result.size());
		assertTrue(result.contains(idPackage4));
		result = packageOps.getDrive(username2);
		assertEquals(0, result.size());

		assertEquals(3, packageOps.getDeliveryStatus(idPackage1).intValue());
		assertEquals(0, packageOps.getDeliveryStatus(idPackage2).intValue());
		assertEquals(3, packageOps.getDeliveryStatus(idPackage3).intValue());
		assertEquals(2, packageOps.getDeliveryStatus(idPackage4).intValue());

		assertEquals(-1, packageOps.driveNextPackage(username2));
		assertEquals(idPackage4, packageOps.driveNextPackage(username1));
		assertNotEquals(BigDecimal.ZERO, courierOps.getAverageCourierProfit(0));
		result = packageOps.getDrive(username1);
		assertEquals(0, result.size());
		result = packageOps.getDrive(username2);
		assertEquals(0, result.size());

		assertEquals(3, packageOps.getDeliveryStatus(idPackage1).intValue());
		assertEquals(0, packageOps.getDeliveryStatus(idPackage2).intValue());
		assertEquals(3, packageOps.getDeliveryStatus(idPackage3).intValue());
		assertEquals(3, packageOps.getDeliveryStatus(idPackage4).intValue());

		assertEquals(-1, packageOps.driveNextPackage(username2));
		assertEquals(-1, packageOps.driveNextPackage(username1));
		assertNotEquals(BigDecimal.ZERO, courierOps.getAverageCourierProfit(0));
		result = packageOps.getDrive(username1);
		assertEquals(0, result.size());
		result = packageOps.getDrive(username2);
		assertEquals(0, result.size());

		checkStd();
	}

	@Test
	public void testProfit() {
		int idPackage1;
		int idPackage2;
		int idPackage3;
		int idPackage4;

		int idOffer1;
		int idOffer2;
		int idOffer3;
		int idOffer4;

		assertEquals(BigDecimal.ZERO, courierOps.getAverageCourierProfit(0));

		idPackage1 = packageOps.insertPackage(districtId1, districtId2, username1, 0, BigDecimal.ONE);
		idPackage2 = packageOps.insertPackage(districtId1, districtId2, username1, 0, BigDecimal.ONE);
		idPackage3 = packageOps.insertPackage(districtId1, districtId2, username1, 0, BigDecimal.ONE);
		idPackage4 = packageOps.insertPackage(districtId1, districtId2, username1, 0, BigDecimal.ONE);

		assertNotEquals(-1, idPackage1);
		assertNotEquals(-1, idPackage2);
		assertNotEquals(-1, idPackage3);
		assertNotEquals(-1, idPackage4);

		assertEquals(BigDecimal.ZERO, courierOps.getAverageCourierProfit(0));

		idOffer1 = packageOps.insertTransportOffer(username1, idPackage1, BigDecimal.ONE);
		idOffer2 = packageOps.insertTransportOffer(username2, idPackage3, BigDecimal.ONE);
		idOffer3 = packageOps.insertTransportOffer(username1, idPackage4, BigDecimal.ONE);
		idOffer4 = packageOps.insertTransportOffer(username1, idPackage4, BigDecimal.ONE);

		assertEquals(BigDecimal.ZERO, courierOps.getAverageCourierProfit(0));

		assertNotEquals(-1, idOffer1);
		assertNotEquals(-1, idOffer2);
		assertNotEquals(-1, idOffer3);
		assertNotEquals(-1, idOffer4);

		assertTrue(packageOps.acceptAnOffer(idOffer1));

		assertEquals(BigDecimal.ZERO, courierOps.getAverageCourierProfit(0));

		assertEquals(idPackage1, packageOps.driveNextPackage(username1));

		BigDecimal courierProfit = courierOps.getAverageCourierProfit(1);
		BigDecimal expectedPrice = getPackagePrice(0, BigDecimal.ONE, distance12, BigDecimal.ONE);
		expectedPrice = expectedPrice.subtract(new BigDecimal(distance12 * 15.0D).multiply(BigDecimal.ONE));

		assertTrue(courierProfit.compareTo(expectedPrice.multiply(new BigDecimal(1.05D))) >= 0);
		assertTrue(courierProfit.compareTo(expectedPrice.multiply(new BigDecimal(0.95D))) <= 0);

		courierProfit = courierOps.getAverageCourierProfit(0);
		expectedPrice = expectedPrice.divide(BigDecimal.valueOf(2));
		assertTrue(courierProfit.compareTo(expectedPrice.multiply(new BigDecimal(1.05D))) >= 0);
		assertTrue(courierProfit.compareTo(expectedPrice.multiply(new BigDecimal(0.95D))) <= 0);

		assertEquals(-1, packageOps.driveNextPackage(username1));

		courierProfit = courierOps.getAverageCourierProfit(1);
		expectedPrice = getPackagePrice(0, BigDecimal.ONE, distance12, BigDecimal.ONE);
		expectedPrice = expectedPrice.subtract(new BigDecimal(distance12 * 15.0D).multiply(BigDecimal.ONE));
		assertTrue(courierProfit.compareTo(expectedPrice.multiply(new BigDecimal(1.05D))) >= 0);
		assertTrue(courierProfit.compareTo(expectedPrice.multiply(new BigDecimal(0.95D))) <= 0);

		assertTrue(packageOps.acceptAnOffer(idOffer2));

		assertEquals(idPackage3, packageOps.driveNextPackage(username2));

		courierProfit = courierOps.getAverageCourierProfit(1);
		expectedPrice = getPackagePrice(0, BigDecimal.ONE, distance12, BigDecimal.ONE);
		expectedPrice = expectedPrice.subtract(new BigDecimal(distance12 * 15.0D).multiply(BigDecimal.ONE));
		assertTrue(courierProfit.compareTo(expectedPrice.multiply(new BigDecimal(1.05D))) >= 0);
		assertTrue(courierProfit.compareTo(expectedPrice.multiply(new BigDecimal(0.95D))) <= 0);

		assertTrue(packageOps.acceptAnOffer(idOffer3));

		assertEquals(idPackage4, packageOps.driveNextPackage(username1));

		courierProfit = courierOps.getAverageCourierProfit(1);
		expectedPrice = getPackagePrice(0, BigDecimal.ONE, distance12, BigDecimal.ONE);
		expectedPrice = expectedPrice.subtract(new BigDecimal(distance12 * 15.0D).multiply(BigDecimal.ONE));
		expectedPrice = expectedPrice.multiply(BigDecimal.valueOf(3)).divide(BigDecimal.valueOf(2));
		assertTrue(courierProfit.compareTo(expectedPrice.multiply(new BigDecimal(1.05D))) >= 0);
		assertTrue(courierProfit.compareTo(expectedPrice.multiply(new BigDecimal(0.95D))) <= 0);

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

	public static double euclidean(int x1, int y1, int x2, int y2) {
		return Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
	}

	private static BigDecimal getPackagePrice(int type, BigDecimal weight, double distance, BigDecimal percentage) {
		percentage = percentage.divide(new BigDecimal(100));
		switch (type) {
		case 0:
			return new BigDecimal(10.0D * distance).multiply(percentage.add(new BigDecimal(1)));
		case 1:
			return new BigDecimal((25.0D + weight.doubleValue() * 100.0D) * distance)
					.multiply(percentage.add(new BigDecimal(1)));
		case 2:
			return new BigDecimal((75.0D + weight.doubleValue() * 600.0D) * distance)
					.multiply(percentage.add(new BigDecimal(1)));
		}
		return null;
	}
}
