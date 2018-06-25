package student.test;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import student.pn140041_CityOperations;
import student.pn140041_DistrictOperations;

public class DistrictOperationTest extends UnitTestBase {

	private static pn140041_CityOperations cityOps = new pn140041_CityOperations();
	private static pn140041_DistrictOperations districtOps = new pn140041_DistrictOperations();

	private static int idA;
	private static int idB;
	private static int idC;

	@Override
	@Before
	public void setUp() throws Exception {
		super.setUp();

		idA = cityOps.insertCity("A", "123");
		idB = cityOps.insertCity("B", "456");
		idC = cityOps.insertCity("C", "789");
	}

	@Test
	public void testInsertDistrictSingle() {
		int id = districtOps.insertDistrict("A", idA, 0, 0);
		assertNotEquals(-1, id);

		checkCities();
	}

	@Test
	public void testInsertDistrictMultipleInSameCiy() {
		int id1 = districtOps.insertDistrict("A", idA, 0, 0);
		int id2 = districtOps.insertDistrict("B", idA, 0, 0);

		assertNotEquals(-1, id1);
		assertNotEquals(-1, id2);

		checkCities();
	}

	@Test
	public void testInsertDistrictMultipleInDifferentCiy() {
		int id1 = districtOps.insertDistrict("A", idA, 0, 0);
		int id2 = districtOps.insertDistrict("B", idB, 0, 0);

		assertNotEquals(-1, id1);
		assertNotEquals(-1, id2);

		checkCities();
	}

	@Test
	public void testInsertDistrictMultipleInSameCiyDifferentCoordinates() {
		int id1 = districtOps.insertDistrict("A", idA, 1, 5);
		int id2 = districtOps.insertDistrict("B", idA, 7, 8);

		assertNotEquals(-1, id1);
		assertNotEquals(-1, id2);

		checkCities();
	}

	@Test
	public void testGetAllDistrictsEmpty() {
		assertEquals(0, districtOps.getAllDistricts().size());

		checkCities();
	}

	@Test
	public void testGetAllDistrictFromCitryEmpty() {
		assertEquals(0, districtOps.getAllDistrictsFromCity(idA).size());
		assertEquals(0, districtOps.getAllDistrictsFromCity(idB).size());
		assertEquals(0, districtOps.getAllDistrictsFromCity(idC).size());

		checkCities();
	}

	@Test
	public void testGetAllDistrictFromCitryInvalidCity() {
		assertNull(districtOps.getAllDistrictsFromCity(Math.max(Math.max(idA, idB), idC) + 1));
		assertNull(districtOps.getAllDistrictsFromCity(Math.min(Math.min(idA, idB), idC) - 1));

		checkCities();
	}

	@Test
	public void testGetAllDistrictsSameCity() {
		List<Integer> result = null;

		result = districtOps.getAllDistricts();
		assertEquals(0, result.size());

		int id1 = districtOps.insertDistrict("A", idA, 1, 5);

		result = districtOps.getAllDistricts();
		assertEquals(1, result.size());
		assertTrue(result.contains(id1));
		assertFalse(result.contains(id1 + 1));

		int id2 = districtOps.insertDistrict("B", idA, 7, 8);

		result = districtOps.getAllDistricts();
		assertEquals(2, result.size());

		assertTrue(result.contains(id1));
		assertTrue(result.contains(id2));
		assertFalse(result.contains(id2 + 1));

		checkCities();
	}

	@Test
	public void testGetAllDistrictsDifferentCity() {
		List<Integer> result = null;

		result = districtOps.getAllDistricts();
		assertEquals(0, result.size());

		int id1 = districtOps.insertDistrict("A", idA, 1, 5);

		result = districtOps.getAllDistricts();
		assertEquals(1, result.size());
		assertTrue(result.contains(id1));
		assertFalse(result.contains(id1 + 1));

		int id2 = districtOps.insertDistrict("B", idB, 7, 8);

		result = districtOps.getAllDistricts();
		assertEquals(2, result.size());

		assertTrue(result.contains(id1));
		assertTrue(result.contains(id2));
		assertFalse(result.contains(id2 + 1));

		checkCities();
	}

