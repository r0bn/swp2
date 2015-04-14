package de.hft_stuttgart.spirit;

public class Location {
// core implementierung -> schnittstelle zu android benötigt
	
	public String name;
	public double lat;
	public double lon;
	
	public Location(String name){
		this.name = name;
	}
	
	public void setLongitude(double lon){
		this.lon = lon;
	}
	
	public void setLatitude(double lat){
		this.lat = lat;
	}
}
