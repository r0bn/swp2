package de.hft_stuttgart.spirit;

import java.io.Serializable;


public class Poi implements Serializable{
	
	// Hier Felder einfügen
	private String name;
	private String longitude;
	private String latitude;
	private String coordinates;
	private String video;
	Location l;

	public Poi() {

	}

	public Poi(String place_name, String longt, String lat) {
		this.setLatitude(lat);
		this.setLongitude(longt);
		this.setName(place_name);
	}

	public String getName() {
		return name;
	}
	
	public String getVideo() {
		return video;
	}

	public void setVideo(String video) {
		this.video = video;
	}
	
	public void setCoordinates(String coordinates) {
		this.coordinates = coordinates;
		String[] parts = coordinates.split("\\s");
		latitude = parts[0];
		longitude = parts[1];
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getLongitude() {
		return Double.parseDouble(longitude);
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public double getLatitude() {
		return Double.parseDouble(latitude);
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	@Override
	public String toString() {
		return "Place name: " + name + "\n" + "Longitude:" + longitude + "\n"
				+ "Latitude:" + latitude;
	}
}