	@Test
	public void testGetAllDistrictsFromSameCity() {
		List<Integer> result = null;

		result = districtOps.getAllDistrictsFromCity(idA);
		assertEquals(0, result.size());
		result = districtOps.getAllDistricts();
		assertEquals(0, result.size());

		int id1 = districtOps.insertDistrict("A", idA, 1, 5);

		result = districtOps.getAllDistrictsFromCity(idA);
		assertEquals(1, result.size());
		assertTrue(result.contains(id1));
		assertFalse(result.contains(id1 + 1));
		result = districtOps.getAllDistricts();
		assertEquals(1, result.size());

		int id2 = districtOps.insertDistrict("B", idA, 7, 8);

		result = districtOps.getAllDistrictsFromCity(idA);
		assertEquals(2, result.size());

		assertTrue(result.contains(id1));
		assertTrue(result.contains(id2));
		assertFalse(result.contains(id2 + 1));

		result = districtOps.getAllDistricts();
		assertEquals(2, result.size());

		checkCities();
	}

	@Test
	public void testGetAllDistrictsFromDifferentCity() {
		List<Integer> resultA = null;
		List<Integer> resultB = null;
		List<Integer> result = null;

		resultA = districtOps.getAllDistrictsFromCity(idA);
		resultB = districtOps.getAllDistrictsFromCity(idB);
		assertEquals(0, resultA.size());
		assertEquals(0, resultB.size());
		result = districtOps.getAllDistricts();
		assertEquals(0, result.size());

		int id1 = districtOps.insertDistrict("A", idA, 1, 5);

		resultA = districtOps.getAllDistrictsFromCity(idA);
		resultB = districtOps.getAllDistrictsFromCity(idB);
		assertEquals(1, resultA.size());
		assertEquals(0, resultB.size());
		assertTrue(resultA.contains(id1));
		assertFalse(resultA.contains(id1 + 1));
		assertFalse(resultB.contains(id1));
		result = districtOps.getAllDistricts();
		assertEquals(1, result.size());

		int id2 = districtOps.insertDistrict("B", idB, 7, 8);

		resultA = districtOps.getAllDistrictsFromCity(idA);
		resultB = districtOps.getAllDistrictsFromCity(idB);
		assertEquals(1, resultA.size());
		assertEquals(1, resultB.size());

		assertTrue(resultA.contains(id1));
		assertTrue(resultB.contains(id2));
		assertFalse(resultA.contains(id2));
		assertFalse(resultB.contains(id1));

		result = districtOps.getAllDistricts();
		assertEquals(2, result.size());

		int id3 = districtOps.insertDistrict("BB", idB, 7, 8);

		resultA = districtOps.getAllDistrictsFromCity(idA);
		resultB = districtOps.getAllDistrictsFromCity(idB);
		assertEquals(1, resultA.size());
		assertEquals(2, resultB.size());

		assertTrue(resultA.contains(id1));
		assertTrue(resultB.contains(id2));
		assertTrue(resultB.contains(id3));
		assertFalse(resultA.contains(id3));
		assertFalse(resultA.contains(id2));
		assertFalse(resultB.contains(id1));

		result = districtOps.getAllDistricts();
		assertEquals(3, result.size());

		checkCities();
	}

	@Test
	public void testDeleteAllDistrictsFromCitySingleCity() {
		int id1 = districtOps.insertDistrict("AA", idA, 0, 0);
		assertEquals(1, districtOps.getAllDistricts().size());
		assertEquals(1, districtOps.getAllDistrictsFromCity(idA).size());

		assertEquals(1, districtOps.deleteAllDistrictsFromCity("A"));

		assertEquals(0, districtOps.getAllDistricts().size());
		assertEquals(0, districtOps.getAllDistrictsFromCity(idA).size());

		checkCities();
	}

