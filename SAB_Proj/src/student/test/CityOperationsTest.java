package student.test;

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import org.junit.*;
import org.junit.experimental.categories.Category;
import org.junit.rules.*;
import org.junit.runner.*;

import student.pn140041_CityOperations;
import student.pn140041_GeneralOperations;
import student.helper.Logger;

public class CityOperationsTest extends UnitTestBase {

	private static pn140041_CityOperations cityOps = new pn140041_CityOperations();
	private static pn140041_GeneralOperations generalOps = new pn140041_GeneralOperations();

	@Test
	public void testInsertCitySingle() {
		int id = cityOps.insertCity("A", "123");
		Assert.assertEquals(1, cityOps.getAllCities().size());
		Assert.assertTrue(cityOps.getAllCities().contains(id));
	}

	@Test
	public void testInsertCityMultiple() {
		int id1 = cityOps.insertCity("A", "123");
		Assert.assertEquals(1, cityOps.getAllCities().size());
		Assert.assertTrue(cityOps.getAllCities().contains(id1));

		int id2 = cityOps.insertCity("B", "456");
		Assert.assertEquals(2, cityOps.getAllCities().size());
		Assert.assertTrue(cityOps.getAllCities().contains(id1));
		Assert.assertTrue(cityOps.getAllCities().contains(id2));
	}

	@Test
	public void testDeleteCityIdSingle() {
		int id = cityOps.insertCity("A", "123");
		Assert.assertEquals(1, cityOps.getAllCities().size());
		Assert.assertTrue(cityOps.getAllCities().contains(id));

		Assert.assertTrue(cityOps.deleteCity(id));
		Assert.assertEquals(0, cityOps.getAllCities().size());
	}

	@Test
	public void testDeleteCityIdMultiple() {
		int id1 = cityOps.insertCity("A", "123");
		int id2 = cityOps.insertCity("B", "456");
		Assert.assertEquals(2, cityOps.getAllCities().size());
		Assert.assertTrue(cityOps.getAllCities().contains(id1));
		Assert.assertTrue(cityOps.getAllCities().contains(id2));

		Assert.assertTrue(cityOps.deleteCity(id1));
		Assert.assertEquals(1, cityOps.getAllCities().size());
		Assert.assertTrue(cityOps.getAllCities().contains(id2));
		Assert.assertFalse(cityOps.getAllCities().contains(id1));

		Assert.assertTrue(cityOps.deleteCity(id2));
		Assert.assertEquals(0, cityOps.getAllCities().size());
		Assert.assertFalse(cityOps.getAllCities().contains(id2));
		Assert.assertFalse(cityOps.getAllCities().contains(id1));
	}

	@Test
	public void testDeleteCityIdSingleInvalid() {
		int id = cityOps.insertCity("A", "123");
		Assert.assertEquals(1, cityOps.getAllCities().size());
		Assert.assertTrue(cityOps.getAllCities().contains(id));

		Assert.assertFalse(cityOps.deleteCity(id - 1));
		Assert.assertEquals(1, cityOps.getAllCities().size());
	}

	@Test
	public void testDeleteCityIdMultipleInvalid() {
		int id1 = cityOps.insertCity("A", "123");
		int id2 = cityOps.insertCity("B", "456");

		Assert.assertEquals(2, cityOps.getAllCities().size());
		Assert.assertTrue(cityOps.getAllCities().contains(id1));
		Assert.assertTrue(cityOps.getAllCities().contains(id2));

		Assert.assertFalse(cityOps.deleteCity(Math.min(id1, id2) - 1));
		Assert.assertEquals(2, cityOps.getAllCities().size());
		Assert.assertFalse(cityOps.deleteCity(Math.max(id1, id2) + 1));
		Assert.assertEquals(2, cityOps.getAllCities().size());

		Assert.assertTrue(cityOps.deleteCity(id1));
		Assert.assertEquals(1, cityOps.getAllCities().size());
		Assert.assertTrue(cityOps.getAllCities().contains(id2));
		Assert.assertFalse(cityOps.getAllCities().contains(id1));

		Assert.assertTrue(cityOps.deleteCity(id2));
		Assert.assertEquals(0, cityOps.getAllCities().size());
		Assert.assertFalse(cityOps.getAllCities().contains(id2));
		Assert.assertFalse(cityOps.getAllCities().contains(id1));
	}

	@Test
	public void testDeleteCityNameInvalid() {
		Assert.assertEquals(0, cityOps.getAllCities().size());
		Assert.assertEquals(0, cityOps.deleteCity("AAA"));
		Assert.assertEquals(0, cityOps.deleteCity("AAA", "BBB", "CCC"));
		Assert.assertEquals(0, cityOps.getAllCities().size());
	}

