package student.test;

import static org.junit.Assert.*;

import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

import student.pn140041_UserOperations;

public class UserOperationTest extends UnitTestBase {

	private static pn140041_UserOperations userOps = new pn140041_UserOperations();

	@Test
	public void testDeleteUserEmpty() {
		assertEquals(0, userOps.deleteUsers());
		assertEquals(0, userOps.deleteUsers(null));
		assertEquals(0, userOps.deleteUsers(""));
		assertEquals(0, userOps.deleteUsers("A"));
	}

	@Test
	public void testDeclareAdminEmpty() {
		assertEquals(2, userOps.declareAdmin(null));
		assertEquals(2, userOps.declareAdmin(""));
		assertEquals(2, userOps.declareAdmin("A"));
	}

	@Test
	public void testGetSentPackagesEmpty() {
		assertNull(userOps.getSentPackages());
		assertNull(userOps.getSentPackages(null));
		assertNull(userOps.getSentPackages(""));
		assertNull(userOps.getSentPackages("A"));
	}

	@Test
	public void testGetAllUsersEmpty() {
		assertEquals(0, userOps.getAllUsers().size());
	}

	@Test
	public void testInsertUserSingle() {
		String username = "username";
		String firstName = "Name";
		String secondName = "Surname";
		String password = "password123";

		assertTrue(userOps.insertUser(username, firstName, secondName, password));
	}

	@Test
	public void testInsertUserMultiple() {
		String username = "username";
		String firstName = "Name";
		String secondName = "Surname";
		String password = "password123";

		for (int i = 0; i < 5; i++)
			assertTrue(userOps.insertUser(username + i, firstName + i, secondName + i, password + i));
	}

	@Test
	public void testInsertUserWithSameUsername() {
		String username = "username";
		String firstName = "Name";
		String secondName = "Surname";
		String password = "password123";

		assertTrue(userOps.insertUser(username, firstName, secondName, password));
		assertFalse(userOps.insertUser(username, firstName, secondName, password));
	}

	@Test
	public void testInsertUserInvalid() {
		String username = "username";
		String firstName = "Name";
		String secondName = "Surname";
		String password = "password123";

		String firstNameInvalid = "name";
		String secondNameInvalid = "surname";
		String[] passwordInvalid = new String[] { "a", "aaaaaaaaaaaaaaaaaaaaa", "1", "123456789123456", "a1" };

		assertFalse(userOps.insertUser(username, firstNameInvalid, secondName, password));
		assertFalse(userOps.insertUser(username, firstName, secondNameInvalid, password));

		for (int i = 0; i < passwordInvalid.length; i++)
			assertFalse(userOps.insertUser(username, firstName, secondName, passwordInvalid[i]));

		assertTrue(userOps.insertUser(username, firstName, secondName, password));
	}

	@Test
	public void testDeclareAdminSingle() {
		String username = "username";
		String firstName = "Name";
		String secondName = "Surname";
		String password = "password123";

		assertTrue(userOps.insertUser(username, firstName, secondName, password));

		assertEquals(0, userOps.declareAdmin(username));
	}

	@Test
	public void testDeclareAdminSingleTwoTimes() {
		String username = "username";
		String firstName = "Name";
		String secondName = "Surname";
		String password = "password123";

		assertTrue(userOps.insertUser(username, firstName, secondName, password));

		assertEquals(0, userOps.declareAdmin(username));
		assertEquals(1, userOps.declareAdmin(username));
	}

	@Test
	public void testDeclareAdminMultiple() {
		String username = "username";
		String firstName = "Name";
		String secondName = "Surname";
		String password = "password123";

		for (int i = 0; i < 5; i++)
			assertTrue(userOps.insertUser(username + i, firstName + i, secondName + i, password));

		for (int i = 0; i < 5; i++) {
			assertEquals(0, userOps.declareAdmin(username + i));
			assertEquals(1, userOps.declareAdmin(username + i));
		}

		assertEquals(2, userOps.declareAdmin(username));
	}

	@Test
	public void testGetSentPackagesSingle() {
		String username = "username";
		String firstName = "Name";
		String secondName = "Surname";
		String password = "password123";

		assertTrue(userOps.insertUser(username, firstName, secondName, password));

		assertEquals(0, userOps.getSentPackages(username).intValue());
	}