	@Test
	public void testDeleteAllDistrictsFromCitySingleMultipleCities() {
		int id1 = districtOps.insertDistrict("AA", idA, 0, 0);
		assertEquals(1, districtOps.getAllDistricts().size());
		assertEquals(1, districtOps.getAllDistrictsFromCity(idA).size());

		int id2 = districtOps.insertDistrict("BB", idB, 0, 0);
		int id3 = districtOps.insertDistrict("BBB", idB, 0, 0);
		assertEquals(3, districtOps.getAllDistricts().size());
		assertEquals(2, districtOps.getAllDistrictsFromCity(idB).size());

		assertEquals(1, districtOps.deleteAllDistrictsFromCity("A"));

		assertEquals(2, districtOps.getAllDistricts().size());
		assertEquals(0, districtOps.getAllDistrictsFromCity(idA).size());
		assertEquals(2, districtOps.getAllDistrictsFromCity(idB).size());

		assertEquals(2, districtOps.deleteAllDistrictsFromCity("B"));

		assertEquals(0, districtOps.getAllDistricts().size());
		assertEquals(0, districtOps.getAllDistrictsFromCity(idA).size());
		assertEquals(0, districtOps.getAllDistrictsFromCity(idB).size());

		checkCities();
	}

	@Test
	public void testDeleteAllDistrictsFromCityEmpty() {
		assertEquals(0, districtOps.deleteAllDistrictsFromCity("A"));
		assertEquals(0, districtOps.deleteAllDistrictsFromCity("B"));
		assertEquals(0, districtOps.deleteAllDistrictsFromCity("C"));

		checkCities();
	}

	@Test
	public void testDeleteAllDistrictsFromCityNonExistent() {
		assertEquals(0, districtOps.deleteAllDistrictsFromCity("D"));

		checkCities();
	}

	@Test
	public void testDeleteAllDistrictsFromCityTwoTimes() {

		int id = districtOps.insertDistrict("AA", idA, 0, 0);
		assertEquals(1, districtOps.getAllDistricts().size());
		assertEquals(1, districtOps.getAllDistrictsFromCity(idA).size());

		assertEquals(1, districtOps.deleteAllDistrictsFromCity("A"));
		assertEquals(0, districtOps.deleteAllDistrictsFromCity("B"));
		assertEquals(0, districtOps.deleteAllDistrictsFromCity("C"));

		assertEquals(0, districtOps.deleteAllDistrictsFromCity("A"));
		assertEquals(0, districtOps.deleteAllDistrictsFromCity("B"));
		assertEquals(0, districtOps.deleteAllDistrictsFromCity("C"));

		checkCities();
	}

	@Test
	public void testDeleteDistrictInvalidId() {
		assertEquals(0, districtOps.getAllDistricts().size());
		assertFalse(districtOps.deleteDistrict(1));

		checkCities();
	}

	@Test
	public void testDeleteDistrictSingle() {
		int id = districtOps.insertDistrict("AAA", idA, 0, 0);

		assertEquals(1, districtOps.getAllDistricts().size());
		assertEquals(1, districtOps.getAllDistrictsFromCity(idA).size());

		assertTrue(districtOps.deleteDistrict(id));

		assertEquals(0, districtOps.getAllDistricts().size());
		assertEquals(0, districtOps.getAllDistrictsFromCity(idA).size());

		checkCities();
	}

	@Test
	public void testDeleteDistrictSingleTwoTimes() {
		int id = districtOps.insertDistrict("AAA", idA, 0, 0);

		assertEquals(1, districtOps.getAllDistricts().size());
		assertEquals(1, districtOps.getAllDistrictsFromCity(idA).size());

		assertTrue(districtOps.deleteDistrict(id));

		assertEquals(0, districtOps.getAllDistricts().size());
		assertEquals(0, districtOps.getAllDistrictsFromCity(idA).size());

		assertFalse(districtOps.deleteDistrict(id));

		assertEquals(0, districtOps.getAllDistricts().size());
		assertEquals(0, districtOps.getAllDistrictsFromCity(idA).size());

		checkCities();
	}

