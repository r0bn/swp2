package de.hft_stuttgart.spirit;

public class GeoTools {
	// core implementierung -> schnittstelle zu android benötigt

	// TODO: implementieren ;)
	SpiritGeoTools sgt;
	
	public GeoTools(SpiritGeoTools sgt){
		this.sgt = sgt;
	}
	public Location getLocation(){
		return sgt.getLocation();
	}
	
	public float getDistance(Location l){
		return sgt.getDistance(l);
	}
	
	public float getBearingToLocationDegrees(Location l){
		return sgt.getBearingToLocationDegrees(l);
	}
	
}
