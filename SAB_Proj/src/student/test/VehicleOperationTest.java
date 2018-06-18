package student.test;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.util.List;

import org.junit.Test;

import student.pn140041_CityOperations;
import student.pn140041_VehicleOperations;

public class VehicleOperationTest extends UnitTestBase {

	private static pn140041_VehicleOperations vehicleOps = new pn140041_VehicleOperations();
	
	
	@Test
	public void testGetAllVehiclesEmpty() {
		assertEquals(0, vehicleOps.getAllVehichles().size());
	}
	
	@Test
	public void testDeleteVehicleEmpty1() {
		assertEquals(0, vehicleOps.deleteVehicles());
	}
	
	@Test
	public void testDeleteVehicleEmpty2() {
		assertEquals(0, vehicleOps.deleteVehicles("A"));
	}
	
	@Test
	public void testChangeFuelTypeEmpty() {
		assertFalse(vehicleOps.changeFuelType("A", 1));
	}
	
	@Test
	public void testChangeFuelConsumption() {
		assertFalse(vehicleOps.changeConsumption("A", BigDecimal.ONE));
	}
	
	@Test
	public void testInsertVehicleSingle() {
		assertTrue(vehicleOps.insertVehicle("AAA", 1, BigDecimal.ONE));
	}
	
	@Test
	public void testInsertVehicleSingleTwoTimes() {
		assertTrue(vehicleOps.insertVehicle("AAA", 1, BigDecimal.ONE));
		assertFalse(vehicleOps.insertVehicle("AAA", 1, BigDecimal.ONE));
	}
	
	@Test
	public void testInsertVehicleMultiple() {
		assertTrue(vehicleOps.insertVehicle("AAA", 1, BigDecimal.ONE));
		assertTrue(vehicleOps.insertVehicle("BBB", 1, BigDecimal.ONE));
	}
	
	@Test
	public void testGetAllVehiclesSingle(){
		String licencePlate = "AAA";
		assertTrue(vehicleOps.insertVehicle(licencePlate, 1, BigDecimal.ONE));
		
		List<String> result = vehicleOps.getAllVehichles();
		assertEquals(1, result.size());
		assertTrue(result.contains(licencePlate));
	}
	
	@Test
	public void testGetAllVehiclesMltiple(){
		String licencePlate1 = "AAA";
		String licencePlate2 = "BBB";
		assertTrue(vehicleOps.insertVehicle(licencePlate1, 1, BigDecimal.ONE));
		
		List<String> result = vehicleOps.getAllVehichles();
		assertEquals(1, result.size());
		assertTrue(result.contains(licencePlate1));
		assertFalse(result.contains(licencePlate2));
		
		assertTrue(vehicleOps.insertVehicle(licencePlate2, 1, BigDecimal.ONE));
		
		result = vehicleOps.getAllVehichles();
		assertEquals(2, result.size());
		assertTrue(result.contains(licencePlate1));
		assertTrue(result.contains(licencePlate2));		
	}
	
	@Test
	public void testDeleteVehicleSingle(){
		String licencePlate1 = "AAA";
		String licencePlate2 = "BBB";
		List<String> result = null;
		
		assertTrue(vehicleOps.insertVehicle(licencePlate1, 1, BigDecimal.ONE));		
		assertTrue(vehicleOps.insertVehicle(licencePlate2, 1, BigDecimal.ONE));
		
		result = vehicleOps.getAllVehichles();
		assertEquals(2, result.size());
		assertTrue(result.contains(licencePlate1));
		assertTrue(result.contains(licencePlate2));	
		
		assertEquals(0, vehicleOps.deleteVehicles("CCC"));
		
		result = vehicleOps.getAllVehichles();
		assertEquals(2, result.size());
		assertTrue(result.contains(licencePlate1));
		assertTrue(result.contains(licencePlate2));
		
		assertEquals(1, vehicleOps.deleteVehicles(licencePlate1));
		
		result = vehicleOps.getAllVehichles();
		assertEquals(1, result.size());
		assertFalse(result.contains(licencePlate1));
		assertTrue(result.contains(licencePlate2));	
		
		assertEquals(1, vehicleOps.deleteVehicles(licencePlate2));
		
		result = vehicleOps.getAllVehichles();
		assertEquals(0, result.size());
		assertFalse(result.contains(licencePlate1));
		assertFalse(result.contains(licencePlate2));	
	}
	
	@Test
	public void testDeleteVehicleMultiple(){
		String licencePlate1 = "AAA";
		String licencePlate2 = "BBB";
		List<String> result = null;
		
		assertTrue(vehicleOps.insertVehicle(licencePlate1, 1, BigDecimal.ONE));		
		assertTrue(vehicleOps.insertVehicle(licencePlate2, 1, BigDecimal.ONE));
		
		result = vehicleOps.getAllVehichles();
		assertEquals(2, result.size());
		assertTrue(result.contains(licencePlate1));
		assertTrue(result.contains(licencePlate2));	
		
		assertEquals(0, vehicleOps.deleteVehicles("CCC"));
		
		result = vehicleOps.getAllVehichles();
		assertEquals(2, result.size());
		assertTrue(result.contains(licencePlate1));
		assertTrue(result.contains(licencePlate2));
		
		assertEquals(2, vehicleOps.deleteVehicles(licencePlate1, licencePlate2));
		
		result = vehicleOps.getAllVehichles();
		assertEquals(0, result.size());
		assertFalse(result.contains(licencePlate1));
		assertFalse(result.contains(licencePlate2));	
	}
	
	
	@Test
	public void testChangeFuelType(){
		String licencePlate1 = "AAA";
		
		assertTrue(vehicleOps.insertVehicle(licencePlate1, 1, BigDecimal.ONE));
		
		assertFalse(vehicleOps.changeFuelType("BBB", 2));
		assertTrue(vehicleOps.changeFuelType(licencePlate1, 2));
		
	}
	
	@Test
	public void testChangeFuelConsumtion(){
		String licencePlate1 = "AAA";
		
		assertTrue(vehicleOps.insertVehicle(licencePlate1, 1, BigDecimal.ONE));
		
		assertFalse(vehicleOps.changeConsumption("BBB", BigDecimal.TEN));
		assertTrue(vehicleOps.changeConsumption(licencePlate1, BigDecimal.TEN));
	}
	
	@Test
	public void testChangeFuelTypeInvalid(){
		String licencePlate1 = "AAA";
		
		assertTrue(vehicleOps.insertVehicle(licencePlate1, 1, BigDecimal.ONE));
		
		assertFalse(vehicleOps.changeFuelType(licencePlate1, -1));
		assertFalse(vehicleOps.changeFuelType(licencePlate1, 3));
	}
	
	@Test
	public void testChangeFuelConsumtionInvalid(){
		String licencePlate1 = "AAA";
		
		assertTrue(vehicleOps.insertVehicle(licencePlate1, 1, BigDecimal.ONE));
		
		assertFalse(vehicleOps.changeConsumption("BBB", BigDecimal.valueOf(-1)));
		assertFalse(vehicleOps.changeConsumption(licencePlate1, BigDecimal.valueOf(0)));
	}

}