	@Test
	public void testDeleteDistrictSingleInvalidId() {
		int id = districtOps.insertDistrict("AAA", idA, 0, 0);

		assertEquals(1, districtOps.getAllDistricts().size());
		assertEquals(1, districtOps.getAllDistrictsFromCity(idA).size());

		assertFalse(districtOps.deleteDistrict(id + 1));

		assertEquals(1, districtOps.getAllDistricts().size());
		assertEquals(1, districtOps.getAllDistrictsFromCity(idA).size());

		assertTrue(districtOps.deleteDistrict(id));

		assertEquals(0, districtOps.getAllDistricts().size());
		assertEquals(0, districtOps.getAllDistrictsFromCity(idA).size());

		checkCities();
	}

	@Test
	public void testDeleteDistrictMultiple() {
		int id1 = districtOps.insertDistrict("AAA1", idA, 0, 0);
		int id2 = districtOps.insertDistrict("AAA2", idA, 0, 0);

		assertEquals(2, districtOps.getAllDistricts().size());
		assertEquals(2, districtOps.getAllDistrictsFromCity(idA).size());

		assertTrue(districtOps.deleteDistrict(id1));

		assertEquals(1, districtOps.getAllDistricts().size());
		assertEquals(1, districtOps.getAllDistrictsFromCity(idA).size());

		assertTrue(districtOps.deleteDistrict(id2));

		assertEquals(0, districtOps.getAllDistricts().size());
		assertEquals(0, districtOps.getAllDistrictsFromCity(idA).size());
		checkCities();
	}

	@Test
	public void testDeleteDistrictMultiple2() {
		int id1 = districtOps.insertDistrict("AAA1", idA, 0, 0);
		int id2 = districtOps.insertDistrict("AAA2", idA, 0, 0);
		int id3 = districtOps.insertDistrict("BBB", idB, 0, 0);

		assertEquals(3, districtOps.getAllDistricts().size());
		assertEquals(2, districtOps.getAllDistrictsFromCity(idA).size());
		assertEquals(1, districtOps.getAllDistrictsFromCity(idB).size());

		assertTrue(districtOps.deleteDistrict(id1));

		assertEquals(2, districtOps.getAllDistricts().size());
		assertEquals(1, districtOps.getAllDistrictsFromCity(idA).size());
		assertEquals(1, districtOps.getAllDistrictsFromCity(idB).size());

		assertTrue(districtOps.deleteDistrict(id3));

		assertEquals(1, districtOps.getAllDistricts().size());
		assertEquals(1, districtOps.getAllDistrictsFromCity(idA).size());
		assertEquals(0, districtOps.getAllDistrictsFromCity(idB).size());

		assertTrue(districtOps.deleteDistrict(id2));

		assertEquals(0, districtOps.getAllDistricts().size());
		assertEquals(0, districtOps.getAllDistrictsFromCity(idA).size());
		assertEquals(0, districtOps.getAllDistrictsFromCity(idB).size());
		checkCities();
	}

	@Test
	public void testDeleteDistrictSInvalid1() {
		assertEquals(0, districtOps.getAllDistricts().size());
		assertEquals(0, districtOps.deleteDistricts());
		checkCities();
	}

	@Test
	public void testDeleteDistrictSInvalid2() {
		assertEquals(0, districtOps.getAllDistricts().size());
		assertEquals(0, districtOps.deleteDistricts("A"));
		checkCities();
	}

	@Test
	public void testDeleteDistrictsSingle() {
		int id = districtOps.insertDistrict("A", idA, 0, 0);

		assertEquals(1, districtOps.getAllDistricts().size());
		assertEquals(1, districtOps.getAllDistrictsFromCity(idA).size());

		assertEquals(1, districtOps.deleteDistricts("A"));

		assertEquals(0, districtOps.getAllDistricts().size());
		assertEquals(0, districtOps.getAllDistrictsFromCity(idA).size());

		checkCities();
	}

