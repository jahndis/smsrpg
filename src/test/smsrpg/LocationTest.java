package test.smsrpg;

import static org.junit.Assert.*;

import org.junit.Test;

import smsrpg.Location;

public class LocationTest {

	@Test
	public void testConnectToLocation() {
		Location location1 = new Location("Location 1");
		Location location2 = new Location("Location 2");
		location1.connectToLocation(location2);
		
		assertEquals("There should be one location adjacent to 'Location 1'", location1.getAdjacentLocations().size(), 1);
		assertEquals("'Location 2' adjacent to 'Location 1'", location1.getAdjacentLocations().get(0).getName(), "Location 2");
	}

}
