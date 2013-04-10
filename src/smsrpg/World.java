package smsrpg;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.NoSuchElementException;

public class World {

	private ArrayList<Location> locations;
	
	public static World createWorld() {
		World world = new World();
		
		// Create main locations
		world.addLocation("A");
		world.addLocation("B");
		world.addLocation("C");
		world.addLocation("D");
		world.addLocation("E");
		
		// Create path locations
		world.addLocation("a-b");
		world.addLocation("b-c");
		world.addLocation("c-d");
		world.addLocation("d-e");
		world.addLocation("e-a");
		world.addLocation("c-e");
		
		// Connect the locations
		world.connectLocationsWithLocation("a-b", "A", "B");
		world.connectLocationsWithLocation("b-c", "B", "C");
		world.connectLocationsWithLocation("c-d", "C", "D");
		world.connectLocationsWithLocation("d-e", "D", "E");
		world.connectLocationsWithLocation("e-a", "E", "A");
		world.connectLocationsWithLocation("c-e", "C", "E");
		
		return world;
	}
	
	public World() {
		locations = new ArrayList<Location>();
	}
	
	public Location getLocation(String name) {
		for(Location l : locations) {
			if (l.getName().equals(name)) {
				return l;
			}
		}
		
		throw new NoSuchElementException("Location '" + name + "' does not exist in the world");
	}
	
	public void addLocation(String name) {
		locations.add(new Location(name));
	}	
	
	public void connectLocations(String location1Name, String location2Name) {
		getLocation(location1Name).connectToLocation(getLocation(location2Name));
	}
	
	public void connectLocationsWithLocation(String connectorName, String location1Name, String ...locationNames) {
		connectLocations(location1Name, connectorName);
		for (String locationName : locationNames) {
			connectLocations(locationName, connectorName);
		}
	}
	
	public ArrayList<Location> getPath(String sourceName, String targetName) {
		Location source = getLocation(sourceName);
		Location target = getLocation(targetName);
		
		ArrayList<Location> path = new ArrayList<Location>();
		
		ArrayList<String> N = new ArrayList<String>();
		N.add(source.getName());
		
		HashMap<String, Integer> D = new HashMap<String, Integer>();
		HashMap<String, String> P = new HashMap<String, String>();
		
		for (Location l : locations) {
			if (source.getAdjacentLocations().contains(l)) {
				D.put(l.getName(), 1);
				P.put(l.getName(), source.getName());
			} else {
				if (source == l) {
					D.put(l.getName(), 0);
				} else {
					D.put(l.getName(), Integer.MAX_VALUE/2);
				}
			}
		}
		
		String w;
		
		while (N.size() < locations.size()) {
			//Find w not in N such that D(w) is a minimum
			w = null;
			for (Location l : locations) {
				if (!N.contains(l.getName())) {
					if (w == null) {
						w = l.getName();
					} else {
						if (D.get(l.getName()) < D.get(w)) {
							w = l.getName();
						}
					}
				}
			}
			
			N.add(w);
			
			if (w.equals(target.getName())) {
				break;
			}
			
			for (Location l : getLocation(w).getAdjacentLocations()) {
				if (!N.contains(l.getName())) {
					if (D.get(l.getName()) > (D.get(w) + 1)) {
						D.put(l.getName(), D.get(w) + 1);
						P.put(l.getName(), w);
					}
				}
			}
		}

		//Get the path
		String tracer = target.getName();
		while (tracer != null) {
			path.add(0, getLocation(tracer));
			tracer = P.get(tracer);
		}
		
		if (path.size() == 1) {
			return null;
		}
		
		return path;
	}
	
	public ArrayList<Location> getLocations() {
		return locations;
	}
	
}
