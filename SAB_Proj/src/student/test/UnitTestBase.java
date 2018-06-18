package student.test;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

import student.pn140041_GeneralOperations;

public abstract class UnitTestBase {
	
	private static pn140041_GeneralOperations generalOps = new pn140041_GeneralOperations();


	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
		generalOps.eraseAll();
	}
}