	@Test
	public void testDeleteDistrictsSingleTwoTimes() {
		int id = districtOps.insertDistrict("A", idA, 0, 0);

		assertEquals(1, districtOps.getAllDistricts().size());
		assertEquals(1, districtOps.getAllDistrictsFromCity(idA).size());

		assertEquals(1, districtOps.deleteDistricts("A"));

		assertEquals(0, districtOps.getAllDistricts().size());
		assertEquals(0, districtOps.getAllDistrictsFromCity(idA).size());

		assertEquals(0, districtOps.deleteDistricts("A"));

		assertEquals(0, districtOps.getAllDistricts().size());
		assertEquals(0, districtOps.getAllDistrictsFromCity(idA).size());

		checkCities();
	}

	@Test
	public void testDeleteDistrictsMultipleSameCity1() {
		int id1 = districtOps.insertDistrict("A", idA, 0, 0);
		int id2 = districtOps.insertDistrict("B", idA, 0, 0);

		assertEquals(2, districtOps.getAllDistricts().size());
		assertEquals(2, districtOps.getAllDistrictsFromCity(idA).size());

		assertEquals(2, districtOps.deleteDistricts("A", "B"));

		assertEquals(0, districtOps.getAllDistricts().size());
		assertEquals(0, districtOps.getAllDistrictsFromCity(idA).size());

		assertEquals(0, districtOps.deleteDistricts("A"));

		assertEquals(0, districtOps.getAllDistricts().size());
		assertEquals(0, districtOps.getAllDistrictsFromCity(idA).size());

		checkCities();
	}

	@Test
	public void testDeleteDistrictsMultipleSameCity2() {
		int id1 = districtOps.insertDistrict("A", idA, 0, 0);
		int id2 = districtOps.insertDistrict("B", idA, 0, 0);

		assertEquals(2, districtOps.getAllDistricts().size());
		assertEquals(2, districtOps.getAllDistrictsFromCity(idA).size());

		assertEquals(1, districtOps.deleteDistricts("A"));

		assertEquals(1, districtOps.getAllDistricts().size());
		assertEquals(1, districtOps.getAllDistrictsFromCity(idA).size());

		assertEquals(1, districtOps.deleteDistricts("B"));

		assertEquals(0, districtOps.getAllDistricts().size());
		assertEquals(0, districtOps.getAllDistrictsFromCity(idA).size());

		checkCities();
	}

	@Test
	public void testDeleteDistrictsMultipleDifferentCity() {
		int id1 = districtOps.insertDistrict("A", idA, 0, 0);
		int id2 = districtOps.insertDistrict("B", idB, 0, 0);

		assertEquals(2, districtOps.getAllDistricts().size());
		assertEquals(1, districtOps.getAllDistrictsFromCity(idA).size());
		assertEquals(1, districtOps.getAllDistrictsFromCity(idB).size());

		assertEquals(1, districtOps.deleteDistricts("A"));

		assertEquals(1, districtOps.getAllDistricts().size());
		assertEquals(0, districtOps.getAllDistrictsFromCity(idA).size());
		assertEquals(1, districtOps.getAllDistrictsFromCity(idB).size());

		assertEquals(0, districtOps.deleteDistricts("A"));

		assertEquals(1, districtOps.getAllDistricts().size());
		assertEquals(0, districtOps.getAllDistrictsFromCity(idA).size());
		assertEquals(1, districtOps.getAllDistrictsFromCity(idB).size());

		assertEquals(1, districtOps.deleteDistricts("B"));

		assertEquals(0, districtOps.getAllDistricts().size());
		assertEquals(0, districtOps.getAllDistrictsFromCity(idA).size());
		assertEquals(0, districtOps.getAllDistrictsFromCity(idB).size());

		checkCities();
	}

	private static void checkCities() {
		List<Integer> result = cityOps.getAllCities();
		assertEquals(3, result.size());
		assertTrue(result.contains(idA));
		assertTrue(result.contains(idB));
		assertTrue(result.contains(idC));

	}
}