	@Test
	public void testDeleteCityNameSingleInvalid() {
		int id = cityOps.insertCity("A", "123");
		Assert.assertEquals(1, cityOps.getAllCities().size());
		Assert.assertTrue(cityOps.getAllCities().contains(id));

		Assert.assertEquals(0, cityOps.deleteCity("AAA"));
		Assert.assertEquals(1, cityOps.getAllCities().size());
	}

	@Test
	public void testDeleteCityNameMultipleInvalid() {
		int id1 = cityOps.insertCity("A", "123");
		int id2 = cityOps.insertCity("B", "456");

		Assert.assertEquals(2, cityOps.getAllCities().size());
		Assert.assertTrue(cityOps.getAllCities().contains(id1));
		Assert.assertTrue(cityOps.getAllCities().contains(id2));

		Assert.assertEquals(0, cityOps.deleteCity("AAA", "BBB"));
		Assert.assertEquals(2, cityOps.getAllCities().size());
		Assert.assertEquals(0, cityOps.deleteCity("AAA"));
		Assert.assertEquals(2, cityOps.getAllCities().size());

		Assert.assertEquals(1, cityOps.deleteCity("A"));
		Assert.assertEquals(1, cityOps.getAllCities().size());
		Assert.assertTrue(cityOps.getAllCities().contains(id2));
		Assert.assertFalse(cityOps.getAllCities().contains(id1));

		Assert.assertEquals(1, cityOps.deleteCity("B"));
		Assert.assertEquals(0, cityOps.getAllCities().size());
		Assert.assertFalse(cityOps.getAllCities().contains(id2));
		Assert.assertFalse(cityOps.getAllCities().contains(id1));
	}

	@Test
	public void testDeleteCityNameSingle1() {
		int id = cityOps.insertCity("A", "123");
		Assert.assertEquals(1, cityOps.getAllCities().size());
		Assert.assertTrue(cityOps.getAllCities().contains(id));

		Assert.assertEquals(1, cityOps.deleteCity("A"));
		Assert.assertEquals(0, cityOps.getAllCities().size());
		Assert.assertEquals(0, cityOps.deleteCity("A"));

	}

	@Test
	public void testDeleteCityNameSingle2() {
		int id = cityOps.insertCity("A", "123");
		Assert.assertEquals(1, cityOps.getAllCities().size());
		Assert.assertTrue(cityOps.getAllCities().contains(id));

		Assert.assertEquals(1, cityOps.deleteCity("A", "B"));
		Assert.assertEquals(0, cityOps.getAllCities().size());
	}

	@Test
	public void testDeleteCityNameMultiple() {
		int id1 = cityOps.insertCity("A", "123");
		int id2 = cityOps.insertCity("B", "456");
		Assert.assertEquals(2, cityOps.getAllCities().size());
		Assert.assertTrue(cityOps.getAllCities().contains(id1));
		Assert.assertTrue(cityOps.getAllCities().contains(id2));

		Assert.assertEquals(2, cityOps.deleteCity("A", "B"));

		Assert.assertEquals(0, cityOps.getAllCities().size());
		Assert.assertFalse(cityOps.getAllCities().contains(id2));
		Assert.assertFalse(cityOps.getAllCities().contains(id1));
	}

	@Test
	public void testDeleteCityIdInvalid() {
		Assert.assertEquals(0, cityOps.getAllCities().size());
		Assert.assertFalse(cityOps.deleteCity(1));
		Assert.assertEquals(0, cityOps.getAllCities().size());
	}

	@Test
	public void testGetAllCitiesMultiple() {
		int numOfCities = 20;
		List<Integer> ids = new LinkedList<>();

		for (int i = 0; i < numOfCities; i++) {
			int id;
			id = cityOps.insertCity("A" + i, "123" + i);
			ids.add(id);
		}

		List<Integer> result = cityOps.getAllCities();

		Assert.assertEquals(numOfCities, result.size());
		Assert.assertEquals(ids.size(), result.size());

		for (Integer id : result) {
			Assert.assertTrue(ids.contains(id));
		}

		int deleteTo = 10;
		for (int i = 0; i < deleteTo; i++) {
			Assert.assertEquals(1, cityOps.deleteCity("A" + i));
			ids.remove(0);
		}

		result = cityOps.getAllCities();

		Assert.assertEquals(numOfCities - deleteTo, result.size());
		Assert.assertEquals(ids.size(), result.size());

		for (Integer id : result) {
			Assert.assertTrue(ids.contains(id));
		}

	}

	@Test
	public void testGetAllCitiesEmpty() {
		Assert.assertEquals(0, cityOps.getAllCities().size());
	}

}