	@Test
	public void testGetSentPackagesMultiple() {
		String username = "username";
		String firstName = "Name";
		String secondName = "Surname";
		String password = "password123";

		LinkedList<String> usernames = new LinkedList<>();
		for (int i = 0; i < 5; i++) {
			assertTrue(userOps.insertUser(username + i, firstName + i, secondName + i, password + i));
			usernames.add(username + i);
		}

		assertEquals(0, userOps.getSentPackages(username + 0, username + 1, username + 2, username + 3, username + 4)
				.intValue());
		assertNull(userOps.getSentPackages(username + 0, username + 1, username + 2, username + 3, username + 4,
				username + 5));
	}

	@Test
	public void testDeleteUserSingle() {
		String username = "username";
		String firstName = "Name";
		String secondName = "Surname";
		String password = "password123";

		assertTrue(userOps.insertUser(username, firstName, secondName, password));

		assertEquals(1, userOps.deleteUsers(username));
	}

	@Test
	public void testDeleteUserSingleTwoTimes() {
		String username = "username";
		String firstName = "Name";
		String secondName = "Surname";
		String password = "password123";

		assertTrue(userOps.insertUser(username, firstName, secondName, password));

		assertEquals(1, userOps.deleteUsers(username));
		assertEquals(0, userOps.deleteUsers(username));
	}

	@Test
	public void testDeleteUserMultipleOneByOne() {
		String username = "username";
		String firstName = "Name";
		String secondName = "Surname";
		String password = "password123";

		int numOfUsers = 5;

		for (int i = 0; i < numOfUsers; i++)
			assertTrue(userOps.insertUser(username + i, firstName + i, secondName + i, password + i));

		for (int i = 0; i < numOfUsers; i++) {
			assertEquals(1, userOps.deleteUsers(username + i));
			assertEquals(0, userOps.deleteUsers(username + i));
		}
	}

	@Test
	public void testDeleteUserMultiple() {
		String username = "username";
		String firstName = "Name";
		String secondName = "Surname";
		String password = "password123";

		for (int i = 0; i < 5; i++)
			assertTrue(userOps.insertUser(username + i, firstName + i, secondName + i, password + i));

		assertEquals(5, userOps.deleteUsers(username + 0, username + 1, username + 2, username + 3, username + 4));

		for (int i = 0; i < 5; i++)
			assertTrue(userOps.insertUser(username + i, firstName + i, secondName + i, password + i));

		assertEquals(5, userOps.deleteUsers(username + 0, username + 1, username + 2, username + 3, username + 4,
				username + 5, username + 6));
	}

	@Test
	public void testGetAllUsersSingle() {
		String username = "username";
		String firstName = "Name";
		String secondName = "Surname";
		String password = "password123";

		List<String> result = null;

		assertTrue(userOps.insertUser(username, firstName, secondName, password));

		result = userOps.getAllUsers();
		assertEquals(1, result.size());
		assertTrue(result.contains(username));

		assertEquals(1, userOps.deleteUsers(username));

		result = userOps.getAllUsers();
		assertEquals(0, result.size());
		assertFalse(result.contains(username));
	}

	@Test
	public void testGetAllUsersMultiple1() {
		String username = "username";
		String firstName = "Name";
		String secondName = "Surname";
		String password = "password123";

		List<String> result = null;

		result = userOps.getAllUsers();
		assertEquals(0, result.size());

		for (int i = 0; i < 5; i++) {
			assertTrue(userOps.insertUser(username + i, firstName + i, secondName + i, password + i));

			result = userOps.getAllUsers();
			assertEquals(i + 1, result.size());
			assertTrue(result.contains(username + i));
		}

		result = userOps.getAllUsers();
		assertEquals(5, result.size());
		assertFalse(result.contains(username));

		for (int i = 4; i >= 0; i--) {
			result = userOps.getAllUsers();
			assertEquals(i + 1, result.size());
			assertTrue(result.contains(username + i));
			assertEquals(1, userOps.deleteUsers(username + i));
			result = userOps.getAllUsers();
			assertEquals(i, result.size());
			assertFalse(result.contains(username + i));
		}

		result = userOps.getAllUsers();
		assertEquals(0, result.size());
	}
}
