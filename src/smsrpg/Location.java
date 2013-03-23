package smsrpg;

import java.util.ArrayList;

import log.Log;

public class Location { 
	
	private String name;
	
	private ArrayList<Location> adjacentLocations;
	
	public Location(String name) {
		this.name = name;
		adjacentLocations = new ArrayList<Location>();
	}
	
	public void connectToLocation(Location location) {
		if (location != this) {
			if (!adjacentLocations.contains(location)) {
				adjacentLocations.add(location);
			} else {
				Log.log("Location " + location.name + " is already connected to " + name);
			}
			
			if (!location.adjacentLocations.contains(this)) {
				location.adjacentLocations.add(this);
			} else {
				Log.log("Location " + this + " is already connected to " + location.name);
			}
		}
	}
	
	public String getName() {
		return name;
	}
	
	public ArrayList<Location> getAdjacentLocations() {
		return adjacentLocations;
	}
	
}
