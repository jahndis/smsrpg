package test.smsrpg;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.NoSuchElementException;

import log.Log;

import org.junit.BeforeClass;
import org.junit.Test;

import smsrpg.Location;
import smsrpg.World;

public class WorldTest {
	
	@BeforeClass
	public static void setUp() {
		Log.showAll(true);
	}

	@Test
	public void testWorld() {
		World world = new World();
		assertTrue("World shouldn't have any locations", world.getLocations().isEmpty());
	}
	
	@Test
	public void testAddLocation() {
		World world = new World();
		world.addLocation("Location 1");
		
		assertEquals("World should have one location", world.getLocations().size(), 1);
	}
	
	@Test
	public void testGetLocation() {
		World world = new World();
		world.addLocation("Location 1");
		
		assertEquals("Get location should return 'Location 1'", world.getLocation("Location 1").getName(), "Location 1");
	}
	
	@Test(expected=NoSuchElementException.class)
	public void testGetInvalidLocation() {
		World world = new World();
		world.getLocation("Invalid Location");
	}
	
	@Test
	public void testConnectLocations() {
		World world = new World();
		world.addLocation("Location 1");
		world.addLocation("Location 2");
		world.connectLocations("Location 1", "Location 2");
		
		assertEquals("There should be a path with length 1 between 'Location 1' and 'Location 2'", world.getPath("Location 1", "Location 2").size(), 2);
		assertEquals("There first location in the path should be 'Location 1'", world.getPath("Location 1", "Location 2").get(0).getName(), "Location 1");
		assertEquals("There first location in the path should be 'Location 2'", world.getPath("Location 1", "Location 2").get(1).getName(), "Location 2");
	}
	
	@Test
	public void testConnectLocationsWithLocation() {
		World world = new World();
		world.addLocation("Location 1");
		world.addLocation("Location 2");
		world.addLocation("Connection");
		world.connectLocationsWithLocation("Connection", "Location 1", "Location 2");
		
		assertEquals("There should be a path with length 2 between 'Location 1' and 'Location 2'", world.getPath("Location 1", "Location 2").size(), 3);
		assertEquals("There first location in the path should be 'Connection'", world.getPath("Location 1", "Location 2").get(0).getName(), "Location 1");
		assertEquals("There first location in the path should be 'Connection'", world.getPath("Location 1", "Location 2").get(1).getName(), "Connection");
		assertEquals("There second location in the path should be 'Location 2'", world.getPath("Location 1", "Location 2").get(2).getName(), "Location 2");
	}
	
	@Test
	public void testGetPath() {
		World world = new World();
		world.addLocation("Location 1");
		world.addLocation("Location 2");
		world.addLocation("Location 3");
		world.addLocation("Location 4");
		world.addLocation("Location 5");
		world.addLocation("Path 1 to 2");
		world.addLocation("Path 2 to 3");
		world.addLocation("Path 2 to 4");
		world.addLocation("Path 4 to 5");
		world.connectLocationsWithLocation("Path 1 to 2", "Location 1", "Location 2");
		world.connectLocationsWithLocation("Path 2 to 3", "Location 2", "Location 3");
		world.connectLocationsWithLocation("Path 2 to 4", "Location 2", "Location 4");
		world.connectLocationsWithLocation("Path 4 to 5", "Location 4", "Location 5");
		
		ArrayList<Location> path = world.getPath("Location 1", "Location 5");
		
		assertEquals("Path should have length 7", path.size(), 7);
		assertEquals("There first location in the path should be 'Location1'", path.get(0).getName(), "Location 1");
		assertEquals("There first location in the path should be 'Path 1 to 2'", path.get(1).getName(), "Path 1 to 2");
		assertEquals("There second location in the path should be 'Location 2'", path.get(2).getName(), "Location 2");
		assertEquals("There first location in the path should be 'Path 2 to 4'", path.get(3).getName(), "Path 2 to 4");
		assertEquals("There second location in the path should be 'Location 4'", path.get(4).getName(), "Location 4");
		assertEquals("There first location in the path should be 'Path 4 to 5'", path.get(5).getName(), "Path 4 to 5");
		assertEquals("There second location in the path should be 'Location 5'", path.get(6).getName(), "Location 5");
		
		world.addLocation("Location 6");
		
		path = world.getPath("Location 1", "Location 6");
		
		assertNull("Path should be null", path);
		
		path = world.getPath("Location 1", "Location 1");
		
		assertNull("Path between a location and itself should be null", path);
	}

}
