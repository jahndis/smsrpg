package smsrpg;

import java.util.ArrayList;
import java.util.HashMap;

public class World {

	private ArrayList<Location> locations;
	
	public World() {
		locations = new ArrayList<Location>();
	}
	
	public Location getLocation(String name) {
		for(Location l : locations) {
			if (l.getName().equals(name)) {
				return l;
			}
		}
		
		return null;
	}
	
	public void addLocation(String name) {
		locations.add(new Location(name));
	}	
	
	public void connectLocations(Location location1, Location location2) {
			location1.connectToLocation(location2);
	}
	
	public void connectLocations(String location1, String location2) {
		connectLocations(getLocation(location1), getLocation(location2));
	}
	
	public void connectLocationsWithLocation(Location connector, Location location1, Location ...locations) {
		connectLocations(location1, connector);
		for (Location location : locations) {
			connectLocations(location, connector);
		}
	}
	
	public void connectLocationsWithLocation(String connector, String location1, String ...locations) {
		connectLocations(location1, connector);
		for (String location : locations) {
			connectLocations(location, connector);
		}
	}
	
	public ArrayList<Location> getPath(String location1, String location2) {
		return getPath(getLocation(location1), getLocation(location2));
	}
	
	public ArrayList<Location> getPath(Location source, Location target) {
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
		
		return path;
	}
	
}
